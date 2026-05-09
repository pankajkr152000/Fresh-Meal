package com.foodies.freshmeal.common.date;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.foodies.freshmeal.common.exception.ExceptionCollection;
import com.foodies.freshmeal.common.exception.SystemConfigBean;

/**
 * Refactored AppCalendar
 */
public final class AppCalendar {

    private static final Logger log = LoggerFactory.getLogger(AppCalendar.class);
    
    private static AppCalendar instance;


    // =====================================================
    // TIMEZONE
    // =====================================================

    public static final String INDIA_TIME_ZONE = "Asia/Kolkata";

    public static final ZoneId DEFAULT_ZONE = ZoneId.of(INDIA_TIME_ZONE);

    public static final TimeZone TIME_ZONE = TimeZone.getTimeZone(INDIA_TIME_ZONE);

    // =====================================================
    // FORMATS
    // =====================================================

    public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

    public static final String DEFAULT_TIMESTAMP_FORMAT = "HH:mm:ss";

    public static final String DEFAULT_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    // =====================================================
    // CONSTANTS
    // =====================================================

    public static final int DATE = 1;
    public static final int MONTH = 2;
    public static final int YEAR = 3;
    public static final int WEEK = 4;

    // =====================================================
    // CURRENT DATE METHODS
    // =====================================================
    
    
    private AppCalendar() {
        instance = this;
        if(log.isTraceEnabled()) {
            log.trace("AppCalendar instance created");
        }

    }

    public static AppCalendar getInstance() {
    	if(instance == null) {
    		if(log.isTraceEnabled()) {
    			log.trace("Creating new instance of AppCalendar");
                instance = new AppCalendar();
    		}
    	}
    	return instance;
    }
    
    // =====================================================
    // CURRENT DATE METHODS
    // =====================================================

    public static Date getCurrentDate() {
        return Calendar.getInstance(TIME_ZONE).getTime();
    }

    public static java.sql.Date getCurrentSQLDate() {
        return new java.sql.Date(getCurrentDate().getTime());
    }

    public static Timestamp getCurrentSQLDateTime() {
        return new Timestamp(getCurrentDate().getTime());
    }

    public static String getCurrentDateString() {
        return formatDate(getCurrentDate());
    }

    // =====================================================
    // FORMAT METHODS
    // =====================================================

    public static String formatDate(Date date) {

        if (date == null) {
            return null;
        }

        return new SimpleDateFormat(DEFAULT_DATE_FORMAT)
                .format(date);
    }

