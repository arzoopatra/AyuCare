package com.alzpal.dashboard.dailytips;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.alzpal.R;

import com.alzpal.dashboard.HomeActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;

public class DailyTipsActivity extends AppCompatActivity {

    // UI elements for displaying the tip title, content, and image
    private TextView tipTitleTextView;
    private TextView tipContentTextView;
    private ImageView tipImageView;

    // Constants for shared preferences
    private static final String LAST_SHOWN_TIP_INDEX = "last_shown_tip_index";
    private static final String TOTAL_TIPS = "total_tips";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_tips);

        // Initialize UI elements
        tipTitleTextView = findViewById(R.id.tip_title);
        tipContentTextView = findViewById(R.id.tip_content);
        tipImageView = findViewById(R.id.tip_image);
        Button exitButton = findViewById(R.id.back);

        // Load and display the daily tip
        displayDailyTip();

        // Set up the exit button to go back to HomeActivity
        exitButton.setOnClickListener(view -> {
            Intent intent = new Intent(DailyTipsActivity.this, HomeActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
        });

        // Schedule daily notifications
        scheduleDailyTipNotification();
    }

    // Method to load and display the daily tip
    private void displayDailyTip() {
        String jsonTips = loadTipsFromAssets(); // Load JSON data from assets
        List<Tip> tips = parseJsonTips(jsonTips); // Parse JSON data into a list of tips

        if (tips != null && !tips.isEmpty()) {
            SharedPreferences prefs = getSharedPreferences("com.alzpal", MODE_PRIVATE);
            int lastShownIndex = prefs.getInt(LAST_SHOWN_TIP_INDEX, -1);
            int totalTips = prefs.getInt(TOTAL_TIPS, tips.size());

            // If all tips have been shown, reset the index
            if (lastShownIndex >= totalTips - 1) {
                lastShownIndex = -1;
            }

            // Display the next tip
            Tip nextTip = tips.get(lastShownIndex + 1);
            tipTitleTextView.setText(nextTip.getTitle());
            tipContentTextView.setText(nextTip.getContent());

            // Load and display the image
            String imageUrl = nextTip.getImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                if (imageUrl.startsWith("http")) {
                    Glide.with(this).load(imageUrl).into(tipImageView);
                } else {
                    int resourceId = getResources().getIdentifier(imageUrl, "drawable", getPackageName());
                    tipImageView.setImageResource(resourceId);
                }
            }

            // Save the current tip index
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(LAST_SHOWN_TIP_INDEX, lastShownIndex + 1);
            editor.putInt(TOTAL_TIPS, tips.size());
            editor.apply();
        }
    }

    // Method to load the tips JSON file from the assets folder
    private String loadTipsFromAssets() {
        try (InputStream inputStream = getAssets().open("daily_tips.json");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            return jsonString.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to parse the JSON data into a list of Tip objects
    private List<Tip> parseJsonTips(String jsonTips) {
        if (jsonTips == null || jsonTips.isEmpty()) {
            return null;
        }
        Gson gson = new Gson();
        Type tipsContainerType = new TypeToken<TipsContainer>() {}.getType();
        TipsContainer tipsContainer = gson.fromJson(jsonTips, tipsContainerType);
        return tipsContainer != null ? tipsContainer.getTips() : null;
    }

    // Method to schedule the daily notification for the tip
    private void scheduleDailyTipNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, DailyTipNotificationReceiver.class);

        // Create a PendingIntent to trigger the notification
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Set the notification time to 9:00 AM
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // If the set time is before the current time, schedule for the next day
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Schedule the repeating alarm for daily notifications
        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );
    }

    public void onBackButtonClick(View view) {
    }

    // Inner class representing a single tip
    private static class Tip {
        private String title;
        private String content;
        private String imageUrl;

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }

    // Inner class representing the container for multiple tips
    private static class TipsContainer {
        private List<Tip> tips;

        public List<Tip> getTips() {
            return tips;
        }
    }
}
