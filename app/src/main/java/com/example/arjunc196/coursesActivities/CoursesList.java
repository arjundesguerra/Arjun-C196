
package com.example.arjunc196.coursesActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.arjunc196.MainActivity;
import com.example.arjunc196.R;
import com.example.arjunc196.termsActivities.AddTerms;
import com.example.arjunc196.termsActivities.TermsList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CoursesList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_list);

        // go to add courses
        FloatingActionButton addTermButton = findViewById(R.id.addCourseButton);
        addTermButton.setOnClickListener(view -> {
            Intent intent = new Intent(CoursesList.this, AddCourses.class);
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