package com.example.arjunc196.assessmentActivities;

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

public class AddAssessments extends AppCompatActivity {

    private EditText editAssessmentTitle;
    private Button courseButton;
    private Button typeButton;
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
        setContentView(R.layout.activity_add_assessments);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        editAssessmentTitle = findViewById(R.id.editAssessmentTitle);
        courseButton = findViewById(R.id.courseButton);
        typeButton = findViewById(R.id.typeButton);
        startDateButton = findViewById(R.id.startDateButton);
        endDateButton = findViewById(R.id.endDateButton);
        submitButton = findViewById(R.id.submitButton);

        editAssessmentTitle.setOnFocusChangeListener((v, hasFocus) -> {
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

        courseButton.setOnClickListener(view -> {
            coursePopupMenu(view);
            //hides keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });

        typeButton.setOnClickListener(view -> {
            // create a popup menu with the four status options
            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.getMenu().add("Objective Assessment");
            popupMenu.getMenu().add("Performance Assessment");

            // set a click listener on the menu items to handle the selection
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                // set the text of the status button to the selected option
                typeButton.setText(menuItem.getTitle());
                return true;
            });

            // show the popup menu
            popupMenu.show();

            // hide the soft keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

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
            String assessmentText = editAssessmentTitle.getText().toString();
            String courseText = courseButton.getText().toString();
            String typeText = typeButton.getText().toString();
            String startDate = startDateButton.getText().toString();
            String endDate = endDateButton.getText().toString();

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // insert the assessment information into the database row for the selected term title
            ContentValues contentValues = new ContentValues();
            contentValues.put("assessmentTitle", assessmentText);
            contentValues.put("courseTitle", courseText);
            contentValues.put("assessmentType", typeText);
            contentValues.put("startDate", startDate);
            contentValues.put("endDate", endDate);
            db.insert("assessments", null, contentValues);

            // open the assessments list activity
            Intent intent = new Intent(AddAssessments.this, AssessmentsList.class);
            startActivity(intent);
        });


    }


    private void coursePopupMenu(View view) {
        Cursor cursor = db.query("courses", new String[] {"courseTitle"}, null, null, null, null, null);

        PopupMenu popupMenu = new PopupMenu(this, view);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String courseTitle = cursor.getString(cursor.getColumnIndex("courseTitle"));
            popupMenu.getMenu().add(courseTitle);
        }

        // set a click listener on the menu items to handle the selection
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                courseButton.setText(menuItem.getTitle());
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

        new DatePickerDialog(AddAssessments.this, dateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

}