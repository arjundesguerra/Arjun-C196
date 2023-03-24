package com.example.arjunc196;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "app_database";
    private static final String TABLE_TERMS = "terms";
    private static final String TABLE_COURSES = "courses";
    private static final String TABLE_INSTRUCTORS = "instructors";

    private static final String KEY_ID = "id";
    private static final String KEY_TERM_TITLE = "termTitle";
    private static final String KEY_START_DATE = "startDate";
    private static final String KEY_END_DATE = "endDate";

    private static final String KEY_COURSE_TITLE = "courseTitle";
    private static final String KEY_COURSE_START_DATE = "courseStartDate";
    private static final String KEY_COURSE_END_DATE = "courseEndDate";
    private static final String KEY_STATUS = "status";

    private static final String KEY_INSTRUCTOR_NAME = "instructorName";
    private static final String KEY_INSTRUCTOR_EMAIL = "instructorEmail";
    private static final String KEY_INSTRUCTOR_NUMBER = "instructorNumber";


    private static final String CREATE_TABLE_TERMS = "CREATE TABLE " + TABLE_TERMS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TERM_TITLE + " TEXT,"
            + KEY_START_DATE + " TEXT,"
            + KEY_END_DATE + " TEXT "
            + ")";

    private static final String CREATE_TABLE_COURSES = "CREATE TABLE " + TABLE_COURSES + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_COURSE_TITLE + " TEXT,"
            + KEY_COURSE_START_DATE + " TEXT,"
            + KEY_COURSE_END_DATE + " TEXT,"
            + KEY_TERM_TITLE + " TEXT,"
            + KEY_INSTRUCTOR_NAME + " TEXT, "
            + KEY_STATUS + " TEXT, "
            + "FOREIGN KEY(" + KEY_TERM_TITLE + ") REFERENCES " + TABLE_TERMS + "(" + KEY_TERM_TITLE + ")"
            + ")";

    private static final String CREATE_TABLE_INSTRUCTORS = "CREATE TABLE " + TABLE_INSTRUCTORS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_INSTRUCTOR_NAME + " TEXT,"
            + KEY_INSTRUCTOR_EMAIL + " TEXT,"
            + KEY_INSTRUCTOR_NUMBER + " TEXT "
            + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TERMS);
        db.execSQL(CREATE_TABLE_COURSES);
        db.execSQL(CREATE_TABLE_INSTRUCTORS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TERMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTRUCTORS);
        onCreate(db);
    }
}
