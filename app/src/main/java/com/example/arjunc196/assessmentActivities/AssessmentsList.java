package com.example.arjunc196.assessmentActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.MenuItem;
import android.widget.ListView;

import com.example.arjunc196.DatabaseHelper;
import com.example.arjunc196.MainActivity;
import com.example.arjunc196.R;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class AssessmentsList extends AppCompatActivity {

    private ListView assessmentsListView;
    private DatabaseHelper dbHelper;
    private AssessmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);

        getSupportActionBar().setTitle("Assessments List");

        // go to add assessments
        FloatingActionButton addCourseButton = findViewById(R.id.addAssessmentButton);
        addCourseButton.setOnClickListener(view -> {
            Intent intent = new Intent(AssessmentsList.this, AddAssessments.class);
            startActivity(intent);
        });

        dbHelper = new DatabaseHelper(this);
        assessmentsListView = findViewById(R.id.assessmentsListView);

        adapter = new AssessmentAdapter(this, null);
        assessmentsListView.setAdapter(adapter);

        // go to course details
        assessmentsListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(AssessmentsList.this, AssessmentDetails.class);
            intent.putExtra("assessment_id", id);
            startActivity(intent);
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
        ArrayList<String> projection = new ArrayList<>(Arrays.asList(
                "id AS _id",
                "assessmentTitle",
                "assessmentType",
                "startDate",
                "endDate",
                "courseTitle"
        ));
        Cursor cursor = db.query("assessments", projection.toArray(new String[0]), null, null, null, null, null);
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