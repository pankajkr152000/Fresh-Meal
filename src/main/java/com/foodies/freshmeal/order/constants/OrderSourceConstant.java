package com.foodies.freshmeal.order.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

public enum OrderSourceConstant implements IDisplayOption {

    WEB("Web"),
    MOBILE_APP("Mobile App"),
    ADMIN_PANEL("Admin Panel"),
    POS("POS"),
    PHONE_CALL("Phone Call");

    /**
     * User-friendly display label.
     */
    private final String displayName;

    OrderSourceConstant(String displayName) {
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