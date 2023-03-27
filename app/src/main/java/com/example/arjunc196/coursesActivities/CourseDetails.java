package com.example.arjunc196.coursesActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arjunc196.DatabaseHelper;
import com.example.arjunc196.MainActivity;
import com.example.arjunc196.R;
import com.example.arjunc196.instructorActivities.InstructorAdapter;
import com.example.arjunc196.instructorActivities.InstructorDetails;
import com.example.arjunc196.instructorActivities.InstructorList;
import com.example.arjunc196.notesActivities.AddNotes;
import com.example.arjunc196.notesActivities.NoteAdapter;
import com.example.arjunc196.notesActivities.NoteDetails;

public class CourseDetails extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView courseNameDetails;
    private TextView startDateDetails;
    private TextView endDateDetails;
    private Button addNotesButton;
    private Button deleteButton;


    private ListView instructorListView;
    private ListView noteListView;
    private InstructorAdapter instructorAdapter;
    private NoteAdapter noteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        dbHelper = new DatabaseHelper(this);
        courseNameDetails = findViewById(R.id.courseNameDetails);
        startDateDetails = findViewById(R.id.startDateDetails);
        endDateDetails = findViewById(R.id.endDateDetails);
        deleteButton = findViewById(R.id.deleteButton);
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
        String[] projection = {
                "id AS _id",
                "courseTitle",
                "courseStartDate",
                "courseEndDate"
        };
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(courseId) };
        Cursor cursor = db.query("courses", projection, selection, selectionArgs, null, null, null);
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

        deleteButton.setOnClickListener(v -> {
            // check if there are any assessments associated with this course
            SQLiteDatabase db1 = dbHelper.getReadableDatabase();
            String[] projection1 = { "id AS _id" };
            String selection1 = "courseTitle = ?";
            String[] selectionArgs1 = { courseNameDetails.getText().toString() };
            Cursor cursor1 = db1.query("assessments", projection1, selection1, selectionArgs1, null, null, null);

            if (cursor1.getCount() > 0) {
                // if there are courses with this term, show a toast message
                Toast.makeText(CourseDetails.this, "Cannot delete this course as it has an assessment associated with it", Toast.LENGTH_SHORT).show();
            } else {
                // delete the term from the database
                SQLiteDatabase db2 = dbHelper.getWritableDatabase();
                String selection2 = "id = ?";
                String[] selectionArgs2 = { String.valueOf(courseId) };
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




    }

    @Override
    protected void onResume() {
        super.onResume();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // fetch data from database and bind it to the list view
        String[] instructorProjection = {
                "id AS _id",
                "instructorName",
                "instructorEmail",
                "instructorNumber",
                "courseTitle"
        };
        String instructorSelection = "courseTitle = ?";
        String[] instructorSelectionArgs = {courseNameDetails.getText().toString()};
        Cursor instructorCursor = db.query("instructors", instructorProjection, instructorSelection, instructorSelectionArgs, null, null, null);
        instructorAdapter.swapCursor(instructorCursor);

        // fetch data from database and bind it to the list view
        String[] notesProjection = {
                "id AS _id",
                "noteTitle",
                "courseTitle"
        };
        Cursor noteCursor = db.query("notes", notesProjection, null, null, null, null, null);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, CoursesList.class);
        startActivity(intent);
        finish();
    }
}

