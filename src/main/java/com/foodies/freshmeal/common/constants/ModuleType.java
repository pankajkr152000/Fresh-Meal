package com.foodies.freshmeal.common.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

public enum ModuleType implements IDisplayOption {

    AUTH("Authentication"),
    USER("User"),
    FOOD("Food"),
    CATEGORY("Category"),
    IMAGE("Image"),
    ORDER("Order"),
    PAYMENT("Payment"),
    COUPON("Coupon"),
    REVIEW("Review");

    private final String displayName;

    ModuleType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getLabel() {
        return displayName;
    }

    @Override
    public String getValue() {
        return name();
    }
}
