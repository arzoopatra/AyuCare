package com.alzpal.dashboard.notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alzpal.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying notifications in a RecyclerView.
 * It binds the notification data to the views in each item of the RecyclerView.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private final Context context; // Context for inflating layout resources
    private final List<Notification> notificationList; // List of notifications to display

    /**
     * Constructor for the adapter.
     *
     * @param context The context to use for inflating layouts
     * @param notificationList The list of notifications to display
     */
    public NotificationAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single item in the RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.card_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the notification at the current position
        Notification notification = notificationList.get(position);

        // Bind the notification data to the views
        holder.titleTextView.setText(notification.getTitle());
        holder.messageTextView.setText(notification.getMessage());

        // Format the timestamp to a readable date and time format
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String formattedTimestamp = dateFormat.format(new Date(notification.getTimestamp()));
        holder.timestampTextView.setText(formattedTimestamp);
    }

    @Override
    public int getItemCount() {
        // Return the total number of items in the list
        return notificationList.size();
    }

    /**
     * ViewHolder class for holding the views for each notification item.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView titleTextView; // TextView for the notification title
        public final TextView messageTextView; // TextView for the notification message
        public final TextView timestampTextView; // TextView for the notification timestamp

        /**
         * Constructor for the ViewHolder.
         *
         * @param itemView The view for an individual item in the RecyclerView
         */
        public ViewHolder(View itemView) {
            super(itemView);
            // Initialize the TextViews from the itemView
            titleTextView = itemView.findViewById(R.id.titleTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
        }
    }
}
