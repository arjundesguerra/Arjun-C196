package com.example.arjunc196.notesActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.arjunc196.DatabaseHelper;
import com.example.arjunc196.R;
import com.example.arjunc196.coursesActivities.CourseDetails;

public class AddNotes extends AppCompatActivity {

    private EditText editNoteTitle;
    private EditText editNoteDetails;
    private Button submitButton;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        editNoteTitle = findViewById(R.id.editNoteTitle);
        editNoteDetails = findViewById(R.id.editNoteDetails);
        submitButton = findViewById(R.id.submitButton);

        editNoteTitle.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // clear the text when the EditText gains focus
                ((EditText) v).setText("");
            } else {
                // if the EditText loses focus, check if the text is empty and set it back to the original value
                if (((EditText) v).getText().toString().isEmpty()) {
                    ((EditText) v).setText("Enter a course title");
                }
            }
        });

        editNoteDetails.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // clear the text when the EditText gains focus
                ((EditText) v).setText("");
            } else {
                // if the EditText loses focus, check if the text is empty and set it back to the original value
                if (((EditText) v).getText().toString().isEmpty()) {
                    ((EditText) v).setText("Enter a course title");
                }
            }
        });

        submitButton.setOnClickListener(view -> {
            String noteTitle = editNoteTitle.getText().toString();
            String noteDetails = editNoteDetails.getText().toString();

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // insert the course information into the database row for the selected term title
            ContentValues contentValues = new ContentValues();
            contentValues.put("noteTitle", noteTitle);
            contentValues.put("noteDetails", noteDetails);
            db.insert("notes", null, contentValues);

            // open the course details activity
            finish();
        });

    }
}