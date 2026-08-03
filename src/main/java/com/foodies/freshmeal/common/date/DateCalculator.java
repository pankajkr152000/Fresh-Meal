package com.foodies.freshmeal.common.date;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * ============================================================================
 * DateCalculator
 * ============================================================================
 *
 * Central utility class for date calculations.
 *
 * Responsibilities:
 * • Add/Subtract
 * • Difference
 * • Start/End calculations
 * • Quarter calculations
 * • Financial Year calculations
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @version 1.0
 */
public final class DateCalculator {

    private DateCalculator() {
        throw new IllegalStateException("Utility class");
    }

    // =====================================================
    // ADD
    // =====================================================

    public static LocalDate addDays(LocalDate date, long days) {
        return date == null ? null : date.plusDays(days);
    }

    public static LocalDate addWeeks(LocalDate date, long weeks) {
        return date == null ? null : date.plusWeeks(weeks);
    }

    public static LocalDate addMonths(LocalDate date, long months) {
        return date == null ? null : date.plusMonths(months);
    }

    public static LocalDate addYears(LocalDate date, long years) {
        return date == null ? null : date.plusYears(years);
    }

    public static LocalDateTime addDays(LocalDateTime dateTime, long days) {
        return dateTime == null ? null : dateTime.plusDays(days);
    }

    public static LocalDateTime addHours(LocalDateTime dateTime, long hours) {
        return dateTime == null ? null : dateTime.plusHours(hours);
    }

    public static LocalDateTime addMinutes(LocalDateTime dateTime, long minutes) {
        return dateTime == null ? null : dateTime.plusMinutes(minutes);
    }

    public static LocalDateTime addSeconds(LocalDateTime dateTime, long seconds) {
        return dateTime == null ? null : dateTime.plusSeconds(seconds);
    }

    // =====================================================
    // SUBTRACT
    // =====================================================

    public static LocalDate subtractDays(LocalDate date, long days) {
        return addDays(date, -days);
    }

    public static LocalDate subtractWeeks(LocalDate date, long weeks) {
        return addWeeks(date, -weeks);
    }

    public static LocalDate subtractMonths(LocalDate date, long months) {
        return addMonths(date, -months);
    }

    public static LocalDate subtractYears(LocalDate date, long years) {
        return addYears(date, -years);
    }

    // =====================================================
    // DIFFERENCE
    // =====================================================

    public static long daysBetween(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end);
    }

    public static long weeksBetween(LocalDate start, LocalDate end) {
        return ChronoUnit.WEEKS.between(start, end);
    }

    public static long monthsBetween(LocalDate start, LocalDate end) {
        return ChronoUnit.MONTHS.between(start, end);
    }

    public static long yearsBetween(LocalDate start, LocalDate end) {
        return ChronoUnit.YEARS.between(start, end);
    }

    public static long hoursBetween(LocalDateTime start,
            LocalDateTime end) {

        return ChronoUnit.HOURS.between(start, end);
    }

    public static long minutesBetween(LocalDateTime start,
            LocalDateTime end) {

        return ChronoUnit.MINUTES.between(start, end);
    }

    public static long secondsBetween(LocalDateTime start,
            LocalDateTime end) {

        return ChronoUnit.SECONDS.between(start, end);
    }

    // =====================================================
    // START OF
    // =====================================================

    public static LocalDateTime startOfDay(LocalDate date) {

        return date == null
                ? null
                : date.atStartOfDay();
    }

    public static LocalDate startOfMonth(LocalDate date) {

        return date == null
                ? null
                : date.withDayOfMonth(1);
    }

    public static LocalDate startOfYear(LocalDate date) {

        return date == null
                ? null
                : date.withDayOfYear(1);
    }

    public static LocalDate startOfQuarter(LocalDate date) {

        if (date == null) {
            return null;
        }

        int quarter = (date.getMonthValue() - 1) / 3;

        return LocalDate.of(
                date.getYear(),
                quarter * 3 + 1,
                1);
    }

    // =====================================================
    // END OF
    // =====================================================

    public static LocalDateTime endOfDay(LocalDate date) {

        return date == null
                ? null
                : date.atTime(LocalTime.MAX);
    }

    public static LocalDate endOfMonth(LocalDate date) {

        return date == null
                ? null
                : date.withDayOfMonth(date.lengthOfMonth());
    }

    public static LocalDate endOfYear(LocalDate date) {

        return date == null
                ? null
                : date.withDayOfYear(date.lengthOfYear());
    }

    public static LocalDate endOfQuarter(LocalDate date) {

        if (date == null) {
            return null;
        }

        return startOfQuarter(date)
                .plusMonths(2)
                .withDayOfMonth(
                        startOfQuarter(date)
                                .plusMonths(2)
                                .lengthOfMonth());
    }

    // =====================================================
    // FINANCIAL YEAR
    // =====================================================

    public static LocalDate financialYearStart(LocalDate date) {

        if (date == null) {
            return null;
        }

        int year = date.getMonthValue() < 4
                ? date.getYear() - 1
                : date.getYear();

        return LocalDate.of(year, 4, 1);
    }

    public static LocalDate financialYearEnd(LocalDate date) {

        return financialYearStart(date)
                .plusYears(1)
                .minusDays(1);
    }

    // =====================================================
    // WEEKEND
    // =====================================================

    public static boolean isWeekend(LocalDate date) {

        if (date == null) {
            return false;
        }

        return date.getDayOfWeek() == DayOfWeek.SATURDAY
                || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

}