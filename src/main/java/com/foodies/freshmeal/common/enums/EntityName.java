// package com.foodies.freshmeal.common.enums;

// /**
//  * ============================================================================
//  * EntityName
//  * ============================================================================
//  *
//  * Represents the unique entity names used across the FreshMeal application.
//  *
//  * These values are primarily used for:
//  * <ul>
//  * <li>Database sequence generation</li>
//  * <li>Audit logging</li>
//  * <li>Entity identification</li>
//  * <li>Generic utilities</li>
//  * </ul>
//  *
//  * @author Pankaj Kumar
//  * @since 1.0
//  *        ============================================================================
//  */
// public enum EntityName {

//     DATABASE_SEQUENCE,

//     USER_ENTITY,

//     USER_PROFILE_ENTITY,

//     LOGIN_HISTORY_ENTITY,

//     FOOD_ENTITY,

//     ORDER_ENTITY,

//     PRODUCT_ENTITY,

//     IMAGE_ENTITY,

//     RESTAURANT_ENTITY,

//     DELIVERY_PARTNER_ENTITY,

//     CATEGORY_ENTITY,

//     COUPON_ENTITY,

//     PAYMENT_ENTITY,

//     REFUND_ENTITY,

//     REVIEW_ENTITY,

//     CART_ENTITY,

//     NOTIFICATION_ENTITY

// }

package com.foodies.freshmeal.common.enums;

/**
 * ============================================================================
 * Enum : EntityName
 * ============================================================================
 *
 * Represents all persistent entities within the FreshMeal application.
 *
 * <p>
 * Each entity is assigned a unique code which is used for:
 * </p>
 * <ul>
 * <li>Database sequence generation</li>
 * <li>Audit logging</li>
 * <li>Entity identification</li>
 * <li>Generic utility operations</li>
 * </ul>
 *
 * <p>
 * <strong>Note:</strong>
 * Entity codes must remain stable once released.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 *        ============================================================================
 */
public enum EntityName {

    /**
     * Database sequence entity.
     */
    DATABASE_SEQUENCE("DBS", "Database Sequence"),

    /**
     * User entity.
     */
    USER_ENTITY("USR", "User"),

    /**
     * User profile entity.
     */
    USER_PROFILE_ENTITY("UPR", "User Profile"),

    /**
     * Login history entity.
     */
    LOGIN_HISTORY_ENTITY("LGH", "Login History"),

    /**
     * Food entity.
     */
    FOOD_ENTITY("FOD", "Food"),

    /**
     * Order entity.
     */
    ORDER_ENTITY("ORD", "Order"),

    /**
     * Product entity.
     */
    PRODUCT_ENTITY("PRD", "Product"),

    /**
     * Image entity.
     */
    IMAGE_ENTITY("IMG", "Image"),

    /**
     * Restaurant entity.
     */
    RESTAURANT_ENTITY("RST", "Restaurant"),

    /**
     * Delivery partner entity.
     */
    DELIVERY_PARTNER_ENTITY("DLP", "Delivery Partner"),

    /**
     * Category entity.
     */
    CATEGORY_ENTITY("CAT", "Category"),

    /**
     * Coupon entity.
     */
    COUPON_ENTITY("CPN", "Coupon"),

    /**
     * Payment entity.
     */
    PAYMENT_ENTITY("PAY", "Payment"),

    /**
     * Refund entity.
     */
    REFUND_ENTITY("RFD", "Refund"),

    /**
     * Review entity.
     */
    REVIEW_ENTITY("REV", "Review"),

    /**
     * Cart entity.
     */
    CART_ENTITY("CRT", "Cart"),

    /**
     * Notification entity.
     */
    NOTIFICATION_ENTITY("NTF", "Notification"),

    /**
     * Address entity.
     */
    ADDRESS_ENTITY("ADR", "Address");

    /**
     * Unique entity code.
     */
    private final String code;

    /**
     * User-friendly entity name.
     */
    private final String displayName;

    /**
     * Creates an entity.
     *
     * @param code        unique entity code
     * @param displayName user-friendly entity name
     */
    EntityName(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    /**
     * Returns the unique entity code.
     *
     * @return entity code
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the user-friendly entity name.
     *
     * @return display name
     */
    public String getDisplayName() {
        return displayName;
    }

}