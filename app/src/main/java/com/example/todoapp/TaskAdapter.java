package com.example.todoapp;
import android.content.res.ColorStateList;
//import android.content.res.ColorStateList;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.app.DatePickerDialog;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    public interface OnTaskChangedListener {
        void onTaskChanged();
    }

    ArrayList<Task> taskList;
    DatabaseHelper db;
    Context context;
    OnTaskChangedListener listener;

    public TaskAdapter(
            Context context,
            ArrayList<Task> taskList,
            OnTaskChangedListener listener) {

        this.context = context;
        this.taskList = taskList;
        this.listener = listener;

        db = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_task, parent, false);

        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull TaskViewHolder holder,
            int position) {

        Task task = taskList.get(position);

        holder.txtTaskTitle.setText(task.getTitle());

        holder.txtTaskDescription.setText(
                task.getDescription());

        holder.txtDueDate.setText(
                "Due: " + task.getDueDate());
        if (task.getTag() != null &&
                !task.getTag().isEmpty()) {

            holder.txtTaskTag.setText(
                    "Tag: " + task.getTag());

        } else {

            holder.txtTaskTag.setText(
                    "Tag: None");
        }

        holder.checkCompleted.setOnCheckedChangeListener(null);

        holder.checkCompleted.setChecked(
                task.getCompleted() == 1);
        holder.checkCompleted.setButtonTintList(
                ColorStateList.valueOf(
                        holder.itemView.getContext()
                                .getResources()
                                .getColor(R.color.accent)
                )
        );

        holder.checkCompleted.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    int status = isChecked ? 1 : 0;

                    db.updateTaskStatus(
                            task.getId(),
                            status);

                    task.setCompleted(status);

                    if (listener != null) {
                        listener.onTaskChanged();
                    }
                });

        // EDIT TASK
        holder.txtTaskTitle.setOnClickListener(v -> {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(context);

            builder.setTitle("Edit Task");

            android.widget.LinearLayout layout =
                    new android.widget.LinearLayout(context);

            layout.setOrientation(
                    android.widget.LinearLayout.VERTICAL);

            EditText titleInput =
                    new EditText(context);

            titleInput.setHint("Title");
            titleInput.setText(task.getTitle());

            EditText descriptionInput =
                    new EditText(context);

            descriptionInput.setHint("Description");
            descriptionInput.setText(
                    task.getDescription());
            EditText tagInput =
                    new EditText(context);

            tagInput.setHint("Tag");

            tagInput.setText(
                    task.getTag());

            EditText dueDateInput =
                    new EditText(context);

            dueDateInput.setHint("Due Date");
            dueDateInput.setText(
                    task.getDueDate());

            dueDateInput.setFocusable(false);
            dueDateInput.setClickable(true);

            dueDateInput.setOnClickListener(v1 -> {

                Calendar calendar = Calendar.getInstance();

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog =
                        new DatePickerDialog(
                                context,
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

            builder.setPositiveButton("Update",
                    (dialog, which) -> {

                        String newTitle =
                                titleInput.getText()
                                        .toString()
                                        .trim();

                        String newDescription =
                                descriptionInput.getText()
                                        .toString()
                                        .trim();

                        String newDueDate =
                                dueDateInput.getText()
                                        .toString()
                                        .trim();

                        String newTag =
                                tagInput.getText()
                                        .toString()
                                        .trim();

                        if (!newTitle.isEmpty()) {

                            db.updateTask(
                                    task.getId(),
                                    newTitle,
                                    newDescription,
                                    newDueDate,
                                    newTag);

                            taskList.set(
                                    position,
                                    new Task(
                                            task.getId(),
                                            newTitle,
                                            newDescription,
                                            newDueDate,
                                            newTag,
                                            task.getCompleted()));

                            notifyDataSetChanged();

                            if (listener != null) {
                                listener.onTaskChanged();
                            }
                        }
                    });

            builder.setNegativeButton(
                    "Cancel",
                    (dialog, which) -> dialog.dismiss());

            builder.show();
        });

        // DELETE TASK
        holder.btnDelete.setOnClickListener(v -> {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(context);

            builder.setTitle("Delete Task");
            builder.setMessage("Are you sure?");

            builder.setPositiveButton("Delete",
                    (dialog, which) -> {

                        db.deleteTask(task.getId());

                        taskList.remove(position);

                        notifyItemRemoved(position);
                        notifyItemRangeChanged(
                                position,
                                taskList.size());

                        if (listener != null) {
                            listener.onTaskChanged();
                        }
                    });

            builder.setNegativeButton(
                    "Cancel",
                    (dialog, which) -> dialog.dismiss());

            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtTaskTitle;
        TextView txtTaskDescription;
        TextView txtDueDate;

        TextView txtTaskTag;
        ImageButton btnDelete;
        CheckBox checkCompleted;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTaskTitle =
                    itemView.findViewById(R.id.txtTaskTitle);

            txtTaskDescription =
                    itemView.findViewById(
                            R.id.txtTaskDescription);

            txtDueDate =
                    itemView.findViewById(
                            R.id.txtDueDate);

            txtTaskTag =
                    itemView.findViewById(
                            R.id.txtTaskTag);
            btnDelete =
                    itemView.findViewById(R.id.btnDelete);

            checkCompleted =
                    itemView.findViewById(
                            R.id.checkCompleted);
        }
    }
}