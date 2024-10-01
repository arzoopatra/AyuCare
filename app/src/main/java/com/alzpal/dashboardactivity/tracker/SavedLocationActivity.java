package com.alzpal.dashboardactivity.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alzpal.AppDatabase;
import com.alzpal.R;

import java.util.List;

public class SavedLocationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SavedPatientsAdapter adapter;
    private AppDatabase db;
    private List<UserDetails> patientsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_location);

        recyclerView = findViewById(R.id.recyclerViewSavedPatients);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = AppDatabase.getDatabase(this);

        // Load saved patients and update UI
        loadSavedPatients();
    }

    private void loadSavedPatients() {
        new Thread(() -> {
            patientsList = db.userDetailsDao().getAll();
            runOnUiThread(() -> {
                if (patientsList.isEmpty()) {
                    // Show a placeholder message if no patients are saved
                    TextView noSavedPatientsTextView = findViewById(R.id.no_saved_patients_text_view);
                    noSavedPatientsTextView.setText("No Saved Patients Locations Found");
                } else {
                    // Initialize the adapter and set it to RecyclerView
                    adapter = new SavedPatientsAdapter(this, patientsList, db.userDetailsDao());
                    recyclerView.setAdapter(adapter);
                }
            });
        }).start();
    }

    /**
     * Called when the back button is clicked.
     * Navigates to the TrackerActivity.
     */
    public void onBackButtonClick(android.view.View view) {
        Intent intent = new Intent(SavedLocationActivity.this, TrackerActivity.class);
        startActivity(intent);
        finish();
    }
}
