package com.example.a5_3_1app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class OneRepMaxActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private TextInputEditText weightField;
    private TextInputEditText repsField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_rep_max);

        // grab and initialize views
        linearLayout = findViewById(R.id.LinearLayout);
        weightField = findViewById(R.id.weightField);
        repsField = findViewById(R.id.repsField);
        Button calculateButton = findViewById(R.id.calculateButton);

        // set title bar and back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Calculate One Rep Max");
        actionBar.setDisplayHomeAsUpEnabled(true);

        // calculate ORM once button is clicked
        calculateButton.setOnClickListener(v -> createORMTextView());

    }

    /**
     * Sets the calculated ORM as a text view and displays to screen
     */
    @SuppressLint({"SetTextI18n"})
    void createORMTextView() {
        double weight = Double.parseDouble(weightField.getText().toString());
        double reps = Double.parseDouble(repsField.getText().toString());
        int oneRepMax = calculateOneRepMax(weight, reps);

        TextView ormTextView;

        // if textview already exists, simply grab that view
        if (linearLayout.getChildCount() > 5) {
            ormTextView = (TextView) linearLayout.getChildAt(5);

        } else {
            ormTextView = new TextView(this);
            linearLayout.addView(ormTextView);
        }

        // add extra bottom margin to the text view (needed for landscape mode)
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 50);
        ormTextView.setLayoutParams(params);

        ormTextView.setTextSize(40);
        ormTextView.setText(oneRepMax + " lbs");
        ormTextView.setGravity(Gravity.CENTER);
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