
package com.foodies.freshmeal.cart.constants;

/**
 * ============================================================================
 * Cart API Message Constants
 * ============================================================================
 *
 * Centralized success messages used by Cart APIs.
 *
 * <p>
 * These messages are intended for successful API responses and are separate
 * from CartErrorConstants, which contains business and validation errors.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public final class CartApiMessageConstants {

    /**
     * Private constructor.
     */
    private CartApiMessageConstants() {
        throw new IllegalStateException("Utility class");
    }

    // =========================================================================
    // Cart Item CRUD Messages
    // =========================================================================

    /**
     * Cart item added successfully.
     */
    public static final String CART_ITEM_CREATED = "Cart item added successfully.";

    /**
     * Cart item quantity updated successfully.
     */
    public static final String CART_ITEM_UPDATED = "Cart item quantity updated successfully.";

    /**
     * Cart item removed successfully.
     */
    public static final String CART_ITEM_DELETED = "Cart item removed successfully.";

    // =========================================================================
    // Cart Management Messages
    // =========================================================================

    /**
     * Cart cleared successfully.
     */
    public static final String CART_CLEARED = "Cart cleared successfully.";

    // =========================================================================
    // Cart Retrieval Messages
    // =========================================================================

    /**
     * Active cart retrieved successfully.
     */
    public static final String ACTIVE_CART_FOUND = "Active cart retrieved successfully.";

    /**
     * Cart summary retrieved successfully.
     */
    public static final String CART_SUMMARY_FOUND = "Cart summary retrieved successfully.";

}