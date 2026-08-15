package com.foodies.freshmeal.restaurant.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

/**
 * ============================================================================
 * Enum : RestaurantStatus
 * ============================================================================
 *
 * Represents the current business status of a restaurant.
 *
 * Business Meaning
 * ----------------
 *
 * ACTIVE
 * Restaurant is operational.
 *
 * INACTIVE
 * Restaurant is temporarily inactive.
 *
 * SUSPENDED
 * Restaurant has been administratively suspended.
 *
 * CLOSED
 * Restaurant is permanently closed.
 *
 * NOTE
 * ----
 * This status represents the restaurant lifecycle.
 * Operational availability is maintained separately using isAvailable.
 *
 * ============================================================================
 */
public enum RestaurantStatusConstant implements IDisplayOption {

    ACTIVE("Active"),

    INACTIVE("Inactive"),

    SUSPENDED("Suspended"),

    CLOSED("Closed");

    /**
     * User-friendly display label.
     */
    private final String displayName;

    /**
     * Creates a restaurant status.
     *
     * @param displayName user-friendly display label
     */
    RestaurantStatusConstant(String displayName) {
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