package com.alzpal.dashboardactivity.memorygames;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.alzpal.dashboard.HomeActivity;
import com.alzpal.dashboardactivity.memorygames.game_2.ImageGamesActivity;
import com.alzpal.R;
import com.alzpal.dashboardactivity.memorygames.game_1.GameActivity;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import timber.log.Timber;

public class MemoryGamesActivity extends AppCompatActivity {

    private ImageView avatarImageView;
    private TextView coinsTextView;
    private PieChart pieChart;
    private TextView winsTextView;
    private TextView lossesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_games);

        // Initialize views
        avatarImageView = findViewById(R.id.avatar_image_view);
        coinsTextView = findViewById(R.id.coin_text_view);
        pieChart = findViewById(R.id.pie_chart);
        winsTextView = findViewById(R.id.wins_text_view);
        lossesTextView = findViewById(R.id.losses_text_view);

        // Load saved avatar and display it
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        int savedAvatarId = prefs.getInt("avatarId", R.drawable.others); // Default to placeholder
        avatarImageView.setImageResource(savedAvatarId);

        // Update the coins TextView
        updateCoinsDisplay();

        // Initialize CardViews
        CardView cardsGameCard = findViewById(R.id.cards_game);
        CardView imageGamesCard = findViewById(R.id.image_games);

        // Set click listeners for cards
        cardsGameCard.setOnClickListener(v -> openCardsGameActivity());
        imageGamesCard.setOnClickListener(v -> openImageGamesActivity());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Post a delayed task to update the PieChart to ensure data is available
        new Handler().postDelayed(this::updatePieChart, 200);
    }

    private void updateCoinsDisplay() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        int coins = prefs.getInt("coins", 0); // Retrieve coins
        coinsTextView.setText("Coins: " + coins); // Update TextView
    }

    private void openCardsGameActivity() {
        // Start Cards Game Activity
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    private void openImageGamesActivity() {
        // Start Image Games Activity
        Intent intent = new Intent(this, ImageGamesActivity.class);
        startActivity(intent);
    }

    private void updatePieChart() {
        // Retrieve stored results
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        int wins = prefs.getInt("wins", 0);
        int losses = prefs.getInt("losses", 0);

        // Log the retrieved values
        Timber.d("Wins: %d, Losses: %d", wins, losses);

        // Clear previous data
        pieChart.clearChart();

        // Add data to PieChart
        pieChart.addPieSlice(new PieModel("Wins", wins, Color.parseColor("#45A834")));
        pieChart.addPieSlice(new PieModel("Losses", losses, Color.parseColor("#FF0000")));

        pieChart.startAnimation();

        // Update the TextViews for wins and losses
        winsTextView.setText("Wins : " + wins);
        lossesTextView.setText("Loss : " + losses);
    }

    public void onBackButtonClick(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear the back stack

        // Create activity options for custom transitions
        ActivityOptions options = ActivityOptions.makeCustomAnimation(
                this, // Context
                R.anim.slide_in_right, // Enter animation
                R.anim.slide_out_left // Exit animation
        );

        // Start HomeActivity with the custom animation options
        startActivity(intent, options.toBundle());

        // Finish the current activity
        finish();
    }
}
