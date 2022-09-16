package com.example.a5_3_1app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // JUST USED FOR TESTING - DELETE LATER
        // DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
        // this.deleteDatabase("exercise.db");

        Button startWorkoutButton = (Button) findViewById(R.id.startWorkoutButton);
        startWorkoutButton.setOnClickListener(v -> {

            DataBaseHelper db = new DataBaseHelper(MainActivity.this);

            if (db.getAllExercises().size() == 8) {
                Intent intent = new Intent(MainActivity.this,
                        ViewDaysActivity.class);
                startActivity(intent);
            } else {
                Toast toast = Toast.makeText(
                        MainActivity.this,
                        "Please set your training maxes first by clicking Set Program",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });


        Button setProgramButton = (Button) findViewById(R.id.setProgramButton);
        setProgramButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    SetProgramActivity.class);
            startActivity(intent);
        });

        Button oneRepMaxButton = findViewById(R.id.oneRepCalcButton);
        oneRepMaxButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    OneRepMaxActivity.class);
            startActivity(intent);
        });
    }
}