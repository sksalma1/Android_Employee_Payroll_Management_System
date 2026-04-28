package com.example.payroll;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ViewEmployeesActivity extends AppCompatActivity {

    LinearLayout employeeContainer;
    DatabaseHelper dbHelper;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employees);

        employeeContainer = findViewById(R.id.employeeContainer);
        btnBack = findViewById(R.id.btnBack);
        dbHelper = new DatabaseHelper(this);

        Cursor cursor = dbHelper.getAllEmployees();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String empDetails = "ID: " + cursor.getString(cursor.getColumnIndexOrThrow("emp_id")) + "\n"
                        + "Name: " + cursor.getString(cursor.getColumnIndexOrThrow("name")) + "\n"
                        + "Designation: " + cursor.getString(cursor.getColumnIndexOrThrow("designation")) + "\n"
                        + "Total Days: " + cursor.getInt(cursor.getColumnIndexOrThrow("total_days")) + "\n"
                        + "Salary/Day: " + cursor.getDouble(cursor.getColumnIndexOrThrow("salary_per_day")) + "\n"
                        + "Worked Days: " + cursor.getInt(cursor.getColumnIndexOrThrow("worked_days")) + "\n"
                        + "Allowances: " + cursor.getDouble(cursor.getColumnIndexOrThrow("allowances")) + "\n"
                        + "Deductions: " + cursor.getDouble(cursor.getColumnIndexOrThrow("deductions")) + "\n"
                        + "Net Salary: " + cursor.getDouble(cursor.getColumnIndexOrThrow("net_salary"));

                TextView tv = new TextView(this);
                tv.setText(empDetails);
                tv.setPadding(24, 24, 24, 24);
                tv.setTextSize(16);
                tv.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, 24);
                tv.setLayoutParams(params);

                employeeContainer.addView(tv);
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "No employee data found.", Toast.LENGTH_SHORT).show();
        }

        // Back button functionality
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close this activity and go back
            }
        });
    }
}
