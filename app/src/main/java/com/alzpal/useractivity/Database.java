package com.alzpal.useractivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AlzPal.db"; // Database name
    private static final int DATABASE_VERSION = 2; // Incremented version for schema changes

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a 'users' table with an additional 'Gender' column
        String createTableQuery = "CREATE TABLE users (" +
                "Username TEXT PRIMARY KEY, " +
                "EmailId TEXT, " +
                "Password TEXT, " +
                "Gender TEXT)"; // Added Gender column
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            if (oldVersion < 2) {
                db.execSQL("ALTER TABLE users ADD COLUMN Gender TEXT"); // Add Gender column in version 2
            }
        }
    }

    // Register a new user with gender
    public void register(String Username, String EmailId, String Password, String Gender) {
        ContentValues cv = new ContentValues();
        cv.put("Username", Username);
        cv.put("EmailId", EmailId);
        cv.put("Password", Password);
        cv.put("Gender", Gender); // Save gender

        try (SQLiteDatabase db = getWritableDatabase()) {
            db.insert("users", null, cv);
        }
    }

    public int login(String Username, String Password) {
        int result = 0;
        String[] args = {Username, Password};

        try (SQLiteDatabase db = getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM users WHERE Username=? AND Password=?", args)) {
            if (cursor.moveToFirst()) {
                result = 1;
            }
        }
        return result;
    }

    public Cursor getUserDetails(String Username) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM users WHERE Username = ?";
        return db.rawQuery(query, new String[]{Username});
    }

    // Method to update user details
    public void updateUserDetails(String Username, String EmailId, String Password, String Gender) {
        ContentValues cv = new ContentValues();
        cv.put("EmailId", EmailId);
        cv.put("Password", Password);
        cv.put("Gender", Gender);

        try (SQLiteDatabase db = getWritableDatabase()) {
            db.update("users", cv, "Username = ?", new String[]{Username});
        }
    }
}
