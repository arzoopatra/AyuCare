package com.alzpal.dashboardactivity.memorygames.avatars;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alzpal.dashboardactivity.memorygames.MemoryGamesActivity;
import com.alzpal.R;

import java.util.ArrayList;
import java.util.List;

public class AvatarSelectionActivity extends AppCompatActivity {

    private EditText avatarNameEditText;
    private GridView avatarGridView;
    private Button registerButton;
    private AvatarAdapter avatarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the user is already registered
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isRegistered = prefs.getBoolean("isRegistered", false);
        if (isRegistered) {
            // If already registered, skip the registration and go to the next activity
            startMemoryGamesActivity();
            return;
        }

        setContentView(R.layout.activity_avatar_selection);

        avatarNameEditText = findViewById(R.id.avatar_name_edit_text);
        avatarGridView = findViewById(R.id.avatar_grid_view);
        registerButton = findViewById(R.id.register_button);

        // Initialize GridView with avatars
        avatarAdapter = new AvatarAdapter(this, getAvatarList());
        avatarGridView.setAdapter(avatarAdapter);

        // Set OnClickListener for registration button
        registerButton.setOnClickListener(v -> registerUser(prefs));
    }

    // Method to handle user registration
    private void registerUser(SharedPreferences prefs) {
        String name = avatarNameEditText.getText().toString().trim();
        int selectedPosition = avatarAdapter.getSelectedPosition();

        if (selectedPosition != -1 && !name.isEmpty()) {
            // Save avatar name and ID to SharedPreferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("avatarName", name);
            editor.putInt("avatarId", getAvatarIdFromPosition(selectedPosition));
            editor.putBoolean("isRegistered", true); // Mark user as registered
            editor.apply();

            // Start MemoryGamesActivity
            startMemoryGamesActivity();
        } else {
            // Show a message if no avatar is selected or name is empty
            Toast.makeText(this, "Please Select an Avatar and Enter your Name", Toast.LENGTH_SHORT).show();
        }
    }

    // Navigate to MemoryGamesActivity
    private void startMemoryGamesActivity() {
        Intent intent = new Intent(AvatarSelectionActivity.this, MemoryGamesActivity.class);
        startActivity(intent);
        finish();
    }

    // Method to get the avatar list
    private List<Avatar> getAvatarList() {
        List<Avatar> avatars = new ArrayList<>();
        avatars.add(new Avatar(R.drawable.avatar1));
        avatars.add(new Avatar(R.drawable.avatar2));
        avatars.add(new Avatar(R.drawable.avatar3));
        avatars.add(new Avatar(R.drawable.avatar4));
        avatars.add(new Avatar(R.drawable.avatar5));
        avatars.add(new Avatar(R.drawable.avatar6));
        avatars.add(new Avatar(R.drawable.avatar7));
        avatars.add(new Avatar(R.drawable.avatar8));
        return avatars;
    }

    // Method to map avatar position to its resource ID
    private int getAvatarIdFromPosition(int position) {
        switch (position) {
            case 0: return R.drawable.avatar1;
            case 1: return R.drawable.avatar2;
            case 2: return R.drawable.avatar3;
            case 3: return R.drawable.avatar4;
            case 4: return R.drawable.avatar5;
            case 5: return R.drawable.avatar6;
            case 6: return R.drawable.avatar7;
            case 7: return R.drawable.avatar8;
            default: return -1;
        }
    }
}
