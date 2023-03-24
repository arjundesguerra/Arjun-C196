package com.example.arjunc196;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.arjunc196.coursesActivities.CoursesList;
import com.example.arjunc196.instructorActivities.InstructorList;
import com.example.arjunc196.termsActivities.TermsList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button goToTerms = findViewById(R.id.goToTerms);
        goToTerms.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TermsList.class);
            startActivity(intent);
        });

        Button goToInstructors = findViewById(R.id.goToInstructors);
        goToInstructors.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, InstructorList.class);
            startActivity(intent);
        });

        Button goToMentors = findViewById(R.id.goToCourses);
        goToMentors.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CoursesList.class);
            startActivity(intent);
        });
    }
}