package com.foodies.freshmeal.common.date;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Small helper methods.
 */
public final class DateHelper {

    private DateHelper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Returns today's business date.
     */
    public static LocalDate today() {

        return AppCalendar.getBusinessLocalDate();
    }

    /**
     * Returns current business date-time.
     */
    public static LocalDateTime now() {

        return AppCalendar.getBusinessLocalDateTime();
    }

    /**
     * Yesterday.
     */
    public static LocalDate yesterday() {

        return today().minusDays(1);
    }

    /**
     * Tomorrow.
     */
    public static LocalDate tomorrow() {

        return today().plusDays(1);
    }

    /**
     * Current month.
     */
    public static int currentMonth() {

        return today().getMonthValue();
    }

    /**
     * Current year.
     */
    public static int currentYear() {

        return today().getYear();
    }

    /**
     * Current day of month.
     */
    public static int currentDay() {

        return today().getDayOfMonth();
    }

}