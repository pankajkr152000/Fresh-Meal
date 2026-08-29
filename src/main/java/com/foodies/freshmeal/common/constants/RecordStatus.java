package com.foodies.freshmeal.common.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

public enum RecordStatus implements IDisplayOption {

    ACTIVE("Active"),

    INACTIVE("Inactive"),

    DELETED("Deleted"),

    SUSPENDED("Suspended"),

    CLOSED("Closed");

    /**
     * User-friendly display label.
     */
    private final String displayName;

    /**
     * Creates a branch status.
     *
     * @param displayName user-friendly display label
     */
    RecordStatus(String displayName) {
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
