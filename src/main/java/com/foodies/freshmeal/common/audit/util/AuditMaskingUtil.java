package com.foodies.freshmeal.common.audit.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import com.foodies.freshmeal.common.audit.annotation.Sensitive;

public final class AuditMaskingUtil {

/*
Object maskedRequest =
        AuditMaskingUtil.maskObject(request);

auditLog.setRequestBody(
        convertToDocument(maskedRequest));

Object maskedResponse =
        AuditMaskingUtil.maskObject(response);

auditLog.setResponseBody(
        convertToDocument(maskedResponse));


*/


    private AuditMaskingUtil() {
    }

    public static Object maskObject(Object source) {

        if (source == null) {
            return null;
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

        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException
                | SecurityException | InvocationTargetException e) {

            return source;
        }
    }
}