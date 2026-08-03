package com.foodies.freshmeal.order.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

public enum DeviceTypeConstant implements IDisplayOption {

    MOBILE("Mobile"),
    TABLET("Tablet"),
    DESKTOP("Desktop"),
    KIOSK("Kiosk"),
    OTHER("Other");

    /**
     * User-friendly display label.
     */
    private final String displayName;

    DeviceTypeConstant(String displayName) {
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