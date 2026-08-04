package com.foodies.freshmeal.common.constants;

/**
 * ============================================================================
 * Class : RegexConstants
 * ============================================================================
 *
 * Centralized regular expression patterns used throughout the FreshMeal
 * application.
 *
 * <p>
 * This class eliminates duplicate regular expressions and ensures consistent
 * validation across all application modules.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 1.0
 *        ============================================================================
 */
public final class RegexConstants {

    /**
     * Private constructor.
     */
    private RegexConstants() {
        throw new IllegalStateException("Utility class");
    }

    // =========================================================
    // Common
    // =========================================================

    /**
     * Non-empty text.
     */
    public static final String NOT_BLANK = "^(?!\\s*$).+";

    /**
     * Alphabetic characters with spaces.
     */
    public static final String ALPHABETS_WITH_SPACE = "^[A-Za-z ]+$";

    /**
     * Alpha numeric characters.
     */
    public static final String ALPHA_NUMERIC = "^[A-Za-z0-9]+$";

    /**
     * Alpha numeric characters with spaces.
     */
    public static final String ALPHA_NUMERIC_WITH_SPACE = "^[A-Za-z0-9 ]+$";

    /**
     * Digits only.
     */
    public static final String NUMERIC = "^\\d+$";

    // =========================================================
    // User
    // =========================================================

    /**
     * Username.
     */
    public static final String USERNAME = "^[A-Za-z][A-Za-z0-9._]{3,49}$";

    /**
     * Password.
     *
     * Minimum 8 characters,
     * one uppercase,
     * one lowercase,
     * one digit,
     * one special character.
     */
    public static final String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)"
            + "(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,100}$";

    // =========================================================
    // Contact
    // =========================================================

    /**
     * Email address.
     */
    public static final String EMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    /**
     * Indian mobile number.
     */
    public static final String MOBILE_NUMBER = "^[6-9]\\d{9}$";

    /**
     * Six digit OTP.
     */
    public static final String OTP = "^\\d{6}$";

    // =========================================================
    // Address
    // =========================================================

    /**
     * Indian PIN code.
     */
    public static final String PIN_CODE = "^\\d{6}$";

    // =========================================================
    // Government IDs
    // =========================================================

    /**
     * PAN number.
     */
    public static final String PAN = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$";

    /**
     * GST number.
     */
    public static final String GST = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}"
            + "[1-9A-Z]{1}Z[0-9A-Z]{1}$";

    /**
     * FSSAI License Number.
     */
    public static final String FSSAI = "^\\d{14}$";

    // =========================================================
    // URLs
    // =========================================================

    /**
     * HTTP / HTTPS URL.
     */
    public static final String URL = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";

    // =========================================================
    // Entity IDs
    // =========================================================

    /**
     * Food ID.
     */
    public static final String FOOD_ID = "^FMFOD\\d{7}$";

    /**
     * Order ID.
     */
    public static final String ORDER_ID = "^FMORD\\d{7}$";

    /**
     * User ID.
     */
    public static final String USER_ID = "^FMUSR\\d{7}$";

    /**
     * Image ID.
     */
    public static final String IMAGE_ID = "^FMIMG\\d{7}$";

}