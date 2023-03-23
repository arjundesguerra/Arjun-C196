package com.example.arjunc196.coursesActivities;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.arjunc196.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CourseAdapter extends CursorAdapter {

    private LayoutInflater inflater;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    public CourseAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.course_cell, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView courseTitleTextView = view.findViewById(R.id.titleText);
        TextView startDateTextView = view.findViewById(R.id.startText);
        TextView endDateTextView = view.findViewById(R.id.endText);
        TextView termTitleView = view.findViewById(R.id.termText);


        String courseTitle = cursor.getString(cursor.getColumnIndexOrThrow("courseTitle"));
        String startDateString = cursor.getString(cursor.getColumnIndexOrThrow("courseStartDate"));
        String endDateString = cursor.getString(cursor.getColumnIndexOrThrow("courseEndDate"));
        String termTitle = cursor.getString(cursor.getColumnIndexOrThrow("termTitle"));

        courseTitleTextView.setText(courseTitle);
        termTitleView.setText(termTitle);


        try {
            // Parse the startDate and endDate strings into Date objects
            Date startDate = dateFormat.parse(startDateString);
            Date endDate = dateFormat.parse(endDateString);

            // Format the Date objects into strings and set the text of the TextViews
            startDateTextView.setText(dateFormat.format(startDate));
            endDateTextView.setText(dateFormat.format(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    private void setDateText(TextView textView, String dateString) {
        try {
            // Parse the dateString into a Date object
            Date date = dateFormat.parse(dateString);
            // Format the Date object into a string and set the text of the TextView
            textView.setText(dateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
