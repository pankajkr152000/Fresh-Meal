package com.foodies.freshmeal.restaurant.constants;

/**
 * ============================================================================
 * Restaurant API Endpoint Constants
 * ============================================================================
 *
 * Centralized API endpoint definitions for the Restaurant module.
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
 *
 * <p>
 * The existing Food module is intentionally not modified because it is
 * already stable and uses its established API conventions.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @version 1.0
 */
public final class RestaurantApiConstants {

    /**
     * Private constructor.
     */
    private RestaurantApiConstants() {
        throw new IllegalStateException("Utility class");
    }

    // =========================================================================
    // Base URL
    // =========================================================================

    /**
     * Base URL for all Restaurant APIs.
     */
    public static final String BASE_URL = "/api/restaurants";

    // =========================================================================
    // Restaurant CRUD APIs
    // =========================================================================

    /**
     * Creates a new restaurant.
     *
     * POST /api/restaurants/create
     */
    public static final String CREATE = "/create";

    /**
     * Retrieves all active restaurants.
     *
     * GET /api/restaurants/readAllRestaurants
     */
    public static final String READ_ALL_RESTAURANTS = "/readAllRestaurants";

    /**
     * Retrieves a restaurant by its identifier.
     *
     * <p>
     * The restaurant identifier is supplied through the request body,
     * following the existing Food view API convention.
     * </p>
     *
     * POST /api/restaurants/view
     */
    public static final String GET_RESTAURANT_BY_ID = "/view";

    /**
     * Updates an existing restaurant.
     *
     * PUT /api/restaurants/update
     */
    public static final String UPDATE = "/update";

    // =========================================================================
    // Restaurant Search APIs
    // =========================================================================

    /**
     * General Restaurant search.
     */
    public static final String SEARCH = "/search";

    /**
     * Searches restaurants by identifier.
     */
    public static final String SEARCH_BY_ID = "/search/id";

    /**
     * Searches restaurants by name.
     */
    public static final String SEARCH_BY_NAME = "/search/name";

    /**
     * Searches restaurants by cuisine type.
     */
    public static final String SEARCH_BY_CUISINE = "/search/cuisine";

    // =========================================================================
    // Restaurant Metadata APIs
    // =========================================================================

    /**
     * Retrieves available restaurant cuisine types.
     */
    public static final String CUISINE_TYPES = "/metadata/cuisine-types";

    /**
     * Retrieves Restaurant metadata.
     */
    public static final String RESTAURANT_METADATA = "/metadata";

}