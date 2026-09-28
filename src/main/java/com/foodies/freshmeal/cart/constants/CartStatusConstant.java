package com.foodies.freshmeal.cart.constants;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.foodies.freshmeal.common.contract.IDisplayOption;
import com.foodies.freshmeal.common.dto.DisplayOptionResponse;

/**
 * ============================================================================
 * Enum : CartStatus
 * ============================================================================
 *
 * Represents the business lifecycle of a customer's cart.
 *
 * <p>
 * Cart status represents the lifecycle of the cart itself and must not be
 * confused with whether the cart currently contains items.
 * </p>
 *
 * <p>
 * An ACTIVE cart may be empty and can continue to be reused by the customer.
 * </p>
 *
 * ============================================================================
 *
 * Lifecycle
 * ============================================================================
 *
 * ACTIVE
 * -------
 * The cart is available for normal customer operations.
 *
 * CHECKOUT_IN_PROGRESS
 * --------------------
 * The cart is temporarily involved in checkout processing.
 * Normal cart mutations should be restricted while checkout is in progress.
 *
 * CONVERTED
 * ---------
 * The cart has successfully resulted in an order.
 * This is a terminal business state.
 *
 * ABANDONED
 * ---------
 * The cart has not been actively used within the configured abandonment
 * period. It may still be recovered and returned to ACTIVE.
 *
 * EXPIRED
 * -------
 * The cart has exceeded the configured retention period after abandonment.
 * This is a terminal lifecycle state.
 *
 * ============================================================================
 */
public enum CartStatusConstant implements IDisplayOption {

    /**
     * Cart is currently active and available for customer operations.
     */
    ACTIVE("Active"),

    /**
     * Cart is currently participating in checkout processing.
     */
    CHECKOUT_IN_PROGRESS("Checkout In Progress"),

    /**
     * Cart has successfully resulted in an order.
     */
    CONVERTED("Converted"),

    /**
     * Cart has been abandoned due to inactivity.
     */
    ABANDONED("Abandoned"),

    /**
     * Cart has exceeded its retention period and is no longer usable.
     */
    EXPIRED("Expired");

    /**
     * User-friendly display name of the cart status.
     */
    private final String displayName;

    /**
     * Creates a cart status with its corresponding display name.
     *
     * @param displayName human-readable status name
     */
    CartStatusConstant(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the statuses that may legally follow the current status.
     *
     * <p>
     * Transition rules are maintained inside the domain constant so that
     * lifecycle behavior remains centralized and reusable across service,
     * validation, and API layers.
     *
     * @return set of valid next statuses
     */
    public Set<CartStatusConstant> getAllowedTransitions() {

        return switch (this) {

            case ACTIVE -> EnumSet.of(
                    CHECKOUT_IN_PROGRESS,
                    ABANDONED);

            case CHECKOUT_IN_PROGRESS -> EnumSet.of(
                    ACTIVE,
                    CONVERTED);

            case CONVERTED -> EnumSet.noneOf(CartStatusConstant.class);

            case ABANDONED -> EnumSet.of(
                    ACTIVE,
                    EXPIRED);

            case EXPIRED -> EnumSet.noneOf(CartStatusConstant.class);
        };
    }

    /**
     * Determines whether the cart can transition to the supplied status.
     *
     * @param newStatus target status
     * @return {@code true} when the transition is allowed
     */
    public boolean canTransitionTo(final CartStatusConstant newStatus) {

        return newStatus != null
                && getAllowedTransitions().contains(newStatus);
    }

    /**
     * Returns the allowed transition display names.
     *
     * @return immutable set of display names
     */
    public Set<String> getAllowedTransitionsString() {

        return getAllowedTransitions()
                .stream()
                .map(cartStatusConstant -> cartStatusConstant.getLabel())
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Returns the allowed transition options for API responses and UI
     * rendering.
     *
     * @return immutable set of transition options
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