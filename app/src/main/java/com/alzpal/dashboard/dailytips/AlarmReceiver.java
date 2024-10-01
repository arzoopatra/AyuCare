package com.alzpal.dashboard.dailytips;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    // This method is called when the BroadcastReceiver receives an Intent broadcast.
    @Override
    public void onReceive(Context context, Intent intent) {

        // Schedule the daily tip notification when the alarm is triggered
        scheduleDailyTipNotification(context);

        // Log a message indicating that the daily tip notification has been scheduled
        Log.d("AlarmReceiver", "Daily Tip Notification Scheduled.");
    }

    // Method to schedule the daily tip notification
    private void scheduleDailyTipNotification(Context context) {
        // Create an intent that will be used to trigger the DailyTipNotificationReceiver
        Intent notificationIntent = new Intent(context, DailyTipNotificationReceiver.class);

        // Create a PendingIntent that will be broadcast when the alarm goes off
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0, // Request code (can be used to distinguish between different PendingIntents)
                notificationIntent, // The intent to broadcast
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT // Flags to ensure the PendingIntent is immutable and updates if it already exists
        );

        // Get the AlarmManager system service to set up the repeating alarm
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Calculate the time at which the alarm should first trigger (1 day from now)
        long triggerAtMillis = System.currentTimeMillis() + AlarmManager.INTERVAL_DAY;

        // Set up a repeating alarm that will trigger the PendingIntent at the same time each day
        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP, // Wake up the device if necessary when the alarm triggers
                triggerAtMillis, // The time at which the alarm should first go off
                AlarmManager.INTERVAL_DAY, // The interval at which the alarm should repeat (1 day)
                pendingIntent // The PendingIntent that should be triggered when the alarm goes off
        );
    }
}
