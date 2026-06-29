package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin;
    TextView txtRegister,txtForgotPassword;

    DatabaseHelper db;

    boolean isPasswordVisible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);
        txtForgotPassword =
                findViewById(R.id.txtForgotPassword);

        db = new DatabaseHelper(this);

        etPassword.setOnTouchListener((v, event) -> {

            if (event.getAction() == android.view.MotionEvent.ACTION_UP) {

                if (event.getRawX() >= (etPassword.getRight()
                        - etPassword.getCompoundDrawables()[2].getBounds().width())) {

                    if (isPasswordVisible) {

                        etPassword.setInputType(
                                android.text.InputType.TYPE_CLASS_TEXT |
                                        android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);

                        isPasswordVisible = false;

                    } else {

                        etPassword.setInputType(
                                android.text.InputType.TYPE_CLASS_TEXT |
                                        android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                        isPasswordVisible = true;
                    }

                    etPassword.setSelection(
                            etPassword.getText().length());

                    return true;
                }
            }

            return false;
        });

        // Open Register Page
        txtRegister.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    RegisterActivity.class);

            startActivity(intent);
        });

        txtForgotPassword.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    ForgotPasswordActivity.class);

            startActivity(intent);
        });

        // Login Button
        btnLogin.setOnClickListener(v -> {

            String username =
                    etUsername.getText()
                            .toString()
                            .trim();

            String password =
                    etPassword.getText()
                            .toString()
                            .trim();

            if(username.isEmpty() || password.isEmpty()) {

                Toast.makeText(
                        MainActivity.this,
                        "Please enter username and password",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }
            if(db.checkUser(
                    username,
                    password)) {

                Toast.makeText(
                        MainActivity.this,
                        "Login Successful",
                        Toast.LENGTH_SHORT
                ).show();

                Intent intent =
                        new Intent(
                                MainActivity.this,
                                DashboardActivity.class);

                // Send username to Dashboard
                intent.putExtra(
                        "username",
                        username);

                startActivity(intent);

                finish();

            } else {

                Toast.makeText(
                        MainActivity.this,
                        "Invalid Username or Password",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}