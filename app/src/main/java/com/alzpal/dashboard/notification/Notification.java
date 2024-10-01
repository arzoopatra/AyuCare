package com.alzpal.dashboard.notification;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * This class represents a Notification entity that will be stored in a Room database.
 * It includes fields for the notification's ID, title, message, timestamp, and type.
 */
@Entity(tableName = "notifications") // Annotation that marks this class as a Room entity, with the table name "notifications"
public class Notification {

    @PrimaryKey(autoGenerate = true) // The primary key for this entity; Room will auto-generate unique IDs
    private int id;

    private String title; // Field to store the title of the notification
    private String message; // Field to store the message/content of the notification
    private long timestamp; // Field to store the timestamp of when the notification was created or received
    private String type; // Field to store the type of notification (e.g., "Alarm", "Daily Tip")

    /**
     * Constructor for the Notification class.
     *
     * @param title     The title of the notification
     * @param message   The message/content of the notification
     * @param timestamp The timestamp of the notification
     * @param type      The type of the notification (e.g., "Alarm", "Daily Tip")
     */
    public Notification(String title, String message, long timestamp, String type) {
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
        this.type = type;
    }

    // Getters and setters for accessing and modifying the fields

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
