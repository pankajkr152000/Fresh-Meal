package com.foodies.freshmeal.order.constants;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.foodies.freshmeal.common.contract.IDisplayOption;
import com.foodies.freshmeal.common.dto.DisplayOptionResponse;

/**
 * ============================================================================
 * Enum : PaymentStatusConstant
 * ============================================================================
 *
 * Represents the lifecycle status of an order payment.
 *
 * Payment status is maintained independently from OrderStatusConstant because
 * an order and its payment can progress through different lifecycles.
 *
 * ============================================================================
 *
 * Payment Lifecycle
 * -----------------
 *
 * PENDING
 * Payment is expected but has not yet been initiated or completed.
 *
 * INITIATED
 * Payment attempt has been initiated with the payment provider.
 *
 * SUCCESS
 * Payment has been successfully completed.
 *
 * FAILED
 * Payment attempt failed.
 *
 * CANCELLED
 * Payment attempt was cancelled before successful completion.
 *
 * PARTIALLY_REFUNDED
 * A portion of a successful payment has been refunded.
 *
 * REFUNDED
 * The complete successful payment amount has been refunded.
 *
 * ============================================================================
 *
 * Allowed Status Transitions
 * --------------------------
 *
 * PENDING
 * -> INITIATED
 * -> CANCELLED
 *
 * INITIATED
 * -> SUCCESS
 * -> FAILED
 * -> CANCELLED
 *
 * FAILED
 * -> INITIATED
 *
 * SUCCESS
 * -> PARTIALLY_REFUNDED
 * -> REFUNDED
 *
 * PARTIALLY_REFUNDED
 * -> REFUNDED
 *
 * CANCELLED
 * -> No transitions
 *
 * REFUNDED
 * -> No transitions
 *
 * ============================================================================
 */
public enum PaymentStatusConstant implements IDisplayOption {

    PENDING("Pending"),

    INITIATED("Initiated"),

    SUCCESS("Success"),

    FAILED("Failed"),

    CANCELLED("Cancelled"),

    PARTIALLY_REFUNDED("Partially Refunded"),

    REFUNDED("Refunded");

    /**
     * User-friendly display label.
     */
    private final String displayName;

    /**
     * Creates a payment status.
     *
     * @param displayName user-friendly display label
     */
    PaymentStatusConstant(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns all valid next payment statuses.
     *
     * @return immutable set of allowed next statuses
     */
    public Set<PaymentStatusConstant> getAllowedTransitions() {

        return switch (this) {

            case PENDING -> EnumSet.of(
                    INITIATED,
                    CANCELLED);

            case INITIATED -> EnumSet.of(
                    SUCCESS,
                    FAILED,
                    CANCELLED);

            case SUCCESS -> EnumSet.of(
                    PARTIALLY_REFUNDED,
                    REFUNDED);

            case FAILED -> EnumSet.of(
                    INITIATED);

            case PARTIALLY_REFUNDED -> EnumSet.of(
                    REFUNDED);

            case CANCELLED,
                    REFUNDED ->
                EnumSet.noneOf(PaymentStatusConstant.class);
        };
    }

    /**
     * Determines whether a payment can transition to the supplied status.
     *
     * @param newStatus target payment status
     * @return true when the transition is allowed
     */
    public boolean canTransitionTo(PaymentStatusConstant newStatus) {

        if (newStatus == null) {
            return false;
        }

        return getAllowedTransitions().contains(newStatus);
    }

    /**
     * Returns the labels of all valid next payment statuses.
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
     * Returns all valid next payment statuses as frontend-friendly options.
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