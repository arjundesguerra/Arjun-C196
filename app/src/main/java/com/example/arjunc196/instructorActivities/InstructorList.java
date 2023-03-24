package com.example.arjunc196.instructorActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.arjunc196.R;
import com.example.arjunc196.coursesActivities.AddCourses;
import com.example.arjunc196.coursesActivities.CoursesList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class InstructorList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_list);

        // go to add instructors
        FloatingActionButton addInstructorButton = findViewById(R.id.addInstructorButton);
        addInstructorButton.setOnClickListener(view -> {
            Intent intent = new Intent(InstructorList.this, AddInstructors.class);
            startActivity(intent);
        });

    }
}