package com.maalelan.postcardstorehouse.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class for handling date formatting and parsing.
 */
public class DateUtils {

    private static final SimpleDateFormat sdf =new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    /**
     * Parses a date string into a Date object
     * @param dateString the date as string, in format "dd.MM.yyyy"
     * @return parsed Date object or null if parsing fails
     */
    public static Date parse(String dateString) {
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Formats a Date object into a string with "dd.MM.yyyy" format.
     * @param date the Date object
     * @return formatted string or empty string if date is null
     */
    public static String format(Date date) {
        return date != null ? sdf.format(date) : "";
    }
}
