package com.example.a5_3_1app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class WorkoutActivity extends AppCompatActivity {

    private TextView title;
    private TextView mainWorkoutTitle;
    private TextView extraExerciseTitle;

    private CheckBox mainExerciseCheckBox1;
    private CheckBox mainExerciseCheckBox2;
    private CheckBox mainExerciseCheckBox3;
    private CheckBox mainExerciseCheckBox4;
    private CheckBox mainExerciseCheckBox5;
    private CheckBox mainExerciseCheckBox6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        // set views
        title = findViewById(R.id.workoutDayTitle);
        mainWorkoutTitle = findViewById(R.id.mainWorkoutTitle);
        extraExerciseTitle = findViewById(R.id.extraExerciseTitle);
        mainExerciseCheckBox1 = findViewById(R.id.mainExercise1);
        mainExerciseCheckBox2 = findViewById(R.id.mainExercise2);
        mainExerciseCheckBox3 = findViewById(R.id.mainExercise3);
        mainExerciseCheckBox4 = findViewById(R.id.mainExercise4);
        mainExerciseCheckBox5 = findViewById(R.id.mainExercise5);
        mainExerciseCheckBox6 = findViewById(R.id.mainExercise6);

        // get the week and day of the workout from the previous activity
        String radioBtnText = getIntent().getStringExtra("RADIO_BTN_SELECTED");
        int week = Integer.parseInt(String.valueOf(radioBtnText.charAt(5)));
        int day = Integer.parseInt(String.valueOf(radioBtnText.charAt(11)));

        // replace the placeholders with the appropriate exercise titles and set numbers
        setWorkoutTitles(week, day);
        setWorkoutNumbers(week, day);

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
        String titleText = "Week " + week + ": Day " + day;
        title.setText(titleText);

        DataBaseHelper db = new DataBaseHelper(WorkoutActivity.this);
        ExerciseModel mainExercise = db.getExerciseFromDB(day, false);
        mainWorkoutTitle.setText(mainExercise.getName());

        ExerciseModel extraExercise = db.getExerciseFromDB(day, true);
        extraExerciseTitle.setText(extraExercise.getName());
    }

    /**
     * Sets the appropriate numbers for each set depending on the week and day
     * @param week the current week of the cycle
     * @param day the current day of the cycle
     */
    private void setWorkoutNumbers(int week, int day) {
        DataBaseHelper db = new DataBaseHelper(WorkoutActivity.this);
        ExerciseModel mainExercise = db.getExerciseFromDB(day, false);

        // percentages of TM and reps for each set
        double[] percentsPerSet = {0.3, 0.4, 0.5, 0, 0, 0};
        int[] repsPerSet = {5, 5, 3, 0, 0, 0};

        // set percents and reps depending on the week (FOLLOWS 5/3/1 PROGRAM)
        switch (week) {
            case 1:
                percentsPerSet[3] = 0.65;
                percentsPerSet[4] = 0.75;
                percentsPerSet[5] = 0.85;

                repsPerSet[3] = 5;
                repsPerSet[4] = 5;
                repsPerSet[5] = 5;
                break;

            case 2:
                percentsPerSet[3] = 0.70;
                percentsPerSet[4] = 0.80;
                percentsPerSet[5] = 0.90;

                repsPerSet[3] = 3;
                repsPerSet[4] = 3;
                repsPerSet[5] = 3;
                break;

            case 3:
                percentsPerSet[3] = 0.75;
                percentsPerSet[4] = 0.85;
                percentsPerSet[5] = 0.95;

                repsPerSet[3] = 5;
                repsPerSet[4] = 3;
                repsPerSet[5] = 1;
                break;
        }

        // Calculate the percentage math (rounded to nearest fifth) and set the text
        // to all exercise checkboxes
        long set1Weight = 5 * (Math.round(percentsPerSet[0] * mainExercise.getTrainingMax() / 5));
        List<String> set1PlatesPerSide = calculatePlatesPerSide((double) set1Weight);
        String platesPerSideString;

        if (set1PlatesPerSide == null) {
            platesPerSideString = "[BAR ONLY]";
        } else {
            platesPerSideString = set1PlatesPerSide.toString();
        }

        String set1Text = repsPerSet[0] + " x " + set1Weight + " lb\t\t"  + platesPerSideString;
        mainExerciseCheckBox1.setText(set1Text);

        long set2Weight = 5 * (Math.round(percentsPerSet[1] * mainExercise.getTrainingMax() / 5));
        List<String> set2PlatesPerSide = calculatePlatesPerSide((double)set2Weight);

        if (set2PlatesPerSide == null) {
            platesPerSideString = "[BAR ONLY]";
        } else {
            platesPerSideString = set2PlatesPerSide.toString();
        }

        String set2Text = repsPerSet[1] + " x " + set2Weight  + " lb\t\t" + platesPerSideString;
        mainExerciseCheckBox2.setText(set2Text);

        long set3Weight = 5 * (Math.round(percentsPerSet[2] * mainExercise.getTrainingMax() / 5));
        List<String> set3PlatesPerSide = calculatePlatesPerSide((double)set3Weight);

        if (set2PlatesPerSide == null) {
            platesPerSideString = "[BAR ONLY]";
        } else {
            platesPerSideString = set3PlatesPerSide.toString();
        }

        String set3Text = repsPerSet[2] + " x " + set3Weight + " lb\t\t" + platesPerSideString;
        mainExerciseCheckBox3.setText(set3Text);

        long set4Weight = 5 * (Math.round(percentsPerSet[3] * mainExercise.getTrainingMax() / 5));
        List<String> set4PlatesPerSide = calculatePlatesPerSide((double)set4Weight);

        if (set2PlatesPerSide == null) {
            platesPerSideString = "[BAR ONLY]";
        } else {
            platesPerSideString = set4PlatesPerSide.toString();
        }

        String set4Text = repsPerSet[3] + " x " + set4Weight + " lb\t\t" + platesPerSideString;
        mainExerciseCheckBox4.setText(set4Text);

        long set5Weight = 5 * (Math.round(percentsPerSet[4] * mainExercise.getTrainingMax() / 5));
        List<String> set5PlatesPerSide = calculatePlatesPerSide((double)set5Weight);

        if (set2PlatesPerSide == null) {
            platesPerSideString = "[BAR ONLY]";
        } else {
            platesPerSideString = set5PlatesPerSide.toString();
        }

        String set5Text = repsPerSet[4] + " x " + set5Weight + " lb\t\t" + platesPerSideString;
        mainExerciseCheckBox5.setText(set5Text);

        long set6Weight = 5 * (Math.round(percentsPerSet[5] * mainExercise.getTrainingMax() / 5));
        List<String> set6PlatesPerSide = calculatePlatesPerSide((double)set6Weight);

        if (set2PlatesPerSide == null) {
            platesPerSideString = "[BAR ONLY]";
        } else {
            platesPerSideString = set6PlatesPerSide.toString();
        }

        String set6Text = repsPerSet[5] + " x " + set6Weight + " lb\t\t" + platesPerSideString;
        mainExerciseCheckBox6.setText(set6Text);
    }

    /**
     * Calculates and returns the amount of plates needed per side
     * @param setWeight the weight of the given set
     * @return the plates needed per side
     */
    private List<String> calculatePlatesPerSide(double setWeight) {
        List<String> platesPerSide = new ArrayList<>();

        // no plates needed
        if (setWeight <= 45) {
            return null;
        }

        // subtract the bar and get one side
        setWeight -= 45;
        setWeight /= 2;

        // calculate and add how many 45lb plates are needed
        int numOf45LbPlates = (int)(setWeight / 45);
        for (int i = 0; i < numOf45LbPlates; i++) {
            platesPerSide.add("45");
        }

        setWeight -= (numOf45LbPlates * 45);

        // calculate and add how many 35lb plates are needed
        int numOf35LbPlates = (int)(setWeight / 35);
        for (int i = 0; i < numOf35LbPlates; i++) {
            platesPerSide.add("35");
        }

        setWeight -= (numOf35LbPlates * 35);

        // calculate and add how many 25lb plates are needed
        int numOf25LbPlates = (int)(setWeight / 25);
        for (int i = 0; i < numOf25LbPlates; i++) {
            platesPerSide.add("25");
        }

        setWeight -= (numOf25LbPlates * 25);

        // calculate and add how many 10lb plates are needed
        int numOf10LbPlates = (int)(setWeight / 10);
        for (int i = 0; i < numOf10LbPlates; i++) {
            platesPerSide.add("10");
        }

        setWeight -= (numOf10LbPlates * 10);

        // calculate and add how many 5lb plates are needed
        int numOf5LbPlates = (int)(setWeight / 5);
        for (int i = 0; i < numOf5LbPlates; i++) {
            platesPerSide.add("5");
        }

        setWeight -= (numOf5LbPlates * 5);

        // calculate and add how many 2.5lb plates are needed
        int numOf2Point5LbPlates = (int)(setWeight / 2.5);
        for (int i = 0; i < numOf2Point5LbPlates; i++) {
            platesPerSide.add("2.5");
        }

        return platesPerSide;
    }
}