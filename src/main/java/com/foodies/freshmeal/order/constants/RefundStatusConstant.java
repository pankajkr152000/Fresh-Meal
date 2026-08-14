package com.foodies.freshmeal.order.constants;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.foodies.freshmeal.common.contract.IDisplayOption;
import com.foodies.freshmeal.common.dto.DisplayOptionResponse;

/**
 * ============================================================================
 * Enum : RefundStatusConstant
 * ============================================================================
 *
 * Represents the lifecycle status of a refund associated with an order
 * payment.
 *
 * Refund status is maintained independently from PaymentStatusConstant because
 * payment completion and refund processing are separate business processes.
 *
 * ============================================================================
 *
 * Refund Status Meaning
 * ---------------------
 *
 * NOT_APPLICABLE
 * No refund is applicable for the payment.
 *
 * PENDING
 * Refund has been requested but processing has not yet started.
 *
 * PROCESSING
 * Refund is currently being processed by the payment system.
 *
 * COMPLETED
 * Refund has been successfully completed.
 *
 * FAILED
 * Refund processing failed.
 *
 * ============================================================================
 *
 * Allowed Status Transitions
 * --------------------------
 *
 * NOT_APPLICABLE
 * -> No transitions
 *
 * PENDING
 * -> PROCESSING
 *
 * PROCESSING
 * -> COMPLETED
 * -> FAILED
 *
 * FAILED
 * -> PENDING
 *
 * COMPLETED
 * -> No transitions
 *
 * ============================================================================
 */
public enum RefundStatusConstant implements IDisplayOption {

    NOT_APPLICABLE("Not Applicable"),

    PENDING("Pending"),

    PROCESSING("Processing"),

    COMPLETED("Completed"),

    FAILED("Failed");

    /**
     * User-friendly display label.
     */
    private final String displayName;

    /**
     * Creates a refund status.
     *
     * @param displayName user-friendly display label
     */
    RefundStatusConstant(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns all valid next refund statuses.
     *
     * @return immutable set of allowed next statuses
     */
    public Set<RefundStatusConstant> getAllowedTransitions() {

        return switch (this) {

            case NOT_APPLICABLE,
                    COMPLETED ->
                EnumSet.noneOf(RefundStatusConstant.class);

            case PENDING -> EnumSet.of(
                    PROCESSING);

            case PROCESSING -> EnumSet.of(
                    COMPLETED,
                    FAILED);

            case FAILED -> EnumSet.of(
                    PENDING);
        };
    }

    /**
     * Determines whether the refund can transition to the supplied status.
     *
     * @param newStatus target refund status
     * @return true when the transition is allowed; otherwise false
     */
    public boolean canTransitionTo(RefundStatusConstant newStatus) {

        if (newStatus == null) {
            return false;
        }

        return getAllowedTransitions().contains(newStatus);
    }

    /**
     * Returns all valid next refund statuses as display labels.
     *
     * @return immutable set of display labels
     */
    public Set<String> getAllowedTransitionsString() {

        return getAllowedTransitions()
                .stream()
                .filter(status -> status != null)
                // .map(OrderStatusConstant::getLabel)
                .map(status -> status.getLabel())
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Returns all valid next refund statuses as frontend-friendly options.
     *
     * @return immutable set of display options
     */
    public Set<DisplayOptionResponse> getAllowedTransitionOptions() {

        return getAllowedTransitions()
                .stream()
                .map(status -> new DisplayOptionResponse(
                        status.getLabel(),
                        status.name()))
                .collect(Collectors.toUnmodifiableSet());
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