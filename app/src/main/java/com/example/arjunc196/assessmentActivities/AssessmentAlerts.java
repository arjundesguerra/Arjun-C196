package com.example.arjunc196.assessmentActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arjunc196.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AssessmentAlerts extends AppCompatActivity {

    private TextView startDate;
    private TextView endDate;
    private Button timeButton;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_alerts);
        getSupportActionBar().setTitle("Create Assessment Alert");

        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        timeButton = findViewById(R.id.timeButton);
        submitButton = findViewById(R.id.submitButton);

        String startDateIntent = getIntent().getStringExtra("startDate");
        String endDateIntent = getIntent().getStringExtra("endDate");

        startDate.setText(startDateIntent);
        endDate.setText(endDateIntent);

        timeButton.setOnClickListener(v -> {
            // Get current time
            final Calendar calendar = Calendar.getInstance();
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    AssessmentAlerts.this,
                    (view, hourOfDay1, minute1) -> {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay1);
                        calendar.set(Calendar.MINUTE, minute1);
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.US);
                        String selectedTime = sdf.format(calendar.getTime());
                        timeButton.setText(selectedTime);
                    },
                    hourOfDay,
                    minute,
                    false
            );
            timePickerDialog.show();
        });

        submitButton.setOnClickListener(v -> {
            if (timeButton.getText().toString().equals("Select a Time")) {
                Toast.makeText(AssessmentAlerts.this, "Please select a time", Toast.LENGTH_SHORT).show();
                return;
            }
            // Get selected time
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
            String startTimeString = startDate.getText().toString() + " " + timeButton.getText().toString();
            String endTimeString = endDate.getText().toString() + " " + timeButton.getText().toString();
            try {
                Date startTime = sdf.parse(startTimeString);
                Date endTime = sdf.parse(endTimeString);
                calendar.setTime(startTime);
                calendar.set(Calendar.SECOND, 0); // set seconds to 0
                // Schedule alert for start time
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent startIntent = new Intent(AssessmentAlerts.this, AssessmentAlertReceiver.class);
                startIntent.putExtra("message", "Alert: Assessment starts now");
                startIntent.putExtra("ringtone", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
                PendingIntent startPendingIntent = PendingIntent.getBroadcast(
                        AssessmentAlerts.this, 0, startIntent, 0);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), startPendingIntent);

                // Schedule alert for end time
                calendar.setTime(endTime);
                calendar.set(Calendar.SECOND, 0); // set seconds to 0
                Intent endIntent = new Intent(AssessmentAlerts.this, AssessmentAlertReceiver.class);
                endIntent.putExtra("message", "Alert: Assessment ends now");
                endIntent.putExtra("ringtone", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
                PendingIntent endPendingIntent = PendingIntent.getBroadcast(
                        AssessmentAlerts.this, 1, endIntent, 0);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), endPendingIntent);
                String toastMessage = "Alarm has been set for " + startTimeString + " and " + endTimeString;
                Toast.makeText(AssessmentAlerts.this, toastMessage, Toast.LENGTH_LONG).show();


            } catch (ParseException e) {
                e.printStackTrace();
            }

            finish();
        });




    }
}