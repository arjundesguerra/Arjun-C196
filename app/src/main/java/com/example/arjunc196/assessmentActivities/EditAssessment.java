package com.example.arjunc196.assessmentActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.arjunc196.DatabaseHelper;
import com.example.arjunc196.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditAssessment extends AppCompatActivity {

    private TextView assessmentTitle;
    private TextView assessmentCourse;
    private Button editType;
    private Button editStartDate;
    private Button editEndDate;
    private Button submitButton;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private Calendar startCalendar = Calendar.getInstance();
    private Calendar endCalendar = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assessment);

        getSupportActionBar().setTitle("Edit Assessments");

        assessmentTitle = findViewById(R.id.assessmentNameDetails);
        assessmentCourse = findViewById(R.id.courseNameDetails);
        editType = findViewById(R.id.editType);
        editStartDate = findViewById(R.id.editStartDate);
        editEndDate = findViewById(R.id.editEndDate);
        submitButton = findViewById(R.id.submitButton);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        Intent intent = getIntent();
        String assessmentName = intent.getStringExtra("assessment_name");
        String courseTitle = intent.getStringExtra("course_title");

        assessmentTitle.setText(assessmentName);
        assessmentCourse.setText(courseTitle);

        editType.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.getMenu().add("Objective Assessment");
            popupMenu.getMenu().add("Performance Assessment");

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                editType.setText(menuItem.getTitle());
                return true;
            });

            popupMenu.show();
        });

        editStartDate.setOnClickListener(v -> {
            showDatePickerDialog(startCalendar, editStartDate);

            //hides keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        });

        editEndDate.setOnClickListener(v -> {
            showDatePickerDialog(endCalendar, editEndDate);

            //hides keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        });

        submitButton.setOnClickListener(view -> {

            String typeText = editType.getText().toString();
            String startDateText = editStartDate.getText().toString();
            String endDateText = editEndDate.getText().toString();

            SQLiteDatabase db = dbHelper.getWritableDatabase();


            // update the course information into the database row for the selected course
            ContentValues contentValues = new ContentValues();
            contentValues.put("assessmentType", typeText);
            contentValues.put("startDate", startDateText);
            contentValues.put("endDate", endDateText);
            String whereClause = "assessmentTitle=?";
            String[] whereArgs = new String[]{assessmentTitle.getText().toString()};
            db.update("assessments", contentValues, whereClause, whereArgs);

            finish();
        });


    }

    private void showDatePickerDialog(final Calendar calendar, final Button button) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                button.setText(dateFormat.format(calendar.getTime()));
            }
        };

        new DatePickerDialog(EditAssessment.this, dateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}