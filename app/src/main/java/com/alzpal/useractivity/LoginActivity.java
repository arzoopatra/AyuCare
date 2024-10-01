package com.alzpal.useractivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alzpal.dashboard.HomeActivity;
import com.alzpal.R;

public class LoginActivity extends AppCompatActivity {

    // Declare variables for UI elements
    EditText edUsername, edPassword;
    Button btnLogin;
    TextView tvNewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        edUsername = findViewById(R.id.editTextLoginUsername);
        edPassword = findViewById(R.id.editTextLoginPassword);
        btnLogin = findViewById(R.id.buttonLogin);
        tvNewUser = findViewById(R.id.textViewNewUser);

        // Set click listener for the login button
        btnLogin.setOnClickListener(view -> {
            String username = edUsername.getText().toString().trim(); // Retrieve and trim username
            String password = edPassword.getText().toString().trim(); // Retrieve and trim password

            // Check if username or password fields are empty
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
            } else {
                // Create an instance of the Database helper
                Database db = new Database(this);

                // Check if the username and password are valid
                if (db.login(username, password) == 1) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

                    // Save the login state and username in SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Username", username); // Save the username
                    editor.putBoolean("isLoggedIn", true); // Set login state to true
                    editor.apply();

                    // Redirect to HomeActivity
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish(); // Close LoginActivity
                } else {
                    Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set click listener for new user text view
        tvNewUser.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
