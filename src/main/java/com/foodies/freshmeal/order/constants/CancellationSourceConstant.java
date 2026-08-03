package com.foodies.freshmeal.order.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

public enum CancellationSourceConstant implements IDisplayOption {

    CUSTOMER("Customer"),
    RESTAURANT("Restaurant"),
    DELIVERY_PARTNER("Delivery Partner"),
    ADMIN("Admin"),
    SYSTEM("System");

    /**
     * User-friendly display label.
     */
    private final String displayName;

    CancellationSourceConstant(String displayName) {
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