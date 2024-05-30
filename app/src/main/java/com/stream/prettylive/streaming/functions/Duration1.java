package com.stream.prettylive.streaming.functions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Duration1 {
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

    public static long calculateDuration(long startTime, long endTime) {
        // Calculate the difference between the end time and start time
        return endTime - startTime;
    }




}
