package com.foodies.freshmeal.common.constants;

/**
 * ============================================================================
 * Class : ValidationConstants
 * ============================================================================
 *
 * Centralized validation constants used throughout the FreshMeal application.
 *
 * <p>
 * This class eliminates magic numbers from validation annotations and ensures
 * consistent validation rules across all modules.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 1.0
 *        ============================================================================
 */
public final class ValidationConstants {

    /**
     * Private constructor.
     */
    private ValidationConstants() {
        throw new IllegalStateException("Utility class");
    }

    // =========================================================
    // Common Text Length
    // =========================================================

    public static final int NAME_MIN_LENGTH = 2;

    public static final int NAME_MAX_LENGTH = 100;

    public static final int TITLE_MAX_LENGTH = 150;

    public static final int DESCRIPTION_MAX_LENGTH = 1000;

    public static final int SHORT_DESCRIPTION_MAX_LENGTH = 255;

    public static final int REMARKS_MAX_LENGTH = 500;

    public static final int NOTES_MAX_LENGTH = 2000;

    // =========================================================
    // Authentication
    // =========================================================

    public static final int USERNAME_MIN_LENGTH = 4;

    public static final int USERNAME_MAX_LENGTH = 50;

    public static final int PASSWORD_MIN_LENGTH = 8;

    public static final int PASSWORD_MAX_LENGTH = 100;

    public static final int OTP_LENGTH = 6;

    // =========================================================
    // Contact Information
    // =========================================================

    public static final int EMAIL_MAX_LENGTH = 255;

    public static final int MOBILE_NUMBER_LENGTH = 10;

    public static final int COUNTRY_CODE_MAX_LENGTH = 5;

    // =========================================================
    // Address
    // =========================================================

    public static final int ADDRESS_LINE_MAX_LENGTH = 255;

    public static final int CITY_MAX_LENGTH = 100;

    public static final int STATE_MAX_LENGTH = 100;

    public static final int COUNTRY_MAX_LENGTH = 100;

    public static final int PIN_CODE_LENGTH = 6;

    // =========================================================
    // Food
    // =========================================================

    public static final int FOOD_NAME_MAX_LENGTH = 150;

    public static final int FOOD_DESCRIPTION_MAX_LENGTH = 2000;

    // =========================================================
    // Restaurant
    // =========================================================

    public static final int RESTAURANT_NAME_MAX_LENGTH = 150;

    // =========================================================
    // Order
    // =========================================================

    public static final int ORDER_NOTES_MAX_LENGTH = 1000;

    // =========================================================
    // Payment
    // =========================================================

    public static final int TRANSACTION_ID_MAX_LENGTH = 100;

    public static final int PAYMENT_REFERENCE_MAX_LENGTH = 100;

    // =========================================================
    // Image
    // =========================================================

    public static final long IMAGE_MAX_SIZE = 5 * 1024 * 1024L;

    // =========================================================
    // Pagination
    // =========================================================

    public static final int DEFAULT_PAGE = 0;

    public static final int DEFAULT_PAGE_SIZE = 10;

    public static final int MAX_PAGE_SIZE = 100;

    // =========================================================
    // Numeric Limits
    // =========================================================

    public static final double MIN_PRICE = 0.0;

    public static final double MAX_PRICE = 999999.99;

    public static final int MIN_QUANTITY = 1;

    public static final int MAX_QUANTITY = 999;

}
