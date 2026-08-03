package com.foodies.freshmeal.common.constants;

/**
 * ============================================================================
 * Class : SequenceConstants
 * ============================================================================
 *
 * Contains MongoDB sequence collection names and entity ID generation patterns
 * used throughout the FreshMeal application.
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

    // ========================================================================
    // Mongo Sequence Collection Names
    // ========================================================================

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

    // ========================================================================
    // Entity ID Patterns
    // ========================================================================

    public static final String USER_ID_PATTERN = "FMUSR%07d";

    public static final String USER_PROFILE_ID_PATTERN = "FMUPR%07d";

    public static final String LOGIN_HISTORY_ID_PATTERN = "FMLGH%07d";

    public static final String FOOD_ID_PATTERN = "FMFOD%07d";

    public static final String ORDER_ID_PATTERN = "FMORD%07d";

    public static final String IMAGE_ID_PATTERN = "FMIMG%07d";

    public static final String RESTAURANT_ID_PATTERN = "FMRST%07d";

    public static final String DELIVERY_PARTNER_ID_PATTERN = "FMDLP%07d";

    public static final String PAYMENT_ID_PATTERN = "FMPAY%07d";

    public static final String REFUND_ID_PATTERN = "FMRFD%07d";

    public static final String COUPON_ID_PATTERN = "FMCPN%07d";

    public static final String CATEGORY_ID_PATTERN = "FMCAT%07d";

}