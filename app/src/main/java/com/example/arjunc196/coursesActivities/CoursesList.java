package com.example.arjunc196.coursesActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.arjunc196.DatabaseHelper;
import com.example.arjunc196.MainActivity;
import com.example.arjunc196.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CoursesList extends AppCompatActivity {

    private ListView courseListView;
    private DatabaseHelper dbHelper;
    private CourseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_list);

        getSupportActionBar().setTitle("Courses List");

        // go to add courses
        FloatingActionButton addCourseButton = findViewById(R.id.addCourseButton);
        addCourseButton.setOnClickListener(view -> {
            Intent intent = new Intent(CoursesList.this, AddCourses.class);
            startActivity(intent);
        });

        dbHelper = new DatabaseHelper(this);
        courseListView = findViewById(R.id.coursesListView);

        adapter = new CourseAdapter(this, null);
        courseListView.setAdapter(adapter);

        // go to course details
        courseListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent courseIdIntent = new Intent(CoursesList.this, CourseDetails.class);
            courseIdIntent.putExtra("course_id", id);
            startActivity(courseIdIntent);
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
    protected void onStart() {
        super.onStart();
        // fetch data from database and bind it to the list view
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<String> projection = new ArrayList<>();
        projection.add("id AS _id");
        projection.add("courseTitle");
        projection.add("courseStartDate");
        projection.add("courseEndDate");
        projection.add("termTitle");
        projection.add("instructorName");
        projection.add("status");
        Cursor cursor = db.query("courses", projection.toArray(new String[0]), null, null, null, null, null);
        adapter.swapCursor(cursor);

    }



    @Override
    protected void onStop() {
        super.onStop();
        // close the cursor to release resources
        adapter.swapCursor(null);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
