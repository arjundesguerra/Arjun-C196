package com.example.arjunc196.assessmentActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arjunc196.DatabaseHelper;
import com.example.arjunc196.R;
import com.example.arjunc196.coursesActivities.CourseAlerts;
import com.example.arjunc196.coursesActivities.CourseDetails;
import com.example.arjunc196.coursesActivities.EditCourse;

public class AssessmentDetails extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView assessmentNameDetails;
    private TextView courseNameDetails;
    private TextView assessmentTypeDetails;
    private TextView startDateDetails;
    private TextView endDateDetails;
    private Button editButton;
    private Button alertButton;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);

        dbHelper = new DatabaseHelper(this);
        assessmentNameDetails = findViewById(R.id.assessmentNameDetails);
        courseNameDetails = findViewById(R.id.courseNameDetails);
        assessmentTypeDetails = findViewById(R.id.assessmentTypeDetails);
        startDateDetails = findViewById(R.id.startDateDetails);
        endDateDetails = findViewById(R.id.endDateDetails);
        editButton = findViewById(R.id.editButton);
        alertButton = findViewById(R.id.alertButton);
        deleteButton = findViewById(R.id.deleteButton);

        // get the assessment ID passed through the intent
        Intent intent = getIntent();
        long assessmentId = intent.getLongExtra("assessment_id", -1);

        // fetch the assessment from the database using the assessment ID
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                "id AS _id",
                "assessmentTitle",
                "assessmentType",
                "startDate",
                "endDate",
                "courseTitle"
        };
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(assessmentId) };
        Cursor cursor = db.query("assessments", projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            // display the assessment details in the UI
            String assessmentName = cursor.getString(cursor.getColumnIndexOrThrow("assessmentTitle"));
            String courseTitle = cursor.getString(cursor.getColumnIndexOrThrow("courseTitle"));
            String assesmentType = cursor.getString(cursor.getColumnIndexOrThrow("assessmentType"));
            String startDate = cursor.getString(cursor.getColumnIndexOrThrow("startDate"));
            String endDate = cursor.getString(cursor.getColumnIndexOrThrow("endDate"));

            assessmentNameDetails.setText(assessmentName);
            courseNameDetails.setText(courseTitle);
            assessmentTypeDetails.setText(assesmentType);
            startDateDetails.setText(startDate);
            endDateDetails.setText(endDate);
        }
        cursor.close();

        editButton.setOnClickListener(v -> {
            Intent goToEdit = new Intent(AssessmentDetails.this, EditAssessment.class);
            goToEdit.putExtra("assessment_name", assessmentNameDetails.getText().toString());
            goToEdit.putExtra("course_title", courseNameDetails.getText().toString());
            startActivity(goToEdit);
        });

        deleteButton.setOnClickListener(v -> {
            // delete the assessment from the database
            SQLiteDatabase db1 = dbHelper.getWritableDatabase();
            String selection1 = "id = ?";
            String[] selectionArgs1 = { String.valueOf(assessmentId) };
            db1.delete("assessments", selection1, selectionArgs1);
            Toast.makeText(AssessmentDetails.this, "Assessment deleted successfully", Toast.LENGTH_SHORT).show();
            finish();
        });

        alertButton.setOnClickListener(v -> {
            Intent goToAlerts = new Intent(AssessmentDetails.this, AssessmentAlerts.class);
            // add start and end dates as extras
            goToAlerts.putExtra("startDate", startDateDetails.getText().toString());
            goToAlerts.putExtra("endDate", endDateDetails.getText().toString());
            startActivity(goToAlerts);
        });


    }

    protected void onResume() {
        // refreshes details page
        super.onResume();
        Intent intent = getIntent();
        long assessmentId = intent.getLongExtra("assessment_id", -1);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                "id AS _id",
                "assessmentTitle",
                "assessmentType",
                "startDate",
                "endDate",
                "courseTitle"
        };
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(assessmentId)};
        Cursor cursor = db.query("assessments", projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            String assessmentName = cursor.getString(cursor.getColumnIndexOrThrow("assessmentTitle"));
            String courseTitle = cursor.getString(cursor.getColumnIndexOrThrow("courseTitle"));
            String assesmentType = cursor.getString(cursor.getColumnIndexOrThrow("assessmentType"));
            String startDate = cursor.getString(cursor.getColumnIndexOrThrow("startDate"));
            String endDate = cursor.getString(cursor.getColumnIndexOrThrow("endDate"));

            assessmentNameDetails.setText(assessmentName);
            courseNameDetails.setText(courseTitle);
            assessmentTypeDetails.setText(assesmentType);
            startDateDetails.setText(startDate);
            endDateDetails.setText(endDate);
        }
        cursor.close();
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}