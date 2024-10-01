package com.alzpal.dashboardactivity.reminder.useractivity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;

import com.alzpal.dashboardactivity.reminder.notification.NotificationHelper;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String medName = intent.getStringExtra("medName");
        String medQty = intent.getStringExtra("medQty");
        String userName = intent.getStringExtra("userName");

        MediaPlayer player = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        player.start();

        NotificationHelper notificationHelper = new NotificationHelper(context);
        Intent resultIntent = new Intent(context, AlarmActivity.class);
        resultIntent.putExtra("medName", medName);
        resultIntent.putExtra("medQty", medQty);
        resultIntent.putExtra("userName", userName);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context,
                1,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        String message = userName + ", Please take " + medQty + " a Look " + medName + ".";
        notificationHelper.sendNotification("Emergency", message, resultPendingIntent);
    }
}
