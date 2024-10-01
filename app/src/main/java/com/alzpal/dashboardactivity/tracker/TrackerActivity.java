package com.alzpal.dashboardactivity.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.alzpal.R;
import com.alzpal.dashboard.HomeActivity;

public class TrackerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        // Initialize CardView and ImageView elements
        CardView shareLocationCard = findViewById(R.id.reminder_card); // Ensure this ID matches the layout
        CardView trackLocationCard = findViewById(R.id.tracker_card); // Ensure this ID matches the layout
        CardView savedPatientsLocationCard = findViewById(R.id.SavedPatientsLocation); // Ensure this ID matches the layout
        Button backButton = findViewById(R.id.back); // Ensure this ID matches the layout

        // Set onClickListener for the "Share Location" card
        shareLocationCard.setOnClickListener(v -> shareLocation());

        // Set onClickListener for the "Track Location" card
        trackLocationCard.setOnClickListener(v -> {
            // Start the TrackLocationActivity
            Intent intent = new Intent(TrackerActivity.this, TrackLocationActivity.class);
            startActivity(intent);
        });

        // Set onClickListener for the "Saved Patients Location" card
        savedPatientsLocationCard.setOnClickListener(v -> {
            // Start the SavedLocationActivity
            Intent intent = new Intent(TrackerActivity.this, SavedLocationActivity.class);
            startActivity(intent);
        });

        // Set onClickListener for the back button
        backButton.setOnClickListener(v -> onBackPressed()); // Go back to the previous screen
    }

    private void shareLocation() {
        // Create an Intent to share the current location
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "I'm at: [Your Location URL]"); // Replace with actual location URL
        sendIntent.setType("text/plain");

        // Create and start the share Intent
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Navigate back to HomeActivity
        Intent intent = new Intent(TrackerActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Finish the current activity to remove it from the back stack
    }

    public void onBackButtonClick(View view) {
    }
}
