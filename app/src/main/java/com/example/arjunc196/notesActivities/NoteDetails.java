package com.example.arjunc196.notesActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arjunc196.DatabaseHelper;
import com.example.arjunc196.R;

import java.util.ArrayList;

public class NoteDetails extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView noteNameDetails;
    private TextView noteContentDetails;
    private Button shareNote;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        getSupportActionBar().setTitle("Note Details");

        dbHelper = new DatabaseHelper(this);
        noteNameDetails = findViewById(R.id.noteName);
        noteContentDetails = findViewById(R.id.noteContent);
        shareNote = findViewById(R.id.shareNote);
        deleteButton = findViewById(R.id.deleteButton);

        // get the note ID passed through the intent
        Intent intent = getIntent();
        long noteId = intent.getLongExtra("note_id", -1);


        // fetch the note from the database using the assessment ID
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<String> projection = new ArrayList<>();
        projection.add("id AS _id");
        projection.add("noteTitle");
        projection.add("noteDetails");

        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(noteId) };
        Cursor cursor = db.query("notes", projection.toArray(new String[0]), selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            // display the note details in the UI
            String noteTitle = cursor.getString(cursor.getColumnIndexOrThrow("noteTitle"));
            String noteContent = cursor.getString(cursor.getColumnIndexOrThrow("noteDetails"));

            noteNameDetails.setText(noteTitle);
            noteContentDetails.setText(noteContent);
        }
        cursor.close();


        shareNote.setOnClickListener(v -> {
            // create an intent to send an SMS
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse("sms:"));
            sendIntent.putExtra("sms_body", "Note Title: " + noteNameDetails.getText().toString() + "\n\nNote Details: " + noteContentDetails.getText().toString());

            // start the activity to send the SMS
            startActivity(sendIntent);
        });

        deleteButton.setOnClickListener(v -> {
            // delete the note from the database
            SQLiteDatabase db1 = dbHelper.getWritableDatabase();
            String selection1 = "id = ?";
            String[] selectionArgs1 = { String.valueOf(noteId) };
            db1.delete("notes", selection1, selectionArgs1);
            Toast.makeText(NoteDetails.this, "Note deleted successfully", Toast.LENGTH_SHORT).show();
            finish();
        });

    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}