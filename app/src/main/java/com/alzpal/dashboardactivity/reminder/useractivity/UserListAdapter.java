package com.alzpal.dashboardactivity.reminder.useractivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alzpal.dashboardactivity.reminder.database.MedicalDB;
import com.alzpal.R;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {
    private Cursor user_list;
    public Context context;
    public MedicalDB helper;

    public UserListAdapter(Context context, MedicalDB helper) {
        this.context = context;
        this.helper = helper;
    }

    public void setUserData(Cursor cursor){
        this.user_list = cursor;
        if(user_list != null) {
            user_list.moveToFirst();
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (user_list != null && !user_list.isClosed()) {
            // Move to the correct position in the cursor
            user_list.moveToPosition(position);

            // Set the user name in the TextView
            holder.tv.setText(user_list.getString(1));

            // Save the user ID
            holder.id = user_list.getInt(0);

            // Set the delete button's onClick listener
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Delete the user from the database using the saved user ID
                    helper.deleteUser(helper.getWritableDatabase(), String.valueOf(holder.id));

                    // Refresh the cursor and update the adapter data
                    Cursor updatedUserList = helper.getUserList(helper.getWritableDatabase());
                    setUserData(updatedUserList);

                    // Update the empty view if the user list is empty
                    if (getItemCount() == 0) {
                        PillsActivity.empty_view.setText(R.string.empty_users);
                    }
                }
            });

            // Set the TextView's onClick listener to open MedicineActivity
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start the MedicineActivity with the userId as an extra
                    Intent intent = new Intent(context, MedicineActivity.class);
                    intent.putExtra("userId", holder.id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add this line
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return user_list != null ? user_list.getCount() : 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageButton deleteBtn;
        int id;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.user_name);
            deleteBtn = itemView.findViewById(R.id.deleteUser);
        }
    }
}
