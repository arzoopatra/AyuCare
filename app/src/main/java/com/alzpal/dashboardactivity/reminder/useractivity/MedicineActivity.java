package com.alzpal.dashboardactivity.reminder.useractivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alzpal.dashboardactivity.reminder.database.MedicalDB;
import com.alzpal.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MedicineActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    public RecyclerView medList;
    public TextView medUserName;
    public MedicineListAdapter medListAdapter;
    public FloatingActionButton medFab;
    public int user_id;
    Button medTime;
    EditText medName, medQty;
    Button isRepeat;
    CheckBox sun, mon, tue, wed, thu, fri, sat;

    public MedicalDB DbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
        DbHelper = MedicalDB.getInstance(getApplicationContext());
        user_id = getIntent().getIntExtra("userId", 0);

        medList = findViewById(R.id.med_list);
        medUserName = findViewById(R.id.med_user_name);
        medFab = findViewById(R.id.med_fab);

        medUserName.setText(DbHelper.getUserName(DbHelper.getWritableDatabase(), user_id));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        medList.setLayoutManager(linearLayoutManager);
        medListAdapter = new MedicineListAdapter(getApplicationContext(), DbHelper, user_id);
        medListAdapter.setUserData(DbHelper.getMedicineListById(DbHelper.getWritableDatabase(), user_id));
        medList.setAdapter(medListAdapter);

        medFab.setOnClickListener(v -> medicineAdder().show());

        medUserName.setOnClickListener(v -> myTextDialog().show());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Apply custom animations for transitions
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private AlertDialog myTextDialog() {
        View layout = View.inflate(this, R.layout.update_user_dialog, null);
        EditText savedText = layout.findViewById(R.id.update_username);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("UPDATE", (dialog, which) -> {
            DbHelper.updateUser(DbHelper.getWritableDatabase(), user_id, savedText.getText().toString());
            medUserName.setText(savedText.getText().toString());
        });
        builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel());
        builder.setView(layout);
        return builder.create();
    }

    private AlertDialog medicineAdder() {
        View layout = View.inflate(this, R.layout.add_med_dialog, null);
        medName = layout.findViewById(R.id.add_med_name);
        medQty = layout.findViewById(R.id.add_med_qty);
        medTime = layout.findViewById(R.id.add_med_time);
        isRepeat = layout.findViewById(R.id.repeat);
        sun = layout.findViewById(R.id.sunday);
        mon = layout.findViewById(R.id.monday);
        tue = layout.findViewById(R.id.tuesday);
        wed = layout.findViewById(R.id.wednesday);
        thu = layout.findViewById(R.id.thursday);
        fri = layout.findViewById(R.id.friday);
        sat = layout.findViewById(R.id.saturday);

        medTime.setOnClickListener(v -> {
            DialogFragment timePicker = new TimePickerFragment();
            timePicker.show(getSupportFragmentManager(), "time picker");
        });

        isRepeat.setOnClickListener(v -> {
            boolean isChecked = v.isPressed();
            if (isChecked) {
                // Check all checkboxes
                setAllCheckBoxes(true);
            } else {
                // Uncheck all checkboxes
                setAllCheckBoxes(false);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("ADD", (dialog, which) -> {
            String temp = medQty.getText().toString();
            int qty = 0;
            if (!"".equals(temp))
                qty = Integer.parseInt(temp);
            String days = "0000000";
            if (isRepeat.isPressed()) {
                days = setDaysFormat(sun, mon, tue, wed, thu, fri, sat);
            }
            DbHelper.addMedicine(DbHelper.getWritableDatabase(), user_id, medName.getText().toString(), qty, medTime.getText().toString(), days);
            medListAdapter.setUserData(DbHelper.getMedicineListById(DbHelper.getWritableDatabase(), user_id));
            medListAdapter.notifyDataSetChanged();
            medList.setAdapter(medListAdapter);
        });
        builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel());
        builder.setView(layout);
        return builder.create();
    }

    public String setDaysFormat(CheckBox sun, CheckBox mon, CheckBox tue, CheckBox wed, CheckBox thu, CheckBox fri, CheckBox sat) {
        String dayString = "" + (sun.isChecked() ? "1" : "0") + (mon.isChecked() ? "1" : "0") + (tue.isChecked() ? "1" : "0") + (wed.isChecked() ? "1" : "0") + (thu.isChecked() ? "1" : "0") + (fri.isChecked() ? "1" : "0") + (sat.isChecked() ? "1" : "0");
        return dayString;
    }

    public void setAllCheckBoxes(boolean checked) {
        sun.setChecked(checked);
        mon.setChecked(checked);
        tue.setChecked(checked);
        wed.setChecked(checked);
        thu.setChecked(checked);
        fri.setChecked(checked);
        sat.setChecked(checked);
        setChildrenEnabled(!checked);
    }

    public void setChildrenEnabled(Boolean enable) {
        sun.setEnabled(enable);
        mon.setEnabled(enable);
        tue.setEnabled(enable);
        wed.setEnabled(enable);
        thu.setEnabled(enable);
        fri.setEnabled(enable);
        sat.setEnabled(enable);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        medTime.setText(String.format("%02d:%02d", hourOfDay, minute));
    }

    public void onBackButtonClick(View view) {
        onBackPressed(); // Ensure the back button uses the same behavior
    }
}
