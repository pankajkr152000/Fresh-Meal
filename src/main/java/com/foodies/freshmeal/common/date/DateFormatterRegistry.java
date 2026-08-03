package com.foodies.freshmeal.common.date;

import java.time.format.DateTimeFormatter;

/**
 * ============================================================================
 * DateFormatterRegistry
 * ============================================================================
 *
 * Central registry for all commonly used DateTimeFormatter instances.
 *
 * This class provides meaningful names for different formatter use cases
 * instead of exposing formatter constants throughout the application.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @version 1.0
 */
public final class DateFormatterRegistry {

    private DateFormatterRegistry() {
        throw new IllegalStateException("Utility class");
    }

    // =====================================================
    // DEFAULT
    // =====================================================

    public static DateTimeFormatter defaultDate() {
        return DateConstants.DEFAULT_DATE_FORMATTER;
    }

    public static DateTimeFormatter defaultTime() {
        return DateConstants.DEFAULT_TIME_FORMATTER;
    }

    public static DateTimeFormatter defaultDateTime() {
        return DateConstants.DEFAULT_DATE_TIME_FORMATTER;
    }

    // =====================================================
    // DISPLAY
    // =====================================================

    /**
     * Example:
     * Mon, 03 Aug 2026, 14:35:45
     */
    public static DateTimeFormatter displayDateTime() {
        return DateConstants.DAY_DATE_MONTH_YEAR_TIME_FORMATTER;
    }

    /**
     * Example:
     * Mon, 03/08/2026, 14:35:45
     */
    public static DateTimeFormatter displayCompactDateTime() {
        return DateConstants.DAY_DATE_TIME_FORMATTER;
    }

    /**
     * Example:
     * Mon, 03 Aug 2026, 02:35:45 PM
     */
    public static DateTimeFormatter display12HourDateTime() {
        return DateConstants.DAY_DATE_MONTH_YEAR_TIME_12H_FORMATTER;
    }

    /**
     * Example:
     * Mon, 03/08/2026, 02:35:45 PM
     */
    public static DateTimeFormatter displayCompact12HourDateTime() {
        return DateConstants.DAY_DATE_TIME_12H_FORMATTER;
    }

    // =====================================================
    // REPORTS
    // =====================================================

    public static DateTimeFormatter reportDate() {
        return DateConstants.DEFAULT_DATE_FORMATTER;
    }

    public static DateTimeFormatter reportDateTime() {
        return DateConstants.DAY_DATE_MONTH_YEAR_TIME_FORMATTER;
    }

    // =====================================================
    // AUDIT
    // =====================================================

    public static DateTimeFormatter auditDateTime() {
        return DateConstants.DAY_DATE_MONTH_YEAR_TIME_FORMATTER;
    }

    // =====================================================
    // INVOICE
    // =====================================================

    public static DateTimeFormatter invoiceDate() {
        return DateConstants.DEFAULT_DATE_FORMATTER;
    }

    public static DateTimeFormatter invoiceDateTime() {
        return DateConstants.DAY_DATE_MONTH_YEAR_TIME_FORMATTER;
    }

    // =====================================================
    // API
    // =====================================================

    public static DateTimeFormatter apiDateTime() {
        return DateConstants.ISO_DATE_TIME_FORMATTER;
    }

    // =====================================================
    // MONTH
    // =====================================================

    public static DateTimeFormatter monthYear() {
        return DateConstants.MONTH_YEAR_FORMATTER;
    }

    public static DateTimeFormatter yearMonth() {
        return DateConstants.YEAR_MONTH_FORMATTER;
    }

}