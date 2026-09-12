package com.foodies.freshmeal.common.audit.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.foodies.freshmeal.common.audit.annotation.Sensitive;

/**
 * ====================================================================================
 * <b>Audit Masking Utility</b>
 * ====================================================================================
 *
 * <p>
 * Central utility responsible for creating audit-safe representations of Java
 * objects by masking fields annotated with {@link Sensitive}.
 * </p>
 *
 * <h3>Purpose</h3>
 *
 * <p>
 * Audit logging frequently requires request and response payloads to be stored
 * for troubleshooting, traceability and operational analysis. However, these
 * payloads may contain sensitive information such as passwords, tokens,
 * verification codes or other confidential values.
 * </p>
 *
 * <p>
 * This utility provides a centralized mechanism for masking such fields before
 * they are written to audit logs or persisted as audit data.
 * </p>
 *
 * <h3>Responsibilities</h3>
 *
 * <ul>
 * <li>Inspect objects for fields annotated with {@link Sensitive}.</li>
 * <li>Recursively inspect nested objects.</li>
 * <li>Mask sensitive fields using the mask configured by
 * {@link Sensitive#mask()}.</li>
 * <li>Traverse collections, maps and arrays.</li>
 * <li>Preserve simple Java values without unnecessary reflection.</li>
 * <li>Create audit-safe copies without modifying the original objects.</li>
 * <li>Fail safely when an object cannot be inspected.</li>
 * </ul>
 *
 * <h3>Architecture Position</h3>
 *
 * <pre>
 * Controller
 *     |
 *     v
 * AuditAspect
 *     |
 *     v
 * AuditMaskingUtil
 *     |
 *     +-- Request Payload
 *     |
 *     +-- Response Payload
 *     |
 *     v
 * Safe Audit Representation
 *     |
 *     +-- Application Logs
 *     |
 *     +-- MongoDB Audit Logs
 * </pre>
 *
 * <h3>Nested Object Support</h3>
 *
 * <p>
 * Sensitive fields may exist several levels below the root object. For example:
 * </p>
 *
 * <pre>
 * LoginResponse
 *     |
 *     +-- TokenResponse
 *             |
 *             +-- accessToken       -> [MASKED]
 *             +-- refreshToken      -> [MASKED]
 *             +-- loginSessionId    -> [MASKED]
 * </pre>
 *
 * <p>
 * Therefore, masking is performed recursively rather than only against fields
 * declared directly on the root object.
 * </p>
 *
 * <h3>Non-Responsibilities</h3>
 *
 * <ul>
 * <li>Does not modify the original request or response object.</li>
 * <li>Does not perform persistence.</li>
 * <li>Does not perform logging.</li>
 * <li>Does not determine which fields are sensitive by field name.</li>
 * <li>Does not alter API responses returned to clients.</li>
 * </ul>
 *
 * <h3>Security Principle</h3>
 *
 * <p>
 * The original object must never be modified merely for auditing purposes.
 * Masking is performed on an independent representation so that application
 * behaviour and API contracts remain unchanged.
 * </p>
 *
 * @author Pankaj Kumar
 */
public final class AuditMaskingUtil {

    /**
     * Private constructor prevents utility class instantiation.
     */
    private AuditMaskingUtil() {
        // Utility class.
    }

    /**
     * ====================================================================================
     * <b>Mask Object</b>
     * ====================================================================================
     *
     * <p>
     * Creates an audit-safe representation of the supplied object.
     * </p>
     *
     * <p>
     * The object is inspected recursively. Any field annotated with
     * {@link Sensitive} is replaced with the mask configured on that annotation.
     * </p>
     *
     * <p>
     * The original object is never modified.
     * </p>
     *
     * @param source source object to mask
     *
     * @return audit-safe representation of the source object
     */
    public static Object maskObject(Object source) {

        return maskValue(source);
    }

    /**
     * ====================================================================================
     * <b>Mask Value</b>
     * ====================================================================================
     *
     * <p>
     * Recursively processes a value according to its runtime type.
     * </p>
     *
     * <p>
     * The method supports simple values, maps, collections, arrays and regular
     * Java objects.
     * </p>
     *
     * @param value value to process
     *
     * @return masked audit-safe representation
     */
    private static Object maskValue(Object value) {

        if (value == null) {
            return null;
        }

        /*
         * Simple values do not contain fields requiring recursive inspection.
         */
        if (isSimpleValue(value)) {
            return value;
        }

        /*
         * Map values are recursively inspected because sensitive objects may
         * exist inside a map.
         */
        if (value instanceof Map<?, ?> map) {
            return maskMap(map);
        }

        /*
         * Collections are recursively inspected because their elements may
         * contain nested sensitive fields.
         */
        if (value instanceof Collection<?> collection) {
            return maskCollection(collection);
        }

        /*
         * Java arrays require reflection because the component type may be
         * primitive or an arbitrary object type.
         */
        if (value.getClass().isArray()) {
            return maskArray(value);
        }

        /*
         * Regular Java objects are inspected field by field.
         */
        return maskPojo(value);
    }

