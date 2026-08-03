package com.foodies.freshmeal.common.date;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.util.StringUtils;

/**
 * ============================================================================
 * DateFormatter
 * ============================================================================
 *
 * Central utility class responsible for:
 *
 * • Formatting
 * • Parsing
 * • Business Date Formatting
 * • ISO Formatting
 * • Custom Formatting
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @version 1.0
 */
public final class DateFormatter {

    private DateFormatter() {
        throw new IllegalStateException("Utility class");
    }

    // ========================================================================
    // LOCAL DATE
    // ========================================================================

    public static String format(LocalDate date) {

        return format(date, DateConstants.DEFAULT_DATE_FORMATTER);
    }

    public static String format(LocalDate date,
            DateTimeFormatter formatter) {

        if (date == null) {
            return null;
        }

        return date.format(formatter);
    }

    // ========================================================================
    // LOCAL TIME
    // ========================================================================

    public static String format(LocalTime time) {

        return format(time, DateConstants.DEFAULT_TIME_FORMATTER);
    }

    public static String format(LocalTime time,
            DateTimeFormatter formatter) {

        if (time == null) {
            return null;
        }

        return time.format(formatter);
    }

    // ========================================================================
    // LOCAL DATE TIME
    // ========================================================================

    public static String format1(LocalDateTime dateTime) {

        return format(dateTime,
                DateConstants.DEFAULT_DATE_TIME_FORMATTER1);
    }

    public static String format(LocalDateTime dateTime) {

        return format(dateTime,
                DateConstants.DEFAULT_DATE_TIME_FORMATTER);
    }

    public static String format(LocalDateTime dateTime,
            DateTimeFormatter formatter) {

        if (dateTime == null) {
            return null;
        }

        return dateTime.format(formatter);
    }

    // ========================================================================
    // JAVA DATE
    // ========================================================================

    public static String format(Date date) {

        return format(date,
                DateConstants.DEFAULT_DATE_FORMAT);
    }

    public static String format(Date date,
            String pattern) {

        if (date == null) {
            return null;
        }

        return new SimpleDateFormat(pattern)
                .format(date);
    }

    // ========================================================================
    // TIMESTAMP
    // ========================================================================

    public static String format(Timestamp timestamp) {

        if (timestamp == null) {
            return null;
        }

        return format(timestamp.toLocalDateTime());
    }

    // ========================================================================
    // BUSINESS DATE
    // ========================================================================

    public static String businessDate() {

        return format(AppCalendar.getBusinessLocalDate(),
                DateConstants.DEFAULT_DATE_FORMATTER);
    }

    public static String businessTime() {

        return format(AppCalendar.getBusinessLocalTime(),
                DateConstants.DEFAULT_TIME_FORMATTER);
    }

    public static String businessDateTime() {

        return format(AppCalendar.getBusinessLocalDateTime());
    }

    public static String businessDayDateMonthYearTime() {

        return format(
                AppCalendar.getBusinessLocalDateTime(),
                DateConstants.DAY_DATE_MONTH_YEAR_TIME_FORMATTER);
    }

    public static String businessDayDateTime() {

        return format(
                AppCalendar.getBusinessLocalDateTime(),
                DateConstants.DAY_DATE_TIME_FORMATTER);
    }

    public static String businessDayDateMonthYearTime12H() {

        return format(
                AppCalendar.getBusinessLocalDateTime(),
                DateConstants.DAY_DATE_MONTH_YEAR_TIME_12H_FORMATTER);
    }

    public static String businessDayDateTime12H() {

        return format(
                AppCalendar.getBusinessLocalDateTime(),
                DateConstants.DAY_DATE_TIME_12H_FORMATTER);
    }

    public static String businessMonthYear() {

        return format(
                AppCalendar.getBusinessLocalDate(),
                DateConstants.MONTH_YEAR_FORMATTER);
    }

    public static String businessYearMonth() {

        return format(
                AppCalendar.getBusinessLocalDate(),
                DateConstants.YEAR_MONTH_FORMATTER);
    }

    public static String businessIsoDateTime() {

        return format(
                AppCalendar.getBusinessLocalDateTime(),
                DateConstants.ISO_DATE_TIME_FORMATTER);
    }

    // ========================================================================
    // PARSE
    // ========================================================================

    public static LocalDate parseDate(String value) {

        if (!StringUtils.hasText(value)) {
            return null;
        }

        return LocalDate.parse(
                value,
                DateConstants.DEFAULT_DATE_FORMATTER);
    }

    public static LocalTime parseTime(String value) {

        if (!StringUtils.hasText(value)) {
            return null;
        }

        return LocalTime.parse(
                value,
                DateConstants.DEFAULT_TIME_FORMATTER);
    }

    public static LocalDateTime parseDateTime(String value) {

        if (!StringUtils.hasText(value)) {
            return null;
        }

        return LocalDateTime.parse(
                value,
                DateConstants.DEFAULT_DATE_TIME_FORMATTER);
    }

    // ========================================================================
    // CUSTOM PARSE
    // ========================================================================

    public static LocalDate parseDate(String value,
            DateTimeFormatter formatter) {

        if (!StringUtils.hasText(value)) {
            return null;
        }

        return LocalDate.parse(value, formatter);
    }

    public static LocalDateTime parseDateTime(
            String value,
            DateTimeFormatter formatter) {

        if (!StringUtils.hasText(value)) {
            return null;
        }

        return LocalDateTime.parse(value, formatter);
    }

    public static LocalTime parseTime(
            String value,
            DateTimeFormatter formatter) {

        if (!StringUtils.hasText(value)) {
            return null;
        }

        return LocalTime.parse(value, formatter);
    }

}