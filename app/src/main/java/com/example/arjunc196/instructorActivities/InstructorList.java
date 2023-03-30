package com.example.arjunc196.instructorActivities;

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
import com.example.arjunc196.coursesActivities.AddCourses;
import com.example.arjunc196.coursesActivities.CourseDetails;
import com.example.arjunc196.coursesActivities.CoursesList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class InstructorList extends AppCompatActivity {

    private ListView instructorListView;
    private DatabaseHelper dbHelper;
    private InstructorAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_list);

        getSupportActionBar().setTitle("Instructor List");

        // go to add instructors
        FloatingActionButton addInstructorButton = findViewById(R.id.addInstructorButton);
        addInstructorButton.setOnClickListener(view -> {
            Intent intent = new Intent(InstructorList.this, AddInstructors.class);
            startActivity(intent);
        });

        dbHelper = new DatabaseHelper(this);
        instructorListView = findViewById(R.id.instructorListView);

        adapter = new InstructorAdapter(this, null);
        instructorListView.setAdapter(adapter);

        // go to instructor details
        instructorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(InstructorList.this, InstructorDetails.class);
                intent.putExtra("instructor_id", id);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // fetch data from database and bind it to the list view
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<String> projectionList = new ArrayList<>();
        projectionList.add("id AS _id");
        projectionList.add("instructorName");
        projectionList.add("instructorEmail");
        projectionList.add("instructorNumber");
        Cursor cursor = db.query("instructors", projectionList.toArray(new String[0]), null, null, null, null, null);
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