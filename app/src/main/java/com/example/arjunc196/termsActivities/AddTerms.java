package com.example.arjunc196.termsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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

        termTitleEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // clear the text when the EditText gains focus
                ((EditText) v).setText("");
            } else {
                // if the EditText loses focus, check if the text is empty and set it back to the original value
                if (((EditText) v).getText().toString().isEmpty()) {
                    ((EditText) v).setText("Enter a term title");
                }
            }
        });


        submitButton.setOnClickListener(view -> {
            // get the entered data
            String termTitle = termTitleEditText.getText().toString();
            String startDate = dateFormat.format(startCalendar.getTime());
            String endDate = dateFormat.format(endCalendar.getTime());

            // insert the data into the database
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("termTitle", termTitle);
            values.put("startDate", startDate);
            values.put("endDate", endDate);
            db.insert("terms", null, values);

            // open the terms list activity
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
