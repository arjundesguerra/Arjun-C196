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
        startDateButton = findViewById(R.id.startDateButton);
        endDateButton = findViewById(R.id.endDateButton);
        submitButton = findViewById(R.id.submitButton);


        termButton.setOnClickListener(view -> {
            showPopupMenu(view);
            //hides keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });

        editCourseTitle.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // clear the text when the EditText gains focus
                ((EditText) v).setText("");
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

            // get the course title, start date, and end date
            String courseTitle = editCourseTitle.getText().toString();
            String courseStartDate = startDateButton.getText().toString();
            String courseEndDate = endDateButton.getText().toString();

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // insert the course information into the database row for the selected term title
            ContentValues contentValues = new ContentValues();
            contentValues.put("courseTitle", courseTitle);
            contentValues.put("courseStartDate", courseStartDate);
            contentValues.put("courseEndDate", courseEndDate);
            contentValues.put("termTitle", selectedTermTitle);
            db.insert("courses", null, contentValues);



            // open the course list activity
            Intent intent = new Intent(AddCourses.this, CoursesList.class);
            startActivity(intent);
        });



    }


    private void showPopupMenu(View view) {
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
