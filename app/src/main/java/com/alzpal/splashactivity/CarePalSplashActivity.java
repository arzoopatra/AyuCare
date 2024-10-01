package com.alzpal.splashactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.alzpal.dashboardactivity.chatpal.MainActivity;

public class CarePalSplashActivity extends Activity {

    // Handler for managing delayed tasks
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the Handler
        handler = new Handler();

        // Post a delayed task to transition to the next activity
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an Intent to start the MainActivity
                Intent intent = new Intent(CarePalSplashActivity.this, MainActivity.class);
                startActivity(intent); // Start the MainActivity
                finish(); // Close the current activity
            }
        }, 2000); // Delay of 2000 milliseconds (2 seconds)
    }
}
