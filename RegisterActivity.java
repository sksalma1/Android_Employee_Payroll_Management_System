package com.example.payroll;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;

public class RegisterActivity extends AppCompatActivity {
    EditText etUsername, etPassword;
    Button btnRegister;
    DBUserHelper dbUserHelper;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etRegUsername);
        etPassword = findViewById(R.id.etRegPassword);
        btnRegister = findViewById(R.id.btnRegister);
        dbUserHelper = new DBUserHelper(this);
        databaseHelper = new DatabaseHelper(this);

        // Show Toast when username field gains focus
        etUsername.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Toast.makeText(this, "Please enter your employee ID as the username", Toast.LENGTH_LONG).show();
            }
        });

        btnRegister.setOnClickListener(v -> {
            String user = etUsername.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            // Validate inputs
            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if the username already exists in the users table
            if (dbUserHelper.checkUser(user, pass)) {
                // User already exists, show message and go back to LoginActivity
                Toast.makeText(this, "User already registered. Please log in.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                // Check if the username matches an emp_id in the employees table
                Cursor cursor = databaseHelper.getEmployee(user);
                if (cursor != null && cursor.getCount() > 0) {
                    // emp_id exists, proceed with registration
                    if (dbUserHelper.insertUser(user, pass)) {
                        Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // emp_id does not exist
                    Toast.makeText(this, "Invalid username. Please enter your employee ID.", Toast.LENGTH_LONG).show();
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
        });
    }
}
