package com.foodies.freshmeal.common.exception;

import java.util.Map;

public final class ErrorMessage {

    private ErrorMessage() {
    }

    private static final Map<ErrorCode, String> MESSAGES = Map.ofEntries(

            Map.entry(ErrorCode.INTERNAL_SERVER_ERROR,
                    "Something went wrong."),

            Map.entry(ErrorCode.VALIDATION_FAILED,
                    "Validation failed."),

            Map.entry(ErrorCode.INVALID_REQUEST,
                    "Invalid request."),

            Map.entry(ErrorCode.FOOD_NOT_FOUND,
                    "Food not found."),

            Map.entry(ErrorCode.FOOD_ALREADY_EXISTS,
                    "Food already exists."),

            Map.entry(ErrorCode.INVALID_FOOD_STATUS,
                    "Invalid food status."),

            Map.entry(ErrorCode.USER_NOT_FOUND,
                    "User not found."),

            Map.entry(ErrorCode.USER_ALREADY_EXISTS,
                    "User already exists."),

            Map.entry(ErrorCode.INVALID_CREDENTIALS,
                    "Invalid username or password."),

            Map.entry(ErrorCode.ACCESS_DENIED,
                    "Access denied."));

    public static String get(ErrorCode errorCode) {
        return MESSAGES.get(errorCode);
    }

}