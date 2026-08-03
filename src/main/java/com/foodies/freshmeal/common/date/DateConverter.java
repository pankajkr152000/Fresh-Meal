package com.foodies.freshmeal.common.date;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * ============================================================================
 * DateConverter
 * ============================================================================
 *
 * Central utility for converting between:
 *
 * Date
 * Timestamp
 * LocalDate
 * LocalDateTime
 * Instant
 * Calendar
 * XMLGregorianCalendar
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @version 1.0
 */
public final class DateConverter {

    private DateConverter() {
        throw new IllegalStateException("Utility class");
    }

    // ========================================================================
    // DATE → LOCALDATE
    // ========================================================================

    public static LocalDate toLocalDate(Date date) {

        if (date == null) {
            return null;
        }

        return date.toInstant()
                .atZone(DateConstants.DEFAULT_ZONE)
                .toLocalDate();
    }

    // ========================================================================
    // DATE → LOCALDATETIME
    // ========================================================================

    public static LocalDateTime toLocalDateTime(Date date) {

        if (date == null) {
            return null;
        }

        return date.toInstant()
                .atZone(DateConstants.DEFAULT_ZONE)
                .toLocalDateTime();
    }

    // ========================================================================
    // LOCALDATE → DATE
    // ========================================================================

    public static Date toDate(LocalDate localDate) {

        if (localDate == null) {
            return null;
        }

        return Date.from(
                localDate.atStartOfDay(DateConstants.DEFAULT_ZONE)
                        .toInstant());
    }

    // ========================================================================
    // LOCALDATETIME → DATE
    // ========================================================================

    public static Date toDate(LocalDateTime localDateTime) {

        if (localDateTime == null) {
            return null;
        }

        return Date.from(
                localDateTime.atZone(DateConstants.DEFAULT_ZONE)
                        .toInstant());
    }

    // ========================================================================
    // LOCALDATE → LOCALDATETIME
    // ========================================================================

    public static LocalDateTime toLocalDateTime(LocalDate localDate) {

        if (localDate == null) {
            return null;
        }

        return localDate.atStartOfDay();
    }

    // ========================================================================
    // LOCALDATETIME → LOCALDATE
    // ========================================================================

    public static LocalDate toLocalDate(LocalDateTime localDateTime) {

        if (localDateTime == null) {
            return null;
        }

        return localDateTime.toLocalDate();
    }

    // ========================================================================
    // LOCALTIME → LOCALDATETIME
    // ========================================================================

    public static LocalDateTime toLocalDateTime(LocalDate date,
            LocalTime time) {

        if (date == null || time == null) {
            return null;
        }

        return LocalDateTime.of(date, time);
    }

    // ========================================================================
    // TIMESTAMP → LOCALDATETIME
    // ========================================================================

    public static LocalDateTime toLocalDateTime(Timestamp timestamp) {

        if (timestamp == null) {
            return null;
        }

        return timestamp.toLocalDateTime();
    }

    // ========================================================================
    // LOCALDATETIME → TIMESTAMP
    // ========================================================================

    public static Timestamp toTimestamp(LocalDateTime localDateTime) {

        if (localDateTime == null) {
            return null;
        }

        return Timestamp.valueOf(localDateTime);
    }

    // ========================================================================
    // DATE → TIMESTAMP
    // ========================================================================

    public static Timestamp toTimestamp(Date date) {

        if (date == null) {
            return null;
        }

        return new Timestamp(date.getTime());
    }

    // ========================================================================
    // TIMESTAMP → DATE
    // ========================================================================

    public static Date toDate(Timestamp timestamp) {

        if (timestamp == null) {
            return null;
        }

        return new Date(timestamp.getTime());
    }

    // ========================================================================
    // DATE → INSTANT
    // ========================================================================

    public static Instant toInstant(Date date) {

        if (date == null) {
            return null;
        }

        return date.toInstant();
    }

    // ========================================================================
    // INSTANT → DATE
    // ========================================================================

    public static Date toDate(Instant instant) {

        if (instant == null) {
            return null;
        }

        return Date.from(instant);
    }

    // ========================================================================
    // DATE → CALENDAR
    // ========================================================================

    public static Calendar toCalendar(Date date) {

        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance(DateConstants.DEFAULT_TIME_ZONE);
        calendar.setTime(date);

        return calendar;
    }

    // ========================================================================
    // CALENDAR → DATE
    // ========================================================================

    public static Date toDate(Calendar calendar) {

        if (calendar == null) {
            return null;
        }

        return calendar.getTime();
    }

    // ========================================================================
    // DATE → XMLGregorianCalendar
    // ========================================================================

    public static XMLGregorianCalendar toXMLGregorianCalendar(Date date) {

        if (date == null) {
            return null;
        }

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        try {

            return DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(calendar);

        } catch (DatatypeConfigurationException e) {

            throw new RuntimeException(e);
        }
    }

    // ========================================================================
    // XMLGregorianCalendar → DATE
    // ========================================================================

    public static Date toDate(XMLGregorianCalendar calendar) {

        if (calendar == null) {
            return null;
        }

        return calendar.toGregorianCalendar().getTime();
    }

}