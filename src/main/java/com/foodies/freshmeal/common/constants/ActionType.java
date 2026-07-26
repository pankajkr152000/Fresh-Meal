package com.foodies.freshmeal.common.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

public enum ActionType implements IDisplayOption {

	CREATE("Create"),
    UPDATE("Update"),
    DELETE("Delete"),
    VIEW("View"),
    ENABLE("Enable"),
    DISABLE("Disable"),
    LOGIN("Login"),
    LOGOUT("Logout"),
    UPLOAD("Upload"),
    DOWNLOAD("Download");

    private final String displayName;

    ActionType(String displayName) {
        this.displayName = displayName;
    }

    public String getLabel() {
        return displayName;
    }

    public String getValue() {
        return name();
    }
}
