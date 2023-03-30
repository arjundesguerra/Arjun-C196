package com.example.arjunc196.coursesActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arjunc196.DatabaseHelper;
import com.example.arjunc196.R;
import com.example.arjunc196.instructorActivities.InstructorAdapter;
import com.example.arjunc196.instructorActivities.InstructorDetails;
import com.example.arjunc196.notesActivities.AddNotes;
import com.example.arjunc196.notesActivities.NoteAdapter;
import com.example.arjunc196.notesActivities.NoteDetails;

import java.util.ArrayList;

public class CourseDetails extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView courseNameDetails;
    private TextView startDateDetails;
    private TextView endDateDetails;
    private Button editCourseButton;
    private Button addNotesButton;
    private Button alertButton;
    private Button deleteButton;


    private ListView instructorListView;
    private ListView noteListView;
    private InstructorAdapter instructorAdapter;
    private NoteAdapter noteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        getSupportActionBar().setTitle("Course Details");

        dbHelper = new DatabaseHelper(this);
        courseNameDetails = findViewById(R.id.courseNameDetails);
        startDateDetails = findViewById(R.id.startDateDetails);
        endDateDetails = findViewById(R.id.endDateDetails);
        editCourseButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);
        alertButton = findViewById(R.id.alertButton);
        addNotesButton = findViewById(R.id.addNote);

        instructorListView = findViewById(R.id.instructorListView);
        instructorAdapter = new InstructorAdapter(this, null);
        instructorListView.setAdapter(instructorAdapter);

        noteListView = findViewById(R.id.notesListView);
        noteAdapter = new NoteAdapter(this, null);
        noteListView.setAdapter(noteAdapter);


        // get the course ID passed through the intent
        Intent intent = getIntent();
        long courseId = intent.getLongExtra("course_id", -1);

        // fetch the course from the database using the course ID
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<String> projectionList = new ArrayList<>();
        projectionList.add("id AS _id");
        projectionList.add("courseTitle");
        projectionList.add("courseStartDate");
        projectionList.add("courseEndDate");

        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(courseId)};

        Cursor cursor = db.query("courses", projectionList.toArray(new String[projectionList.size()]), selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            // display the course details in the UI
            String courseTitle = cursor.getString(cursor.getColumnIndexOrThrow("courseTitle"));
            String courseStartDate = cursor.getString(cursor.getColumnIndexOrThrow("courseStartDate"));
            String courseEndDate = cursor.getString(cursor.getColumnIndexOrThrow("courseEndDate"));

            courseNameDetails.setText(courseTitle);
            startDateDetails.setText(courseStartDate);
            endDateDetails.setText(courseEndDate);
        }

        cursor.close();


        // go to instructor details
        instructorListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent instructorIntent = new Intent(CourseDetails.this, InstructorDetails.class);
            instructorIntent.putExtra("instructor_id", id);
            startActivity(instructorIntent);
        });

        // go to note details
        noteListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent noteIntent = new Intent(CourseDetails.this, NoteDetails.class);
            noteIntent.putExtra("note_id", id);
            startActivity(noteIntent);
        });

        editCourseButton.setOnClickListener(v -> {
            Intent goToEdit = new Intent(CourseDetails.this, EditCourse.class);
            goToEdit.putExtra("course_title", courseNameDetails.getText().toString());
            startActivity(goToEdit);
        });

        alertButton.setOnClickListener(v -> {
            Intent goToAlerts = new Intent(CourseDetails.this, CourseAlerts.class);
            // add start and end dates as extras
            goToAlerts.putExtra("startDate", startDateDetails.getText().toString());
            goToAlerts.putExtra("endDate", endDateDetails.getText().toString());
            startActivity(goToAlerts);
        });

        deleteButton.setOnClickListener(v -> {
            // check if there are any assessments associated with this course
            SQLiteDatabase db1 = dbHelper.getReadableDatabase();
            String[] projection1 = {"id AS _id"};
            String selection1 = "courseTitle = ?";
            String[] selectionArgs1 = {courseNameDetails.getText().toString()};
            Cursor cursor1 = db1.query("assessments", projection1, selection1, selectionArgs1, null, null, null);

            if (cursor1.getCount() > 0) {
                // if there are courses with this term, show a toast message
                Toast.makeText(CourseDetails.this, "Cannot delete this course as it has an assessment associated with it", Toast.LENGTH_SHORT).show();
            } else {
                // delete the term from the database
                SQLiteDatabase db2 = dbHelper.getWritableDatabase();
                String selection2 = "id = ?";
                String[] selectionArgs2 = {String.valueOf(courseId)};
                db2.delete("courses", selection2, selectionArgs2);
                Toast.makeText(CourseDetails.this, "Course deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
            cursor1.close();
        });

        addNotesButton.setOnClickListener(view -> {
            Intent goToNotes = new Intent(CourseDetails.this, AddNotes.class);
            startActivity(goToNotes);
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle the back button click
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // refreshes the course details
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Intent intent = getIntent();
        long courseId = intent.getLongExtra("course_id", -1);
        ArrayList<String> projectionList = new ArrayList<>();
        projectionList.add("id AS _id");
        projectionList.add("courseTitle");
        projectionList.add("courseStartDate");
        projectionList.add("courseEndDate");
        String[] projection = projectionList.toArray(new String[projectionList.size()]);
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(courseId)};
        Cursor cursor = db.query("courses", projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String courseTitle = cursor.getString(cursor.getColumnIndex("courseTitle"));
            @SuppressLint("Range") String startDate = cursor.getString(cursor.getColumnIndex("courseStartDate"));
            @SuppressLint("Range") String endDate = cursor.getString(cursor.getColumnIndex("courseEndDate"));

            // update the UI with the fetched course details
            courseNameDetails.setText(courseTitle);
            startDateDetails.setText(startDate);
            endDateDetails.setText(endDate);
        }

        // fetch instructor data from database and bind it to the list view
        ArrayList<String> instructorProjection = new ArrayList<>();
        instructorProjection.add("id AS _id");
        instructorProjection.add("instructorName");
        instructorProjection.add("instructorEmail");
        instructorProjection.add("instructorNumber");
        instructorProjection.add("courseTitle");

        String instructorSelection = "courseTitle = ?";
        String[] instructorSelectionArgs = {courseNameDetails.getText().toString()};
        Cursor instructorCursor = db.query("instructors", instructorProjection.toArray(new String[0]), instructorSelection, instructorSelectionArgs, null, null, null);
        instructorAdapter.swapCursor(instructorCursor);

        // fetch note data from database and bind it to the list view
        ArrayList<String> notesProjection = new ArrayList<>();
        notesProjection.add("id AS _id");
        notesProjection.add("noteTitle");
        notesProjection.add("courseTitle");

        String noteSelection = "courseTitle = ?";
        String[] noteSelectionArgs = {courseNameDetails.getText().toString()};

        Cursor noteCursor = db.query("notes", notesProjection.toArray(new String[0]), noteSelection, noteSelectionArgs, null, null, null);
        noteAdapter.swapCursor(noteCursor);

    }

    @Override
    protected void onStop() {
        super.onStop();
        // close the cursor to release resources
        instructorAdapter.swapCursor(null);
        noteAdapter.swapCursor(null);

    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

}