    /**
     * ====================================================================================
     * <b>Mask POJO</b>
     * ====================================================================================
     *
     * <p>
     * Creates a field-based audit representation of a regular Java object.
     * </p>
     *
     * <p>
     * Sensitive fields are replaced with their configured mask. Non-sensitive
     * fields are recursively processed.
     * </p>
     *
     * @param source source object
     *
     * @return masked object representation
     */
    private static Object maskPojo(Object source) {

        Class<?> clazz = source.getClass();

        /*
         * LinkedHashMap preserves declaration/processing order as much as
         * possible and provides a BSON/JSON-friendly representation.
         */
        Map<String, Object> target = new LinkedHashMap<>();

        try {

            for (Field field : getAllFields(clazz)) {

                /*
                 * Static fields are implementation metadata rather than object
                 * state and should not be included in audit payloads.
                 */
                if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                /*
                 * Synthetic fields are compiler-generated implementation
                 * details and should not appear in audit payloads.
                 */
                if (field.isSynthetic()) {
                    continue;
                }

                field.setAccessible(true);

                Sensitive sensitive = field.getAnnotation(Sensitive.class);

                if (sensitive != null) {

                    target.put(field.getName(), sensitive.mask());

                    continue;
                }

                Object fieldValue = field.get(source);

                target.put(field.getName(), maskValue(fieldValue));
            }

            return target;

        } catch (IllegalAccessException | IllegalArgumentException | SecurityException ex) {

            /*
             * Auditing must never break the business API. If reflection cannot
             * inspect the object safely, return the original object and allow
             * the existing audit conversion layer to handle it.
             */
            return source;
        }
    }

    /**
     * ====================================================================================
     * <b>Mask Map</b>
     * ====================================================================================
     *
     * <p>
     * Recursively masks map keys' values.
     * </p>
     *
     * <p>
     * Map keys are preserved because masking is intended for values rather than
     * structural identifiers.
     * </p>
     *
     * @param source source map
     *
     * @return masked map
     */
    private static Map<Object, Object> maskMap(Map<?, ?> source) {

        Map<Object, Object> target = new LinkedHashMap<>();

        for (Map.Entry<?, ?> entry : source.entrySet()) {

            Object key = entry.getKey();
            Object value = entry.getValue();

            target.put(key, maskValue(value));
        }

        return target;
    }

    /**
     * ====================================================================================
     * <b>Mask Collection</b>
     * ====================================================================================
     *
     * <p>
     * Recursively processes every element in a collection.
     * </p>
     *
     * @param source source collection
     *
     * @return masked collection
     */
    private static List<Object> maskCollection(Collection<?> source) {

        List<Object> target = new ArrayList<>(source.size());

        for (Object element : source) {
            target.add(maskValue(element));
        }

        return target;
    }

    /**
     * ====================================================================================
     * <b>Mask Array</b>
     * ====================================================================================
     *
     * <p>
     * Recursively processes array elements.
     * </p>
     *
     * @param source source array
     *
     * @return masked array representation
     */
    private static List<Object> maskArray(Object source) {

        int length = Array.getLength(source);

        List<Object> target = new ArrayList<>(length);

        for (int i = 0; i < length; i++) {

            Object element = Array.get(source, i);

            target.add(maskValue(element));
        }

        return target;
    }

    /**
     * ====================================================================================
     * <b>Get All Fields</b>
     * ====================================================================================
     *
     * <p>
     * Returns fields declared by the supplied class and its parent classes.
     * </p>
     *
     * <p>
     * This is important for audit DTOs/entities that inherit sensitive or
     * business fields from a superclass.
     * </p>
     *
     * @param clazz object class
     *
     * @return all fields declared across the class hierarchy
     */
    private static List<Field> getAllFields(Class<?> clazz) {

        List<Field> fields = new ArrayList<>();

        Class<?> currentClass = clazz;

        while (currentClass != null && currentClass != Object.class) {

            Field[] declaredFields = currentClass.getDeclaredFields();

            fields.addAll(Arrays.asList(declaredFields));

            currentClass = currentClass.getSuperclass();
        }

        return fields;
    }

    /**
     * ====================================================================================
     * <b>Simple Value Check</b>
     * ====================================================================================
     *
     * <p>
     * Determines whether a value can be returned directly without recursive
     * field inspection.
     * </p>
     *
     * <p>
     * Simple values include Java primitives/wrappers, strings, enumerations and
     * common date/time values.
     * </p>
     *
     * @param value value to inspect
     *
     * @return {@code true} when the value does not require reflection
     */
    private static boolean isSimpleValue(Object value) {

        if (value == null) {
            return true;
        }

        Class<?> clazz = value.getClass();

        return clazz.isPrimitive()
                || value instanceof String
                || value instanceof Number
                || value instanceof Boolean
                || value instanceof Character
                || value instanceof Enum<?>
                || value instanceof Date
                || value instanceof Temporal;
    }
}