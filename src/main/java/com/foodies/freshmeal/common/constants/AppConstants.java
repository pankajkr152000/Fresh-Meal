package com.foodies.freshmeal.common.constants;

/**
 * ============================================================================
 * Class : AppConstants
 * ============================================================================
 *
 * Centralized application-wide constants used throughout the FreshMeal
 * application.
 *
 * <p>
 * This class contains generic reusable constants that are shared across
 * multiple modules. Business-specific constants should be placed in their
 * respective constant classes.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 1.0
 *        ============================================================================
 */
public final class AppConstants {

    /**
     * Private constructor.
     */
    private AppConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String ADMINPANEL_URL = "http://localhost:5173";

    // =========================================================
    // String Constants
    // =========================================================

    public static final String EMPTY = "";

    public static final String SPACE = " ";

    public static final String COMMA = ",";

    public static final String COLON = ":";

    public static final String SEMICOLON = ";";

    public static final String HYPHEN = "-";

    public static final String UNDERSCORE = "_";

    public static final String DOT = ".";

    public static final String SLASH = "/";

    public static final String BACK_SLASH = "\\";

    public static final String PIPE = "|";

    public static final String AT = "@";

    public static final String HASH = "#";

    public static final String QUESTION_MARK = "?";

    public static final String AMPERSAND = "&";

    public static final String EQUAL = "=";

    public static final String ASTERISK = "*";

    public static final String OPEN_BRACKET = "(";

    public static final String CLOSE_BRACKET = ")";

    public static final String OPEN_CURLY_BRACE = "{";

    public static final String CLOSE_CURLY_BRACE = "}";

    public static final String OPEN_SQUARE_BRACKET = "[";

    public static final String CLOSE_SQUARE_BRACKET = "]";

    // =========================================================
    // Boolean Values
    // =========================================================

    public static final String YES = "YES";

    public static final String NO = "NO";

    public static final String TRUE = "TRUE";

    public static final String FALSE = "FALSE";

    // =========================================================
    // Numeric Constants
    // =========================================================

    public static final int ZERO = 0;

    public static final int ONE = 1;

    public static final int TWO = 2;

    public static final int THREE = 3;

    public static final int FOUR = 4;

    public static final int FIVE = 5;

    public static final int TEN = 10;

    public static final int HUNDRED = 100;

    public static final int THOUSAND = 1000;

    // =========================================================
    // File Size
    // =========================================================

    public static final long ONE_KB = 1024L;

    public static final long ONE_MB = ONE_KB * ONE_KB;

    public static final long ONE_GB = ONE_MB * ONE_KB;

    // =========================================================
    // Date & Time
    // =========================================================

    public static final int HOURS_PER_DAY = 24;

    public static final int MINUTES_PER_HOUR = 60;

    public static final int SECONDS_PER_MINUTE = 60;

    public static final int DAYS_PER_WEEK = 7;

    public static final int MONTHS_PER_YEAR = 12;

    // =========================================================
    // Pagination
    // =========================================================

    public static final String ASC = "ASC";

    public static final String DESC = "DESC";

    // =========================================================
    // Content
    // =========================================================

    public static final String UNKNOWN = "Unknown";

    public static final String NOT_AVAILABLE = "N/A";

    public static final String DEFAULT = "Default";

    public static final String SYSTEM = "System";

}
