package com.example.arjunc196.instructorActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.arjunc196.R;

public class AddInstructors extends AppCompatActivity {

    private EditText editInstructorName;
    private EditText editInstructorEmail;
    private EditText editInstructorNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_instructors);

        editInstructorName = findViewById(R.id.editInstructorName);
        editInstructorEmail = findViewById(R.id.editInstructorEmail);
        editInstructorNumber = findViewById(R.id.editInstructorNumber);

        editInstructorName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // clear the text when the EditText gains focus
                ((EditText) v).setText("");
            } else {
                // if the EditText loses focus, check if the text is empty and set it back to the original value
                if (((EditText) v).getText().toString().isEmpty()) {
                    ((EditText) v).setText("Enter instructor name");
                }
            }
        });

        editInstructorEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // clear the text when the EditText gains focus
                ((EditText) v).setText("");
            } else {
                // if the EditText loses focus, check if the text is empty and set it back to the original value
                if (((EditText) v).getText().toString().isEmpty()) {
                    ((EditText) v).setText("Enter instructor email");
                }
            }
        });

        editInstructorNumber.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // clear the text when the EditText gains focus
                ((EditText) v).setText("");
            } else {
                // if the EditText loses focus, check if the text is empty and set it back to the original value
                if (((EditText) v).getText().toString().isEmpty()) {
                    ((EditText) v).setText("Enter instructor phone number");
                }
            }
        });



    }
}