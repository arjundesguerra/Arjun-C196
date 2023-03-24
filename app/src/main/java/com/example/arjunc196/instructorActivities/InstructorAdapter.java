package com.example.arjunc196.instructorActivities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.arjunc196.MainActivity;
import com.example.arjunc196.R;

public class InstructorAdapter extends CursorAdapter {

    private LayoutInflater inflater;

    public InstructorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.instructor_cell, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = view.findViewById(R.id.nameText);
        TextView emailTextView = view.findViewById(R.id.emailText);
        TextView numberTextView = view.findViewById(R.id.numberText);

        String name = cursor.getString(cursor.getColumnIndexOrThrow("instructorName"));
        String email = cursor.getString(cursor.getColumnIndexOrThrow("instructorEmail"));
        String number = cursor.getString(cursor.getColumnIndexOrThrow("instructorNumber"));

        nameTextView.setText(name);
        emailTextView.setText(email);
        numberTextView.setText(number);



    }

}
