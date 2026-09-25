package com.foodies.freshmeal.delivery.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

public enum DeliveryPartnerStatus implements IDisplayOption {

    PENDING("Pending"),
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    SUSPENDED("Suspended");

    private final String label;

    DeliveryPartnerStatus(final String label) {
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
