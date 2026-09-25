package com.foodies.freshmeal.delivery.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

public enum DeliveryPartnerVerificationStatus implements IDisplayOption {

    PENDING("Pending"),
    VERIFIED("Verified"),
    REJECTED("Rejected");

    private final String label;

    DeliveryPartnerVerificationStatus(final String label) {
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
