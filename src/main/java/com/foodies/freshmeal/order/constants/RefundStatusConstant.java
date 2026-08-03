package com.foodies.freshmeal.order.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

public enum RefundStatusConstant implements IDisplayOption {

    NOT_APPLICABLE("Not Applicable"),
    PENDING("Pending"),
    PROCESSING("Processing"),
    COMPLETED("Completed"),
    FAILED("Failed");

    /**
     * User-friendly display label.
     */
    private final String displayName;

    RefundStatusConstant(String displayName) {
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