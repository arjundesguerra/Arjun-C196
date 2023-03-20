package com.example.arjunc196.termsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.arjunc196.DatabaseHelper;
import com.example.arjunc196.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TermsList extends AppCompatActivity {

    private ListView termListView;
    private DatabaseHelper dbHelper;
    private TermAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        dbHelper = new DatabaseHelper(this);
        termListView = findViewById(R.id.termListView);

        // go to add terms
        FloatingActionButton addTermButton = findViewById(R.id.addTermButton);
        addTermButton.setOnClickListener(view -> {
            Intent intent = new Intent(TermsList.this, AddTerms.class);
            startActivity(intent);
        });

        adapter = new TermAdapter(this, null);
        termListView.setAdapter(adapter);

        // go to term details
        termListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TermsList.this, TermDetails.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // fetch data from database and bind it to the list view
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                "id AS _id",
                "termTitle",
                "startDate",
                "endDate"
        };
        Cursor cursor = db.query("terms", projection, null, null, null, null, null);
        adapter.swapCursor(cursor);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // close the cursor to release resources
        adapter.swapCursor(null);
    }
}
