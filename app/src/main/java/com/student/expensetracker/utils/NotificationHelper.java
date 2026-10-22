package com.student.expensetracker.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.student.expensetracker.R;
import com.student.expensetracker.activities.MainActivity;

/**
 * Utility to manage and display student expense alerts and budget warning notifications.
 */
public class NotificationHelper {

    private static final String CHANNEL_ID = "student_expense_channel";
    private static final String CHANNEL_NAME = "Expense Tracker Notifications";
    private static final String CHANNEL_DESC = "Notifications for new expenses and budget warnings";

    private static final int EXPENSE_NOTIF_ID = 1001;
    private static final int BUDGET_NOTIF_ID = 1002;

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESC);
            channel.enableVibration(true);

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static void showExpenseAddedNotification(Context context, String title, double amount, String category) {
        createNotificationChannel(context);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
        );

        String contentText = String.format("Recorded ₹%,.2f under %s", amount, category);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_receipt)
                .setContentTitle("Expense Added: " + title)
                .setContentText(contentText)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Expense of " + DateTimeUtils.formatCurrency(amount) + " for '" + title + "' (" + category + ") was successfully saved."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            try {
                manager.notify(EXPENSE_NOTIF_ID, builder.build());
            } catch (SecurityException ignored) {
                // Post notification permission might be pending on Android 13+
            }
        }
    }

    public static void showBudgetWarningNotification(Context context, int percentageUsed, double remaining) {
        createNotificationChannel(context);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                1,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
        );

        String title = percentageUsed >= 100 ? "⚠️ Budget Exceeded!" : "⚠️ Budget Limit Alert!";
        String message = percentageUsed >= 100
                ? "You have exceeded 100% of your monthly budget! Please control extra spending."
                : "You have spent " + percentageUsed + "% of your monthly budget. Remaining: " + DateTimeUtils.formatCurrency(remaining);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_warning)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            try {
                manager.notify(BUDGET_NOTIF_ID, builder.build());
            } catch (SecurityException ignored) {
            }
        }
    }
}
