package com.example.a5_3_1app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class ViewDays extends AppCompatActivity {
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;
    private RadioButton radioButton5;
    private RadioButton radioButton6;
    private RadioButton radioButton7;
    private RadioButton radioButton8;
    private RadioButton radioButton9;
    private RadioButton radioButton10;
    private RadioButton radioButton11;
    private RadioButton radioButton12;

    private RadioGroup radioGroup;
    private List<RadioButton> listOfRadioButtons;

    private Button goButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_days);

        // initialize radio buttons
        radioGroup = findViewById(R.id.radioGroup);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);
        radioButton5 = findViewById(R.id.radioButton5);
        radioButton6 = findViewById(R.id.radioButton6);
        radioButton7 = findViewById(R.id.radioButton7);
        radioButton8 = findViewById(R.id.radioButton8);
        radioButton9 = findViewById(R.id.radioButton9);
        radioButton10 = findViewById(R.id.radioButton10);
        radioButton11= findViewById(R.id.radioButton11);
        radioButton12 = findViewById(R.id.radioButton12);

        // put all radio buttons in a list for convenience
        listOfRadioButtons = new ArrayList<>();
        listOfRadioButtons.add(radioButton1);
        listOfRadioButtons.add(radioButton2);
        listOfRadioButtons.add(radioButton3);
        listOfRadioButtons.add(radioButton4);
        listOfRadioButtons.add(radioButton5);
        listOfRadioButtons.add(radioButton6);
        listOfRadioButtons.add(radioButton7);
        listOfRadioButtons.add(radioButton8);
        listOfRadioButtons.add(radioButton9);
        listOfRadioButtons.add(radioButton10);
        listOfRadioButtons.add(radioButton11);
        listOfRadioButtons.add(radioButton12);

        // See if CYCLE_PROGRESS db is pre-populated and populate it if not
        DataBaseHelper db = new DataBaseHelper(ViewDays.this);
        db.populateCycleProgressTable();
        displayFinishedDays();

        goButton = findViewById(R.id.goButton);
        goButton.setOnClickListener(view -> {

            // grab the current radio button selected
            int radioButtonID = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButtonSelected = radioGroup.findViewById(radioButtonID);
            String radioBtnText = String.valueOf(radioButtonSelected.getText());

            Intent intent = new Intent(ViewDays.this,
                    WorkoutActivity.class);

            // pass which radio button was selected to WorkoutActivity
            intent.putExtra("RADIO_BTN_SELECTED", radioBtnText);
            startActivity(intent);
        });
    }

    /**
     * Get cycle progress from the db and cross any days that have already been completed
     */
    private void displayFinishedDays() {
        DataBaseHelper db = new DataBaseHelper(ViewDays.this);
        List<Boolean> allCycleProgress = db.getAllCycleProgress();

        for (int i = 0; i < 12; i++) {
            if (allCycleProgress.get(i)) {
                listOfRadioButtons.get(i).setPaintFlags
                        (listOfRadioButtons.get(i).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
    }
}