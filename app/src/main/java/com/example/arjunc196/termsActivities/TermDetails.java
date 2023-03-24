package com.example.arjunc196.termsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arjunc196.DatabaseHelper;
import com.example.arjunc196.R;

public class TermDetails extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView termTitleTextView;
    private TextView startDateTextView;
    private TextView endDateTextView;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);

        dbHelper = new DatabaseHelper(this);
        termTitleTextView = findViewById(R.id.termNameDetails);
        startDateTextView = findViewById(R.id.startDateDetails);
        endDateTextView = findViewById(R.id.endDateDetails);
        deleteButton = findViewById(R.id.deleteButton);

        // get the term ID passed through the intent
        Intent intent = getIntent();
        long termId = intent.getLongExtra("term_id", -1);

        // fetch the term from the database using the term ID
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
            // display the term details in the UI
            String termTitle = cursor.getString(cursor.getColumnIndexOrThrow("termTitle"));
            String startDate = cursor.getString(cursor.getColumnIndexOrThrow("startDate"));
            String endDate = cursor.getString(cursor.getColumnIndexOrThrow("endDate"));

            termTitleTextView.setText(termTitle);
            startDateTextView.setText(startDate);
            endDateTextView.setText(endDate);
        }
        cursor.close();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if there are any courses with this term
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                String[] projection = { "id AS _id" };
                String selection = "termTitle = ?";
                String[] selectionArgs = { termTitleTextView.getText().toString() };
                Cursor cursor = db.query("courses", projection, selection, selectionArgs, null, null, null);

                if (cursor.getCount() > 0) {
                    // if there are courses with this term, show a toast message
                    Toast.makeText(TermDetails.this, "Cannot delete this term as it has courses associated with it", Toast.LENGTH_SHORT).show();
                } else {
                    // delete the term from the database
                    SQLiteDatabase db2 = dbHelper.getWritableDatabase();
                    String selection2 = "id = ?";
                    String[] selectionArgs2 = { String.valueOf(termId) };
                    db2.delete("terms", selection2, selectionArgs2);
                    Toast.makeText(TermDetails.this, "Term deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                cursor.close();
            }
        });


    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}