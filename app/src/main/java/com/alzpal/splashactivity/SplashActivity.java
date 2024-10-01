package com.alzpal.splashactivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.alzpal.R;
import com.alzpal.useractivity.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    // Duration for which the splash screen is displayed (2 seconds)
    private static final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // Set the layout for this activity

        // Create a new Handler to manage delayed tasks
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an Intent to start LoginActivity
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent); // Start LoginActivity
                finish(); // Close SplashActivity so the user cannot return to it
            }
        }, SPLASH_DISPLAY_LENGTH); // Delay of 2000 milliseconds (2 seconds)
    }
}
