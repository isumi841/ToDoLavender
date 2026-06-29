package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText etFullName, etUsername, etPassword;
    Button btnRegister;
    TextView txtLogin;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFullName = findViewById(R.id.etFullName);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);

        db = new DatabaseHelper(this);

        btnRegister.setOnClickListener(v -> {

            String fullname = etFullName.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if(fullname.isEmpty() || username.isEmpty() || password.isEmpty()) {

                Toast.makeText(RegisterActivity.this,
                        "Please fill all fields",
                        Toast.LENGTH_SHORT).show();

                return;
            }


            boolean inserted =
                    db.registerUser(
                            fullname,
                            username,
                            password);

            if(inserted) {

                Toast.makeText(RegisterActivity.this,
                        "Registration Successful",
                        Toast.LENGTH_SHORT).show();

                startActivity(new Intent(
                        RegisterActivity.this,
                        MainActivity.class));

                finish();

            } else {

                Toast.makeText(RegisterActivity.this,
                        "Username already exists",
                        Toast.LENGTH_SHORT).show();
            }
        });

        txtLogin.setOnClickListener(v -> {
            finish();
        });
    }
}