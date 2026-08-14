package com.foodies.freshmeal.order.constants;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.foodies.freshmeal.common.contract.IDisplayOption;
import com.foodies.freshmeal.common.dto.DisplayOptionResponse;

/**
 * ============================================================================
 * Enum : DeliveryStatusConstant
 * ============================================================================
 *
 * Represents the delivery-specific lifecycle of an order.
 *
 * <p>
 * Delivery status is intentionally maintained separately from
 * OrderStatusConstant. Order status represents the overall business lifecycle,
 * while delivery status represents the fulfillment/delivery progress.
 * </p>
 *
 * ============================================================================
 *
 * Delivery Lifecycle
 * ------------------
 *
 * PENDING
 * Delivery process has not yet started.
 *
 * CONFIRMED
 * Delivery has been confirmed and fulfillment can proceed.
 *
 * PREPARING
 * The restaurant is preparing the order for delivery.
 *
 * READY
 * Order is ready to be collected by the delivery partner.
 *
 * OUT_FOR_DELIVERY
 * Delivery partner has collected the order and delivery has started.
 *
 * ON_THE_WAY
 * Delivery partner is travelling towards the customer's location.
 *
 * DELIVERED
 * Order has been successfully delivered.
 *
 * CANCELLED
 * Delivery process has been cancelled.
 *
 * ============================================================================
 *
 * Allowed Status Transitions
 * --------------------------
 *
 * PENDING
 * -> CONFIRMED
 * -> CANCELLED
 *
 * CONFIRMED
 * -> PREPARING
 * -> CANCELLED
 *
 * PREPARING
 * -> READY
 * -> CANCELLED
 *
 * READY
 * -> OUT_FOR_DELIVERY
 * -> CANCELLED
 *
 * OUT_FOR_DELIVERY
 * -> ON_THE_WAY
 * -> DELIVERED
 *
 * ON_THE_WAY
 * -> DELIVERED
 * -> CANCELLED
 *
 * DELIVERED
 * -> No transitions
 *
 * CANCELLED
 * -> No transitions
 *
 * ============================================================================
 */
public enum DeliveryStatusConstant implements IDisplayOption {

    PENDING("Pending"),

    CONFIRMED("Confirmed"),

    PREPARING("Preparing"),

    READY("Ready"),

    OUT_FOR_DELIVERY("Out for Delivery"),

    ON_THE_WAY("On the Way"),

    DELIVERED("Delivered"),

    CANCELLED("Cancelled");

    /**
     * User-friendly display label.
     */
    private final String displayName;

    /**
     * Creates a delivery status.
     *
     * @param displayName user-friendly display label
     */
    DeliveryStatusConstant(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns all valid next delivery statuses for the current status.
     *
     * @return immutable set of allowed next statuses
     */
    public Set<DeliveryStatusConstant> getAllowedTransitions() {

        return switch (this) {

            case PENDING -> EnumSet.of(
                    CONFIRMED,
                    CANCELLED);

            case CONFIRMED -> EnumSet.of(
                    PREPARING,
                    CANCELLED);

            case PREPARING -> EnumSet.of(
                    READY,
                    CANCELLED);

            case READY -> EnumSet.of(
                    OUT_FOR_DELIVERY,
                    CANCELLED);

            case OUT_FOR_DELIVERY -> EnumSet.of(
                    ON_THE_WAY,
                    DELIVERED);

            case ON_THE_WAY -> EnumSet.of(
                    DELIVERED,
                    CANCELLED);

            case DELIVERED,
                    CANCELLED ->
                EnumSet.noneOf(DeliveryStatusConstant.class);
        };
    }

    /**
     * Determines whether the delivery status can transition to the supplied
     * status.
     *
     * @param newStatus target delivery status
     * @return true when the transition is allowed; otherwise false
     */
    public boolean canTransitionTo(DeliveryStatusConstant newStatus) {

        if (newStatus == null) {
            return false;
        }

        return getAllowedTransitions().contains(newStatus);
    }

    /**
     * Returns all valid next delivery statuses as display labels.
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
     * Returns all valid next delivery statuses as frontend-friendly options.
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