package com.foodies.freshmeal.common.exception;

import java.util.Map;

public final class ErrorMessage {

    private ErrorMessage() {
    }

    private static final Map<ErrorCodeConstants, String> MESSAGES = Map.ofEntries(

            Map.entry(ErrorCodeConstants.INTERNAL_SERVER_ERROR,
                    "Something went wrong."),

            Map.entry(ErrorCodeConstants.VALIDATION_FAILED,
                    "Validation failed."),

            Map.entry(ErrorCodeConstants.INVALID_REQUEST,
                    "Invalid request."),

            Map.entry(ErrorCodeConstants.FOOD_NOT_FOUND,
                    "Food not found."),

            Map.entry(ErrorCodeConstants.FOOD_ALREADY_EXISTS,
                    "Food already exists."),

            Map.entry(ErrorCodeConstants.INVALID_FOOD_STATUS,
                    "Invalid food status."),

            Map.entry(ErrorCodeConstants.USER_NOT_FOUND,
                    "User not found."),

            Map.entry(ErrorCodeConstants.USER_ALREADY_EXISTS,
                    "User already exists."),

            Map.entry(ErrorCodeConstants.INVALID_CREDENTIALS,
                    "Invalid username or password."),

            Map.entry(ErrorCodeConstants.ACCESS_DENIED,
                    "Access denied."));

    public static String get(ErrorCodeConstants ErrorCodeConstants) {
        return MESSAGES.get(ErrorCodeConstants);
    }

}