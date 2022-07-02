package com.example.a5_3_1app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.List;

public class UpdateCycleActivity extends AppCompatActivity {

    private LinearLayout listLayout;
    private Button updateCycleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_cycle);

        // set title bar and back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Update Cycle");
        actionBar.setDisplayHomeAsUpEnabled(true);

        // initialize views
        listLayout = findViewById(R.id.listLayout);
        updateCycleButton = findViewById(R.id.updateCycleButton);

        // add checkboxes for all the exercises and add to scrollview
        addAllExercisesAsCheckBoxes();

        // update exercises once user clicks Update Cycle button
        updateSelectedExercises();
    }

    /**
     * Adds the exercises as checkboxes to the linear layout programmatically
     */
    private void addAllExercisesAsCheckBoxes() {
        DataBaseHelper db = new DataBaseHelper(this);
        List<ExerciseModel> allExercises = db.getAllExercises();

        for (ExerciseModel exercise : allExercises) {

            // set style and spacing of checkbox
            CheckBox checkBox = new CheckBox(
                    new ContextThemeWrapper(this, androidx.appcompat.R.style.Theme_AppCompat)
            );

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(0, 0, 0, 50);
            checkBox.setLayoutParams(params);
            checkBox.setText(exercise.getName());
            checkBox.setTextSize(18);
            checkBox.setPadding(80, 0, 0, 0);

            listLayout.addView(checkBox);
        }
    }

    /**
     * Sets on click listener to updateCycleButton that updates all the exercises selected, resets
     * the cycle progress and redirects user to the Set Program page
     */
    private void updateSelectedExercises() {
        updateCycleButton.setOnClickListener(view -> {

            // exercise name as key, update amount as value
            HashMap<String, Integer> updateValues = new HashMap<>();
            updateValues.put("Overhead Press", 5);
            updateValues.put("Squat", 10);
            updateValues.put("Bench Press", 5);
            updateValues.put("Deadlift", 10);
            updateValues.put("Bicep Curls", 5);
            updateValues.put("Bent Over Rows", 5);
            updateValues.put("Lunges", 10);
            updateValues.put("Calf Raises", 10);

            DataBaseHelper db = new DataBaseHelper(this);
            List<ExerciseModel> allExercises = db.getAllExercises();

            int numOfCheckBoxes = listLayout.getChildCount();

            // loop through all checkboxes and update corresponding exercise if checkbox is checked
            for (int i = 0; i < numOfCheckBoxes; i++) {
                CheckBox checkBox = (CheckBox) listLayout.getChildAt(i);
                ExerciseModel selectedExercise = null;

                // find the right exercise model from the checkbox text
                for (ExerciseModel exerciseModel : allExercises) {
                    if (exerciseModel.getName().contentEquals(checkBox.getText())) {
                        selectedExercise = exerciseModel;
                    }
                }

                // update exercise if checked
                if (checkBox.isChecked()) {
                    int updateValue = updateValues.get(selectedExercise.getName());
                    selectedExercise.setTrainingMax(selectedExercise.getTrainingMax() + updateValue);
                    db.updateExercise(selectedExercise);
                }
            }

            db.resetProgressCycle();

            // send user to Set Program page
            Intent intent = new Intent(UpdateCycleActivity.this,
                    SetProgramActivity.class);
            startActivity(intent);
        });
    }
}