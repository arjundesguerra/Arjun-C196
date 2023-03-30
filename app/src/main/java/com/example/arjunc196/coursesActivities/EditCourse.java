package com.example.arjunc196.coursesActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arjunc196.DatabaseHelper;
import com.example.arjunc196.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditCourse extends AppCompatActivity {

    private TextView courseTitleView;
    private Button editStatus;
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
        setContentView(R.layout.activity_edit_course);

        getSupportActionBar().setTitle("Edit Course");

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        Intent intent = getIntent();
        String courseTitle = intent.getStringExtra("course_title");

        courseTitleView = findViewById(R.id.courseTitleTextView);
        courseTitleView.setText(courseTitle);

        editStatus = findViewById(R.id.statusButton);
        editStatus.setOnClickListener(view -> {
            // create a popup menu with the four status options
            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.getMenu().add("In Progress");
            popupMenu.getMenu().add("Completed");
            popupMenu.getMenu().add("Dropped");
            popupMenu.getMenu().add("Plan To Take");

            // set a click listener on the menu items to handle the selection
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                // set the text of the status button to the selected option
                editStatus.setText(menuItem.getTitle());
                return true;
            });

            // show the popup menu
            popupMenu.show();

            // hide the soft keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });

        editStartDate = findViewById(R.id.startDateButton);
        editStartDate.setOnClickListener(v -> {
            showDatePickerDialog(startCalendar, editStartDate);

            //hides keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        });

        editEndDate = findViewById(R.id.endDateButton);
        editEndDate.setOnClickListener(v -> {
            showDatePickerDialog(endCalendar, editEndDate);

            //hides keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        });

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(view -> {

            String statusText = editStatus.getText().toString();
            String startDateText = editStartDate.getText().toString();
            String endDateText = editEndDate.getText().toString();

            SQLiteDatabase db = dbHelper.getWritableDatabase();


            // update the course information into the database row for the selected course
            ContentValues contentValues = new ContentValues();
            contentValues.put("status", statusText);
            contentValues.put("courseStartDate", startDateText);
            contentValues.put("courseEndDate", endDateText);
            String whereClause = "courseTitle=?";
            String[] whereArgs = new String[]{courseTitleView.getText().toString()};
            db.update("courses", contentValues, whereClause, whereArgs);

            finish();
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

        new DatePickerDialog(EditCourse.this, dateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

}