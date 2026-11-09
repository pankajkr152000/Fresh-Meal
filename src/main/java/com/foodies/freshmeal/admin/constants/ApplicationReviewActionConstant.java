package com.foodies.freshmeal.admin.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

/**
 * ============================================================================
 * Enum : Application Review Action
 * ============================================================================
 *
 * Represents an action performed during an application's review lifecycle.
 *
 * These values are recorded in the application's review history.
 *
 * ============================================================================
 */
public enum ApplicationReviewActionConstant implements IDisplayOption {

    SUBMITTED("Submitted"),

    REVIEW_STARTED("Review Started"),

    CORRECTION_REQUESTED("Correction Requested"),

    RESUBMITTED("Resubmitted"),

    APPROVED("Approved"),

    REJECTED("Rejected"),

    WITHDRAWN("Withdrawn"),

    CANCELLED("Cancelled");

    private final String displayName;

    ApplicationReviewActionConstant(final String displayName) {
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