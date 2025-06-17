package com.maalelan.postcardstorehouse.utils;

import java.util.Date;

public class InputValidator {

    /**

     */
    public static Date getValidSentDate(String sentDateStr) {
        if (sentDateStr == null || sentDateStr.trim().isEmpty()) {
            return new Date();
        }
        Date parsed = DateUtils.parse(sentDateStr);
        if (parsed == null) return null;
        return parsed;
    }

    public static Date getValidReceivedDate(String receivedDateStr) {
        if (receivedDateStr == null || receivedDateStr.trim().isEmpty()) {
            return new Date();
        }
        Date parsed = DateUtils.parse(receivedDateStr);
        if (parsed == null) return null;
        return parsed;
    }


    public static boolean isChronological(Date sentDate, Date receivedDate) {
        if (sentDate == null || receivedDate == null) return true;
        return !receivedDate.before(sentDate);
    }
    public static boolean isValidCountry(String country) {
        return country != null && !country.trim().isEmpty() && country.length() <= 50;
    }

    public static boolean isValidTopic(String topic) {
        return topic != null && !topic.trim().isEmpty() && topic.length() <= 100;
    }

    public static boolean isValidNotes(String notes) {
        return notes != null && !notes.trim().isEmpty() && notes.length() <= 300;
    }

}
