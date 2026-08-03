package com.foodies.freshmeal.order.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

public enum PlatformTypeConstant implements IDisplayOption {

    ANDROID("Android"),
    IOS("iOS"),
    WINDOWS("Windows"),
    MACOS("macOS"),
    LINUX("Linux"),
    WEB("Web"),
    OTHER("Other");

    /**
     * User-friendly display label.
     */
    private final String displayName;

    PlatformTypeConstant(String displayName) {
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