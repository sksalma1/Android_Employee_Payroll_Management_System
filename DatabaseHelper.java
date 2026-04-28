package com.example.payroll;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "payroll.db";
    public static final String TABLE = "employees";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE + " (" +
                "emp_id TEXT PRIMARY KEY, " +
                "name TEXT, " +
                "designation TEXT, " +
                "total_days INTEGER, " +
                "salary_per_day REAL, " +
                "worked_days INTEGER, " +
                "allowances REAL, " +
                "deductions REAL, " +
                "net_salary REAL)";
        db.execSQL(createTableQuery);
        Log.d("DatabaseHelper", "Table " + TABLE + " created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("DatabaseHelper", "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public boolean insertEmployee(String empId, String name, String designation, int totalDays,
                                  double salaryPerDay, int workedDays, double allowances,
                                  double deductions, double netSalary) {
        SQLiteDatabase db = null;
        try {
            // Validate inputs
            if (empId == null || empId.trim().isEmpty() || name == null || name.trim().isEmpty()) {
                Log.e("DatabaseHelper", "Insert failed: emp_id or name is empty");
                return false;
            }
            if (totalDays < 0 || workedDays < 0 || salaryPerDay < 0 || allowances < 0 || deductions < 0 || netSalary < 0) {
                Log.e("DatabaseHelper", "Insert failed: Negative values not allowed");
                return false;
            }
            if (workedDays > totalDays) {
                Log.e("DatabaseHelper", "Insert failed: worked_days (" + workedDays + ") exceeds total_days (" + totalDays + ")");
                return false;
            }

            db = this.getWritableDatabase();
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put("emp_id", empId);
            cv.put("name", name);
            cv.put("designation", designation);
            cv.put("total_days", totalDays);
            cv.put("salary_per_day", salaryPerDay);
            cv.put("worked_days", workedDays);
            cv.put("allowances", allowances);
            cv.put("deductions", deductions);
            cv.put("net_salary", netSalary);
            long result = db.insert(TABLE, null, cv);
            if (result != -1) {
                db.setTransactionSuccessful();
                Log.d("DatabaseHelper", "Employee inserted successfully: emp_id=" + empId);
                return true;
            } else {
                Log.e("DatabaseHelper", "Insert failed: emp_id=" + empId);
                return false;
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Insert failed for emp_id: " + empId, e);
            return false;
        } finally {
            if (db != null) {
                if (db.inTransaction()) {
                    db.endTransaction();
                }
                if (db.isOpen()) {
                    db.close();
                }
            }
        }
    }

    // Fetch a single employee by emp_id
    // Caller must close the returned Cursor
    public Cursor getEmployee(String empId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE + " WHERE emp_id=?", new String[]{empId});
        return cursor;
    }

    public boolean updateEmployee(String empId, String name, String designation, int totalDays,
                                  double salaryPerDay, int workedDays, double allowances,
                                  double deductions, double netSalary) {
        SQLiteDatabase db = null;
        try {
            // Validate inputs
            if (empId == null || empId.trim().isEmpty() || name == null || name.trim().isEmpty()) {
                Log.e("DatabaseHelper", "Update failed: emp_id or name is empty");
                return false;
            }
            if (totalDays < 0 || workedDays < 0 || salaryPerDay < 0 || allowances < 0 || deductions < 0 || netSalary < 0) {
                Log.e("DatabaseHelper", "Update failed: Negative values not allowed");
                return false;
            }
            if (workedDays > totalDays) {
                Log.e("DatabaseHelper", "Update failed: worked_days (" + workedDays + ") exceeds total_days (" + totalDays + ")");
                return false;
            }

            db = this.getWritableDatabase();
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put("name", name);
            cv.put("designation", designation);
            cv.put("total_days", totalDays);
            cv.put("salary_per_day", salaryPerDay);
            cv.put("worked_days", workedDays);
            cv.put("allowances", allowances);
            cv.put("deductions", deductions);
            cv.put("net_salary", netSalary);
            int rowsAffected = db.update(TABLE, cv, "emp_id=?", new String[]{empId});
            if (rowsAffected > 0) {
                db.setTransactionSuccessful();
                Log.d("DatabaseHelper", "Employee updated successfully: emp_id=" + empId);
                return true;
            } else {
                Log.e("DatabaseHelper", "Update failed: emp_id=" + empId + " not found");
                return false;
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Update  Update failed for emp_id: " + empId, e);
            return false;
        } finally {
            if (db != null) {
                if (db.inTransaction()) {
                    db.endTransaction();
                }
                if (db.isOpen()) {
                    db.close();
                }
            }
        }
    }

    public boolean deleteEmployee(String empId) {
        SQLiteDatabase db = null;
        try {
            // Validate input
            if (empId == null || empId.trim().isEmpty()) {
                Log.e("DatabaseHelper", "Delete failed: emp_id is empty");
                return false;
            }

            db = this.getWritableDatabase();
            db.beginTransaction();
            int rowsAffected = db.delete(TABLE, "emp_id=?", new String[]{empId});
            if (rowsAffected > 0) {
                db.setTransactionSuccessful();
                Log.d("DatabaseHelper", "Employee deleted successfully: emp_id=" + empId);
                return true;
            } else {
                Log.e("DatabaseHelper", "Delete failed: emp_id=" + empId + " not found");
                return false;
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Delete failed for emp_id: " + empId, e);
            return false;
        } finally {
            if (db != null) {
                if (db.inTransaction()) {
                    db.endTransaction();
                }
                if (db.isOpen()) {
                    db.close();
                }
            }
        }
    }

    // Fetch all employees
    // Caller must close the returned Cursor
    public Cursor getAllEmployees() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE, null);
        return cursor;
    }
}