package com.example.arjunc196;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "app_database";
    private static final String TABLE_NAME = "terms";
    private static final String KEY_ID = "id";
    private static final String KEY_TERM_TITLE = "termTitle";
    private static final String KEY_START_DATE = "startDate";
    private static final String KEY_END_DATE = "endDate";

    private static final String CREATE_TABLE_TERMS = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TERM_TITLE + " TEXT,"
            + KEY_START_DATE + " TEXT,"
            + KEY_END_DATE + " TEXT"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TERMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
