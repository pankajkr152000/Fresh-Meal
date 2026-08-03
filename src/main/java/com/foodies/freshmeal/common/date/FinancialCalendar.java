package com.foodies.freshmeal.common.date;

import java.time.LocalDate;
import java.time.Month;

/**
 * ============================================================================
 * FinancialCalendar
 * ============================================================================
 *
 * Utility class for Financial Year and Quarter calculations.
 *
 * Default Financial Year:
 *
 * 1 April -> 31 March
 *
 * Example:
 *
 * 01 Apr 2026 → 31 Mar 2027
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @version 1.0
 */
public final class FinancialCalendar {

    /**
     * Financial Year starts in April.
     */
    private static final Month FINANCIAL_YEAR_START_MONTH = Month.APRIL;

    private FinancialCalendar() {
        throw new IllegalStateException("Utility class");
    }

    // =====================================================
    // FINANCIAL YEAR
    // =====================================================

    public static LocalDate getFinancialYearStart(LocalDate date) {

        if (date == null) {
            return null;
        }

        int year = date.getMonthValue() < FINANCIAL_YEAR_START_MONTH.getValue()
                ? date.getYear() - 1
                : date.getYear();

        return LocalDate.of(year,
                FINANCIAL_YEAR_START_MONTH,
                1);
    }

    public static LocalDate getFinancialYearEnd(LocalDate date) {

        if (date == null) {
            return null;
        }

        return getFinancialYearStart(date)
                .plusYears(1)
                .minusDays(1);
    }

    // =====================================================
    // CURRENT
    // =====================================================

    public static LocalDate getCurrentFinancialYearStart() {

        return getFinancialYearStart(
                AppCalendar.getBusinessLocalDate());
    }

    public static LocalDate getCurrentFinancialYearEnd() {

        return getFinancialYearEnd(
                AppCalendar.getBusinessLocalDate());
    }

    // =====================================================
    // STRING
    // =====================================================

    /**
     * Example:
     *
     * 2026-27
     */
    public static String getFinancialYear(LocalDate date) {

        if (date == null) {
            return null;
        }

        LocalDate start = getFinancialYearStart(date);

        int startYear = start.getYear();
        int endYear = start.plusYears(1).getYear();

        return startYear + "-"
                + String.valueOf(endYear).substring(2);
    }

    public static String getCurrentFinancialYear() {

        return getFinancialYear(
                AppCalendar.getBusinessLocalDate());
    }

    // =====================================================
    // QUARTER
    // =====================================================

    public static int getQuarter(LocalDate date) {

        if (date == null) {
            return 0;
        }

        return ((date.getMonthValue() - 1) / 3) + 1;
    }

    public static String getQuarterName(LocalDate date) {

        return "Q" + getQuarter(date);
    }

    // =====================================================
    // QUARTER START
    // =====================================================

    public static LocalDate getQuarterStart(LocalDate date) {

        if (date == null) {
            return null;
        }

        int quarter = ((date.getMonthValue() - 1) / 3);

        int startMonth = quarter * 3 + 1;

        return LocalDate.of(
                date.getYear(),
                startMonth,
                1);
    }

    // =====================================================
    // QUARTER END
    // =====================================================

    public static LocalDate getQuarterEnd(LocalDate date) {

        if (date == null) {
            return null;
        }

        return getQuarterStart(date)
                .plusMonths(2)
                .withDayOfMonth(
                        getQuarterStart(date)
                                .plusMonths(2)
                                .lengthOfMonth());
    }

    // =====================================================
    // CURRENT QUARTER
    // =====================================================

    public static int getCurrentQuarter() {

        return getQuarter(
                AppCalendar.getBusinessLocalDate());
    }

    public static String getCurrentQuarterName() {

        return "Q" + getCurrentQuarter();
    }

    public static LocalDate getCurrentQuarterStart() {

        return getQuarterStart(
                AppCalendar.getBusinessLocalDate());
    }

    public static LocalDate getCurrentQuarterEnd() {

        return getQuarterEnd(
                AppCalendar.getBusinessLocalDate());
    }

}