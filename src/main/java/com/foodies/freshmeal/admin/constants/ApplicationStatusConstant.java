package com.foodies.freshmeal.admin.constants;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import com.foodies.freshmeal.common.contract.IDisplayOption;

/**
 * ============================================================================
 * Enum : Application Status
 * ============================================================================
 *
 * Represents the lifecycle status of an onboarding application.
 *
 * Application status transitions are controlled by the Admin Review workflow.
 *
 * ============================================================================
 */
public enum ApplicationStatusConstant implements IDisplayOption {

    SUBMITTED("Submitted"),

    UNDER_REVIEW("Under Review"),

    NEEDS_CORRECTION("Needs Correction"),

    APPROVED("Approved"),

    REJECTED("Rejected"),

    WITHDRAWN("Withdrawn"),

    CANCELLED("Cancelled");

    private final String displayName;

    ApplicationStatusConstant(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the allowed next application statuses.
     *
     * @return unmodifiable set of allowed transitions
     */
    public Set<ApplicationStatusConstant> getAllowedTransitions() {

        return switch (this) {

            case SUBMITTED -> Collections.unmodifiableSet(
                    EnumSet.of(
                            UNDER_REVIEW,
                            WITHDRAWN,
                            CANCELLED));

            case UNDER_REVIEW -> Collections.unmodifiableSet(
                    EnumSet.of(
                            NEEDS_CORRECTION,
                            APPROVED,
                            REJECTED,
                            WITHDRAWN,
                            CANCELLED));

            case NEEDS_CORRECTION -> Collections.unmodifiableSet(
                    EnumSet.of(
                            UNDER_REVIEW,
                            WITHDRAWN,
                            CANCELLED));

            case APPROVED, REJECTED, WITHDRAWN, CANCELLED ->
                Collections.emptySet();
        };
    }

    /**
     * Checks whether the application can transition to the supplied status.
     *
     * @param nextStatus proposed next application status
     * @return true if the transition is allowed
     */
    public boolean canTransitionTo(final ApplicationStatusConstant nextStatus) {

        return nextStatus != null && getAllowedTransitions().contains(nextStatus);
    }

    /**
     * Indicates whether the current application status is terminal.
     *
     * @return true if no further lifecycle transition is permitted
     */
    public boolean isTerminal() {
        return getAllowedTransitions().isEmpty();
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