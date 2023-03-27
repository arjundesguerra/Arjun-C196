package com.example.arjunc196.coursesActivities;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

import com.example.arjunc196.DatabaseHelper;
import com.example.arjunc196.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddCourses extends AppCompatActivity {

    private EditText editCourseTitle;
    private Button termButton;
    private Button instructorButton;
    private Button statusButton;
    private Button startDateButton;
    private Button endDateButton;
    private Button submitButton;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Calendar startCalendar = Calendar.getInstance();
    private Calendar endCalendar = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_courses);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        editCourseTitle = findViewById(R.id.editCourseTitle);
        termButton = findViewById(R.id.termButton);
        instructorButton = findViewById(R.id.instructorButton);
        statusButton = findViewById(R.id.statusButton);
        startDateButton = findViewById(R.id.startDateButton);
        endDateButton = findViewById(R.id.endDateButton);
        submitButton = findViewById(R.id.submitButton);


        termButton.setOnClickListener(view -> {
            termPopupMenu(view);
            //hides keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });

        instructorButton.setOnClickListener(view -> {
            instructorPopupMenu(view);
            //hides keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });

        statusButton.setOnClickListener(view -> {
            // create a popup menu with the four status options
            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.getMenu().add("In Progress");
            popupMenu.getMenu().add("Completed");
            popupMenu.getMenu().add("Dropped");
            popupMenu.getMenu().add("Plan To Take");

            // set a click listener on the menu items to handle the selection
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                // set the text of the status button to the selected option
                statusButton.setText(menuItem.getTitle());
                return true;
            });

            // show the popup menu
            popupMenu.show();

            // hide the soft keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });


        editCourseTitle.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // clear the text when the EditText gains focus
                ((EditText) v).setText("");
            } else {
                // if the EditText loses focus, check if the text is empty and set it back to the original value
                if (((EditText) v).getText().toString().isEmpty()) {
                    ((EditText) v).setText("Enter a course title");
                }
            }
        });

        startDateButton.setOnClickListener(v -> {
            showDatePickerDialog(startCalendar, startDateButton);

            //hides keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        });

        endDateButton.setOnClickListener(v -> {
            showDatePickerDialog(endCalendar, endDateButton);

            //hides keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        });

        submitButton.setOnClickListener(view -> {
            // get the selected term title
            String selectedTermTitle = termButton.getText().toString();
            String status = statusButton.getText().toString();

            // get the course title, start date, and end date
            String courseTitle = editCourseTitle.getText().toString();
            String courseStartDate = startDateButton.getText().toString();
            String courseEndDate = endDateButton.getText().toString();
            String instructorName = instructorButton.getText().toString();

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // insert the course information into the database row for the selected term title
            ContentValues contentValues = new ContentValues();
            contentValues.put("courseTitle", courseTitle);
            contentValues.put("courseStartDate", courseStartDate);
            contentValues.put("courseEndDate", courseEndDate);
            contentValues.put("termTitle", selectedTermTitle);
            contentValues.put("instructorName", instructorName);
            contentValues.put("status", status);
            db.insert("courses", null, contentValues);

            // puts course title in the instructor database as well
            ContentValues instructorContentValues = new ContentValues();
            instructorContentValues.put("courseTitle", courseTitle);
            String[] args = new String[]{instructorName};
            db.update("instructors", instructorContentValues, "instructorName=?", args);

            // open the course list activity
            Intent intent = new Intent(AddCourses.this, CoursesList.class);
            startActivity(intent);
        });

    }


    private void termPopupMenu(View view) {
        // query the database for all term titles
        Cursor cursor = db.query("terms", new String[]{"termTitle"}, null, null, null, null, null);

        // create a popup menu and add menu items for each term title
        PopupMenu popupMenu = new PopupMenu(this, view);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String termTitle = cursor.getString(cursor.getColumnIndex("termTitle"));
            popupMenu.getMenu().add(termTitle);
        }

        // set a click listener on the menu items to handle the selection
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                termButton.setText(menuItem.getTitle());
                return true;
            }
        });

        popupMenu.show();

    }

    private void instructorPopupMenu(View view) {
        // query the database for all instructor names
        Cursor cursor = db.query("instructors", new String[]{"instructorName"}, null, null, null, null, null);

        // create a popup menu and add menu items for each instructor name
        PopupMenu popupMenu = new PopupMenu(this, view);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String instructorName = cursor.getString(cursor.getColumnIndex("instructorName"));
            popupMenu.getMenu().add(instructorName);
        }

        // set a click listener on the menu items to handle the selection
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                instructorButton.setText(menuItem.getTitle());
                return true;
            }
        });

        popupMenu.show();

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

        new DatePickerDialog(AddCourses.this, dateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


}
