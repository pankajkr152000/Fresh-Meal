
package com.foodies.freshmeal.cart.constants;

/**
 * ============================================================================
 * Cart API Endpoint Constants
 * ============================================================================
 *
 * Centralized API endpoint definitions for the Cart module.
 *
 * <p>
 * Controllers should use these constants instead of hard-coding endpoint
 * paths.
 * </p>
 *
 * <p>
 * Backend CRUD terminology for new FreshMeal modules:
 *
 * <ul>
 * <li>Create</li>
 * <li>Read</li>
 * <li>Update</li>
 * <li>Delete</li>
 * </ul>
 * </p>
 *
 * <p>
 * Cart-specific operations such as clearing the cart and retrieving its
 * summary are defined separately to keep the API contract explicit.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @version 1.0
 */
public final class CartApiConstants {

    /**
     * Private constructor.
     */
    private CartApiConstants() {
        throw new IllegalStateException("Utility class");
    }

    // =========================================================================
    // Base URL
    // =========================================================================

    /**
     * Base URL for all Cart APIs.
     */
    public static final String BASE_URL = "/api/cart";

    // =========================================================================
    // Cart Item CRUD APIs
    // =========================================================================

    /**
     * Adds a food item to the authenticated customer's active cart.
     *
     * <p>
     * POST /api/cart/items/create
     * </p>
     */
    public static final String CREATE_ITEM = "/items/create";

    /**
     * Updates the quantity of an existing cart item.
     *
     * <p>
     * The requested quantity represents the final desired quantity rather
     * than an increment.
     * </p>
     *
     * <p>
     * PUT /api/cart/items/update
     * </p>
     */
    public static final String UPDATE_ITEM = "/items/update";

    /**
     * Removes a food item from the authenticated customer's active cart.
     *
     * <p>
     * DELETE /api/cart/items/delete
     * </p>
     */
    public static final String DELETE_ITEM = "/items/delete";

    // =========================================================================
    // Cart Management APIs
    // =========================================================================

    /**
     * Removes all items from the authenticated customer's active cart.
     *
     * <p>
     * The cart document is retained and remains reusable after clearing.
     * </p>
     *
     * <p>
     * DELETE /api/cart/clear
     * </p>
     */
    public static final String CLEAR_CART = "/clear";

    // =========================================================================
    // Cart Retrieval APIs
    // =========================================================================

    /**
     * Retrieves the authenticated customer's active cart.
     *
     * <p>
     * If no active cart exists, an empty cart response is returned.
     * This operation does not create a cart.
     * </p>
     *
     * <p>
     * GET /api/cart/readActiveCart
     * </p>
     */
    public static final String READ_ACTIVE_CART = "/readActiveCart";

    /**
     * Retrieves a lightweight summary of the authenticated customer's
     * active cart.
     *
     * <p>
     * The response contains the cart status, total item count, total
     * quantity and subtotal.
     * </p>
     *
     * <p>
     * GET /api/cart/readSummary
     * </p>
     */
    public static final String READ_CART_SUMMARY = "/readSummary";

}