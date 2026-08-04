package com.foodies.freshmeal.common.exception;

import com.foodies.freshmeal.common.contract.IDisplayOption;

public enum ErrorCodeConstants implements IDisplayOption {

    // ================= Common =================
    INTERNAL_SERVER_ERROR("Internal Server Error"),
    VALIDATION_FAILED("Validation Failed"),
    INVALID_REQUEST("Invalid Request"),

    // ================= Food =================
    FOOD_NOT_FOUND("Food Not Found"),
    FOOD_ALREADY_EXISTS("Food Already Exists"),
    INVALID_FOOD_STATUS("Invalid Food Status"),

    // ================= User =================
    USER_NOT_FOUND("User Not Found"),
    USER_ALREADY_EXISTS("User Already Exists"),

    // ================= Authentication =================
    INVALID_CREDENTIALS("Invalid Credentials"),
    ACCESS_DENIED("Access Denied");

    private final String label;

    ErrorCodeConstants(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getValue() {
        return name();
    }
}