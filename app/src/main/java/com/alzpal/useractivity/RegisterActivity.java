package com.alzpal.useractivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.alzpal.R;

public class RegisterActivity extends AppCompatActivity {

    // Declare variables for UI elements
    EditText edUsername, edEmailId, edPassword, edConfirm;
    RadioGroup genderRadioGroup;
    Button btn;
    TextView TV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge layout (if supported)
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Initialize UI elements
        edUsername = findViewById(R.id.editTextRegUsername);
        edEmailId = findViewById(R.id.editTextRegEmailId);
        edPassword = findViewById(R.id.editTextRegPassword);
        edConfirm = findViewById(R.id.editTextRegConfirmPassword);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        btn = findViewById(R.id.buttonSignUp);
        TV = findViewById(R.id.textViewExistingUser);

        // Set click listener for the "Existing User" text view
        TV.setOnClickListener(view -> {
            // Start LoginActivity when "Existing User" is clicked
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });

        // Set click listener for the Sign Up button
        btn.setOnClickListener(v -> {
            // Retrieve text entered by the user
            String Username = edUsername.getText().toString();
            String Password = edPassword.getText().toString();
            String Confirm = edConfirm.getText().toString();
            String EmailId = edEmailId.getText().toString();
            int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();

            // Ensure a gender is selected
            if (selectedGenderId == -1) {
                Toast.makeText(getApplicationContext(), "Please Select a Gender", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get selected gender text
            RadioButton selectedGenderButton = findViewById(selectedGenderId);
            String Gender = selectedGenderButton.getText().toString();

            // Create an instance of the Database helper
            Database db = new Database(getApplicationContext());

            // Validate input fields
            if (Username.isEmpty() || EmailId.isEmpty() || Password.isEmpty() || Confirm.isEmpty()) {
                // Show a message if any field is empty
                Toast.makeText(getApplicationContext(), "Please Fill all Details", Toast.LENGTH_SHORT).show();
            } else {
                // Check if the password and confirm password fields match
                if (Password.equals(Confirm)) {
                    // Check if the password meets complexity requirements
                    if (isValid(Password)) {
                        // Register the user in the database
                        db.register(Username, EmailId, Password, Gender); // Updated to include gender
                        Toast.makeText(getApplicationContext(), "Record Inserted", Toast.LENGTH_SHORT).show();

                        // Save gender and profile image URL/resource ID
                        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("gender", Gender);
                        editor.apply();

                        // Start LoginActivity after successful registration
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        // Show a message if the password does not meet the complexity requirements
                        Toast.makeText(getApplicationContext(), "Password must contain at least 8 characters, having Letter, Digit, and Special Symbol", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Show a message if the passwords do not match
                    Toast.makeText(getApplicationContext(), "Password and Confirm Password did not Match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to validate password complexity
    public static boolean isValid(String password) {
        int hasLetter = 0, hasDigit = 0, hasSpecial = 0;

        // Check if the password length is less than 8 characters
        if (password.length() < 8) {
            return false;
        } else {
            // Check if the password contains at least one letter
            for (int i = 0; i < password.length(); i++) {
                if (Character.isLetter(password.charAt(i))) {
                    hasLetter = 1;
                }
            }

            // Check if the password contains at least one digit
            for (int i = 0; i < password.length(); i++) {
                if (Character.isDigit(password.charAt(i))) {
                    hasDigit = 1;
                }
            }

            // Check if the password contains at least one special character
            for (int i = 0; i < password.length(); i++) {
                char c = password.charAt(i);
                if ((c >= 33 && c <= 46) || c == 64) {
                    hasSpecial = 1;
                }
            }

            // Return true if all conditions are met
            return hasLetter == 1 && hasDigit == 1 && hasSpecial == 1;
        }
    }
}
