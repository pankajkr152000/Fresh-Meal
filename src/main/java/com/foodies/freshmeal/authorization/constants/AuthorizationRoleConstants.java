package com.foodies.freshmeal.authorization.constants;

import com.foodies.freshmeal.common.constants.RoleType;

/**
 * ============================================================================
 * Constants : AuthorizationRoleConstants
 * ============================================================================
 *
 * <p>
 * Centralized Spring Security role expressions used by the FreshMeal
 * authorization layer.
 * </p>
 *
 * <p>
 * Business roles themselves remain defined by {@link RoleType}. This class
 * only provides framework-facing authorization expressions so controllers
 * and services do not duplicate role strings throughout the application.
 * </p>
 *
 * <h3>Authorization Model</h3>
 *
 * <p>
 * FreshMeal currently uses role-based authorization. Fine-grained resource
 * ownership and permission checks will be introduced at the business-service
 * level where required.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public final class AuthorizationRoleConstants {

    /**
     * Prevents instantiation of this constants class.
     */
    private AuthorizationRoleConstants() {
    }

    // =========================================================================
    // Role Expressions
    // =========================================================================

    /**
     * Administrative role expression.
     */
    public static final String ADMIN =
            "hasRole('" + RoleType.ADMIN + "')";

    /**
     * Standard application-user role expression.
     */
    public static final String USER =
            "hasRole('" + RoleType.USER + "')";

    /**
     * Restaurant-owner role expression.
     */
    public static final String RESTAURANT_OWNER =
            "hasRole('" + RoleType.RESTAURANT_OWNER + "')";

    /**
     * Delivery-partner role expression.
     */
    public static final String DELIVERY_PARTNER =
            "hasRole('" + RoleType.DELIVERY_PARTNER + "')";

    /**
     * Administrator or restaurant-owner role expression.
     */
    public static final String ADMIN_OR_RESTAURANT_OWNER =
            "hasAnyRole('"
                    + RoleType.ADMIN
                    + "','"
                    + RoleType.RESTAURANT_OWNER
                    + "')";

    /**
     * Administrator or delivery-partner role expression.
     */
    public static final String ADMIN_OR_DELIVERY_PARTNER =
            "hasAnyRole('"
                    + RoleType.ADMIN
                    + "','"
                    + RoleType.DELIVERY_PARTNER
                    + "')";

    /**
     * Administrator or standard-user role expression.
     */
    public static final String ADMIN_OR_USER =
            "hasAnyRole('"
                    + RoleType.ADMIN
                    + "','"
                    + RoleType.USER
                    + "')";
}