package com.stream.prettylive.streaming.functions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Duration {
    public static String convertTimestampToDate(long timestamp) {
        // Convert the timestamp to milliseconds
        Date date = new Date(timestamp);

        // Create a SimpleDateFormat object with the desired date format
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault()); // Set the timezone to the device's default timezone

        // Format the date and return it as a string
        return sdf.format(date);
    }

    public static long calculateDurationInMinutes(long startTime, long endTime) {
        // Calculate the difference between the end time and start time
        long durationInMillis = endTime - startTime;

        // Convert the duration from milliseconds to minutes

        // Return the duration in minutes
        return TimeUnit.MILLISECONDS.toMinutes(durationInMillis);
    }
//
//    public static String calculateDuration(long startTime, long endTime) {
//        // Calculate the difference between the end time and start time
//        long duration = endTime - startTime;
//
//        // Convert the duration to seconds, minutes, and hours
//        long seconds = duration / 1000;
//        long minutes = seconds / 60;
//        long hours = minutes / 60;
//
//        // Calculate the remaining seconds and minutes after subtracting the hours
//        seconds %= 60;
//        minutes %= 60;
//
//        // Format the duration as HH:mm:ss
//        String durationString = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
//
//        return durationString;
//    }

    public static long calculateDuration(long startTime, long endTime) {
        // Calculate the difference between the end time and start time
        return endTime - startTime;
    }


}
