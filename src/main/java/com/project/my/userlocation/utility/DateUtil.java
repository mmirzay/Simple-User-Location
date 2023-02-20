package com.project.my.userlocation.utility;

import com.project.my.userlocation.exception.DateFormatException;
import lombok.experimental.UtilityClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utility class for parse and formatting dates for {@code createdOn} value of {@link com.project.my.userlocation.entity.Location}s.
 * It also includes default pattern for dates. It uses {@code UTC} as default timezone.
 */
@UtilityClass
public class DateUtil {

    public static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_TIME_PATTERN);

    static {
        SIMPLE_DATE_FORMAT.setTimeZone(UTC_TIME_ZONE);
    }

    public static Date parse(String date) {
        try {
            return SIMPLE_DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            throw new DateFormatException(MessageTranslatorUtil.getText("util.date.parse.exception.message", date));
        }
    }

    public static String format(Date date){
        return SIMPLE_DATE_FORMAT.format(date);
    }
}