package com.example.arjunc196.termsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.example.arjunc196.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Terms extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        FloatingActionButton addTermButton = findViewById(R.id.addTermButton);
        addTermButton.setOnClickListener(view -> {
            Intent intent = new Intent(Terms.this, addTerms.class);
            startActivity(intent);
        });

    }

}