
package com.foodies.freshmeal.checkout.constants;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.foodies.freshmeal.common.contract.IDisplayOption;
import com.foodies.freshmeal.common.dto.DisplayOptionResponse;

/**
 * ============================================================================
 * Enum : CheckoutStatusConstant
 * ============================================================================
 *
 * Represents the business lifecycle of a customer's checkout session.
 *
 * <p>
 * Checkout status represents the lifecycle of the checkout session itself
 * and must not be confused with the lifecycle of an order or payment.
 * </p>
 *
 * <p>
 * A checkout session captures the customer's purchase intent, validates
 * the cart and its contents, and preserves a snapshot of the information
 * presented to the customer for confirmation.
 * </p>
 *
 * ============================================================================
 * Lifecycle
 * ============================================================================
 *
 * INITIATED
 * ---------
 * A checkout session has been created and is ready for validation.
 *
 * VALIDATING
 * ----------
 * The system is validating the cart, restaurant, food availability,
 * pricing, and delivery information.
 *
 * READY_FOR_CONFIRMATION
 * ----------------------
 * Validation has completed successfully. The customer can review the
 * checkout details and proceed with confirmation.
 *
 * CONFIRMED
 * ---------
 * The customer has confirmed the checkout. The system can proceed with
 * order creation.
 *
 * ORDER_CREATED
 * -------------
 * An order has been successfully created from the checkout session.
 * This is a terminal business state.
 *
 * FAILED
 * ------
 * Checkout processing has failed. A failed checkout may be restarted
 * when the applicable business rules permit it.
 *
 * CANCELLED
 * ---------
 * The customer or system has cancelled the checkout session.
 * This is a terminal business state.
 *
 * EXPIRED
 * -------
 * The checkout session has exceeded its configured validity period.
 * This is a terminal business state.
 *
 * ============================================================================
 */
public enum CheckoutStatusConstant implements IDisplayOption {

    /**
     * Checkout session has been initiated.
     */
    INITIATED("Initiated"),

    /**
     * Checkout session is undergoing validation.
     */
    VALIDATING("Validating"),

    /**
     * Checkout session is ready for customer confirmation.
     */
    READY_FOR_CONFIRMATION("Ready For Confirmation"),

    /**
     * Customer has confirmed the checkout.
     */
    CONFIRMED("Confirmed"),

    /**
     * An order has been successfully created.
     */
    ORDER_CREATED("Order Created"),

    /**
     * Checkout processing has failed.
     */
    FAILED("Failed"),

    /**
     * Checkout session has been cancelled.
     */
    CANCELLED("Cancelled"),

    /**
     * Checkout session has expired.
     */
    EXPIRED("Expired");

    /**
     * User-friendly display name of the checkout status.
     */
    private final String displayName;

    /**
     * Creates a checkout status with its corresponding display name.
     *
     * @param displayName human-readable status name
     */
    CheckoutStatusConstant(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the statuses that may legally follow the current status.
     *
     * <p>
     * Transition rules are maintained inside the domain constant so that
     * checkout lifecycle behavior remains centralized and reusable across
     * service, validation, and API layers.
     * </p>
     *
     * @return set of valid next statuses
     */
    public Set<CheckoutStatusConstant> getAllowedTransitions() {

        return switch (this) {

            case INITIATED -> EnumSet.of(
                    VALIDATING);

            case VALIDATING -> EnumSet.of(
                    READY_FOR_CONFIRMATION,
                    FAILED);

            case READY_FOR_CONFIRMATION -> EnumSet.of(
                    VALIDATING,
                    CONFIRMED,
                    CANCELLED,
                    EXPIRED);

            case CONFIRMED -> EnumSet.of(
                    ORDER_CREATED,
                    FAILED);

            case FAILED -> EnumSet.of(
                    INITIATED);

            case ORDER_CREATED, CANCELLED, EXPIRED ->
                EnumSet.noneOf(CheckoutStatusConstant.class);
        };
    }

    /**
     * Determines whether the checkout can transition to the supplied status.
     *
     * @param newStatus target status
     * @return {@code true} when the transition is allowed
     */
    public boolean canTransitionTo(
            final CheckoutStatusConstant newStatus) {

        return newStatus != null
                && getAllowedTransitions().contains(newStatus);
    }

    /**
     * Returns the allowed transition display names.
     *
     * @return immutable set of valid next-status display names
     */
    public Set<String> getAllowedTransitionsString() {

        return getAllowedTransitions()
                .stream()
                .map(checkoutStatusConstant -> checkoutStatusConstant.getLabel())
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