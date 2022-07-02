package com.example.a5_3_1app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class OneRepMaxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_rep_max);

        // grab and initialize views
        TextView ormTextField =     findViewById(R.id.oneRepMaxNumber);
        TextInputEditText weightField = findViewById(R.id.weightField);
        TextInputEditText repsField = findViewById(R.id.repsField);
        Button calculateButton = findViewById(R.id.calculateButton);

        // set title bar and back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Calculate One Rep Max");
        actionBar.setDisplayHomeAsUpEnabled(true);

        // calculate ORM once button is clicked
        calculateButton.setOnClickListener(v -> {
            double weight = Double.parseDouble(weightField.getText().toString());
            double reps = Double.parseDouble(repsField.getText().toString());
            int oneRepMax = calculateOneRepMax(weight, reps);
            ormTextField.setText(String.valueOf(oneRepMax + " lbs"));
        });

    }

    /**
     * Calculate on rep max of an exercise
     * @param weight weight of the exercise
     * @param reps reps accomplished
     * @return estimated one-rep max
     */
    private int calculateOneRepMax(double weight, double reps) {
        return (int) (weight / (1.0278 - 0.0278 * reps));
    }
}