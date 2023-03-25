package com.example.arjunc196.assessmentActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.arjunc196.MainActivity;
import com.example.arjunc196.R;
import com.example.arjunc196.coursesActivities.AddCourses;
import com.example.arjunc196.coursesActivities.CoursesList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AssessmentsList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);

        // go to add assessments
        FloatingActionButton addCourseButton = findViewById(R.id.addAssessmentButton);
        addCourseButton.setOnClickListener(view -> {
            Intent intent = new Intent(AssessmentsList.this, AddAssessments.class);
            startActivity(intent);
        });
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}