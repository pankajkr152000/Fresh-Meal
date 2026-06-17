package com.foodies.freshmeal.common.date;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.foodies.freshmeal.common.exception.SystemConfigBean;

public class DataFormatUtil {
    /**
     * Default date and time format. Used to format date.
     */
    public static final String DEFAULT_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm";

    public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

    public static final String DATE_FORMAT_1 = "MM/dd/yyyy";

    public static final String DATE_FORMAT_2 = "yyyy/MM/dd";

    public static final String DATE_FORMAT_DDMMYYYY = "ddMMyyyy";

    public static final String DATE_FORMAT_DDMMUUUU = "ddMMuuuu";

    private static final String DEFAULT_12_HR_DATE_TIME_FORMAT = "dd/MM/yyyy hh:mm:ss a";

    public static final String DEFAULT_24_HR_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    public static final DateTimeFormatter LOG_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Used to concatenate array of String where comma(, ) is the separator.
     */
    public static final String COMMA_SPACE_SEPARATOR = ", ";

    /**
     * Used to concatenate array of String where comma(,) is the separator.
     */
    public static final String COMMA_SEPARATOR = ",";

    /**
     * Used to concatenate array of String where front slash(/) is the
     * separator.
     */
    public static final String HYPHEN_SEPARATOR = " - ";

    /**
     * Used to concatenate array of String where hyphen(-) is the
     * separator.
     */
    public static final String SLASH_SPACE_SEPARATOR = "/ ";

    /**
     * Used to concatenate array of String where space( ) is the separator.
     */
    public static final String SPACE = " ";

    /**
     * Empty String constant.
     */
    public static final String EMPTY_STRING = "";

    public static final String TILDA_SEPARATOR = "~";

    public static final String DECIMAL_PACKED_BINARY = "%012.2f";

    public static final String INTEGERAL_PACKED_BINARY = "%09d";

    public static final String DATE_TIME_DATEFORMAT_XML_KEY = "//Config/EnvComCalendar/DateFormat";

    public static final String DATE_TIME_TIMESTAMPFORMAT_XML_KEY = "//Config/EnvComCalendar/LocalDateTimeFormat";

    public static final String DATE_TIMESTAMPFORMAT_XML_KEY = "//Config/EnvComCalendar/DateTimeFormat";

    // public static final String DATE_FORMAT =
    // SystemConfigBean.getAttribute(DATE_TIME_DATEFORMAT_XML_KEY);

    public static String getDateFormat() {
        return SystemConfigBean.getAttribute(DATE_TIME_DATEFORMAT_XML_KEY);
    }

    public static final Long SEGMENT_NO_DIGIT_COUNT = 3L;

    public static final String DISPLAY_POLICY_NUMBER_WITH_BRACES = "//Config/DisplayPolicyNumberWithBraces";

    public static final String DISPLAY_POLICY_NUMBER_WITH_BRACES_VALUE_YES = "Y";

    public static final String DISPLAY_POLICY_NUMBER_WITH_BRACES_VALUE_NO = "N";

    public static final String OPEN_BRACE = "[";

    public static final String CLOSE_BRACE = "]";

    public static final String SEGMENT_DELIMITER = "-";

    public static final String SLASH_SEPARATOR = "/";

    public static final String COLON_SEPARATOR = ":";

    public static final String ADDRESS_SIGN_SEPARATOR = "@";

    /**
     * Formats <code>Date</code> and returns <code>String</code>
     * 
     * @param date
     *             <code>Date</code> to format
     * @return Formatted date as <code>String</code>
     */
    public static String formatDate(Date date) {

        if (date == null) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        return sdf.format(date);
    }

    /**
     * Formats <code>Date</code> and returns <code>String</code>
     * 
     * @param date
     *             <code>Date</code> to format
     * @return Formatted date and time as <code>String</code>
     */
    public static String formatDateTime(Date date) {

        if (date == null) {
            return null;
        }

        String pattern = SystemConfigBean
                .getAttribute(DATE_TIME_DATEFORMAT_XML_KEY);

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        return sdf.format(date);
    }

