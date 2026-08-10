package com.foodies.freshmeal.common.constants;

/**
 * ============================================================================
 * Class : SequenceConstants
 * ============================================================================
 *
 * Centralized MongoDB sequence collection names and identifier generation
 * patterns used throughout the FreshMeal application.
 *
 * <p>
 * FreshMeal maintains two different types of identifiers:
 * </p>
 *
 * <ul>
 * <li>
 * <b>Database Identifier</b> -
 * Internal identifier used by the database and backend.
 * </li>
 * <li>
 * <b>Business Identifier</b> -
 * External identifier used by frontend, consumers, URLs,
 * receipts, communication, etc.
 * </li>
 * </ul>
 *
 * <p>
 * Example:
 * </p>
 *
 * <pre>
 * Database ID   : ORD_DB_000001
 * Order Number  : FM-ORD-0000001
 * </pre>
 *
 * <p>
 * Database identifiers must never be treated as customer-facing identifiers.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 *        ============================================================================
 */
public final class SequenceConstants {

    /**
     * Private constructor.
     */
    private SequenceConstants() {
        throw new IllegalStateException("Utility class");
    }

    // =========================================================================
    // MongoDB Sequence Collection Names
    // =========================================================================

    public static final String USER_ENTITY_SEQUENCE = "user_entity_sequence";

    public static final String USER_PROFILE_SEQUENCE = "user_profile_sequence";

    public static final String LOGIN_HISTORY_SEQUENCE = "login_history_sequence";

    public static final String FOOD_SEQUENCE = "food_sequence";

    public static final String ORDER_SEQUENCE = "order_sequence";

    public static final String IMAGE_SEQUENCE = "image_sequence";

    public static final String RESTAURANT_SEQUENCE = "restaurant_sequence";

    public static final String DELIVERY_PARTNER_SEQUENCE = "delivery_partner_sequence";

    public static final String PAYMENT_SEQUENCE = "payment_sequence";

    public static final String REFUND_SEQUENCE = "refund_sequence";

    public static final String COUPON_SEQUENCE = "coupon_sequence";

    public static final String CATEGORY_SEQUENCE = "category_sequence";

    // =========================================================================
    // Internal Database ID Patterns
    // =========================================================================
    //
    // These identifiers are used internally by the backend/database.
    //
    // They should NOT be exposed as customer-facing identifiers.
    // =========================================================================

    public static final String USER_DB_ID_PATTERN = "USR_DB_%06d";

    public static final String USER_PROFILE_DB_ID_PATTERN = "UPR_DB_%06d";

    public static final String LOGIN_HISTORY_DB_ID_PATTERN = "LGH_DB_%06d";

    public static final String FOOD_DB_ID_PATTERN = "FOOD_DB_%06d";

    public static final String ORDER_DB_ID_PATTERN = "ORD_DB_%06d";

    public static final String IMAGE_DB_ID_PATTERN = "IMG_DB_%06d";

    public static final String RESTAURANT_DB_ID_PATTERN = "RST_DB_%06d";

    public static final String DELIVERY_PARTNER_DB_ID_PATTERN = "DLP_DB_%06d";

    public static final String PAYMENT_DB_ID_PATTERN = "PAY_DB_%06d";

    public static final String REFUND_DB_ID_PATTERN = "RFD_DB_%06d";

    public static final String COUPON_DB_ID_PATTERN = "CPN_DB_%06d";

    public static final String CATEGORY_DB_ID_PATTERN = "CAT_DB_%06d";

    // =========================================================================
    // External / Business Identifier Patterns
    // =========================================================================
    //
    // These identifiers are intended for:
    //
    // • Frontend
    // • Consumer applications
    // • URLs
    // • Order tracking
    // • Receipts
    // • Customer communication
    // • Business operations
    //
    // =========================================================================

    public static final String USER_NUMBER_PATTERN = "FM-USR-%07d";

    public static final String USER_PROFILE_NUMBER_PATTERN = "FM-UPR-%07d";

    public static final String FOOD_NUMBER_PATTERN = "FM-FOOD-%07d";

    public static final String ORDER_NUMBER_PATTERN = "FM-ORD-%07d";

    public static final String RESTAURANT_NUMBER_PATTERN = "FM-RST-%07d";

    public static final String DELIVERY_PARTNER_NUMBER_PATTERN = "FM-DLP-%07d";

    public static final String PAYMENT_NUMBER_PATTERN = "FM-PAY-%07d";

    public static final String REFUND_NUMBER_PATTERN = "FM-RFD-%07d";

    public static final String COUPON_NUMBER_PATTERN = "FM-CPN-%07d";

    public static final String CATEGORY_NUMBER_PATTERN = "FM-CAT-%07d";
}