package com.example.arjunc196.termsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.arjunc196.DatabaseHelper;
import com.example.arjunc196.MainActivity;
import com.example.arjunc196.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TermsList extends AppCompatActivity {

    private ListView termListView;
    private DatabaseHelper dbHelper;
    private TermAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_list);

        getSupportActionBar().setTitle("Term List");

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
                intent.putExtra("term_id", id);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle the back button click
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // fetch data from database and bind it to the list view
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<String> projectionList = new ArrayList<>();
        projectionList.add("id AS _id");
        projectionList.add("termTitle");
        projectionList.add("startDate");
        projectionList.add("endDate");
        String[] projection = projectionList.toArray(new String[0]);
        Cursor cursor = db.query("terms", projection, null, null, null, null, null);
        adapter.swapCursor(cursor);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // close the cursor to release resources
        adapter.swapCursor(null);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
