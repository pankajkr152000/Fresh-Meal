package com.foodies.freshmeal.common.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;

import org.springframework.util.StringUtils;

/**
 * ============================================================================
 * DateValidator
 * ============================================================================
 *
 * Utility class responsible for validating dates, times and date ranges.
 *
 * Responsibilities:
 * • Date Validation
 * • Time Validation
 * • DateTime Validation
 * • Future/Past Validation
 * • Range Validation
 * • Month/Year Validation
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @version 1.0
 */
public final class DateValidator {

    private DateValidator() {
        throw new IllegalStateException("Utility class");
    }

    // =====================================================
    // DATE FORMAT VALIDATION
    // =====================================================

    public static boolean isValidDate(String value) {

        if (!StringUtils.hasText(value)) {
            return false;
        }

        try {

            LocalDate.parse(
                    value,
                    DateConstants.DEFAULT_DATE_FORMATTER);

            return true;

        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    public static boolean isValidTime(String value) {

        if (!StringUtils.hasText(value)) {
            return false;
        }

        try {

            LocalTime.parse(
                    value,
                    DateConstants.DEFAULT_TIME_FORMATTER);

            return true;

        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    public static boolean isValidDateTime(String value) {

        if (!StringUtils.hasText(value)) {
            return false;
        }

        try {

            LocalDateTime.parse(
                    value,
                    DateConstants.DEFAULT_DATE_TIME_FORMATTER);

            return true;

        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    // =====================================================
    // DATE COMPARISON
    // =====================================================

    public static boolean isToday(LocalDate date) {

        return date != null &&
                date.equals(AppCalendar.getBusinessLocalDate());
    }

    public static boolean isPast(LocalDate date) {

        return date != null &&
                date.isBefore(AppCalendar.getBusinessLocalDate());
    }

    public static boolean isFuture(LocalDate date) {

        return date != null &&
                date.isAfter(AppCalendar.getBusinessLocalDate());
    }

    public static boolean isCurrentMonth(LocalDate date) {

        if (date == null) {
            return false;
        }

        YearMonth current = YearMonth.from(AppCalendar.getBusinessLocalDate());

        return YearMonth.from(date).equals(current);
    }

    public static boolean isCurrentYear(LocalDate date) {

        return date != null &&
                date.getYear() == AppCalendar.getBusinessLocalDate().getYear();
    }

    // =====================================================
    // RANGE VALIDATION
    // =====================================================

    public static boolean isWithinRange(LocalDate date,
            LocalDate startDate,
            LocalDate endDate) {

        if (date == null ||
                startDate == null ||
                endDate == null) {

            return false;
        }

        return !date.isBefore(startDate)
                && !date.isAfter(endDate);
    }

    public static boolean isWithinRange(LocalDateTime dateTime,
            LocalDateTime startDate,
            LocalDateTime endDate) {

        if (dateTime == null ||
                startDate == null ||
                endDate == null) {

            return false;
        }

        return !dateTime.isBefore(startDate)
                && !dateTime.isAfter(endDate);
    }

    // =====================================================
    // DATE ORDER
    // =====================================================

    public static boolean isStartBeforeEnd(LocalDate startDate,
            LocalDate endDate) {

        return startDate != null
                && endDate != null
                && !startDate.isAfter(endDate);
    }

    public static boolean isStartBeforeEnd(LocalDateTime startDate,
            LocalDateTime endDate) {

        return startDate != null
                && endDate != null
                && !startDate.isAfter(endDate);
    }

}