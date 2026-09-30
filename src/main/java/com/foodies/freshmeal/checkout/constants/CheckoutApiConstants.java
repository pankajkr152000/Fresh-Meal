
package com.foodies.freshmeal.checkout.constants;

/**
 * ============================================================================
 * Class : CheckoutApiConstants
 * ============================================================================
 *
 * Centralized API endpoint constants for the Checkout module.
 *
 * <p>
 * Maintaining endpoint paths in a single location improves consistency,
 * simplifies maintenance, and prevents hard-coded URLs across controllers.
 * </p>
 *
 * ============================================================================
 */
public final class CheckoutApiConstants {

    /**
     * Base URL for all checkout-related APIs.
     */
    public static final String BASE_URL = "/api/checkout";

    /**
     * Initiates a new checkout session.
     */
    public static final String INITIATE_CHECKOUT = "/initiate";

    /**
     * Retrieves the customer's active checkout session.
     */
    public static final String READ_ACTIVE_CHECKOUT = "/readActiveCheckout";

    /**
     * Retrieves checkout details by checkout number.
     */
    public static final String READ_CHECKOUT_BY_NUMBER = "/readByNumber";

    /**
     * Validates the checkout session and refreshes its review snapshot.
     */
    public static final String VALIDATE_CHECKOUT = "/validate";

    /**
     * Confirms the checkout and initiates order creation.
     */
    public static final String CONFIRM_CHECKOUT = "/confirm";

    /**
     * Cancels an active checkout session.
     */
    public static final String CANCEL_CHECKOUT = "/cancel";

    /**
     * Prevents instantiation of this utility class.
     *
     * @throws IllegalStateException if instantiation is attempted
     */
    private CheckoutApiConstants() {
        throw new IllegalStateException("Utility class");
    }
}