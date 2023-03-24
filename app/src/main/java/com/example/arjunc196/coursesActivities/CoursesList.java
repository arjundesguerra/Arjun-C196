package com.example.arjunc196.coursesActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.arjunc196.DatabaseHelper;
import com.example.arjunc196.MainActivity;
import com.example.arjunc196.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CoursesList extends AppCompatActivity {

    private ListView courseListView;
    private DatabaseHelper dbHelper;
    private CourseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_list);

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
        courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CoursesList.this, CourseDetails.class);
                intent.putExtra("course_id", id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // fetch data from database and bind it to the list view
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                "id AS _id",
                "courseTitle",
                "courseStartDate",
                "courseEndDate",
                "termTitle",
                "instructorName",
                "status"
        };
        Cursor cursor = db.query("courses", projection, null, null, null, null, null);
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
