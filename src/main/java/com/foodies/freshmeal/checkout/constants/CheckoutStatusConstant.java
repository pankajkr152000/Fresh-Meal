
package com.foodies.freshmeal.checkout.constants;

import java.util.Collections;
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
 * Checkout status represents the lifecycle of the checkout process itself
 * and must not be confused with the lifecycle of an order or payment.
 * </p>
 *
 * <p>
 * A checkout session captures the customer's purchase intent, validates
 * the cart and its contents, preserves the reviewed information, and
 * coordinates the transition toward order creation.
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
 * CONFIRMATION_IN_PROGRESS
 * ------------------------
 * The customer has confirmed the checkout, and the system is coordinating
 * order creation.
 *
 * <p>
 * This state prevents duplicate confirmation processing and allows the
 * system to reconcile uncertain outcomes before attempting recovery.
 * </p>
 *
 * ORDER_CREATED
 * -------------
 * An order has been successfully created and associated with the checkout.
 * This is a terminal business state.
 *
 * FAILED
 * ------
 * Checkout processing has failed. A new attempt may be permitted only
 * after the failure has been classified and retry eligibility established.
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
     * Customer has confirmed the checkout and order creation is in progress.
     */
    CONFIRMATION_IN_PROGRESS("Confirmation In Progress"),

    /**
     * An order has been successfully created and associated with checkout.
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
     * <p>
     * The returned set is immutable. Callers cannot modify the lifecycle
     * rules by changing the returned collection.
     * </p>
     *
     * <p>
     * A transition to FAILED from CONFIRMATION_IN_PROGRESS must only occur
     * after the system has established that order creation did not succeed
     * and that no unresolved operation remains.
     * </p>
     *
     * @return immutable set of valid next statuses
     */
    public Set<CheckoutStatusConstant> getAllowedTransitions() {

        final EnumSet<CheckoutStatusConstant> allowedTransitions = switch (this) {

            case INITIATED -> EnumSet.of(
                    VALIDATING);

            case VALIDATING -> EnumSet.of(
                    READY_FOR_CONFIRMATION,
                    FAILED);

            case READY_FOR_CONFIRMATION -> EnumSet.of(
                    VALIDATING,
                    CONFIRMATION_IN_PROGRESS,
                    CANCELLED,
                    EXPIRED);

            case CONFIRMATION_IN_PROGRESS -> EnumSet.of(
                    ORDER_CREATED,
                    FAILED);

            case FAILED -> EnumSet.of(
                    INITIATED);

            case ORDER_CREATED, CANCELLED, EXPIRED ->
                EnumSet.noneOf(CheckoutStatusConstant.class);
        };

        return Collections.unmodifiableSet(allowedTransitions);
    }

    /**
     * Determines whether the checkout can transition to the supplied status.
     *
     * <p>
     * This method checks only the validity of the lifecycle transition.
     * It does not replace the additional business checks required before
     * executing a transition.
     * </p>
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
     * <p>
     * The returned set is immutable and follows the natural enum order
     * of the allowed statuses.
     * </p>
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
     * <p>
     * The returned set is immutable. Its iteration order follows the
     * natural enum order of the allowed statuses.
     * </p>
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
     * Determines whether the current status is terminal.
     *
     * @return {@code true} when no further lifecycle transitions are allowed
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