package com.example.todoapp;


import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;
import java.util.Calendar;
import java.util.Collections;
import android.content.Intent;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private String username;

    Button btnAddTask;
    RecyclerView recyclerTasks;

    TextView txtWelcome;
    TextView txtTotalTasks;
    TextView txtCompletedTasks;
    TextView txtLogout;
    DatabaseHelper db;

    ArrayList<Task> taskList;
    TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        txtWelcome = findViewById(R.id.txtWelcome);
        txtTotalTasks = findViewById(R.id.txtTotalTasks);
        txtCompletedTasks = findViewById(R.id.txtCompletedTasks);
        txtLogout = findViewById(R.id.txtLogout);
        username = getIntent().getStringExtra("username");

        if (username != null && !username.isEmpty()) {
            txtWelcome.setText("Hi, " + username);
        } else {
            txtWelcome.setText("Hi");
        }

        btnAddTask = findViewById(R.id.btnAddTask);
        recyclerTasks = findViewById(R.id.recyclerTasks);

        db = new DatabaseHelper(this);

        taskList = new ArrayList<>();

        adapter = new TaskAdapter(
                this,
                taskList,
                () -> {
                    loadTasks();
                    updateCounts();
                }
        );

        recyclerTasks.setLayoutManager(
                new LinearLayoutManager(this));

        recyclerTasks.setAdapter(adapter);

        loadTasks();
        updateCounts();

        txtLogout.setOnClickListener(v -> {

            String[] options = {
                    "👤 Change Username",
                    "🔒 Change Password",
                    "🚪 Logout"
            };

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(
                            DashboardActivity.this);

            builder.setTitle("⚙ Account Settings");

            builder.setItems(options,
                    (dialog, which) -> {

                        if (which == 0) {

                            EditText input =
                                    new EditText(
                                            DashboardActivity.this);

                            input.setHint(
                                    "Enter new username");

                            new AlertDialog.Builder(
                                    DashboardActivity.this)
                                    .setTitle(
                                            "Change Username")
                                    .setView(input)
                                    .setPositiveButton(
                                            "Update",
                                            (d, w) -> {

                                                String newUsername =
                                                        input.getText()
                                                                .toString()
                                                                .trim();

                                                if (!newUsername.isEmpty()) {

                                                    boolean success =
                                                            db.updateUsername(
                                                                    username,
                                                                    newUsername);

                                                    if (success) {

                                                        username =
                                                                newUsername;

                                                        txtWelcome.setText(
                                                                "Hi, " + username);

                                                        Toast.makeText(
                                                                        DashboardActivity.this,
                                                                        "Username updated",
                                                                        Toast.LENGTH_SHORT)
                                                                .show();
                                                    }
                                                }
                                            })
                                    .setNegativeButton(
                                            "Cancel",
                                            null)
                                    .show();
                        }

                        else if (which == 1) {

                            EditText input =
                                    new EditText(
                                            DashboardActivity.this);

                            input.setHint(
                                    "Enter new password");

                            new AlertDialog.Builder(
                                    DashboardActivity.this)
                                    .setTitle(
                                            "Change Password")
                                    .setView(input)
                                    .setPositiveButton(
                                            "Update",
                                            (d, w) -> {

                                                String newPassword =
                                                        input.getText()
                                                                .toString()
                                                                .trim();

                                                if (!newPassword.isEmpty()) {

                                                    Toast.makeText(
                                                            DashboardActivity.this,
                                                            "Username = " + username,
                                                            Toast.LENGTH_LONG
                                                    ).show();

                                                    boolean success =
                                                            db.updatePassword(
                                                                    username,
                                                                    newPassword);

                                                    if (success) {

                                                        Toast.makeText(
                                                                        DashboardActivity.this,
                                                                        "Password updated",
                                                                        Toast.LENGTH_SHORT)
                                                                .show();
                                                    }
                                                }
                                            })
                                    .setNegativeButton(
                                            "Cancel",
                                            null)
                                    .show();
                        }

                        else if (which == 2) {

                            new AlertDialog.Builder(
                                    DashboardActivity.this)
                                    .setTitle(
                                            "Logout")
                                    .setMessage(
                                            "Are you sure you want to logout?")
                                    .setPositiveButton(
                                            "Yes",
                                            (d, w) -> {

                                                Intent intent =
                                                        new Intent(
                                                                DashboardActivity.this,
                                                                MainActivity.class);

                                                startActivity(intent);

                                                finish();
                                            })
                                    .setNegativeButton(
                                            "No",
                                            null)
                                    .show();
                        }
                    });

            builder.show();
        });


        btnAddTask.setOnClickListener(v -> {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this);

            builder.setTitle("Add New Task");

            android.widget.LinearLayout layout =
                    new android.widget.LinearLayout(this);

            layout.setOrientation(
                    android.widget.LinearLayout.VERTICAL);

            EditText titleInput = new EditText(this);
            titleInput.setHint("Task Title");

            EditText descriptionInput = new EditText(this);
            descriptionInput.setHint("Task Description");

            EditText dueDateInput = new EditText(this);
            dueDateInput.setHint("Due Date (YYYY-MM-DD)");

            EditText tagInput = new EditText(this);
            tagInput.setHint("Tag (Optional)");

            dueDateInput.setFocusable(false);
            dueDateInput.setClickable(true);

            dueDateInput.setOnClickListener(v1 -> {

                Calendar calendar = Calendar.getInstance();

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog =
                        new DatePickerDialog(
                                this,
                                (view,
                                 selectedYear,
                                 selectedMonth,
                                 selectedDay) -> {

                                    String date =
                                            selectedYear + "-" +
                                                    String.format("%02d", selectedMonth + 1) + "-" +
                                                    String.format("%02d", selectedDay);

                                    dueDateInput.setText(date);
                                },
                                year,
                                month,
                                day);

                datePickerDialog.getDatePicker()
                        .setMinDate(System.currentTimeMillis());

                datePickerDialog.show();
            });
            layout.addView(titleInput);
            layout.addView(descriptionInput);
            layout.addView(dueDateInput);
            layout.addView(tagInput);

            builder.setView(layout);

            builder.setPositiveButton("Save",
                    (dialog, which) -> {

                        String title =
                                titleInput.getText()
                                        .toString()
                                        .trim();

                        String description =
                                descriptionInput.getText()
                                        .toString()
                                        .trim();

                        String dueDate =
                                dueDateInput.getText()
                                        .toString()
                                        .trim();
                        String tag =
                                tagInput.getText()
                                        .toString()
                                        .trim();

                        if (title.isEmpty()) {

                            Toast.makeText(
                                    this,
                                    "Task title required",
                                    Toast.LENGTH_SHORT).show();

                            return;
                        }

                        if (db.addTask(
                                title,
                                description,
                                dueDate,
                                tag )) {

                            Toast.makeText(
                                    this,
                                    "Task Added",
                                    Toast.LENGTH_SHORT).show();

                            loadTasks();
                            updateCounts();

                        } else {

                            Toast.makeText(
                                    this,
                                    "Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

            builder.setNegativeButton(
                    "Cancel",
                    (dialog, which) -> dialog.dismiss());

            builder.show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadTasks();
        updateCounts();
    }

    private void loadTasks() {

        taskList.clear();

        Cursor cursor = db.getTasks();

        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String description = cursor.getString(2);
            String dueDate = cursor.getString(3);
            String tag = cursor.getString(4);
            int completed = cursor.getInt(5);

            taskList.add(
                    new Task(
                            id,
                            title,
                            description,
                            dueDate,
                            tag,
                            completed));
        }




        cursor.close();
        adapter.notifyDataSetChanged();
    }


    private void updateCounts() {

        int totalTasks = db.getTaskCount();

        int completedTasks =
                db.getCompletedTaskCount();

        txtTotalTasks.setText(
                String.valueOf(totalTasks));

        txtCompletedTasks.setText(
                String.valueOf(completedTasks));
    }
}