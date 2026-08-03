package com.foodies.freshmeal.common.date;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * ============================================================================
 * BusinessCalendar
 * ============================================================================
 *
 * Handles business day calculations.
 *
 * Responsibilities:
 * • Working Days
 * • Weekends
 * • Business Day Navigation
 * • Business Day Calculations
 *
 * Future:
 * • Holiday Support
 * • Company Calendar
 * • Festival Calendar
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @version 1.0
 */
public final class BusinessCalendar {

    private BusinessCalendar() {
        throw new IllegalStateException("Utility class");
    }

    // =====================================================
    // WEEKEND
    // =====================================================

    public static boolean isWeekend(LocalDate date) {

        if (date == null) {
            return false;
        }

        DayOfWeek day = date.getDayOfWeek();

        return day == DayOfWeek.SATURDAY
                || day == DayOfWeek.SUNDAY;
    }

    // =====================================================
    // WEEKDAY
    // =====================================================

    public static boolean isWeekday(LocalDate date) {

        return !isWeekend(date);
    }

    // =====================================================
    // NEXT WORKING DAY
    // =====================================================

    public static LocalDate nextWorkingDay(LocalDate date) {

        if (date == null) {
            return null;
        }

        LocalDate next = date.plusDays(1);

        while (isWeekend(next)) {
            next = next.plusDays(1);
        }

        return next;
    }

    // =====================================================
    // PREVIOUS WORKING DAY
    // =====================================================

    public static LocalDate previousWorkingDay(LocalDate date) {

        if (date == null) {
            return null;
        }

        LocalDate previous = date.minusDays(1);

        while (isWeekend(previous)) {
            previous = previous.minusDays(1);
        }

        return previous;
    }

    // =====================================================
    // ADD WORKING DAYS
    // =====================================================

    public static LocalDate addWorkingDays(LocalDate date,
            int workingDays) {

        if (date == null) {
            return null;
        }

        LocalDate result = date;

        int added = 0;

        while (added < workingDays) {

            result = result.plusDays(1);

            if (isWeekday(result)) {
                added++;
            }
        }

        return result;
    }

    // =====================================================
    // SUBTRACT WORKING DAYS
    // =====================================================

    public static LocalDate subtractWorkingDays(LocalDate date,
            int workingDays) {

        if (date == null) {
            return null;
        }

        LocalDate result = date;

        int deducted = 0;

        while (deducted < workingDays) {

            result = result.minusDays(1);

            if (isWeekday(result)) {
                deducted++;
            }
        }

        return result;
    }

    // =====================================================
    // BUSINESS DAY COUNT
    // =====================================================

    public static long businessDaysBetween(LocalDate start,
            LocalDate end) {

        if (start == null || end == null) {
            return 0;
        }

        long count = 0;

        LocalDate current = start;

        while (!current.isAfter(end)) {

            if (isWeekday(current)) {
                count++;
            }

            current = current.plusDays(1);
        }

        return count;
    }

}