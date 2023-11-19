package com.example.HikeAppCW.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


public class AppDatabase {
    private static DatabaseHelper databaseHelper;

    public static void init(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
        }
    }

    public static SQLiteDatabase getWritableDatabase() {
        return databaseHelper.getWritableDatabase();
    }

    public static SQLiteDatabase getReadableDatabase() {
        return databaseHelper.getReadableDatabase();
    }
}
