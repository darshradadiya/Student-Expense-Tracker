package com.student.expensetracker.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Utility helper class for date and time formatting throughout the app.
 */
public class DateTimeUtils {

    private static final String ISO_FORMAT = "yyyy-MM-dd";
    private static final String DISPLAY_FORMAT = "dd MMM yyyy";
    private static final String MONTH_PREFIX_FORMAT = "yyyy-MM";
    private static final String MONTH_NAME_FORMAT = "MMMM yyyy";

    public static String getCurrentDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat(ISO_FORMAT, Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String getCurrentMonthPrefix() {
        SimpleDateFormat sdf = new SimpleDateFormat(MONTH_PREFIX_FORMAT, Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String getCurrentMonthDisplayName() {
        SimpleDateFormat sdf = new SimpleDateFormat(MONTH_NAME_FORMAT, Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String formatDateForDisplay(String isoDate) {
        if (isoDate == null || isoDate.isEmpty()) return "";
        try {
            SimpleDateFormat input = new SimpleDateFormat(ISO_FORMAT, Locale.getDefault());
            SimpleDateFormat output = new SimpleDateFormat(DISPLAY_FORMAT, Locale.getDefault());
            Date date = input.parse(isoDate);
            return date != null ? output.format(date) : isoDate;
        } catch (ParseException e) {
            return isoDate;
        }
    }

    public static String getPastDateString(int daysAgo) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo);
        SimpleDateFormat sdf = new SimpleDateFormat(ISO_FORMAT, Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    public static String formatCurrency(double amount) {
        return String.format(Locale.getDefault(), "₹%,.2f", amount);
    }

    public static String formatCurrencyNoDecimals(double amount) {
        return String.format(Locale.getDefault(), "₹%,.0f", amount);
    }
}
