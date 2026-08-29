package com.foodies.freshmeal.restaurant.constants;

/**
 * ============================================================================
 * Restaurant Branch API Endpoint Constants
 * ============================================================================
 *
 * Centralized API endpoint definitions for the Restaurant Branch module.
 *
 * <p>
 * Controllers should use these constants instead of hard-coding endpoint
 * paths.
 * </p>
 *
 * <p>
 * Restaurant Branch APIs are maintained separately from Restaurant APIs because
 * a branch represents a distinct physical operating location belonging to a
 * Restaurant.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public final class RestaurantBranchApiConstants {

    /**
     * Private constructor.
     */
    private RestaurantBranchApiConstants() {
        throw new IllegalStateException("Utility class");
    }

    // =========================================================================
    // Base URL
    // =========================================================================

    /**
     * Base URL for all Restaurant Branch APIs.
     */
    public static final String BASE_URL = "/api/restaurant-branches";

    // =========================================================================
    // Restaurant Branch CRUD APIs
    // =========================================================================

    /**
     * Creates a new restaurant branch.
     *
     * <p>
     * POST /api/restaurant-branches/create
     * </p>
     */
    public static final String CREATE = "/create";

    /**
     * Retrieves all active restaurant branches.
     *
     * <p>
     * GET /api/restaurant-branches/readAllBranches
     * </p>
     */
    public static final String READ_ALL_BRANCHES = "/readAllBranches";

    /**
     * Retrieves a restaurant branch by its identifier.
     *
     * <p>
     * The branch identifier is supplied through the request body.
     * </p>
     *
     * <p>
     * POST /api/restaurant-branches/view
     * </p>
     */
    public static final String GET_BRANCH_BY_ID = "/view";

    /**
     * Updates an existing restaurant branch.
     *
     * <p>
     * PUT /api/restaurant-branches/update
     * </p>
     */
    public static final String UPDATE = "/update";

    // =========================================================================
    // Restaurant Branch Query APIs
    // =========================================================================

    /**
     * Retrieves all active branches belonging to a restaurant.
     *
     * <p>
     * POST /api/restaurant-branches/by-restaurant
     * </p>
     */
    public static final String GET_BY_RESTAURANT_ID = "/by-restaurant";
}