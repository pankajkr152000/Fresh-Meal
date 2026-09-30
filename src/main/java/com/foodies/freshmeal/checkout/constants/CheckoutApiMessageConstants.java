
package com.foodies.freshmeal.checkout.constants;

/**
 * ============================================================================
 * Class : CheckoutApiMessageConstants
 * ============================================================================
 *
 * Centralized success messages for Checkout module API responses.
 *
 * <p>
 * Keeping API messages in a dedicated constants class ensures consistency
 * across controllers and simplifies future message updates.
 * </p>
 *
 * <p>
 * This class contains only success messages. Error codes and error messages
 * must be maintained separately in CheckoutErrorConstants.
 * </p>
 *
 * ============================================================================
 */
public final class CheckoutApiMessageConstants {

    /**
     * Success message returned when a checkout session is initiated.
     */
    public static final String CHECKOUT_INITIATED = "Checkout session initiated successfully.";

    /**
     * Success message returned when the active checkout is retrieved.
     */
    public static final String ACTIVE_CHECKOUT_FOUND = "Active checkout retrieved successfully.";

    /**
     * Success message returned when checkout details are retrieved.
     */
    public static final String CHECKOUT_FOUND = "Checkout details retrieved successfully.";

    /**
     * Success message returned when checkout validation completes.
     */
    public static final String CHECKOUT_VALIDATED = "Checkout validated successfully.";

    /**
     * Success message returned when checkout confirmation succeeds.
     */
    public static final String CHECKOUT_CONFIRMED = "Checkout confirmed successfully.";

    /**
     * Success message returned when checkout cancellation succeeds.
     */
    public static final String CHECKOUT_CANCELLED = "Checkout cancelled successfully.";

    /**
     * Prevents instantiation of this utility class.
     *
     * @throws IllegalStateException if instantiation is attempted
     */
    private CheckoutApiMessageConstants() {
        throw new IllegalStateException("Utility class");
    }
}