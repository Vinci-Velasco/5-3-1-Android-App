package com.example.a5_3_1app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class TrainingHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_history);

        // set title bar and back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Training History");
        actionBar.setDisplayHomeAsUpEnabled(true);

        DataBaseHelper db = new DataBaseHelper(TrainingHistoryActivity.this);
        List<CycleModel> cycles = db.getTrainingHistory();

        TextView textView = findViewById(R.id.test);

        if (cycles.isEmpty()) {
            textView.setText("EMPTY");

        } else {
            textView.setText("SUCCESS :)");
        }
    }
}