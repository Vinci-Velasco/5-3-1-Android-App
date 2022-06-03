package com.example.a5_3_1app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class setProgramActivity extends AppCompatActivity {

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
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setAdapter(dataAdapter);

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setAdapter(dataAdapter);

        Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner3.setAdapter(dataAdapter);

        Spinner spinner4 = (Spinner) findViewById(R.id.spinner4);
        spinner4.setAdapter(dataAdapter);


        // set spinner listeners
    }
}