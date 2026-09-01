package com.foodies.freshmeal.common.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

/**
 * ============================================================================
 * Enum : RoleType
 * ============================================================================
 *
 * Defines the application-level roles available within FreshMeal.
 *
 * <p>
 * {@code RoleType} represents the business-level authorization roles assigned
 * to users. These roles are persisted as part of {@code UserEntity} and are
 * later converted into framework-specific authorities by the security layer.
 * </p>
 *
 * <h3>FreshMeal Roles</h3>
 * <ul>
 * <li>
 * {@link #ADMIN} -
 * Administrative user with application-level administrative
 * privileges.
 * </li>
 * <li>
 * {@link #USER} -
 * Standard FreshMeal application user who can use customer-facing
 * functionality.
 * </li>
 * <li>
 * {@link #RESTAURANT_OWNER} -
 * User responsible for managing one or more FreshMeal restaurants
 * or restaurant branches.
 * </li>
 * <li>
 * {@link #DELIVERY_PARTNER} -
 * User responsible for handling delivery-related operations.
 * </li>
 * </ul>
 *
 * <h3>Authorization Design</h3>
 * <p>
 * Roles define broad business responsibilities. Fine-grained permissions
 * should be introduced separately if FreshMeal eventually requires
 * permission-level authorization.
 * </p>
 *
 * <p>
 * For example, a future authorization model may map:
 * </p>
 *
 * <pre>
 * RESTAURANT_OWNER
 *        ↓
 * RESTAURANT_READ
 * RESTAURANT_UPDATE
 * FOOD_CREATE
 * FOOD_UPDATE
 * </pre>
 *
 * <h3>System Operations</h3>
 * <p>
 * Internal system actions should not be represented as a normal user role.
 * System-generated operations should instead be identified through the
 * application's audit/actor model when such functionality is introduced.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public enum RoleType implements IDisplayOption {

    /*
     * default
     */
    SYSTEM("System"),
    // =========================================================================
    // Administrative Roles
    // =========================================================================

    /**
     * Application administrator.
     */
    ADMIN("Admin"),

    // =========================================================================
    // Application User Roles
    // =========================================================================

    /**
     * Standard FreshMeal application user.
     */
    USER("User"),

    /**
     * Restaurant owner responsible for restaurant operations.
     */
    RESTAURANT_OWNER("Restaurant Owner"),

    /**
     * Delivery partner responsible for delivery operations.
     */
    DELIVERY_PARTNER("Delivery Partner");

    // =========================================================================
    // Fields
    // =========================================================================

    /**
     * User-friendly display label.
     */
    private final String displayName;

    // =========================================================================
    // Constructor
    // =========================================================================

    /**
     * Creates a role type.
     *
     * @param displayName user-friendly display label.
     */
    RoleType(String displayName) {
        this.displayName = displayName;
    }

    // =========================================================================
    // IDisplayOption
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return displayName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return name();
    }
}