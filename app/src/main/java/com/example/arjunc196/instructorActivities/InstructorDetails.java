package com.example.arjunc196.instructorActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arjunc196.DatabaseHelper;
import com.example.arjunc196.R;

public class InstructorDetails extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView numberTextView;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_details);

        dbHelper = new DatabaseHelper(this);
        nameTextView = findViewById(R.id.instructorNameDetails);
        emailTextView = findViewById(R.id.emailDetails);
        numberTextView = findViewById(R.id.numberDetails);
        deleteButton = findViewById(R.id.deleteButton);


        // get instructor id
        Intent intent = getIntent();
        long instructorID = intent.getLongExtra("instructor_id", -1);

        // fetch the instructor from the database
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
            "id AS _id",
            "instructorName",
            "instructorEmail",
            "instructorNumber"
        };

        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(instructorID) };
        Cursor cursor = db.query("instructors", projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            String instructorName = cursor.getString(cursor.getColumnIndexOrThrow("instructorName"));
            String instructorEmail = cursor.getString(cursor.getColumnIndexOrThrow("instructorEmail"));
            String instructorNumber = cursor.getString(cursor.getColumnIndexOrThrow("instructorNumber"));

            nameTextView.setText(instructorName);
            emailTextView.setText(instructorEmail);
            numberTextView.setText(instructorNumber);
        }
        cursor.close();

        deleteButton.setOnClickListener(v -> {
           // checks if there are any courses with this instructor
           SQLiteDatabase db1 = dbHelper.getReadableDatabase();
           String[] projection1 = { "id AS _id" };
           String selection1 = "instructorName = ?";
           String[] selectionArgs1 = { nameTextView.getText().toString() };
           Cursor cursor1 = db1.query("courses", projection1, selection1, selectionArgs1, null, null, null);

           if (cursor1.getCount() > 0) {
               // if there are courses with this instructor, show a toast message.
               Toast.makeText(InstructorDetails.this, "Cannot delete this instructor as there are courses associated with them", Toast.LENGTH_SHORT).show();
           } else {
               // delete the instructor from the database
               SQLiteDatabase db2 = dbHelper.getWritableDatabase();
               String selection2 = "id = ?";
               String[] selectionArgs2 = { String.valueOf(instructorID) };
               db2.delete("instructors", selection2, selectionArgs2);
               Toast.makeText(InstructorDetails.this, "Instructor deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
           }

        });

    }
}