    /**
     * Concatenates array of <code>String</code> and returns the concatenated
     * <code>String</code>
     * 
     * @param strings
     *                  Array of <code>String</code>
     * @param separator
     *                  <code>String</code> to be used as separator
     * @return Concatenated <code>String</code>
     */
    public static String concatStrings(String[] strings, String separator) {
        StringBuilder sb = new StringBuilder();
        boolean appendSeparator = false;
        for (String string : strings) {
            if (org.springframework.util.StringUtils.hasText(string)) {
                if (appendSeparator) {
                    sb.append(separator);
                }
                appendSeparator = true;
                sb.append(string);
            }
        }
        return sb.toString();
    }

    public static String getUDFFormat(String str, String sep) {
        String strArr[] = str.split(sep);
        return strArr[1] + sep + strArr[0] + sep + strArr[2];
    }

     
    public static Date getDateFromString(String str) {
        Date date = null;
        try {
            if (StringUtils.hasText(str)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                date = sdf.parse(str);
            }
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        return date;
    }

    public static boolean isValidDateFormat(String str) {

        if (!StringUtils.hasText(str)) {
            return false;
        }

        Pattern p = Pattern.compile("[^0-9/]");
        Matcher m = p.matcher(str);

        if (m.find()) {
            return false;
        }

        String pattern = "dd/MM/yyyy"; // use your required format

        try {

            SimpleDateFormat sdf = new SimpleDateFormat(pattern);

            // Strict parsing
            sdf.setLenient(false);

            // Validate exact length
            if (str.length() != pattern.length()) {
                return false;
            }

            sdf.parse(str);

            return true;

        } catch (ParseException e) {
            return false;
        }
    }

     
    public static Date getDateTimeFormString(String str) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            sdf.setLenient(false);
            date = sdf.parse(str);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        return date;
    }

     
    public static Date getDateTimeFormStringMcb(String str) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setLenient(false);
            date = sdf.parse(str);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        return date;
    }

     
    public static Date getDateFormString(String str) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DataFormatUtil.getDateFormat());
            sdf.setLenient(false);
            date = sdf.parse(str);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        return date;
    }

    public static String getPackedDecimal(String s) {
        return getPackedDecimal(Double.valueOf(s));
    }

    public static String getPackedDecimal(Double d) {
        return getPackedDecimal(DECIMAL_PACKED_BINARY, d);
    }

    public static String getPackedDecimal(String format, Double d) {
        return String.format(DECIMAL_PACKED_BINARY, d);
    }

    public static String getPackedIntegral(String s) {
        return getPackedIntegral(Long.valueOf(s));
    }

    public static String getPackedIntegral(Long l) {
        return getPackedIntegral(INTEGERAL_PACKED_BINARY, l);
    }

    public static String getPackedIntegral(String format, Long l) {
        return String.format(format, l);
    }

    public static Date getDateWithoutTime(Date date) {
        Date newDate = new Date(date.getTime());

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(newDate);

        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);

        return new Date(calendar.getTimeInMillis());
    }

    public static final String getSuccessMessage(String messageCode,
            String recordType, boolean closeWin) {
        return MessageFormat.format(messageCode, new Object[] { recordType,
                closeWin ? "true" : "false" });
    }

    public static final String getNoOfRecordsMessage(String messageCode,
            int count, String recordType, String operationName) {
        return MessageFormat.format(messageCode, new Object[] { count,
                recordType, count == 1 ? "" : "s", operationName });
    }

    public static final String getNoRecordFoundMessage(String messageCode,
            String recordType) {
        return MessageFormat.format(messageCode, new Object[] { recordType });
    }

    public static Date getMidnightDate(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(DataFormatUtil.getDateWithoutTime(date));
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
        calendar.add(Calendar.SECOND, -1);
        Date newDate = new Date();
        newDate.setTime(calendar.getTimeInMillis());
        return newDate;
    }

    /**
     * 
     * @param d=
     *              Double what will be converted to String
     * @param value =
     *              the precision of the Double if not specified it is taken as 2
     * @return the String representation of the Double
     * 
     */
    public static String getStringfromDouble(Double d, Integer... value) {
        int precision = 2;
        if (value.length > 0)
            precision = value[0];
        if (d != null) {
            String formatString = "0.";
            for (int i = 0; i < precision; i++)
                formatString += "0";
            NumberFormat formatter = new DecimalFormat(formatString);
            return formatter.format(d);
        } else
            return null;
    }

    public static String formatDateTimeIn12Format(Date date, String format) {

        if (date == null) {
            return null;
        }

        String pattern;

        if (!StringUtils.hasText(format)) {
            pattern = DEFAULT_12_HR_DATE_TIME_FORMAT;
        } else {
            pattern = format;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        return sdf.format(date);
    }

    public static final String getSuccessMessageForAnyNoOfArgumnet(
            String messageCode, ArrayList<String> arguments, boolean closeWin) {
        arguments.add(closeWin ? "true" : "false");
        return MessageFormat.format(messageCode, arguments.toArray());
    }

    public static Long encodeUnitTimeFormat(String unitTime) {
        long totalSecondCount;
        Integer hoursCount = Integer.valueOf(unitTime.substring(0, 2));
        Integer minuteCount = Integer.valueOf(unitTime.substring(3, 5));
        Long secondCount = Long.valueOf(unitTime.substring(6, 8));
        totalSecondCount = hoursCount * 3600 + minuteCount * 60 + secondCount;
        return totalSecondCount;

    }

    public static long getNoOfWorkingDays(Date startDate, Date endDate,
            Integer[] weekendHolidaysArray, String[] yearlyHolidaysArray)
            throws Exception {

        long noOfDays;

        noOfDays = (endDate.getTime() - startDate.getTime()) / 86400000l;

        // Instance for the working date
        Calendar startCalDate = new GregorianCalendar();

        // Instance for the latest date
        Calendar endCalDate = new GregorianCalendar();
        startCalDate.setTime(startDate);
        endCalDate.setTime(endDate);
        // Needed to increase the endDate value else it will stop at endDate-1
        endCalDate.add(Calendar.DATE, 1);

        /** ************** Test for Weekly Holidays ************ */
        long noOfWeekends = 0;

        if (weekendHolidaysArray != null) {
            while (startCalDate.before(endCalDate)) {
                int dayOfWeek = startCalDate.get(Calendar.DAY_OF_WEEK);
                for (Integer weekendHolidaysArray1 : weekendHolidaysArray) {
                    if (weekendHolidaysArray1 == dayOfWeek) {
                        noOfWeekends++;
                        break;
                    }
                }
                startCalDate.add(Calendar.DATE, 1);
            }
        }

        /** ************** Test for Yearly Holidays ************ */
        long noOfYearlyHolidays = 0;

        SimpleDateFormat sdf_DD_MM_YYYY = new SimpleDateFormat("dd/MM/yyyy");

        int startYear = startCalDate.get(Calendar.YEAR);
        int endYear = endCalDate.get(Calendar.YEAR);

        if (yearlyHolidaysArray != null) {
            for (String yearlyHolidaysArray1 : yearlyHolidaysArray) {
                if (startYear == endYear) {
                    Date holiday = sdf_DD_MM_YYYY.parse(yearlyHolidaysArray1 + "/" + startYear);
                    if (!holiday.before(startDate) && !holiday.after(endDate)) {
                        noOfYearlyHolidays++;
                    }
                } else {
                    for (int year = startCalDate.get(Calendar.YEAR); year <= endCalDate
                            .get(Calendar.YEAR); year++) {
                        Date holiday = sdf_DD_MM_YYYY.parse(yearlyHolidaysArray1 + "/" + year);
                        Calendar date = new GregorianCalendar();
                        date.setTime(holiday);
                        if (isWeekend(date, weekendHolidaysArray)
                                || (year == startYear && holiday
                                        .before(startDate))
                                || (year == endYear && holiday.after(endDate))) {
                            continue;
                        }
                        noOfYearlyHolidays++;
                    }
                }
            }
        }

        long noOfWorkingDays = noOfDays - noOfWeekends - noOfYearlyHolidays;

        return noOfWorkingDays;
    }

    private static boolean isWeekend(Calendar checkDate, Integer[] weekendArray) {
        if (null != weekendArray) {
            for (Integer weekendArray1 : weekendArray) {
                if (weekendArray1 == checkDate.get(Calendar.DAY_OF_WEEK)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    public static String formatDateTimeWithSecond(Date date) {

        if (date == null) {
            return null;
        }

        String pattern = SystemConfigBean
                .getAttribute(DATE_TIME_TIMESTAMPFORMAT_XML_KEY);

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        return sdf.format(date);
    }

    public static String decodeUnitTimeFormat(Long unitTime) {
        Integer hoursCount = 0;
        Integer minuteCount = (int) (unitTime / 60);
        Integer secondCount = (int) (unitTime % 60);

        if (minuteCount >= 60) {
            hoursCount = minuteCount / 60;
            minuteCount = minuteCount % 60;
        }

        String hoursCountValue = hoursCount.toString();
        String minuteCountValue = minuteCount.toString();
        String secondCountValue = secondCount.toString();

        if (hoursCountValue.length() < 2)
            hoursCountValue = "0" + hoursCountValue;
        if (minuteCountValue.length() < 2)
            minuteCountValue = "0" + minuteCountValue;
        if (secondCountValue.length() < 2)
            secondCountValue = "0" + secondCountValue;

        String decodedUnitTime = hoursCountValue + ":" + minuteCountValue + ":"
                + secondCountValue;

        return decodedUnitTime;

    }

     
    public static Date getDateFromString(String str, String format) {
        Date date = null;
        try {
            if (StringUtils.hasText(str)) {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                date = sdf.parse(str);
            }
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        return date;
    }

     
    public static Date getDateTimeFromString(String str, String format) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.setLenient(false);
            date = sdf.parse(str);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        return date;
    }

    public static String getDateAsString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static java.sql.Date getSQLDateFromUtilDate(java.util.Date date) {
        if (date != null) {
            return new java.sql.Date(date.getTime());
        }
        return (java.sql.Date) null;
    }

    public static java.sql.Timestamp getSQLTimestampFromUtilDate(java.util.Date date) {
        if (date != null) {
            return new java.sql.Timestamp(date.getTime());
        }
        return null;
    }

    public static java.util.Date getUtilDateFromSQLDate(java.sql.Date date) {
        if (date != null) {
            return new java.util.Date(date.getTime());
        }
        return null;
    }

    public static java.util.Date getUtilDateFromSQLTimestamp(java.sql.Timestamp timestamp) {
        if (timestamp != null) {
            return new java.util.Date(timestamp.getTime());
        }
        return null;
    }

    public static String getNumberOfASpecifiedDigit(String number, Long noOfDigit) {
        if (StringUtils.hasText(number))
            return null;
        String finalNumberString = "";
        String numberString = number;
        Integer noOfDigitOfNumber = (Integer) numberString.length();
        if (noOfDigitOfNumber >= Integer.valueOf(noOfDigit.toString())) {
            return number;
        }
        Long noOfZerosRequired = noOfDigit - noOfDigitOfNumber;
        if (noOfZerosRequired > 0) {
            for (int i = 0; i < noOfZerosRequired; i++) {
                finalNumberString = finalNumberString.concat("0");
            }
        }
        finalNumberString = finalNumberString.concat(numberString);

        return finalNumberString;
    }

    /**
     * Gives the benefit range depending on startSegmentNo and activeNoOfSegments.
     * eg- Start Segment = 0; No of active segment = 100
     * output - 000-099
     * 
     * @param startSegmentNo
     * @param activeNoOfSegments
     * @return
     */
    public static String getBenefitSegmentRange(Long startSegmentNo, Long activeNoOfSegments) {
        if (startSegmentNo == null || startSegmentNo < 0 || activeNoOfSegments == null || activeNoOfSegments < 0)
            return null; // Error occured return null
        int specifiedDigit = 3;
        String formattedStartSegment = String.format("%0" + specifiedDigit + "d", startSegmentNo);

        if (activeNoOfSegments == 0) {
            return formattedStartSegment;
        }

        Long endSegmentRange = startSegmentNo + activeNoOfSegments - 1;
        String formattedEndSegment = String.format("%0" + specifiedDigit + "d", endSegmentRange);

        String segmentRangeForBenefit = formattedStartSegment + SEGMENT_DELIMITER + formattedEndSegment;

        return segmentRangeForBenefit;
    }

    /**
     * It gives the segment range depending on endSegmentNo and noOfSegmentsUsed
     * eg- endSegmentNo = 199; No of segment used = 50
     * output: 149-199
     * 
     * @param endSegmentNo
     * @param noOfSegmentsUsed
     * 
     */
    public static String getCalculatedBenefitSegmentRange(Long endSegmentNo, Long noOfSegmentsUsed) {
        if (endSegmentNo == null || endSegmentNo < 0 || noOfSegmentsUsed == null || noOfSegmentsUsed < 0)
            return null; // Error occured return null
        int specifiedDigit = 3;
        String formattedEndSegment = String.format("%0" + specifiedDigit + "d", specifiedDigit);

        if (noOfSegmentsUsed == 0) {
            return formattedEndSegment;
        }

        Long startSegmentNo = endSegmentNo - noOfSegmentsUsed + 1;
        String formattedStartSegment = String.format("%0" + specifiedDigit + "d", startSegmentNo);

        String segmentRangeForBenefit = formattedStartSegment + SEGMENT_DELIMITER + formattedEndSegment;

        return segmentRangeForBenefit;
    }

    /**
     * Gives the benefit range depending on startSegmentNo and activeNoOfSegments.
     * eg- Start Segment = 0; No of active segment = 100
     * output - 000-099
     * 
     * @param startSegmentNo
     * @param activeNoOfSegments
     * @return
     */
    public static String getBenefitSegmentRange(Integer startSegmentNo, Integer activeNoOfSegments) {

        if (startSegmentNo == null || startSegmentNo < 0 || activeNoOfSegments == null || activeNoOfSegments < 0)
            return null; // Error occured return null

        return getBenefitSegmentRange(Long.valueOf(startSegmentNo), Long.valueOf(activeNoOfSegments));
    }

    public static String getDisplayPolicyNumber(String legPolNum, boolean isPolicySegmented, String startSegmentNo) {

        String displayPolicyNumber = legPolNum;

        if (isPolicySegmented) {

            String formatedStartSegmentString = getDisplaySegmentNumber(startSegmentNo);

            if (DISPLAY_POLICY_NUMBER_WITH_BRACES_VALUE_YES
                    .equals(SystemConfigBean.getAttribute(DISPLAY_POLICY_NUMBER_WITH_BRACES))) {
                if (org.springframework.util.StringUtils.hasText(formatedStartSegmentString)) {
                    displayPolicyNumber = displayPolicyNumber + OPEN_BRACE + formatedStartSegmentString + CLOSE_BRACE;
                }
            } else {
                if (org.springframework.util.StringUtils.hasText(formatedStartSegmentString)) {
                    displayPolicyNumber = displayPolicyNumber + SEGMENT_DELIMITER + formatedStartSegmentString;
                }
            }
        }
        return displayPolicyNumber;
    }

    public static String getDisplaySegmentNumber(String segmentNo) {
        return String.format(segmentNo, 3, "0");
    }

    public static <T> List<T> getPaginatedSublistForMultiDB(Object startRowNum, Object endRowNum, List<T> list) {

        int totalNoOfRows = list.size();
        int startRowNumber = (startRowNum != null ? (Integer) startRowNum : 0);
        int endRowNumber = (endRowNum != null ? (Integer) endRowNum : totalNoOfRows);
        if (endRowNumber > totalNoOfRows)
            endRowNumber = totalNoOfRows;

        return list.subList(startRowNumber, endRowNumber);
    }

    public static String getStringFromClob(Clob clobData)
            throws SQLException, IOException {

        if (clobData == null) {
            return null;
        }

        StringWriter writer = new StringWriter();

        try (Reader reader = clobData.getCharacterStream();
                BufferedReader br = new BufferedReader(reader)) {

            char[] buffer = new char[1024];
            int length;

            while ((length = br.read(buffer)) != -1) {
                writer.write(buffer, 0, length);
            }
        }

        return writer.toString();
    }

    public static final boolean isValidDate(String date) {

        boolean validDate = false;

        int month, day, year;

        if (StringUtils.hasText(date)) {
            String arr[] = date.split("[/\n]");
            month = Integer.parseInt(arr[1]);
            day = Integer.parseInt(arr[0]);
            year = Integer.parseInt(arr[2]);

            if ((month >= 1 && month <= 12) && (day >= 1 && day <= 31)) {
                // For months with 30 days
                if ((month == 4 || month == 6 || month == 9 || month == 11) && (day <= 30)) {
                    validDate = true;
                }

                // For months with 31 days
                if ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)
                        && (day <= 31)) {
                    validDate = true;
                }

                // For 30/02
                if (month == 2 && day > 29) {
                    validDate = false;
                }

                // For February
                if ((month == 2) && (day < 30)) {
                    // Boolean for valid leap year
                    boolean validLeapYear = false;

                    // A leap year is any year that is divisible by 4 but not divisible by 100
                    // unless it is also divisible by 400
                    if ((year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0))) {
                        validLeapYear = true;
                    }

                    if (validLeapYear == true && day <= 29) {
                        validDate = true;
                    }

                    else if (validLeapYear == false && day <= 28) {
                        validDate = true;
                    }
                }
            }

        }
        return validDate;
    }

    public static final boolean isValidDate(String date, String dateFormat) {

        boolean validDate = false;

        int month, day, year;

        if (StringUtils.hasText(date)
                && StringUtils.hasText(dateFormat)) {

            if (DEFAULT_DATE_FORMAT.equals(dateFormat)) {

                String arr[] = date.split("[/\n]");
                month = Integer.parseInt(arr[1]);
                day = Integer.parseInt(arr[0]);
                year = Integer.parseInt(arr[2]);

                if ((month >= 1 && month <= 12) && (day >= 1 && day <= 31)) {
                    // For months with 30 days
                    if ((month == 4 || month == 6 || month == 9 || month == 11) && (day <= 30)) {
                        validDate = true;
                    }

                    // For months with 31 days
                    if ((month == 1 || month == 2 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10
                            || month == 12) && (day <= 31)) {
                        validDate = true;
                    }

                    // For 30/02
                    if (month == 2 && day > 29) {
                        validDate = false;
                    }

                    // For February
                    if ((month == 2) && (day < 30)) {
                        // Boolean for valid leap year
                        boolean validLeapYear = false;

                        // A leap year is any year that is divisible by 4 but not divisible by 100
                        // unless it is also divisible by 400
                        if ((year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0))) {
                            validLeapYear = true;
                        }

                        if (validLeapYear == true && day <= 29) {
                            validDate = true;
                        }

                        else if (validLeapYear == false && day <= 28) {
                            validDate = true;
                        }
                    }
                }

            } else if (DATE_FORMAT_1.equals(dateFormat)) {

                String arr[] = date.split("[/\n]");
                day = Integer.parseInt(arr[1]);
                month = Integer.parseInt(arr[0]);
                year = Integer.parseInt(arr[2]);

                if ((month >= 1 && month <= 12) && (day >= 1 && day <= 31)) {
                    // For months with 30 days
                    if ((month == 4 || month == 6 || month == 9 || month == 11) && (day <= 30)) {
                        validDate = true;
                    }

                    // For months with 31 days
                    if ((month == 1 || month == 2 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10
                            || month == 12) && (day <= 31)) {
                        validDate = true;
                    }

                    // For 30/02
                    if (month == 2 && day > 29) {
                        validDate = false;
                    }

                    // For February
                    if ((month == 2) && (day < 30)) {
                        // Boolean for valid leap year
                        boolean validLeapYear = false;

                        // A leap year is any year that is divisible by 4 but not divisible by 100
                        // unless it is also divisible by 400
                        if ((year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0))) {
                            validLeapYear = true;
                        }

                        if (validLeapYear == true && day <= 29) {
                            validDate = true;
                        }

                        else if (validLeapYear == false && day <= 28) {
                            validDate = true;
                        }
                    }
                }

            }

        }
        return validDate;
    }

    public static String joinStringWithSeparator(String separator, String... keys) {

        if (keys == null) {
            return null;
        }

        return String.join(separator, keys);
    }

    public static Map<String, String> getUrlDataMap(String transportableData) {

        Map<String, String> urlDataMap = new HashMap<>();

        String[] parameters = StringUtils.hasText(transportableData) ? transportableData
                .split(TILDA_SEPARATOR) : null;

        if (parameters != null)
            for (String parameter : parameters) {

                urlDataMap.put(parameter.split(ADDRESS_SIGN_SEPARATOR)[0],
                        parameter.split(ADDRESS_SIGN_SEPARATOR)[1]);
            }
        return urlDataMap;
    }

    /**
     * This method has been introduced to split a comma separated string up to
     * maximum length of 4000
     * characters. This method is currently used to remove hard parsing in different
     * queries in production.
     */
    public static final List<String> getMaxLenAllowListFromCommaSepdStr(String inputValue) {

        List<String> returnValue = new ArrayList<>();
        if (StringUtils.hasText(inputValue)) {
            /**
             * Replace all single quotes(') with empty value, if present in the input string
             * to form the query in executable format. If this quote removal was not there
             * then there
             * will be double quotes append at first and last place.
             */
            inputValue = inputValue.replace("'", "");
            if (inputValue.length() >= 3999) {
                String lengthAllowableInputValue;
                String lengthAllowableCorrectInputValue;
                do {
                    lengthAllowableInputValue = inputValue.substring(0, 3999);
                    lengthAllowableCorrectInputValue = lengthAllowableInputValue.substring(0,
                            lengthAllowableInputValue.lastIndexOf(","));
                    inputValue = inputValue.substring(lengthAllowableCorrectInputValue.length() + 1,
                            inputValue.length());
                    returnValue.add(lengthAllowableCorrectInputValue);
                } while (inputValue.length() >= 3999);

                returnValue.add(inputValue);

            } else {
                returnValue.add(inputValue);
            }
        }

        return returnValue;
    }

    // Added during LTA protection implementation
     
    public static boolean isGraterThanToday(String inputDate, Date today) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date input;
        try {
            input = sdf.parse(inputDate);
            return input.after(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }
    // End LTA protection implementation

    // for Date Format dd-mmm-yy as input
     
    public static Date getDateFromStringInDifferentDtFormt(String str) {
        Date date = null;
        try {
            if (StringUtils.hasText(str)) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
                date = sdf.parse(str);
            }
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        return date;
    }

     
    public static Date parseStringToDate(String str) {
        Date date = null;
        try {
            if (StringUtils.hasText(str)) {
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DDMMYYYY);
                date = sdf.parse(str);
            }
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        return date;
    }

     
    public static LocalDate parseStringToLocalDate(String givenDateString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_DDMMUUUU)
                    .withResolverStyle(ResolverStyle.STRICT);
            return LocalDate.parse(givenDateString, formatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
	public static LocalDateTime parseStringToLocalDateTime(String dateTime) {

		if (dateTime == null || dateTime.isBlank()) {
			return null;
		}

		try {
			return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(AppCalendar.DEFAULT_DATE_TIME_FORMAT));
		} catch (Exception ex) {
			throw new IllegalArgumentException("Invalid date format. Expected format: " + DEFAULT_DATE_TIME_FORMAT, ex);
		}
	}

    public static String getFinancialYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH); // 0 = January, 3 = April
        int fyStartYear = (month >= Calendar.APRIL) ? year : year - 1;
        int fyEndYearShort = (fyStartYear + 1) % 100;
        return fyStartYear + "/" + String.format("%02d", fyEndYearShort);
    }

    public static String getFinancialYearIfSame(Date startDate, Date endDate) {
        String startFY = getFinancialYear(startDate);
        String endFY = getFinancialYear(endDate);
        if (startFY.equals(endFY)) {
            return startFY;
        } else {
            // throw new IllegalArgumentException("Start and end dates are not in the same
            // financial year.");
            return null;
        }
    }

    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        return cal.get(Calendar.YEAR);
    }

    /**
     * This returns the financial year start date based on year. Ex - If 2024 is
     * provided, FY start date is 1st April 2023.
     * 
     * @author 1344327
     * @param financialYear
     * @return
     */
    public static Date getFinancialYearStartDate(String financialYear) {
        int fy = Integer.parseInt(financialYear);
        int startYear = fy - 1;
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, startYear);
        cal.set(Calendar.MONTH, Calendar.APRIL);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    /**
     * This returns the financial year end date based on year. Ex - If 2024 is
     * provided, FY end date is 31st March 2024.
     * 
     * @author 1344327
     * @param financialYear
     * @return
     */
    public static Date getFinancialYearEndDate(String financialYear) {
        int fy = Integer.parseInt(financialYear);
        int endYear = fy;
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, endYear);
        cal.set(Calendar.MONTH, Calendar.MARCH);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        return cal.getTime();
    }

    /**
     * This returns the financial year. If input is 2024, financial year is 2023-24.
     * 
     * @author 1344327
     * @param year
     * @return
     */
    public static String getFinancialYear(String year) {

        int fy = Integer.parseInt(year);
        int fyEndYearShort = (fy) % 100;
        return (fy - 1) + "/" + String.format("%02d", fyEndYearShort);
    }
}
