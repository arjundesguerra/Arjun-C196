package com.example.arjunc196.termsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import com.example.arjunc196.DatabaseHelper;
import com.example.arjunc196.R;

public class TermDetails extends AppCompatActivity {
    private TextView termNameDetails;
    private TextView startDateDetails;
    private TextView endDateDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);

        // Get the ID of the clicked item
        int termId = getIntent().getIntExtra("termId", -1);

        // Get references to the UI elements
        termNameDetails = findViewById(R.id.termNameDetails);
        startDateDetails = findViewById(R.id.startDateDetails);
        endDateDetails = findViewById(R.id.endDateDetails);

        // Retrieve the data from the database based on the ID
        SQLiteDatabase db = new DatabaseHelper(this).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM terms WHERE id = ?", new String[]{String.valueOf(termId)});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String termName = cursor.getString(cursor.getColumnIndex("termTitle"));
            @SuppressLint("Range") String startDate = cursor.getString(cursor.getColumnIndex("startDate"));
            @SuppressLint("Range") String endDate = cursor.getString(cursor.getColumnIndex("endDate"));

            // Set the text of each TextView to the corresponding data
            termNameDetails.setText(termName);
            startDateDetails.setText(startDate);
            endDateDetails.setText(endDate);
        }
        cursor.close();
        db.close();
    }
}
