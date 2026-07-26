package com.foodies.freshmeal.common.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

public enum RoleType implements IDisplayOption {
    ADMIN("Admin"),
    SYSTEM("System"),
    USER("User");

    /**
     * User-friendly display label.
     */
    private final String displayName;

    /**
     * Creates a Role Type.
     *
     * @param displayName user-friendly display label
     */
    RoleType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return displayName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return name();
    }

}
