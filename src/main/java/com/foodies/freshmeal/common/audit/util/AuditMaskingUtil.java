package com.foodies.freshmeal.common.audit.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.temporal.Temporal;
import java.util.Date;

import com.foodies.freshmeal.common.audit.annotation.Sensitive;

public final class AuditMaskingUtil {

    private AuditMaskingUtil() {
    }

    /**
     * Masks fields annotated with @Sensitive.
     *
     * For simple Java values such as String, Number, Boolean,
     * Enum and Date/Time values, reflection is not required.
     *
     * @param source source object
     * @return masked object
     */
    public static Object maskObject(Object source) {

        if (source == null) {
            return null;
        }

        // =====================================================================
        // Simple values
        // =====================================================================

        if (isSimpleValue(source)) {
            return source;
        }

        try {

            Class<?> clazz = source.getClass();

            Object target = clazz.getDeclaredConstructor().newInstance();

            for (Field field : clazz.getDeclaredFields()) {

                field.setAccessible(true);

                Object value = field.get(source);

                Sensitive sensitive = field.getAnnotation(Sensitive.class);

                if (sensitive != null) {

                    field.set(target, sensitive.mask());

                } else {

                    field.set(target, value);
                }
            }

            return target;

        } catch (IllegalAccessException
                | IllegalArgumentException
                | InstantiationException
                | NoSuchMethodException
                | SecurityException
                | InvocationTargetException e) {

            return source;
        }
    }

    /**
     * Determines whether the object is a simple value that does not
     * require reflection-based field inspection.
     *
     * @param value object to check
     * @return true when the value can be returned directly
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
                || value instanceof Enum
                || value instanceof Date
                || value instanceof Temporal; 
    }
}