package com.example.a5_3_1app;

import androidx.appcompat.app.ActionBar;
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
    private TextView mainWorkoutTitle;
    private TextView BBBWorkoutTitle;
    private TextView extraExerciseTitle;

    private CheckBox mainExerciseCheckBox1;
    private CheckBox mainExerciseCheckBox2;
    private CheckBox mainExerciseCheckBox3;
    private CheckBox mainExerciseCheckBox4;
    private CheckBox mainExerciseCheckBox5;
    private CheckBox mainExerciseCheckBox6;

    private CheckBox BBBCheckBox1;
    private CheckBox BBBCheckBox2;
    private CheckBox BBBCheckBox3;
    private CheckBox BBBCheckBox4;
    private CheckBox BBBCheckBox5;

    private CheckBox extraExerciseCheckBox1;
    private CheckBox extraExerciseCheckBox2;
    private CheckBox extraExerciseCheckBox3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        // set views
        mainWorkoutTitle = findViewById(R.id.mainWorkoutTitle);
        BBBWorkoutTitle = findViewById(R.id.BBBTitle);
        extraExerciseTitle = findViewById(R.id.extraExerciseTitle);

        mainExerciseCheckBox1 = findViewById(R.id.mainExercise1);
        mainExerciseCheckBox2 = findViewById(R.id.mainExercise2);
        mainExerciseCheckBox3 = findViewById(R.id.mainExercise3);
        mainExerciseCheckBox4 = findViewById(R.id.mainExercise4);
        mainExerciseCheckBox5 = findViewById(R.id.mainExercise5);
        mainExerciseCheckBox6 = findViewById(R.id.mainExercise6);

        BBBCheckBox1 = findViewById(R.id.BBBCheckBox1);
        BBBCheckBox2 = findViewById(R.id.BBBCheckBox2);
        BBBCheckBox3 = findViewById(R.id.BBBCheckBox3);
        BBBCheckBox4 = findViewById(R.id.BBBCheckBox4);
        BBBCheckBox5 = findViewById(R.id.BBBCheckBox5);

        extraExerciseCheckBox1 = findViewById(R.id.extraExerciseCheckBox1);
        extraExerciseCheckBox2 = findViewById(R.id.extraExerciseCheckBox2);
        extraExerciseCheckBox3 = findViewById(R.id.extraExerciseCheckBox3);

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
            if(!db.updateCycleProgress(week, day, true)) {
                Toast.makeText(this, "Error with finishing workout", Toast.LENGTH_SHORT).show();

            } else {
                Intent intent = new Intent(WorkoutActivity.this,
                        ViewDaysActivity.class);
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
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(titleText);
        actionBar.setDisplayHomeAsUpEnabled(true);

        DataBaseHelper db = new DataBaseHelper(WorkoutActivity.this);
        ExerciseModel mainExercise = db.getExerciseFromDB(day, false);
        mainWorkoutTitle.setText(mainExercise.getName());

        BBBWorkoutTitle.setText(mainExercise.getName() + " (Boring But Big)");

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

        setMainExerciseNumbers(mainExercise, percentsPerSet, repsPerSet);
        setBBBNumbers(mainExercise);

        ExerciseModel extraExercise = db.getExerciseFromDB(day, true);
        setExtraExerciseNumbers(extraExercise);
    }

    /**
     * Sets the appropriate weight numbers and reps for all sets for the main exercise
     * @param mainExercise the main exercise that needs to be set
     * @param percentsPerSet the percentages of TM for each set
     * @param repsPerSet the reps needed for each set
     */
    private void setMainExerciseNumbers(
            ExerciseModel mainExercise, double[] percentsPerSet, int[] repsPerSet) {

        long set1Weight = 5 * (Math.round(percentsPerSet[0] * mainExercise.getTrainingMax() / 5));
        List<String> set1PlatesPerSide = calculatePlatesPerSide((double) set1Weight);

        String platesPerSideString = getPlatesPerSideAsString(set1PlatesPerSide);
        String set1Text = repsPerSet[0] + " x " + set1Weight + " lb\t\t"  + platesPerSideString;
        mainExerciseCheckBox1.setText(set1Text);

        long set2Weight = 5 * (Math.round(percentsPerSet[1] * mainExercise.getTrainingMax() / 5));
        List<String> set2PlatesPerSide = calculatePlatesPerSide((double)set2Weight);

        platesPerSideString = getPlatesPerSideAsString(set2PlatesPerSide);
        String set2Text = repsPerSet[1] + " x " + set2Weight  + " lb\t\t" + platesPerSideString;
        mainExerciseCheckBox2.setText(set2Text);

        long set3Weight = 5 * (Math.round(percentsPerSet[2] * mainExercise.getTrainingMax() / 5));
        List<String> set3PlatesPerSide = calculatePlatesPerSide((double)set3Weight);

        platesPerSideString = getPlatesPerSideAsString(set3PlatesPerSide);
        String set3Text = repsPerSet[2] + " x " + set3Weight + " lb\t\t" + platesPerSideString;
        mainExerciseCheckBox3.setText(set3Text);

        long set4Weight = 5 * (Math.round(percentsPerSet[3] * mainExercise.getTrainingMax() / 5));
        List<String> set4PlatesPerSide = calculatePlatesPerSide((double)set4Weight);

        platesPerSideString = getPlatesPerSideAsString(set4PlatesPerSide);
        String set4Text = repsPerSet[3] + " x " + set4Weight + " lb\t\t" + platesPerSideString;
        mainExerciseCheckBox4.setText(set4Text);

        long set5Weight = 5 * (Math.round(percentsPerSet[4] * mainExercise.getTrainingMax() / 5));
        List<String> set5PlatesPerSide = calculatePlatesPerSide((double)set5Weight);

        platesPerSideString = getPlatesPerSideAsString(set5PlatesPerSide);
        String set5Text = repsPerSet[4] + " x " + set5Weight + " lb\t\t" + platesPerSideString;
        mainExerciseCheckBox5.setText(set5Text);

        long set6Weight = 5 * (Math.round(percentsPerSet[5] * mainExercise.getTrainingMax() / 5));
        List<String> set6PlatesPerSide = calculatePlatesPerSide((double)set6Weight);

        platesPerSideString = getPlatesPerSideAsString(set6PlatesPerSide);
        String set6Text = repsPerSet[5] + " x " + set6Weight + " lb\t\t" + platesPerSideString;
        mainExerciseCheckBox6.setText(set6Text);
    }


    /**
     * Takes a platesPerSide list and converts into an appropriate string
     * @param platesPerSide the list of strings
     * @return the platesPerSide list as a string
     */
    private String getPlatesPerSideAsString(List<String> platesPerSide) {
        String platesPerSideString;

        if (platesPerSide == null) {
            platesPerSideString = "[BAR ONLY]";
        } else {
            platesPerSideString = platesPerSide.toString();
        }

        return platesPerSideString;
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

    /**
     * Sets the appropriate weight numbers and reps for all sets for the BORING BUT BIG
     * section
     * @param mainExercise the main exercise that needs to be set
     */
    private void setBBBNumbers(ExerciseModel mainExercise) {
        long set1Weight = 5 * (Math.round(0.5 * mainExercise.getTrainingMax() / 5));
        List<String> set1PlatesPerSide = calculatePlatesPerSide((double) set1Weight);

        String platesPerSideString = getPlatesPerSideAsString(set1PlatesPerSide);
        String set1Text = "10" + " x " + set1Weight + " lb\t\t"  + platesPerSideString;
        BBBCheckBox1.setText(set1Text);

        long set2Weight = 5 * (Math.round(0.5 * mainExercise.getTrainingMax() / 5));
        List<String> set2PlatesPerSide = calculatePlatesPerSide((double)set2Weight);

        platesPerSideString = getPlatesPerSideAsString(set2PlatesPerSide);
        String set2Text = "10" + " x " + set2Weight  + " lb\t\t" + platesPerSideString;
        BBBCheckBox2.setText(set2Text);

        long set3Weight = 5 * (Math.round(0.5 * mainExercise.getTrainingMax() / 5));
        List<String> set3PlatesPerSide = calculatePlatesPerSide((double)set3Weight);

        platesPerSideString = getPlatesPerSideAsString(set3PlatesPerSide);
        String set3Text = "10" + " x " + set3Weight + " lb\t\t" + platesPerSideString;
        BBBCheckBox3.setText(set3Text);

        long set4Weight = 5 * (Math.round(0.5 * mainExercise.getTrainingMax() / 5));
        List<String> set4PlatesPerSide = calculatePlatesPerSide((double)set4Weight);

        platesPerSideString = getPlatesPerSideAsString(set4PlatesPerSide);
        String set4Text = "10" + " x " + set4Weight + " lb\t\t" + platesPerSideString;
        BBBCheckBox4.setText(set4Text);

        long set5Weight = 5 * (Math.round(0.5 * mainExercise.getTrainingMax() / 5));
        List<String> set5PlatesPerSide = calculatePlatesPerSide((double)set5Weight);

        platesPerSideString = getPlatesPerSideAsString(set5PlatesPerSide);
        String set5Text = "10" + " x " + set4Weight + " lb\t\t" + platesPerSideString;
        BBBCheckBox5.setText(set5Text);
    }

    /**
     * Sets the appropriate weight numbers and reps for all sets for the extra exercise
     * @param extraExercise the extra exercise that needs to be set
     */
    private void setExtraExerciseNumbers(
            ExerciseModel extraExercise) {

        long set1Weight = 5 * (Math.round(0.75 * extraExercise.getTrainingMax() / 5));
        List<String> set1PlatesPerSide = calculatePlatesPerSide((double) set1Weight);

        String platesPerSideString = getPlatesPerSideAsString(set1PlatesPerSide);
        String set1Text = "10" + " x " + set1Weight + " lb\t\t"  + platesPerSideString;
        extraExerciseCheckBox1.setText(set1Text);

        long set2Weight = 5 * (Math.round(0.65 * extraExercise.getTrainingMax() / 5));
        List<String> set2PlatesPerSide = calculatePlatesPerSide((double)set2Weight);

        platesPerSideString = getPlatesPerSideAsString(set2PlatesPerSide);
        String set2Text = "8" + " x " + set2Weight  + " lb\t\t" + platesPerSideString;
        extraExerciseCheckBox2.setText(set2Text);

        long set3Weight = 5 * (Math.round(0.55 * extraExercise.getTrainingMax() / 5));
        List<String> set3PlatesPerSide = calculatePlatesPerSide((double)set3Weight);

        platesPerSideString = getPlatesPerSideAsString(set3PlatesPerSide);
        String set3Text = "5" + " x " + set3Weight + " lb\t\t" + platesPerSideString;
        extraExerciseCheckBox3.setText(set3Text);
    }
}