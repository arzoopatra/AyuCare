package com.alzpal.dashboard.dailytips;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.alzpal.R;

public class DailyTipNotificationReceiver extends BroadcastReceiver {

    // Unique ID for the notification channel
    private static final String CHANNEL_ID = "daily_tip_channel";

    // This method is called when the BroadcastReceiver receives an Intent broadcast (from the alarm)
    @Override
    public void onReceive(Context context, Intent intent) {
        // Create a notification channel (required for Android 8.0 and higher)
        createNotificationChannel(context);

        // Create an intent to launch the DailyTipsActivity when the notification is tapped
        Intent notificationIntent = new Intent(context, DailyTipsActivity.class);

        // Create a PendingIntent that will be triggered when the notification is clicked
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0, // Request code to differentiate PendingIntents if needed
                notificationIntent, // The intent to launch the DailyTipsActivity
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT // Flags to ensure the PendingIntent is immutable and updates if it already exists
        );

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo) // Set the icon for the notification (ensure the drawable exists)
                .setContentTitle("Daily Tip") // Set the title of the notification
                .setContentText("Tap to View Today's Tip") // Set the text content of the notification
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Set the priority to high to make the notification more prominent
                .setContentIntent(pendingIntent) // Set the PendingIntent to be triggered when the notification is tapped
                .setAutoCancel(true); // Automatically remove the notification when it's tapped

        // Get the NotificationManager system service to show the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Show the notification with a unique ID (0 in this case)
        notificationManager.notify(0, builder.build());
    }

    // Method to create a notification channel (required for Android 8.0 and higher)
    private void createNotificationChannel(Context context) {
        // Check if the device is running Android 8.0 (API level 26) or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Define the channel name and description
            CharSequence name = "Daily Tip Channel";
            String description = "Channel for Daily Tip Notifications";

            // Set the importance level of the channel (high priority)
            int importance = NotificationManager.IMPORTANCE_HIGH;

            // Create the NotificationChannel object
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Get the NotificationManager system service and create the channel
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
