package com.costigan.virtualweight;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    public static final String WEIGHT_RESULT = "com.costigan.virtualweight.WEIGHT_RESULT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps the Send button */
    public void calculateWeight(View view) {
        VirtualWeight vw = new VirtualWeight();
        vw.calcuateWeight();
        WeightResultDto dto = vw.getWeight();

        // Do something in response to button
        /*
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        String weightResult = "81.3";
        intent.putExtra(WEIGHT_RESULT, weightResult);
        startActivity(intent);
        */
        TextView weightResultTextView = findViewById(R.id.weightResultTextView);
        TextView bmrTextView = findViewById(R.id.bmrTextView);
        TextView caloriesOutTextView = findViewById(R.id.caloriesOutTextView);
        TextView caloriesInTextView = findViewById(R.id.caloriesInTextView);
        TextView netCaloriesTextView = findViewById(R.id.netCaloriesTextView);
        TextView netWeightTextView = findViewById(R.id.netWeightTextView);
        TextView statusTextView = findViewById(R.id.statusTextView);

        weightResultTextView.setText(dto.getCurrentWeightAsString());

        bmrTextView.setText(dto.getBmrSinceMidnightAsString());
        caloriesOutTextView.setText(dto.getCaloriesOutAsString());
        caloriesInTextView.setText(dto.getCaloriesInAsString());
        netCaloriesTextView.setText(dto.getNetCaloriesSinceMidnightAsString());
        netWeightTextView.setText(dto.getWeightChangeSinceMidnightAsString());



        statusTextView.setText("Finished calculation");


    }



    }

