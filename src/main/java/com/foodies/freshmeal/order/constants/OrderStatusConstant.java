package com.foodies.freshmeal.order.constants;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.foodies.freshmeal.common.contract.IDisplayOption;
import com.foodies.freshmeal.common.dto.DisplayOptionResponse;

/**
 * ============================================================================
 * Enum : OrderStatusConstant
 * ============================================================================
 *
 * Represents the overall lifecycle status of a customer order.
 *
 * A single order can have only one business status at a time.
 *
 * The status controls the overall business lifecycle of the order from
 * placement through preparation, delivery, completion, failure, cancellation,
 * or return.
 *
 * ============================================================================
 *
 * Status Meaning
 * --------------
 *
 * PLACED
 * Order has been successfully placed by the customer.
 *
 * CONFIRMED
 * Restaurant has accepted the order.
 *
 * PREPARING
 * Restaurant kitchen has started preparing the order.
 *
 * READY_FOR_PICKUP
 * Food is prepared and ready for collection by the delivery partner.
 *
 * PICKED_UP
 * Delivery partner has collected the order from the restaurant.
 *
 * OUT_FOR_DELIVERY
 * Delivery partner is currently delivering the order.
 *
 * DELIVERED
 * Customer has successfully received the order.
 *
 * CANCELLED
 * Order has been cancelled before successful completion.
 *
 * DELIVERY_FAILED
 * Delivery attempt was unsuccessful.
 *
 * RETURNED
 * Order could not be delivered and has been returned/closed.
 *
 * ============================================================================
 *
 * Allowed Status Transitions
 * --------------------------
 *
 * PLACED
 * -> CONFIRMED
 * -> CANCELLED
 *
 * CONFIRMED
 * -> PREPARING
 * -> CANCELLED
 *
 * PREPARING
 * -> READY_FOR_PICKUP
 * -> CANCELLED
 *
 * READY_FOR_PICKUP
 * -> PICKED_UP
 *
 * PICKED_UP
 * -> OUT_FOR_DELIVERY
 *
 * OUT_FOR_DELIVERY
 * -> DELIVERED
 * -> DELIVERY_FAILED
 *
 * DELIVERY_FAILED
 * -> OUT_FOR_DELIVERY
 * -> RETURNED
 *
 * DELIVERED
 * -> No transitions
 *
 * CANCELLED
 * -> No transitions
 *
 * RETURNED
 * -> No transitions
 *
 * ============================================================================
 */
public enum OrderStatusConstant implements IDisplayOption {

    PLACED("Placed"),

    CONFIRMED("Confirmed"),

    PREPARING("Preparing"),

    READY_FOR_PICKUP("Ready For Pickup"),

    PICKED_UP("Picked Up"),

    OUT_FOR_DELIVERY("Out For Delivery"),

    DELIVERED("Delivered"),

    CANCELLED("Cancelled"),

    DELIVERY_FAILED("Delivery Failed"),

    RETURNED("Returned");

    /**
     * User-friendly display label.
     */
    private final String displayName;

    /**
     * Creates an order status with its corresponding display label.
     *
     * @param displayName user-friendly display label
     */
    OrderStatusConstant(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns all valid next statuses for the current status.
     *
     * <p>
     * This method represents the business state machine for the order.
     * </p>
     *
     * @return set of valid next statuses
     */
    public Set<OrderStatusConstant> getAllowedTransitions() {

        return switch (this) {

            case PLACED -> EnumSet.of(
                    CONFIRMED,
                    CANCELLED);

            case CONFIRMED -> EnumSet.of(
                    PREPARING,
                    CANCELLED);

            case PREPARING -> EnumSet.of(
                    READY_FOR_PICKUP,
                    CANCELLED);

            case READY_FOR_PICKUP -> EnumSet.of(
                    PICKED_UP);

            case PICKED_UP -> EnumSet.of(
                    OUT_FOR_DELIVERY);

            case OUT_FOR_DELIVERY -> EnumSet.of(
                    DELIVERED,
                    DELIVERY_FAILED);

            case DELIVERY_FAILED -> EnumSet.of(
                    OUT_FOR_DELIVERY,
                    RETURNED);

            case DELIVERED,
                    CANCELLED,
                    RETURNED ->
                EnumSet.noneOf(OrderStatusConstant.class);
        };
    }

    /**
     * Determines whether the order can transition from the current status
     * to the supplied target status.
     *
     * @param newStatus target order status
     * @return true when the transition is allowed; otherwise false
     */
    public boolean canTransitionTo(OrderStatusConstant newStatus) {

        if (newStatus == null) {
            return false;
        }

        return getAllowedTransitions().contains(newStatus);
    }

    /**
     * Returns the allowed transition labels.
     *
     * <p>
     * This method is retained for compatibility with the Food module's
     * status pattern.
     * </p>
     *
     * @return immutable set of allowed transition labels
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
     * Returns the allowed transitions as UI-friendly options.
     *
     * <p>
     * This method is useful when the frontend needs to dynamically populate
     * the available status actions for the current order status.
     * </p>
     *
     * @return immutable set of allowed display options
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