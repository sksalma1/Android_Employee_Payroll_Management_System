package com.example.payroll;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateEmployeeActivity extends AppCompatActivity {

    EditText edtEmpId, edtName, edtDesignation, edtTotalDays, edtSalaryPerDay,
            edtWorkedDays, edtAllowances, edtDeductions, edtNetSalary;
    Button btnFetch, btnUpdate, btnCalculateSalary, btnBack, btnDelete;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_employee);

        // Initialize EditText fields
        edtEmpId = findViewById(R.id.edtEmpId);
        edtName = findViewById(R.id.edtName);
        edtDesignation = findViewById(R.id.edtDesignation);
        edtTotalDays = findViewById(R.id.edtTotalDays);
        edtSalaryPerDay = findViewById(R.id.edtSalaryPerDay);
        edtWorkedDays = findViewById(R.id.edtWorkedDays);
        edtAllowances = findViewById(R.id.edtAllowances);
        edtDeductions = findViewById(R.id.edtDeductions);
        edtNetSalary = findViewById(R.id.edtNetSalary);

        // Initialize Buttons
        btnFetch = findViewById(R.id.btnFetch);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCalculateSalary = findViewById(R.id.btnCalculateSalary);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Fetch Employee Details
        btnFetch.setOnClickListener(v -> {
            String empId = edtEmpId.getText().toString();
            if (empId.isEmpty()) {
                Toast.makeText(this, "Enter Employee ID to fetch details", Toast.LENGTH_SHORT).show();
            } else {
                Cursor cursor = dbHelper.getEmployee(empId);
                if (cursor != null && cursor.moveToFirst()) {
                    edtName.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    edtDesignation.setText(cursor.getString(cursor.getColumnIndexOrThrow("designation")));
                    edtTotalDays.setText(cursor.getString(cursor.getColumnIndexOrThrow("total_days")));
                    edtSalaryPerDay.setText(cursor.getString(cursor.getColumnIndexOrThrow("salary_per_day")));
                    edtWorkedDays.setText(cursor.getString(cursor.getColumnIndexOrThrow("worked_days")));
                    edtAllowances.setText(cursor.getString(cursor.getColumnIndexOrThrow("allowances")));
                    edtDeductions.setText(cursor.getString(cursor.getColumnIndexOrThrow("deductions")));
                    edtNetSalary.setText(cursor.getString(cursor.getColumnIndexOrThrow("net_salary")));
                    cursor.close();
                } else {
                    Toast.makeText(this, "Employee Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Calculate Net Salary
        btnCalculateSalary.setOnClickListener(v -> {
            String salaryPerDayStr = edtSalaryPerDay.getText().toString();
            String workedDaysStr = edtWorkedDays.getText().toString();
            String allowancesStr = edtAllowances.getText().toString();
            String deductionsStr = edtDeductions.getText().toString();

            if (salaryPerDayStr.isEmpty() || workedDaysStr.isEmpty() ||
                    allowancesStr.isEmpty() || deductionsStr.isEmpty()) {
                Toast.makeText(this, "Fill Salary, Worked Days, Allowances, Deductions", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    double salaryPerDay = Double.parseDouble(salaryPerDayStr);
                    int workedDays = Integer.parseInt(workedDaysStr);
                    double allowances = Double.parseDouble(allowancesStr);
                    double deductions = Double.parseDouble(deductionsStr);

                    if (workedDays < 0 || salaryPerDay < 0 || allowances < 0 || deductions < 0) {
                        Toast.makeText(this, "Values cannot be negative", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double netSalary = (salaryPerDay * workedDays) + allowances - deductions;
                    edtNetSalary.setText(String.format("%.2f", netSalary));
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Enter valid numeric values", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Update Employee Details
        btnUpdate.setOnClickListener(v -> {
            String empId = edtEmpId.getText().toString();
            String name = edtName.getText().toString();
            String designation = edtDesignation.getText().toString();
            String totalDaysStr = edtTotalDays.getText().toString();
            String salaryPerDayStr = edtSalaryPerDay.getText().toString();
            String workedDaysStr = edtWorkedDays.getText().toString();
            String allowancesStr = edtAllowances.getText().toString();
            String deductionsStr = edtDeductions.getText().toString();
            String netSalaryStr = edtNetSalary.getText().toString();

            if (empId.isEmpty() || name.isEmpty() || designation.isEmpty() ||
                    totalDaysStr.isEmpty() || salaryPerDayStr.isEmpty() ||
                    workedDaysStr.isEmpty() || allowancesStr.isEmpty() ||
                    deductionsStr.isEmpty() || netSalaryStr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    int totalDays = Integer.parseInt(totalDaysStr);
                    double salaryPerDay = Double.parseDouble(salaryPerDayStr);
                    int workedDays = Integer.parseInt(workedDaysStr);
                    double allowances = Double.parseDouble(allowancesStr);
                    double deductions = Double.parseDouble(deductionsStr);
                    double netSalary = Double.parseDouble(netSalaryStr);

                    if (totalDays < 0 || workedDays < 0 || salaryPerDay < 0 ||
                            allowances < 0 || deductions < 0 || netSalary < 0) {
                        Toast.makeText(this, "Values cannot be negative", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (workedDays > totalDays) {
                        Toast.makeText(this, "Worked days cannot exceed total days", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    boolean updated = dbHelper.updateEmployee(
                            empId,
                            name,
                            designation,
                            totalDays,
                            salaryPerDay,
                            workedDays,
                            allowances,
                            deductions,
                            netSalary
                    );

                    if (updated) {
                        Toast.makeText(this, "Employee Updated Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Enter valid numeric values", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Delete Employee
        btnDelete.setOnClickListener(v -> {
            String empId = edtEmpId.getText().toString();
            if (empId.isEmpty()) {
                Toast.makeText(this, "Enter Employee ID to delete", Toast.LENGTH_SHORT).show();
            } else {
                boolean deleted = dbHelper.deleteEmployee(empId);
                if (deleted) {
                    Toast.makeText(this, "Employee Deleted Successfully", Toast.LENGTH_SHORT).show();
                    // Clear all fields after deletion
                    edtEmpId.setText("");
                    edtName.setText("");
                    edtDesignation.setText("");
                    edtTotalDays.setText("");
                    edtSalaryPerDay.setText("");
                    edtWorkedDays.setText("");
                    edtAllowances.setText("");
                    edtDeductions.setText("");
                    edtNetSalary.setText("");
                } else {
                    Toast.makeText(this, "Deletion Failed: Employee Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Back to MainActivity
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateEmployeeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}