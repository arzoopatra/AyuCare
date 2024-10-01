package com.alzpal.dashboardactivity.reminder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.NonNull;

public class MedicalDB extends SQLiteOpenHelper {
    public static MedicalDB sInstance;
    public static synchronized MedicalDB getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MedicalDB(context.getApplicationContext());
        }
        return sInstance;
    }

    public static final int version = 1;
    public MedicalDB(Context context) {
        super(context,"database",null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_user_table = "CREATE TABLE USER (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "USER_NAME TEXT NOT NULL);";
        String create_med_table = "CREATE TABLE MEDICINE (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "MED_NAME TEXT NOT NULL," +
                "QTY INTEGER NOT NULL," +
                "DATE_TIME TEXT NOT NULL," +
                "DAYS TEXT NOT NULL," +
                "USER_ID INT NOT NULL," +
                "ENABLE INT NOT NULL," +
                "FOREIGN KEY(USER_ID) REFERENCES USER(_id)" +
                "ON DELETE CASCADE);";
        db.execSQL(create_user_table);
        db.execSQL(create_med_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade if needed
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    //    USER CRUD OPERATIONS:
    public void addUser(SQLiteDatabase sqLiteDatabase, @NonNull String name) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("USER_NAME", name);
        db.insert("USER", null, values);
    }

    public void deleteUser(SQLiteDatabase writableDatabase, @NonNull String user_id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("USER", "_id=?", new String[]{user_id});
    }

    public void updateUser(SQLiteDatabase writableDatabase, @NonNull int user_id, @NonNull String name) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("USER_NAME", name);
        db.update("USER", values, "_id=?", new String[]{"" + user_id});
    }

    public String getUserName(SQLiteDatabase writableDatabase, @NonNull int user_id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor user = db.rawQuery("SELECT * FROM USER WHERE _id=?", new String[]{String.valueOf(user_id)});
        String userName = "";
        if (user.moveToFirst()) {
            userName = user.getString(1);
        }
        user.close();
        return userName;
    }

    //    MEDICINE CRUD OPERATION:
    public void addMedicine(SQLiteDatabase writableDatabase, @NonNull int user_id, @NonNull String med_name, @NonNull int quantity, @NonNull String date_time, @NonNull String days) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("USER_ID", user_id);
        values.put("MED_NAME", med_name);
        values.put("QTY", quantity);
        values.put("DATE_TIME", date_time);
        values.put("DAYS", days);
        values.put("ENABLE", false);
        db.insert("MEDICINE", null, values);
    }

    public void deleteMedicine(SQLiteDatabase writableDatabase, @NonNull int med_id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("MEDICINE", "_id=?", new String[]{"" + med_id});
    }

    public void updateMedicine(@NonNull int med_id, @NonNull String med_name, @NonNull int quantity, @NonNull String date_time, @NonNull String days) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MED_NAME", med_name);
        values.put("QTY", quantity);
        values.put("DATE_TIME", date_time);
        values.put("DAYS", days);
        db.update("MEDICINE", values, "_id=?", new String[]{"" + med_id});
    }

    public void setEnable(SQLiteDatabase writableDatabase, int id, int b) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ENABLE", b);
        db.update("MEDICINE", values, "_id=?", new String[]{"" + id});
    }

    public Cursor getUserList(SQLiteDatabase writableDatabase) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT _id, USER_NAME FROM USER", null);
    }

    public Cursor getMedicineListById(SQLiteDatabase writableDatabase, int user_id) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM MEDICINE WHERE USER_ID=?", new String[]{String.valueOf(user_id)});
    }

    public Cursor getMedicine(SQLiteDatabase writableDatabase, int med_id) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM MEDICINE WHERE _id=?", new String[]{String.valueOf(med_id)});
    }
}
