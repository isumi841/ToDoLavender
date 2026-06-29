package com.example.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.security.MessageDigest;
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TodoApp.db";
    private static final int DATABASE_VERSION = 9;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private String username;

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createUserTable = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "fullname TEXT," +
                "username TEXT UNIQUE," +
                "password TEXT)";

        db.execSQL(createUserTable);

        String createTaskTable = "CREATE TABLE tasks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "description TEXT," +
                "dueDate TEXT," +
                "tag TEXT," +
                "completed INTEGER DEFAULT 0)";

        db.execSQL(createTaskTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS tasks");

        onCreate(db);
    }

    // =========================
    // USER FUNCTIONS
    // =========================


    public String hashPassword(String password) {

        try {

            MessageDigest md =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    md.digest(password.getBytes());

            StringBuilder sb =
                    new StringBuilder();

            for (byte b : hash) {
                sb.append(
                        String.format("%02x", b));
            }

            return sb.toString();

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    public boolean registerUser(String fullname,
                                String username,
                                String password) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("fullname", fullname);
        values.put("username", username);
        values.put("password", password);

        long result =
                db.insert("users",
                        null,
                        values);

        return result != -1;
    }

    public boolean checkUser(String username,
                             String password) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM users WHERE username=? AND password=?",
                new String[]{username, password});
        boolean exists =
                cursor.getCount() > 0;

        cursor.close();

        return exists;
    }

    // =========================
    // TASK FUNCTIONS
    // =========================

    public boolean addTask(String title,
                           String description,
                           String dueDate,
                           String tag) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("title", title);
        values.put("description", description);
        values.put("dueDate", dueDate);
        values.put("tag", tag);

        long result =
                db.insert("tasks",
                        null,
                        values);

        return result != -1;
    }

    public Cursor getTasks() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM tasks ORDER BY dueDate ASC",
                null);
    }

    public void deleteTask(int id) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        db.delete(
                "tasks",
                "id=?",
                new String[]{
                        String.valueOf(id)
                });
    }

    public void updateTask(int id,
                           String title,
                           String description,
                           String dueDate,
                           String tag) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("title", title);
        values.put("description", description);
        values.put("dueDate", dueDate);
        values.put("tag", tag);

        db.update(
                "tasks",
                values,
                "id=?",
                new String[]{
                        String.valueOf(id)
                });
    }

    public void updateTaskStatus(int id,
                                 int completed) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("completed", completed);

        db.update(
                "tasks",
                values,
                "id=?",
                new String[]{
                        String.valueOf(id)
                });
    }

    public int getTaskCount() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM tasks",
                        null);

        int count =
                cursor.getCount();

        cursor.close();

        return count;
    }

    public int getCompletedTaskCount() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM tasks WHERE completed=1",
                        null);

        int count =
                cursor.getCount();

        cursor.close();

        return count;
    }

    public boolean updateUsername(String oldUsername,
                                  String newUsername) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("username", newUsername);

        int result =
                db.update(
                        "users",
                        values,
                        "username=?",
                        new String[]{oldUsername});

        return result > 0;
    }

    public boolean updatePassword(String username,
                                  String newPassword) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("password", newPassword);

        int result =
                db.update(
                        "users",
                        values,
                        "username=?",
                        new String[]{username});

        return result > 0;
    }
}