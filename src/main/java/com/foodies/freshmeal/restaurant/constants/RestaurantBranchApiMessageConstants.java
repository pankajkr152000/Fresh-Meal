package com.foodies.freshmeal.restaurant.constants;

/**
 * ============================================================================
 * Restaurant Branch API Message Constants
 * ============================================================================
 *
 * Centralized success messages used by Restaurant Branch APIs.
 *
 * <p>
 * These messages are intended for successful API responses and are separate
 * from {@link RestaurantErrorConstants}, which contains business and
 * validation errors.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public final class RestaurantBranchApiMessageConstants {

    /**
     * Private constructor.
     */
    private RestaurantBranchApiMessageConstants() {
        throw new IllegalStateException("Utility class");
    }

    // =========================================================================
    // Restaurant Branch CRUD Messages
    // =========================================================================

    /**
     * Restaurant branch created successfully.
     */
    public static final String RESTAURANT_BRANCH_CREATED = "Restaurant branch created successfully.";

    /**
     * Restaurant branch list retrieved successfully.
     */
    public static final String RESTAURANT_BRANCH_LIST_FOUND = "Restaurant branches retrieved successfully.";

    /**
     * Restaurant branch retrieved successfully.
     */
    public static final String RESTAURANT_BRANCH_FOUND = "Restaurant branch retrieved successfully.";

    /**
     * Restaurant branch updated successfully.
     */
    public static final String RESTAURANT_BRANCH_UPDATED = "Restaurant branch updated successfully.";

    /**
     * Restaurant branches belonging to a restaurant retrieved successfully.
     */
    public static final String RESTAURANT_BRANCH_LIST_BY_RESTAURANT_FOUND = "Restaurant branches for the restaurant retrieved successfully.";
}