package com.foodies.freshmeal.common.exception;

import java.util.Map;

public final class ErrorMessage {

    private ErrorMessage() {
    }

    private static final Map<CommonErrorConstants, String> MESSAGES = Map.ofEntries(

            Map.entry(CommonErrorConstants.INTERNAL_SERVER_ERROR,
                    "Something went wrong."),

            Map.entry(CommonErrorConstants.VALIDATION_FAILED,
                    "Validation failed."),

            Map.entry(CommonErrorConstants.INVALID_REQUEST,
                    "Invalid request."),

            Map.entry(CommonErrorConstants.RESOURCE_NOT_FOUND,
                    "Resource not found."),

            Map.entry(CommonErrorConstants.DUPLICATE_RESOURCE,
                    "Already exists."),

            Map.entry(CommonErrorConstants.INVALID_CREDENTIALS,
                    "Invalid username or password."),

            Map.entry(CommonErrorConstants.ACCESS_DENIED,
                    "Access denied."));

    public static String get(IBusinessError CommonErrorConstants) {
        return MESSAGES.get(CommonErrorConstants);
    }

}