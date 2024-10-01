package com.alzpal.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alzpal.R;
import com.alzpal.useractivity.Database;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    TextView usernameTextView, emailTextView, genderTextView;
    ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usernameTextView = findViewById(R.id.editTextLoginUsername);
        emailTextView = findViewById(R.id.emailid);
        genderTextView = findViewById(R.id.gender);
        profileImageView = findViewById(R.id.profileimage);

        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("Username", null);

        if (username == null || username.isEmpty()) {
            Log.e(TAG, "Username is Null or Empty.");
            return;
        }

        Log.d(TAG, "Username Retrieved: " + username);

        Database db = new Database(this);
        Cursor cursor = db.getUserDetails(username);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                // Make sure column names match exactly
                int emailColumnIndex = cursor.getColumnIndex("EmailId");
                int genderColumnIndex = cursor.getColumnIndex("Gender");

                if (emailColumnIndex != -1 && genderColumnIndex != -1) {
                    String email = cursor.getString(emailColumnIndex);
                    String gender = cursor.getString(genderColumnIndex);

                    usernameTextView.setText(username);
                    emailTextView.setText(email);
                    genderTextView.setText(gender);

                    // Set profile image based on gender
                    if (gender.equalsIgnoreCase("Male")) {
                        profileImageView.setImageResource(R.drawable.male);
                    } else if (gender.equalsIgnoreCase("Female")) {
                        profileImageView.setImageResource(R.drawable.female);
                    } else {
                        profileImageView.setImageResource(R.drawable.others);
                    }
                } else {
                    Log.e(TAG, "One or more column indices are invalid.");
                }
            } else {
                Log.e(TAG, "Cursor is Empty.");
            }
            cursor.close();
        } else {
            Log.e(TAG, "Cursor is Null.");
        }
    }

    public void onBackButtonClick(View view) {
        Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
