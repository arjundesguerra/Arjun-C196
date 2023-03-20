package com.example.arjunc196.termsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.arjunc196.R;
import com.example.arjunc196.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTerms extends AppCompatActivity {

    private EditText termTitleEditText;
    private Button startDateButton;
    private Button endDateButton;
    private Button submitButton;
    private Calendar startCalendar = Calendar.getInstance();
    private Calendar endCalendar = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_terms);

        dbHelper = new DatabaseHelper(this);

        termTitleEditText = findViewById(R.id.editTermTitle);
        startDateButton = findViewById(R.id.startDateButton);
        endDateButton = findViewById(R.id.endDateButton);
        submitButton = findViewById(R.id.submitButton);

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(startCalendar, startDateButton);
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(endCalendar, endDateButton);
            }
        });


        submitButton.setOnClickListener(view -> {
            // Get the entered data
            String termTitle = termTitleEditText.getText().toString();
            String startDate = dateFormat.format(startCalendar.getTime());
            String endDate = dateFormat.format(endCalendar.getTime());

            // Insert the data into the database
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("termTitle", termTitle);
            values.put("startDate", startDate);
            values.put("endDate", endDate);
            db.insert("terms", null, values);

            // Open the terms list activity
            Intent intent = new Intent(AddTerms.this, TermsList.class);
            startActivity(intent);
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

        new DatePickerDialog(AddTerms.this, dateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}