package com.example.todoapp;

import android.widget.ImageView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    ImageView btnBack;
    EditText etUsername, etNewPassword;
    Button btnReset;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etUsername = findViewById(R.id.etUsername);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnReset = findViewById(R.id.btnReset);
        btnBack = findViewById(R.id.btnBack);

        db = new DatabaseHelper(this);

        btnBack.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            ForgotPasswordActivity.this,
                            MainActivity.class);

            startActivity(intent);

            finish();
        });

        btnReset.setOnClickListener(v -> {

            String username =
                    etUsername.getText().toString().trim();

            String password =
                    etNewPassword.getText().toString().trim();

            if(username.isEmpty() || password.isEmpty()) {

                Toast.makeText(
                        this,
                        "Fill all fields",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            boolean updated =
                    db.updatePassword(
                            username,
                            password);

            if(updated) {

                Toast.makeText(
                        this,
                        "Password Updated",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Username Not Found",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}