package com.example.arjunc196.termsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import com.example.arjunc196.DatabaseHelper;
import com.example.arjunc196.R;

public class TermDetails extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView termTitleTextView;
    private TextView startDateTextView;
    private TextView endDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);

        dbHelper = new DatabaseHelper(this);
        termTitleTextView = findViewById(R.id.termNameDetails);
        startDateTextView = findViewById(R.id.startDateDetails);
        endDateTextView = findViewById(R.id.endDateDetails);

        // Get the term ID passed through the intent
        Intent intent = getIntent();
        long termId = intent.getLongExtra("term_id", -1);

        // Fetch the term from the database using the term ID
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                "id AS _id",
                "termTitle",
                "startDate",
                "endDate"
        };
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(termId) };
        Cursor cursor = db.query("terms", projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            // Display the term details in the UI
            String termTitle = cursor.getString(cursor.getColumnIndexOrThrow("termTitle"));
            String startDate = cursor.getString(cursor.getColumnIndexOrThrow("startDate"));
            String endDate = cursor.getString(cursor.getColumnIndexOrThrow("endDate"));

            termTitleTextView.setText(termTitle);
            startDateTextView.setText(startDate);
            endDateTextView.setText(endDate);
        }
        cursor.close();
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}