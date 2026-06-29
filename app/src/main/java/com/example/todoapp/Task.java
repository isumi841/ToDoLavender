package com.example.todoapp;

public class Task {

    private int id;
    private String title;
    private String description;
    private String dueDate;
    private String tag;
    private int completed;

    public Task(int id,
                String title,
                String description,
                String dueDate,
                String tag,
                int completed) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.tag = tag;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getTag() {
        return tag;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }
}