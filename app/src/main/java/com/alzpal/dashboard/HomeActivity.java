package com.alzpal.dashboard;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.alzpal.dashboard.dailytips.DailyTipsActivity;
import com.alzpal.dashboard.notification.NotificationActivity;
import com.alzpal.R;
import com.alzpal.dashboardactivity.chatpal.MainActivity;
import com.alzpal.splashactivity.CarePalSplashActivity;
import com.alzpal.splashactivity.MemoryGamesSplashActivity;
import com.alzpal.splashactivity.MusicTherapySplashActivity;
import com.alzpal.splashactivity.ReminderSplashActivity;
import com.alzpal.splashactivity.TrackerSplashActivity;
import com.alzpal.useractivity.LoginActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_VOICE_INPUT = 2;
    private static final int REQUEST_CALL_PERMISSION = 3; // Request code for call permission
    private EditText searchBar;
    private CardView[] cardViews;
    private TextView greetingTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize UI elements
        ImageView profileButton = findViewById(R.id.profile);
        ImageView settingButton = findViewById(R.id.setting);
        ImageView notificationButton = findViewById(R.id.notification);
        ImageView logoutButton = findViewById(R.id.logout);
        ImageView voiceIcon = findViewById(R.id.voice_icon);
        ImageView heartIcon = findViewById(R.id.heart_icon);
        ImageView profileImageView = findViewById(R.id.homeimage);
        greetingTextView = findViewById(R.id.greetingTextView);

        // Initialize card views for different sections
        cardViews = new CardView[]{
                findViewById(R.id.reminder_card),
                findViewById(R.id.tracker_card),
                findViewById(R.id.memorygames_card),
                findViewById(R.id.chatpal_card),
                findViewById(R.id.musictherapy_card),
                findViewById(R.id.caregiver_corner_card)
        };

        // Initialize FloatingActionButton for emergency contact (SOS)
        FloatingActionButton emergencyContactButton = findViewById(R.id.emergency_contact_button);

        // Initialize search bar
        searchBar = findViewById(R.id.search_bar);

        // Check if the user is logged in
        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (!isLoggedIn) {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Load profile image and set greeting message
        loadProfileImage(profileImageView, sharedPreferences);
        setGreetingMessage(sharedPreferences);

        // Set up listeners for buttons
        profileButton.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, ProfileActivity.class)));
        settingButton.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, SettingsActivity.class)));
        notificationButton.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, NotificationActivity.class)));
        logoutButton.setOnClickListener(view -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        });

        cardViews[0].setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, ReminderSplashActivity.class)));
        cardViews[1].setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, TrackerSplashActivity.class)));
        cardViews[2].setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, MemoryGamesSplashActivity.class)));
        cardViews[3].setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, MainActivity.class)));
        cardViews[4].setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, MusicTherapySplashActivity.class)));
        cardViews[5].setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, CarePalSplashActivity.class)));

        voiceIcon.setOnClickListener(v -> startVoiceInput());

        // Update the onClickListener for the heartIcon to navigate to DailyTipsActivity
        heartIcon.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, DailyTipsActivity.class)));

        // Set up search functionality
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterContent(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // Set up SOS functionality for the emergency contact button
        emergencyContactButton.setOnClickListener(v -> makeSOSCall());
    }

    private void makeSOSCall() {
        String emergencyNumber = "112"; // Emergency number for SOS (You can replace with your own)

        // Check if CALL_PHONE permission is granted
        if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Request the permission if not granted
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            // Start the call if permission is granted
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + emergencyNumber));
            startActivity(callIntent);
        }
    }

    private void loadProfileImage(ImageView profileImageView, SharedPreferences sharedPreferences) {
        String gender = sharedPreferences.getString("gender", "others"); // Default to "others" if not found

        int imageResource;
        if ("Male".equalsIgnoreCase(gender)) {
            imageResource = R.drawable.male;
        } else if ("Female".equalsIgnoreCase(gender)) {
            imageResource = R.drawable.female;
        } else {
            imageResource = R.drawable.others; // Fallback image
        }

        Log.d("HomeActivity", "Loading Image Resource: " + imageResource);

        Glide.with(this)
                .load(imageResource)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(profileImageView);
    }

    private void setGreetingMessage(SharedPreferences sharedPreferences) {
        String username = sharedPreferences.getString("Username", "User");
        if (!username.isEmpty()) {
            username = username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();
        }

        // Get current time and set appropriate greeting
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        String greeting;
        if (hourOfDay >= 5 && hourOfDay < 12) {
            greeting = "Good Morning";
        } else if (hourOfDay >= 12 && hourOfDay < 17) {
            greeting = "Good Afternoon";
        } else if (hourOfDay >= 17 && hourOfDay < 21) {
            greeting = "Good Evening";
        } else {
            greeting = "Good Night";
        }

        greetingTextView.setText(greeting + " " + username);
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Now");
        startActivityForResult(intent, REQUEST_CODE_VOICE_INPUT);
    }

    private void filterContent(String query) {
        String lowerCaseQuery = query.toLowerCase(Locale.ROOT);

        for (CardView cardView : cardViews) {
            if (cardView == findViewById(R.id.reminder_card) && "reminder".contains(lowerCaseQuery)) {
                cardView.setVisibility(View.VISIBLE);
            } else if (cardView == findViewById(R.id.tracker_card) && "tracker".contains(lowerCaseQuery)) {
                cardView.setVisibility(View.VISIBLE);
            } else if (cardView == findViewById(R.id.memorygames_card) && "memory games".contains(lowerCaseQuery)) {
                cardView.setVisibility(View.VISIBLE);
            } else if (cardView == findViewById(R.id.chatpal_card) && "chat pal".contains(lowerCaseQuery)) {
                cardView.setVisibility(View.VISIBLE);
            } else if (cardView == findViewById(R.id.musictherapy_card) && "music therapy".contains(lowerCaseQuery)) {
                cardView.setVisibility(View.VISIBLE);
            } else if (cardView == findViewById(R.id.caregiver_corner_card) && "caregiver".contains(lowerCaseQuery)) {
                cardView.setVisibility(View.VISIBLE);
            } else {
                cardView.setVisibility(View.GONE);
            }
        }
    }
}
