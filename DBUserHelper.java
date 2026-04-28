package com.example.payroll;

import android.content.Context;
import android.database.sqlite.*;
import android.content.*;
import android.database.Cursor;

public class DBUserHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "user.db";
    public static final String TABLE = "users";

    public DBUserHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE + " (username TEXT PRIMARY KEY, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("password", password);
        long res = db.insert(TABLE, null, cv);
        return res != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE + " WHERE username=? AND password=?", new String[]{username, password});
        return cursor.getCount() > 0;
    }
}
