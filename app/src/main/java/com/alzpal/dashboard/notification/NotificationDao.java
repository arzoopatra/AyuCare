package com.alzpal.dashboard.notification;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Data Access Object (DAO) for performing database operations on the 'notifications' table.
 */
@Dao
public interface NotificationDao {

    /**
     * Inserts a notification into the database.
     *
     * @param notification The notification to insert
     */
    @Insert
    void insert(Notification notification);

    /**
     * Retrieves all notifications from the database.
     *
     * @return A list of all notifications
     */
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    List<Notification> getAllNotifications();
}
