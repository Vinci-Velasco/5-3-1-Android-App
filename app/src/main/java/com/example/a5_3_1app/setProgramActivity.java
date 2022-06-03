package com.example.a5_3_1app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class setProgramActivity extends AppCompatActivity {

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

    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;
    private Spinner spinner4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_program);

        // options for the spinners
        List<String> options = new ArrayList<>();
        options.add("Bicep Curls");
        options.add("Bent Over Rows");
        options.add("Lunges");
        options.add("Calf Raises");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, options);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        // set spinners
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setAdapter(dataAdapter);

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setAdapter(dataAdapter);

        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner3.setAdapter(dataAdapter);

        spinner4 = (Spinner) findViewById(R.id.spinner4);
        spinner4.setAdapter(dataAdapter);

        // set other buttons and edit text
        ohpEditText = findViewById(R.id.ohpTM);
        squatEditText = findViewById(R.id.squatTM);
        benchEditText = findViewById(R.id.benchTM);
        deadLiftEditText = findViewById(R.id.deadliftTM);

        extraExercise1EditText = findViewById(R.id.extraExercise1EditText);
        extraExercise2EditText = findViewById(R.id.extraExercise2EditText);
        extraExercise3EditText = findViewById(R.id.extraExercise3EditText);
        extraExercise4EditText = findViewById(R.id.extraExercise4EditText);

        saveButton = findViewById(R.id.saveTMButton);

        // save to database once save button is clicked
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setProgramActivity.this,
                        MainActivity.class);
                startActivity(intent);
            }
        });
    }
}