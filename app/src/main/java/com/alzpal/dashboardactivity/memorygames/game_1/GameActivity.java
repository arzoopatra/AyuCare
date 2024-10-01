package com.alzpal.dashboardactivity.memorygames.game_1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.GridView;
import android.widget.TextView;

import com.alzpal.dashboardactivity.memorygames.GameSplashActivity;
import com.alzpal.R;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    private GridView gridView;
    private List<Card> cards;
    private CardAdapter cardAdapter;
    private Card firstCard, secondCard;
    private boolean canClick = true;
    private final Handler handler = new Handler();
    private TextView timerTextView;
    private int countdownTime = 60; // Countdown starting time in seconds
    private Runnable timerRunnable;
    private boolean isTimerStopped = false; // Flag to track if the timer is already stopped
    private boolean hasWon = false; // Flag to track if the user has won the game

    private MediaPlayer cardClickSound;
    private MediaPlayer timerWarningSound;
    private MediaPlayer winSound;
    private MediaPlayer loseSound;
    private MediaPlayer matchSound; // MediaPlayer for card match sound
    private MediaPlayer backgroundMusic; // MediaPlayer for background music

    private int coins = 0; // Track coins

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gridView = findViewById(R.id.grid_layout);
        timerTextView = findViewById(R.id.timer_text_view);

        // Initialize MediaPlayer for sounds
        cardClickSound = MediaPlayer.create(this, R.raw.card_click);
        timerWarningSound = MediaPlayer.create(this, R.raw.timer_warning);
        winSound = MediaPlayer.create(this, R.raw.win);
        loseSound = MediaPlayer.create(this, R.raw.lose);
        matchSound = MediaPlayer.create(this, R.raw.cards_match); // Initialize match sound
        backgroundMusic = MediaPlayer.create(this, R.raw.game1_sound); // Initialize background music

        // Start playing background music
        startBackgroundMusic();

        initializeCards();
        startTimer();

        // Load coins from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        coins = sharedPreferences.getInt("coins", 0); // Load current coins
    }

    private void startBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.setLooping(true); // Loop the background music
            backgroundMusic.start();
        }
    }

    private void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.pause();
            backgroundMusic.seekTo(0);
        }
    }

    private void initializeCards() {
        cards = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            cards.add(new Card(i));
            cards.add(new Card(i));
        }
        Collections.shuffle(cards);

        cardAdapter = new CardAdapter(this, cards, new CardAdapter.CardClickListener() {
            @Override
            public void onCardClicked(Card card) {
                if (!canClick || card.isFlipped() || card.isMatched() || countdownTime <= 0) {
                    return;
                }

                card.flip();
                cardClickSound.start(); // Play sound on card click

                if (firstCard == null) {
                    firstCard = card;
                } else {
                    secondCard = card;
                    canClick = false;
                    handler.postDelayed(() -> checkForMatch(), 1000);
                }
                cardAdapter.notifyDataSetChanged();
            }
        });
        gridView.setAdapter(cardAdapter);
    }

    private void checkForMatch() {
        if (firstCard.getId() == secondCard.getId()) {
            firstCard.setMatched(true);
            secondCard.setMatched(true);
            firstCard.setVisible(false);
            secondCard.setVisible(false);
            cardAdapter.notifyDataSetChanged();

            // Play match sound when cards match
            matchSound.start();

            // Check if all cards are hidden
            if (allCardsHidden()) {
                showWinMessage(); // Show win message if all cards are hidden
                return;
            }
        } else {
            firstCard.flip();
            secondCard.flip();
        }
        firstCard = null;
        secondCard = null;
        canClick = true;
    }

    private boolean allCardsHidden() {
        for (Card card : cards) {
            if (card.isVisible()) {
                return false; // Return false if any card is still visible
            }
        }
        return true; // Return true if all cards are hidden
    }

    private void startTimer() {
        // Ensure timerRunnable is not null
        if (timerRunnable != null) {
            handler.removeCallbacks(timerRunnable);
        }
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (countdownTime > 0) {
                    countdownTime--;
                    int minutes = countdownTime / 60;
                    int seconds = countdownTime % 60;
                    timerTextView.setText(String.format("%02d:%02d", minutes, seconds));

                    // Play timer warning sound when countdown reaches 10 seconds
                    if (countdownTime == 10) {
                        timerWarningSound.start();
                    }

                    handler.postDelayed(this, 1000); // Continue countdown
                } else {
                    stopTimer(); // Stop the timer when countdown reaches 0
                }
            }
        };
        handler.post(timerRunnable); // Start the timer
    }

    private void stopTimer() {
        if (isTimerStopped) return; // Prevent multiple calls to stopTimer
        isTimerStopped = true; // Mark the timer as stopped

        handler.removeCallbacks(timerRunnable); // Stop the timer
        timerTextView.setText("Game Over!");

        // Show game over message if the game was not won
        if (!allCardsHidden()) {
            loseSound.start(); // Play lose sound
            updateLosses(); // Update losses in SharedPreferences
            showSplashScreen("All the Best for Next Time!", false); // Show losing message
        } else {
            showWinMessage(); // Call showWinMessage only if the game was won
        }

        // Stop background music
        stopBackgroundMusic();
    }

    private void showWinMessage() {
        if (!isTimerStopped) { // Ensure the timer is stopped before showing the win message
            stopTimer();
        }

        winSound.start(); // Play the win sound
        hasWon = true; // Mark the game as won
        incrementCoins(); // Increment coins only when the game is won
        updateWins(); // Update wins in SharedPreferences
        showSplashScreen("Congratulations! \nYou did a Wonderful Job!", true); // Show win message
    }

    private void incrementCoins() {
        if (hasWon) {
            // Get the number of wins from SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
            int currentWins = sharedPreferences.getInt("wins", 0);

            // Calculate the coin reward (2, 4, 6, ...)
            int rewardCoins = 2 * (currentWins + 1);

            // Update coins in SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            int currentCoins = sharedPreferences.getInt("coins", 0);
            editor.putInt("coins", currentCoins + rewardCoins);
            editor.apply();
        }
    }

    private void updateWins() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int wins = sharedPreferences.getInt("wins", 0);
        editor.putInt("wins", wins + 1); // Increment wins in SharedPreferences
        editor.apply();
    }

    private void updateLosses() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int losses = sharedPreferences.getInt("losses", 0);
        editor.putInt("losses", losses + 1); // Increment losses in SharedPreferences
        editor.apply();
    }

    private void showSplashScreen(String message, boolean isWin) {
        Intent intent = new Intent(GameActivity.this, GameSplashActivity.class);
        intent.putExtra("message", message);
        intent.putExtra("isWin", isWin);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopBackgroundMusic(); // Pause background music when the activity is paused
    }

    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundMusic(); // Resume background music when the activity is resumed
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cardClickSound != null) cardClickSound.release();
        if (timerWarningSound != null) timerWarningSound.release();
        if (winSound != null) winSound.release();
        if (loseSound != null) loseSound.release();
        if (matchSound != null) matchSound.release();
        if (backgroundMusic != null) backgroundMusic.release();
    }
}
