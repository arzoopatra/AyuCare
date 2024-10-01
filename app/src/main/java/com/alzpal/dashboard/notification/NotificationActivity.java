package com.alzpal.dashboard.notification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alzpal.AppDatabase;
import com.alzpal.dashboard.HomeActivity;
import com.alzpal.R;

import java.util.List;

/**
 * This activity displays the list of notifications stored in the database.
 * It uses a RecyclerView to show the notifications, and if there are no notifications,
 * it shows a message indicating that no notifications are available.
 */
public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView; // RecyclerView for displaying the list of notifications
    private NotificationAdapter adapter; // Adapter to bind the notifications data to the RecyclerView
    private AppDatabase db; // Reference to the AppDatabase for accessing the notifications
    private List<Notification> notificationList; // List to hold the notifications fetched from the database
    private TextView noNotificationsTextView; // TextView to display a message when there are no notifications

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Initialize the RecyclerView and TextView from the layout
        recyclerView = findViewById(R.id.recyclerViewNotifications);
        noNotificationsTextView = findViewById(R.id.noNotificationsTextView);

        // Set the layout manager for the RecyclerView to a LinearLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get an instance of the database
        db = AppDatabase.getDatabase(this);

        // Load the notifications from the database
        loadNotifications();
    }

    /**
     * Loads the notifications from the database in a background thread
     * and updates the UI on the main thread.
     */
    private void loadNotifications() {
        new Thread(() -> {
            // Fetch all notifications from the database
            notificationList = db.notificationDao().getAllNotifications();

            // Update the UI on the main thread
            runOnUiThread(() -> {
                if (notificationList.isEmpty()) {
                    // If the list is empty, hide the RecyclerView and show the "no notifications" message
                    recyclerView.setVisibility(View.GONE);
                    noNotificationsTextView.setVisibility(View.VISIBLE);
                } else {
                    // If there are notifications, display them in the RecyclerView
                    recyclerView.setVisibility(View.VISIBLE);
                    noNotificationsTextView.setVisibility(View.GONE);
                    adapter = new NotificationAdapter(this, notificationList);
                    recyclerView.setAdapter(adapter);
                }
            });
        }).start(); // Start the background thread
    }

    /**
     * Called when the back button is clicked.
     * Navigates to the HomeActivity.
     */
    public void onBackButtonClick(View view) {
        Intent intent = new Intent(NotificationActivity.this, HomeActivity.class);
        startActivity(intent);
        finish(); // Optional: Close the NotificationActivity
    }
}
