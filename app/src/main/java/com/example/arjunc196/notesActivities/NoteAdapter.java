package com.example.arjunc196.notesActivities;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.arjunc196.R;


public class NoteAdapter extends CursorAdapter {

    private LayoutInflater inflater;

    public NoteAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.note_cell, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView noteTitleTextView = view.findViewById(R.id.noteTitle);
        
        String noteTitle = cursor.getString(cursor.getColumnIndexOrThrow("noteTitle"));

        noteTitleTextView.setText(noteTitle);
        
    }
}
