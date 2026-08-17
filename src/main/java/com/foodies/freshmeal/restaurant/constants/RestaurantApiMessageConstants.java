package com.foodies.freshmeal.restaurant.constants;

/**
 * ============================================================================
 * Restaurant API Message Constants
 * ============================================================================
 *
 * Centralized success messages used by Restaurant APIs.
 *
 * <p>
 * These messages are intended for successful API responses and are separate
 * from RestaurantErrorConstants, which contains business and validation
 * errors.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public final class RestaurantApiMessageConstants {

    /**
     * Private constructor.
     */
    private RestaurantApiMessageConstants() {
        throw new IllegalStateException("Utility class");
    }

    // =========================================================================
    // Restaurant CRUD Messages
    // =========================================================================

    /**
     * Restaurant created successfully.
     */
    public static final String RESTAURANT_CREATED = "Restaurant created successfully.";

    /**
     * Restaurant list retrieved successfully.
     */
    public static final String RESTAURANT_LIST_FOUND = "Restaurants retrieved successfully.";

    /**
     * Restaurant retrieved successfully.
     */
    public static final String RESTAURANT_FOUND = "Restaurant retrieved successfully.";

    /**
     * Restaurant updated successfully.
     */
    public static final String RESTAURANT_UPDATED = "Restaurant updated successfully.";

}