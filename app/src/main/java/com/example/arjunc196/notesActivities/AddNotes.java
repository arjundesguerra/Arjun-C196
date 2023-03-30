package com.example.arjunc196.notesActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

import com.example.arjunc196.DatabaseHelper;
import com.example.arjunc196.R;
import com.example.arjunc196.coursesActivities.CourseDetails;

public class AddNotes extends AppCompatActivity {

    private EditText editNoteTitle;
    private EditText editNoteDetails;
    private Button selectCourseButton;
    private Button submitButton;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        getSupportActionBar().setTitle("Add Notes");

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        editNoteTitle = findViewById(R.id.editNoteTitle);
        editNoteDetails = findViewById(R.id.editNoteDetails);
        selectCourseButton = findViewById(R.id.selectCourseButton);
        submitButton = findViewById(R.id.submitButton);

        Intent intent = getIntent();
        String courseTitle = intent.getStringExtra("course_title");

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

        selectCourseButton.setOnClickListener(view -> {
            instructorPopupMenu(view);
            //hides keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });

        submitButton.setOnClickListener(view -> {
            String noteTitle = editNoteTitle.getText().toString();
            String noteDetails = editNoteDetails.getText().toString();
            String courseTitleString = selectCourseButton.getText().toString();

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // insert the course information into the database row for the selected term title
            ContentValues contentValues = new ContentValues();
            contentValues.put("noteTitle", noteTitle);
            contentValues.put("noteDetails", noteDetails);
            contentValues.put("courseTitle", courseTitleString);
            db.insert("notes", null, contentValues);

            // open the course details activity
            finish();
        });

    }

    private void instructorPopupMenu(View view) {
        // query the database for all instructor names
        Cursor cursor = db.query("courses", new String[]{"courseTitle"}, null, null, null, null, null);

        // create a popup menu and add menu items for each instructor name
        PopupMenu popupMenu = new PopupMenu(this, view);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String courseTitle = cursor.getString(cursor.getColumnIndex("courseTitle"));
            popupMenu.getMenu().add(courseTitle);
        }

        // set a click listener on the menu items to handle the selection
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            selectCourseButton.setText(menuItem.getTitle());
            return true;
        });

        popupMenu.show();

    }

}