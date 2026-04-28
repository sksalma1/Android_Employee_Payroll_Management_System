package com.example.payroll;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnAddEmp, btnUpdateEmp, btnViewEmp, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddEmp = findViewById(R.id.btnAddEmp);
        btnUpdateEmp = findViewById(R.id.btnUpdateEmp);
        btnViewEmp = findViewById(R.id.btnViewEmp);
        btnBack = findViewById(R.id.btnBack);  // Initialize the Back button

        btnAddEmp.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AddEmployeeActivity.class))
        );

        btnUpdateEmp.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, UpdateEmployeeActivity.class))
        );

        btnViewEmp.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ViewEmployeesActivity.class))
        );

        // Set the back button to navigate to SplashActivity
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SplashActivity.class)); // Go back to SplashActivity
            finish(); // Close MainActivity
        });
    }


}
