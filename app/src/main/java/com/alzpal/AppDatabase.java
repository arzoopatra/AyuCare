package com.alzpal;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.alzpal.dashboard.notification.Notification;
import com.alzpal.dashboard.notification.NotificationDao;
import com.alzpal.dashboardactivity.tracker.UserDetails;
import com.alzpal.dashboardactivity.tracker.UserDetailsDao;

// Annotate the class with @Database to denote that this is a Room Database
// Specify the entities (tables) and the version number
@Database(entities = {UserDetails.class, Notification.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Abstract methods to get DAOs (Data Access Objects) for accessing the database
    public abstract UserDetailsDao userDetailsDao();
    public abstract NotificationDao notificationDao(); // Data access object for Notification

    // Singleton instance of the database
    private static volatile AppDatabase instance;

    // Method to get the singleton instance of the database
    public static synchronized AppDatabase getDatabase(Context context) {
        if (instance == null) {
            // Create the database instance if it does not exist
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database") // Name of the database
                    .fallbackToDestructiveMigration() // Automatically handle migrations by recreating the database
                    .build();
        }
        return instance;
    }
}


