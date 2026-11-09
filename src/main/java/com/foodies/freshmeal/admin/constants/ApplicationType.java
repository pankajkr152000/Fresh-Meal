package com.foodies.freshmeal.admin.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

/**
 * ============================================================================
 * Enum : Application Type
 * ============================================================================
 *
 * Represents the type of onboarding application submitted to FreshMeal.
 *
 * ============================================================================
 */
public enum ApplicationType implements IDisplayOption {

    RESTAURANT("Restaurant"),

    DELIVERY_PARTNER("Delivery Partner");

    private final String displayName;

    ApplicationType(final String displayName) {
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