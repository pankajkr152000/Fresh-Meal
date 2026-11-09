package com.foodies.freshmeal.restaurant.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

public enum RestaurantVerificationStatus implements IDisplayOption {

    PENDING("Pending"),
    VERIFIED("Verified"),
    REJECTED("Rejected");

    private final String label;

    RestaurantVerificationStatus(final String label) {
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
