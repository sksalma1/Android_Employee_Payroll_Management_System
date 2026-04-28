package com.example.payroll;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ViewSalaryActivity extends AppCompatActivity {
    TextView tvDetails;
    DatabaseHelper dbHelper;
    Button btnBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_salary);

        tvDetails = findViewById(R.id.tvDetails);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);
        dbHelper = new DatabaseHelper(this);

        String empId = getIntent().getStringExtra("empId");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE + " WHERE emp_id=?", new String[]{empId});

        if (cursor.moveToFirst()) {
            String details = "ID: " + cursor.getString(0) +
                    "\nName: " + cursor.getString(1) +
                    "\nDesignation: " + cursor.getString(2) +
                    "\nTotal Days: " + cursor.getInt(3) +
                    "\nWorked Days: " + cursor.getInt(5) +
                    "\nSalary per Day: ₹" + cursor.getDouble(4) +
                    "\nAllowances: ₹" + cursor.getDouble(6) +
                    "\nDeductions: ₹" + cursor.getDouble(7) +
                    "\nNet Salary: ₹" + cursor.getDouble(8);
            tvDetails.setText(details);
        } else {
            tvDetails.setText("No records found.");
        }

        cursor.close();

        // Set click listener for back to login button
        btnBackToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(ViewSalaryActivity.this, SplashActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
        });
    }
}