package com.example.a5_3_1app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WorkoutActivity extends AppCompatActivity {

    private TextView title;
    private TextView mainWorkoutTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        title = findViewById(R.id.workoutDayTitle);
        mainWorkoutTitle = findViewById(R.id.mainWorkoutTitle);

        // get the week and day of the workout from the previous activity
        String radioBtnText = getIntent().getStringExtra("RADIO_BTN_SELECTED");
        int week = Integer.parseInt(String.valueOf(radioBtnText.charAt(5)));
        int day = Integer.parseInt(String.valueOf(radioBtnText.charAt(11)));


        setWorkoutTitles(week, day);

        Button finishWorkoutBtn = findViewById(R.id.finishWorkoutBtn);
        finishWorkoutBtn.setOnClickListener(view -> {

            // set the week and day of this cycle as completed
            DataBaseHelper db = new DataBaseHelper(WorkoutActivity.this);
            if(!db.updateCycleProgress(week, day)) {
                Toast.makeText(this, "Error with finishing workout", Toast.LENGTH_SHORT).show();

            } else {
                Intent intent = new Intent(WorkoutActivity.this,
                        ViewDays.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Sets the main title and exercise names appropriately based on the
     * week and day passed by previous view
     * @param week the current week of the cycle
     * @param day the current day of the cycle
     */
    private void setWorkoutTitles(int week, int day) {
        title.setText("Week " + week + ": Day " + day);

        DataBaseHelper db = new DataBaseHelper(WorkoutActivity.this);
        ExerciseModel mainExercise = db.getExerciseFromDB(day, false);
        mainWorkoutTitle.setText(mainExercise.getName());
    }

    private void setWorkoutNumbers(int week, int day) {

    }
}