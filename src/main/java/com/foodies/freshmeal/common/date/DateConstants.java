package com.foodies.freshmeal.common.date;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * ============================================================================
 * DateConstants
 * ============================================================================
 *
 * Centralized date/time constants used across the FreshMeal application.
 *
 * Responsibilities:
 * - Time Zone
 * - Locale
 * - Date Patterns
 * - Time Patterns
 * - DateTime Patterns
 * - ISO Patterns
 * - DateTimeFormatters
 *
 * NOTE:
 * Do not place business logic in this class.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @version 1.0
 */
public final class DateConstants {

    private DateConstants() {
        throw new IllegalStateException("Utility class");
    }

    // ========================================================================
    // LOCALE
    // ========================================================================

    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    // ========================================================================
    // TIME ZONE
    // ========================================================================

    public static final String INDIA_TIME_ZONE = "Asia/Kolkata";

    public static final ZoneId DEFAULT_ZONE = ZoneId.of(INDIA_TIME_ZONE);

    public static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone(INDIA_TIME_ZONE);

    // ========================================================================
    // DATE PATTERNS
    // ========================================================================

    /**
     * Example:
     * 03/08/2026
     */
    public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

    /**
     * Example:
     * 14:35:45
     */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    /**
     * Example:
     * 03/08/2026 14:35:45
     */
    public static final String DEFAULT_DATE_TIME_FORMAT1 = "dd/MM/yyyy HH:mm:ss";

    /**
     * Example:
     * 03-08-2026 14:35:45
     */
    public static final String DEFAULT_DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";

    /**
     * Example:
     * Mon, 03 Aug 2026, 14:35:45
     */
    public static final String DAY_DATE_MONTH_YEAR_TIME_FORMAT = "EEE, dd MMM yyyy, HH:mm:ss";

    /**
     * Example:
     * Mon, 03/08/2026, 14:35:45
     */
    public static final String DAY_DATE_TIME_FORMAT = "EEE, dd/MM/yyyy, HH:mm:ss";

    /**
     * Example:
     * Mon, 03 Aug 2026, 02:35:45 PM
     */
    public static final String DAY_DATE_MONTH_YEAR_TIME_12H_FORMAT = "EEE, dd MMM yyyy, hh:mm:ss a";

    /**
     * Example:
     * Mon, 03/08/2026, 02:35:45 PM
     */
    public static final String DAY_DATE_TIME_12H_FORMAT = "EEE, dd/MM/yyyy, hh:mm:ss a";

    /**
     * Example:
     * Aug 2026
     */
    public static final String MONTH_YEAR_FORMAT = "MMM yyyy";

    /**
     * Example:
     * 2026-08
     */
    public static final String YEAR_MONTH_FORMAT = "yyyy-MM";

    /**
     * Example:
     * 2026-08-03T14:35:45
     */
    public static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    // ========================================================================
    // FORMATTERS
    // ========================================================================

    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT,
            DEFAULT_LOCALE);

    public static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT,
            DEFAULT_LOCALE);

    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER1 = DateTimeFormatter
            .ofPattern(DEFAULT_DATE_TIME_FORMAT1, DEFAULT_LOCALE);

    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern(DEFAULT_DATE_TIME_FORMAT, DEFAULT_LOCALE);

    public static final DateTimeFormatter DAY_DATE_MONTH_YEAR_TIME_FORMATTER = DateTimeFormatter
            .ofPattern(DAY_DATE_MONTH_YEAR_TIME_FORMAT, DEFAULT_LOCALE);

    public static final DateTimeFormatter DAY_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DAY_DATE_TIME_FORMAT,
            DEFAULT_LOCALE);

    public static final DateTimeFormatter DAY_DATE_MONTH_YEAR_TIME_12H_FORMATTER = DateTimeFormatter
            .ofPattern(DAY_DATE_MONTH_YEAR_TIME_12H_FORMAT, DEFAULT_LOCALE);

    public static final DateTimeFormatter DAY_DATE_TIME_12H_FORMATTER = DateTimeFormatter
            .ofPattern(DAY_DATE_TIME_12H_FORMAT, DEFAULT_LOCALE);

    public static final DateTimeFormatter MONTH_YEAR_FORMATTER = DateTimeFormatter.ofPattern(MONTH_YEAR_FORMAT,
            DEFAULT_LOCALE);

    public static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern(YEAR_MONTH_FORMAT,
            DEFAULT_LOCALE);

    public static final DateTimeFormatter ISO_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(ISO_DATE_TIME_FORMAT,
            DEFAULT_LOCALE);

}
