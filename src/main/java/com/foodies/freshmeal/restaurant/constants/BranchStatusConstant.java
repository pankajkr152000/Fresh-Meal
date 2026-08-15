package com.foodies.freshmeal.restaurant.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

/**
 * ============================================================================
 * Enum : BranchStatus
 * ============================================================================
 *
 * Represents the current business status of a restaurant branch.
 *
 * Business Meaning
 * ----------------
 *
 * ACTIVE
 * Branch is operational.
 *
 * INACTIVE
 * Branch is temporarily inactive.
 *
 * CLOSED
 * Branch is permanently closed.
 *
 * NOTE
 * ----
 * This status represents the branch lifecycle.
 * Operational availability is maintained separately using isAvailable.
 *
 * ============================================================================
 */
public enum BranchStatusConstant implements IDisplayOption {

    ACTIVE("Active"),

    INACTIVE("Inactive"),

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
    BranchStatusConstant(String displayName) {
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