    public static String formatDateTime(Date date) {

        if (date == null) {
            return null;
        }

        return new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT)
                .format(date);
    }

    public static String formatDateTimeWithSecond(Date date) {

        if (date == null) {
            return null;
        }

        String pattern = SystemConfigBean
                .getAttribute(DataFormatUtil.DATE_TIME_TIMESTAMPFORMAT_XML_KEY);

        return new SimpleDateFormat(pattern)
                .format(date);
    }

    public static String formatDateTimeIn12Format(Date date, String format) {

        if (date == null) {
            return null;
        }

        String pattern = StringUtils.hasText(format)
                ? format
                : "dd/MM/yyyy hh:mm:ss a";

        return new SimpleDateFormat(pattern)
                .format(date);
    }

    // =====================================================
    // PARSE METHODS
    // =====================================================

    public static Date convertStringtoDate(String value,
            String pattern)
            throws ExceptionCollection {

        try {

            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            sdf.setLenient(false);

            return sdf.parse(value);

        } catch (ParseException e) {
            throw new ExceptionCollection(e);
        }
    }

    public static String convertDatetoString(Date date,
            String pattern) {

        if (date == null) {
            return null;
        }

        return new SimpleDateFormat(pattern)
                .format(date);
    }

    // =====================================================
    // VALIDATION
    // =====================================================

    public static boolean isValidDateFormat(String value) {

        if (!StringUtils.hasText(value)) {
            return false;
        }

        try {

            SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

            sdf.setLenient(false);

            sdf.parse(value);

            return value.length() == DEFAULT_DATE_FORMAT.length();

        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isValidDateTimeFormat(String value) {

        if (!StringUtils.hasText(value)) {
            return false;
        }

        try {

            SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);

            sdf.setLenient(false);

            sdf.parse(value);

            return true;

        } catch (ParseException e) {
            return false;
        }
    }

    // =====================================================
    // DATE ADD / SUBTRACT
    // =====================================================

    public static Date add(Date startDate,
            int value,
            int periodType) {

        Calendar calendar = Calendar.getInstance(TIME_ZONE);

        calendar.setTime(startDate);

        switch (periodType) {

            case DATE -> calendar.add(Calendar.DATE, value);

            case MONTH -> calendar.add(Calendar.MONTH, value);

            case YEAR -> calendar.add(Calendar.YEAR, value);

            case WEEK -> calendar.add(Calendar.DATE, value * 7);

            default -> throw new IllegalArgumentException("Invalid period type");
        }

        return calendar.getTime();
    }

    public static Date subtract(Date startDate,
            int value,
            int periodType) {

        return add(startDate, -value, periodType);
    }

    // =====================================================
    // DATE DIFFERENCE
    // =====================================================

    public static long diffDate(Date startDate,
            Date endDate,
            int periodType) {

        long diffMillis = endDate.getTime() - startDate.getTime();

        return switch (periodType) {
            case DATE -> diffMillis / (1000 * 60 * 60 * 24);
            case WEEK -> diffMillis / (1000 * 60 * 60 * 24 * 7);
            case MONTH -> diffMillis / (1000L * 60 * 60 * 24 * 30);
            case YEAR -> diffMillis / (1000L * 60 * 60 * 24 * 365);
            default -> 0;
        };
    }

    // =====================================================
    // MONTH HELPERS
    // =====================================================

    public static Date getFirstDateOfMonth(Date date) {

        Calendar calendar = Calendar.getInstance(TIME_ZONE);

        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return calendar.getTime();
    }

    public static Date getLastDateOfMonth(Date date) {

        Calendar calendar = Calendar.getInstance(TIME_ZONE);

        calendar.setTime(date);

        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        return calendar.getTime();
    }

    // =====================================================
    // YEAR HELPERS
    // =====================================================

    public static Date getFirstDateOfYear(Date date) {

        Calendar calendar = Calendar.getInstance(TIME_ZONE);

        calendar.setTime(date);

        calendar.set(Calendar.DAY_OF_YEAR, 1);

        return calendar.getTime();
    }

    public static Date getLastDateOfYear(Date date) {

        Calendar calendar = Calendar.getInstance(TIME_ZONE);

        calendar.setTime(date);

        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);

        return calendar.getTime();
    }

    // =====================================================
    // LEAP YEAR
    // =====================================================

    public static boolean isLeapYear(Date date) {

        Calendar calendar = Calendar.getInstance(TIME_ZONE);

        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);

        return new GregorianCalendar().isLeapYear(year);
    }

    // =====================================================
    // XML GREGORIAN
    // =====================================================

    public static XMLGregorianCalendar toXMLGregorianCalendar(Date date) {

        GregorianCalendar calendar = new GregorianCalendar();

        calendar.setTime(date);

        try {

            return DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(calendar);

        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    // =====================================================
    // HIGH / LOW DATE
    // =====================================================

    public static Date getHighDate() {

        try {
            return new SimpleDateFormat("yyyy-MM-dd")
                    .parse("9999-01-01");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date getLowDate() {

        try {
            return new SimpleDateFormat("yyyy-MM-dd")
                    .parse("0001-01-01");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    // =====================================================
    // BUSINESS DATE
    // =====================================================
    public Date getBusinessDate() {
        return getCurrentDate();
    }

    public Timestamp getBusinessSqlDateTime() {

        return new Timestamp(
                getBusinessDate().getTime());
    }

    public java.sql.Date getBusinessSqlDate() {

        return new java.sql.Date(
                getBusinessDate().getTime());
    }

    public String getBusinessDateString() {

        return formatDate(getBusinessDate());
    }

    // =====================================================
    // TAX YEAR METHODS
    // =====================================================

    public static Date getTaxYearStartDate(Date date) {

        Calendar calendar = Calendar.getInstance(TIME_ZONE);

        calendar.setTime(date);

        // int year = calendar.get(Calendar.YEAR);

        calendar.set(Calendar.MONTH, Calendar.APRIL);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        Date taxYearStartDate = calendar.getTime();

        if (date.before(taxYearStartDate)) {
            calendar.add(Calendar.YEAR, -1);
        }

        return calendar.getTime();
    }

    public static Date getTaxYearEndDate(Date date) {

        Calendar calendar = Calendar.getInstance(TIME_ZONE);

        calendar.setTime(getTaxYearStartDate(date));

        calendar.add(Calendar.YEAR, 1);
        calendar.add(Calendar.DATE, -1);

        return calendar.getTime();
    }

    // =====================================================
    // MONTH / YEAR CALCULATIONS
    // =====================================================

    public static double getMonthsBetween(Date startDate,
            Date endDate) {

        long days = diffDate(startDate, endDate, DATE);

        return days / 30.0;
    }

    public static double getYearsBetween(Date startDate,
            Date endDate) {

        long days = diffDate(startDate, endDate, DATE);

        return days / 365.0;
    }

    // =====================================================
    // DAYS360
    // =====================================================

    public static Integer getDays360(Date startDate,
            Date endDate) {

        long days = diffDate(startDate, endDate, DATE);

        return (int) ((days / 365.0) * 360);
    }

    // =====================================================
    // WORKING DAY METHODS
    // =====================================================

    public static Date addWorkingDays(Date date,
            int days) {

        Calendar calendar = Calendar.getInstance(TIME_ZONE);

        calendar.setTime(date);

        int addedDays = 0;

        while (addedDays < days) {

            calendar.add(Calendar.DATE, 1);

            int day = calendar.get(Calendar.DAY_OF_WEEK);

            if (day != Calendar.SATURDAY
                    && day != Calendar.SUNDAY) {

                addedDays++;
            }
        }

        return calendar.getTime();
    }

    public static Date subtractWorkingDays(Date date,
            int days) {

        Calendar calendar = Calendar.getInstance(TIME_ZONE);

        calendar.setTime(date);

        int deductedDays = 0;

        while (deductedDays < days) {

            calendar.add(Calendar.DATE, -1);

            int day = calendar.get(Calendar.DAY_OF_WEEK);

            if (day != Calendar.SATURDAY
                    && day != Calendar.SUNDAY) {

                deductedDays++;
            }
        }

        return calendar.getTime();
    }

    // =====================================================
    // XML GREGORIAN HELPERS
    // =====================================================

    public static XMLGregorianCalendar getXMLGregorianCurrentTimeStamp() {

        try {

            GregorianCalendar calendar = new GregorianCalendar();

            calendar.setTime(getCurrentDate());

            return DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(calendar);

        } catch (DatatypeConfigurationException e) {

            throw new RuntimeException(e);
        }
    }

    // =====================================================
    // QUARTER HELPERS
    // =====================================================

    public static Date getQuarterEnd(Date date) {

        Calendar calendar = Calendar.getInstance(TIME_ZONE);

        calendar.setTime(date);

        int currentMonth = calendar.get(Calendar.MONTH);

        int quarterEndMonth = currentMonth / 3 * 3 + 2;

        calendar.set(Calendar.MONTH,
                quarterEndMonth);

        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(
                        Calendar.DAY_OF_MONTH));

        return calendar.getTime();
    }

    public static Date getTwoWeekBeforeQuarterEnd(
            Date date) {

        Calendar calendar = Calendar.getInstance(
                TIME_ZONE);

        calendar.setTime(getQuarterEnd(date));

        calendar.add(Calendar.DATE, -14);

        return calendar.getTime();
    }

    // =====================================================
    // YEAR HELPERS
    // =====================================================

    public static int getYearAfterCurrentYear(
            Date date) {

        Calendar calendar = Calendar.getInstance(TIME_ZONE);

        calendar.setTime(date);

        calendar.add(Calendar.YEAR, 1);

        return calendar.get(Calendar.YEAR);
    }

    // =====================================================
    // DATE COMPARISON
    // =====================================================

    public static boolean isBefore(Date date1,
            Date date2) {

        return date1.before(date2);
    }

    public static boolean isAfter(Date date1,
            Date date2) {

        return date1.after(date2);
    }

    public static boolean isEqual(Date date1,
            Date date2) {

        return date1.equals(date2);
    }

    // =====================================================
    // START / END OF DAY
    // =====================================================

    public static Date getStartOfDay(Date date) {

        Calendar calendar = Calendar.getInstance(TIME_ZONE);

        calendar.setTime(date);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static Date getEndOfDay(Date date) {

        Calendar calendar = Calendar.getInstance(TIME_ZONE);

        calendar.setTime(date);

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return calendar.getTime();
    }

    // =====================================================
    // DEBUG
    // =====================================================

    public static void printCurrentDate() {

        log.info(
                "Current Date Time : {}",
                formatDateTime(getCurrentDate()));
    }
}