package com.example.payroll;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddEmployeeActivity extends AppCompatActivity {

    EditText edtEmpId, edtName, edtDesignation, edtTotalDays, edtSalaryPerDay,
            edtWorkedDays, edtAllowances, edtDeductions, edtNetSalary;
    Button btnCalculateSalary, btnAddEmp, btnBack;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        edtEmpId = findViewById(R.id.edtEmpId);
        edtName = findViewById(R.id.edtName);
        edtDesignation = findViewById(R.id.edtDesignation);
        edtTotalDays = findViewById(R.id.edtTotalDays);
        edtSalaryPerDay = findViewById(R.id.edtSalaryPerDay);
        edtWorkedDays = findViewById(R.id.edtWorkedDays);
        edtAllowances = findViewById(R.id.edtAllowances);
        edtDeductions = findViewById(R.id.edtDeductions);
        edtNetSalary = findViewById(R.id.edtNetSalary);
        btnCalculateSalary = findViewById(R.id.btnCalculateSalary);
        btnAddEmp = findViewById(R.id.btnAddEmp);
        btnBack = findViewById(R.id.btnBack);

        dbHelper = new DatabaseHelper(this);

        btnCalculateSalary.setOnClickListener(v -> {
            try {
                double salaryPerDay = Double.parseDouble(edtSalaryPerDay.getText().toString());
                int workedDays = Integer.parseInt(edtWorkedDays.getText().toString());
                double allowances = Double.parseDouble(edtAllowances.getText().toString());
                double deductions = Double.parseDouble(edtDeductions.getText().toString());

                double grossSalary = salaryPerDay * workedDays;
                double netSalary = grossSalary + allowances - deductions;

                edtNetSalary.setText(String.valueOf(netSalary));
                Toast.makeText(this, "Salary Calculated", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Please fill salary, worked days, allowances, and deductions properly", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddEmp.setOnClickListener(v -> {
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
                    totalDaysStr.isEmpty() || salaryPerDayStr.isEmpty() || workedDaysStr.isEmpty() ||
                    allowancesStr.isEmpty() || deductionsStr.isEmpty() || netSalaryStr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields and calculate salary", Toast.LENGTH_SHORT).show();
            } else {
                boolean inserted = dbHelper.insertEmployee(
                        empId,
                        name,
                        designation,
                        Integer.parseInt(totalDaysStr),
                        Double.parseDouble(salaryPerDayStr),
                        Integer.parseInt(workedDaysStr),
                        Double.parseDouble(allowancesStr),
                        Double.parseDouble(deductionsStr),
                        Double.parseDouble(netSalaryStr)
                );

                if (inserted) {
                    Toast.makeText(this, "Employee Added Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to Add Employee. Employee already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(AddEmployeeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
