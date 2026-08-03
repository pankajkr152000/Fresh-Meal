package com.foodies.freshmeal.order.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

public enum OrderTypeConstant implements IDisplayOption {

    DELIVERY("Delivery"),
    TAKEAWAY("Takeaway"),
    DINE_IN("Dine In");

    /**
     * User-friendly display label.
     */
    private final String displayName;

    OrderTypeConstant(String displayName) {
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