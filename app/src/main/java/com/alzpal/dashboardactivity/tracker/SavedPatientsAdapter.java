package com.alzpal.dashboardactivity.tracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alzpal.R;

import java.util.List;

public class SavedPatientsAdapter extends RecyclerView.Adapter<SavedPatientsAdapter.ViewHolder> {
    private final Context context; // Context to start new activities
    private final List<UserDetails> patientsList; // List of saved patient details
    private final UserDetailsDao userDetailsDao; // DAO for accessing user details in the database

    // Constructor to initialize adapter with context, list of patients, and DAO
    public SavedPatientsAdapter(Context context, List<UserDetails> patientsList, UserDetailsDao userDetailsDao) {
        this.context = context;
        this.patientsList = patientsList;
        this.userDetailsDao = userDetailsDao;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.card_saved_patients, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the patient details for the current position
        UserDetails patient = patientsList.get(position);

        // Set the patient's details to the corresponding TextViews
        holder.textViewName.setText(patient.getName());
        holder.textViewAge.setText(String.valueOf(patient.getAge()));
        holder.textViewNumber.setText(patient.getNumber());

        // Set the click listener for the "View Location" button
        holder.buttonViewLocation.setOnClickListener(v -> {
            // Start RealTimeLocationActivity with the patient's number as an extra
            Intent intent = new Intent(context, MapActivity.class);
            intent.putExtra("number", patient.getNumber());
            context.startActivity(intent);
        });

        // Set the click listener for the "Delete" button
        holder.buttonDelete.setOnClickListener(v -> {
            new Thread(() -> {
                // Delete the patient from the database
                userDetailsDao.delete(patient);

                // Remove the patient from the list and notify the adapter
                ((SavedLocationActivity) context).runOnUiThread(() -> {
                    int positionToRemove = patientsList.indexOf(patient);
                    if (positionToRemove != -1) {
                        patientsList.remove(positionToRemove);
                        notifyItemRemoved(positionToRemove);
                        notifyItemRangeChanged(positionToRemove, patientsList.size());
                    }
                });
            }).start(); // Perform deletion in a background thread
        });
    }

    @Override
    public int getItemCount() {
        // Return the number of items in the list
        return patientsList.size();
    }

    // ViewHolder class to hold and bind views for each item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textViewName; // TextView for displaying patient's name
        public final TextView textViewAge; // TextView for displaying patient's age
        public final TextView textViewNumber; // TextView for displaying patient's number
        public final Button buttonViewLocation; // Button to view the patient's location
        public final Button buttonDelete; // Button to delete the patient

        // ViewHolder constructor to bind the views
        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewAge = itemView.findViewById(R.id.textViewAge);
            textViewNumber = itemView.findViewById(R.id.textViewNumber);
            buttonViewLocation = itemView.findViewById(R.id.buttonViewLocation);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
