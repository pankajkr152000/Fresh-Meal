package com.foodies.freshmeal.user.constants;

/**
 * ============================================================================
 * User API Constants
 * ============================================================================
 *
 * <p>
 * Centralized API endpoint constants for the FreshMeal User module.
 * </p>
 *
 * <p>
 * These constants define endpoint paths relative to the User module base URL
 * defined in {@link com.foodies.freshmeal.common.constants.ApiBaseConstants}.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public final class UserApiConstants {

    private UserApiConstants() {
    }

    // =========================================================================
    // User Operations
    // =========================================================================

    /**
     * Add user endpoint.
     */
    public static final String ADD = "/add";

    /**
     * Get user by user number endpoint.
     */
    public static final String GET_BY_USER_NUMBER = "/get-by-user-number";

    /**
     * Update user endpoint.
     */
    public static final String UPDATE = "/update";

    /**
     * Delete user endpoint.
     */
    public static final String DELETE = "/delete";

    /**
     * Enable user endpoint.
     */
    public static final String ENABLE = "/enable";

    /**
     * Disable user endpoint.
     */
    public static final String DISABLE = "/disable";

    /**
     * Lock user endpoint.
     */
    public static final String LOCK = "/lock";

    /**
     * Unlock user endpoint.
     */
    public static final String UNLOCK = "/unlock";
}