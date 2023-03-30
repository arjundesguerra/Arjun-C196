package com.example.arjunc196.instructorActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.arjunc196.DatabaseHelper;
import com.example.arjunc196.R;
import com.example.arjunc196.coursesActivities.AddCourses;
import com.example.arjunc196.coursesActivities.CoursesList;

public class AddInstructors extends AppCompatActivity {

    private EditText editInstructorName;
    private EditText editInstructorEmail;
    private EditText editInstructorNumber;
    private Button submitButton;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_instructors);

        getSupportActionBar().setTitle("Add Instructors");

        editInstructorName = findViewById(R.id.editInstructorName);
        editInstructorEmail = findViewById(R.id.editInstructorEmail);
        editInstructorNumber = findViewById(R.id.editInstructorNumber);
        submitButton = findViewById(R.id.submitButton);

        editInstructorName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // clear the text when the EditText gains focus
                ((EditText) v).setText("");
            } else {
                // if the EditText loses focus, check if the text is empty and set it back to the original value
                if (((EditText) v).getText().toString().isEmpty()) {
                    ((EditText) v).setText("Enter instructor name");
                }
            }
        });

        editInstructorEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // clear the text when the EditText gains focus
                ((EditText) v).setText("");
            } else {
                // if the EditText loses focus, check if the text is empty and set it back to the original value
                if (((EditText) v).getText().toString().isEmpty()) {
                    ((EditText) v).setText("Enter instructor email");
                }
            }
        });

        editInstructorNumber.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // clear the text when the EditText gains focus
                ((EditText) v).setText("");
            } else {
                // if the EditText loses focus, check if the text is empty and set it back to the original value
                if (((EditText) v).getText().toString().isEmpty()) {
                    ((EditText) v).setText("Enter instructor phone number");
                }
            }
        });

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        submitButton.setOnClickListener(view -> {
            String name = editInstructorName.getText().toString();
            String email = editInstructorEmail.getText().toString();
            String number = editInstructorNumber.getText().toString();

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("instructorName", name);
            contentValues.put("instructorEmail", email);
            contentValues.put("instructorNumber", number);
            db.insert("instructors", null, contentValues);

            // open the instructor list activity
            Intent intent = new Intent(AddInstructors.this, InstructorList.class);
            startActivity(intent);

        });



    }
}