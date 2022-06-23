package com.example.a5_3_1app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SetProgramActivity extends AppCompatActivity {

    // references to buttons and other control
    private Button saveButton;
    private EditText ohpEditText;
    private EditText squatEditText;
    private EditText benchEditText;
    private EditText deadLiftEditText;

    private EditText extraExercise1EditText;
    private EditText extraExercise2EditText;
    private EditText extraExercise3EditText;
    private EditText extraExercise4EditText;

    List<String> exerciseSpinnerOptions;
    List<String> templateSpinnerOptions;
    private Spinner templateSpinner;
    private Spinner exerciseSpinner1;
    private Spinner exerciseSpinner2;
    private Spinner exerciseSpinner3;
    private Spinner exerciseSpinner4;

    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_program);

        // initialize all views
        extraExercise1EditText = findViewById(R.id.extraExercise1EditText);
        extraExercise2EditText = findViewById(R.id.extraExercise2EditText);
        extraExercise3EditText = findViewById(R.id.extraExercise3EditText);
        extraExercise4EditText = findViewById(R.id.extraExercise4EditText);

        ohpEditText = findViewById(R.id.ohpTM);
        squatEditText = findViewById(R.id.squatTM);
        benchEditText = findViewById(R.id.benchTM);
        deadLiftEditText = findViewById(R.id.deadliftTM);

        saveButton = findViewById(R.id.saveTMButton);

        templateSpinner = findViewById(R.id.templateSpinner);
        exerciseSpinner1 = findViewById(R.id.spinner1);
        exerciseSpinner2 = findViewById(R.id.spinner2);
        exerciseSpinner3 = findViewById(R.id.spinner3);
        exerciseSpinner4 = findViewById(R.id.spinner4);

        // set title bar and back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Set Program");
        actionBar.setDisplayHomeAsUpEnabled(true);

        // initialize db
        dataBaseHelper = new DataBaseHelper(SetProgramActivity.this);

        setAllSpinners();
        setInitialEditTextValues();

        saveButton.setOnClickListener(view -> {

            // all input fields have been filled
            if (allInputFieldsHaveValues()) {

                ExerciseModel exerciseModel1 = new ExerciseModel(
                        "Overhead Press", Integer.parseInt(ohpEditText.getText().toString()),
                        1, false
                );

                ExerciseModel exerciseModel2 = new ExerciseModel(
                        "Squat", Integer.parseInt(squatEditText.getText().toString()),
                        2, false
                );

                ExerciseModel exerciseModel3 = new ExerciseModel(
                        "Bench Press", Integer.parseInt(benchEditText.getText().toString()),
                        3, false
                );

                ExerciseModel exerciseModel4 = new ExerciseModel(
                        "Deadlift", Integer.parseInt(deadLiftEditText.getText().toString()),
                        4, false
                );

                ExerciseModel extraExerciseModel1 = new ExerciseModel(
                        exerciseSpinner1.getSelectedItem().toString(),
                        Integer.parseInt(extraExercise1EditText.getText().toString()),
                        1, true
                );

                ExerciseModel extraExerciseModel2 = new ExerciseModel(
                        exerciseSpinner2.getSelectedItem().toString(),
                        Integer.parseInt(extraExercise2EditText.getText().toString()),
                        2, true
                );

                ExerciseModel extraExerciseModel3 = new ExerciseModel(
                        exerciseSpinner3.getSelectedItem().toString(),
                        Integer.parseInt(extraExercise3EditText.getText().toString()),
                        3, true
                );

                ExerciseModel extraExerciseModel4 = new ExerciseModel(
                        exerciseSpinner4.getSelectedItem().toString(),
                        Integer.parseInt(extraExercise4EditText.getText().toString()),
                        4, true
                );

                // add/update all exercises to the database
                dataBaseHelper.addOrUpdateExercise(exerciseModel1);
                dataBaseHelper.addOrUpdateExercise(exerciseModel2);
                dataBaseHelper.addOrUpdateExercise(exerciseModel3);
                dataBaseHelper.addOrUpdateExercise(exerciseModel4);
                dataBaseHelper.addOrUpdateExercise(extraExerciseModel1);
                dataBaseHelper.addOrUpdateExercise(extraExerciseModel2);
                dataBaseHelper.addOrUpdateExercise(extraExerciseModel3);
                dataBaseHelper.addOrUpdateExercise(extraExerciseModel4);


                Intent intent = new Intent(SetProgramActivity.this,
                        MainActivity.class);
                startActivity(intent);

            // Not all text fields have been filled
            } else {

                Toast toast = Toast.makeText(
                        SetProgramActivity.this,
                        "All training max's must be inputted before saving!",
                        Toast.LENGTH_SHORT);
                toast.show();
            }

        });
    }

    /**
     * Set all the spinners with their appropriate options, as well as setting the default value
     */
    private void setAllSpinners() {
        // options for the 5/3/1 template spinners
        templateSpinnerOptions = new ArrayList<>();
        templateSpinnerOptions.add("Boring But Big");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, templateSpinnerOptions);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        // set template spinner
        templateSpinner.setAdapter(dataAdapter);

        // options for the exercise spinners
        exerciseSpinnerOptions = new ArrayList<>();
        exerciseSpinnerOptions.add("Bicep Curls");
        exerciseSpinnerOptions.add("Bent Over Rows");
        exerciseSpinnerOptions.add("Lunges");
        exerciseSpinnerOptions.add("Calf Raises");

        dataAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, exerciseSpinnerOptions);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        // set exercise spinners
        exerciseSpinner1.setAdapter(dataAdapter);
        exerciseSpinner2.setAdapter(dataAdapter);
        exerciseSpinner3.setAdapter(dataAdapter);
        exerciseSpinner4.setAdapter(dataAdapter);
    }

    /**
     * Grab the training maxes from the db and set that to the default value of all the
     * edittext fields for the user to see.
     */
    private void setInitialEditTextValues() {
        List<ExerciseModel> allExercises = dataBaseHelper.getAllExercises();


        // all exercises is either empty or has all 8 exercises in the db
        if (!allExercises.isEmpty()) {

            // set all editText fields to the training maxes saved in db
            ohpEditText.setText(String.valueOf(allExercises.get(0).getTrainingMax()));
            squatEditText.setText(String.valueOf(allExercises.get(1).getTrainingMax()));
            benchEditText.setText(String.valueOf(allExercises.get(2).getTrainingMax()));
            deadLiftEditText.setText(String.valueOf(allExercises.get(3).getTrainingMax()));
            extraExercise1EditText.setText(String.valueOf(allExercises.get(4).getTrainingMax()));
            extraExercise2EditText.setText(String.valueOf(allExercises.get(5).getTrainingMax()));
            extraExercise3EditText.setText(String.valueOf(allExercises.get(6).getTrainingMax()));
            extraExercise4EditText.setText(String.valueOf(allExercises.get(7).getTrainingMax()));

            // set spinner values to the name that was saved in the db
            exerciseSpinner1.setSelection(exerciseSpinnerOptions.indexOf(allExercises.get(4).getName()));
            exerciseSpinner2.setSelection(exerciseSpinnerOptions.indexOf(allExercises.get(5).getName()));
            exerciseSpinner3.setSelection(exerciseSpinnerOptions.indexOf(allExercises.get(6).getName()));
            exerciseSpinner4.setSelection(exerciseSpinnerOptions.indexOf(allExercises.get(7).getName()));
        }
    }

    /**
     * Class that checks if all editText and spinners are set before hitting save
     * @return true if all editText and spinners have a value, false otherwise
     */
    private boolean allInputFieldsHaveValues() {

        if (TextUtils.isEmpty(ohpEditText.getText())) {
            return false;
        } else if (TextUtils.isEmpty(squatEditText.getText())) {
            return false;
        } else if (TextUtils.isEmpty(benchEditText.getText())) {
            return false;
        } else if (TextUtils.isEmpty(deadLiftEditText.getText())) {
            return false;
        } else if (TextUtils.isEmpty(extraExercise1EditText.getText())) {
            return false;
        } else if (TextUtils.isEmpty(extraExercise2EditText.getText())) {
            return false;
        } else if (TextUtils.isEmpty(extraExercise3EditText.getText())) {
            return false;
        } else return !TextUtils.isEmpty(extraExercise4EditText.getText());
    }
}