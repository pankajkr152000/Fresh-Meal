package com.foodies.freshmeal.common.date;

import java.time.LocalDate;

/**
 * Utility class for DateRange operations.
 */
public final class DateRangeUtil {

    private DateRangeUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Checks whether the given date lies within the range.
     */
    public static boolean contains(DateRange range,
            LocalDate date) {

        if (range == null
                || date == null
                || range.getStartDate() == null
                || range.getEndDate() == null) {

            return false;
        }

        return !date.isBefore(range.getStartDate())
                && !date.isAfter(range.getEndDate());
    }

    /**
     * Checks whether two ranges overlap.
     */
    public static boolean overlaps(DateRange first,
            DateRange second) {

        if (first == null || second == null) {
            return false;
        }

        return !(first.getEndDate().isBefore(second.getStartDate())
                || first.getStartDate().isAfter(second.getEndDate()));
    }

    /**
     * Returns total number of days.
     */
    public static long totalDays(DateRange range) {

        if (range == null) {
            return 0;
        }

        return DateCalculator.daysBetween(
                range.getStartDate(),
                range.getEndDate()) + 1;
    }

    /**
     * Checks whether the range is valid.
     */
    public static boolean isValid(DateRange range) {

        if (range == null) {
            return false;
        }

        return DateValidator.isStartBeforeEnd(
                range.getStartDate(),
                range.getEndDate());
    }

}