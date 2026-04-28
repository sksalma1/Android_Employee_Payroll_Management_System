package com.example.payroll;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Button btnAdmin = findViewById(R.id.btnAdmin);
        Button btnUser = findViewById(R.id.btnUser);

        btnAdmin.setOnClickListener(v -> {
            // Redirect to MainActivity for Admin
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish(); // Close SplashActivity
        });

        btnUser.setOnClickListener(v -> {
            // Redirect to LoginActivity for User
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish(); // Close SplashActivity
        });
    }
}