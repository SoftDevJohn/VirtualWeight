package com.costigan.virtualweight;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static com.costigan.virtualweight.VwFileManager.SETTINGS_FILE;

public class MainActivity extends AppCompatActivity {
    public static final String WEIGHT_RESULT = "com.costigan.virtualweight.WEIGHT_RESULT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * Called when the user taps the Weigh In button
     */
    public void changeSettings(View view) {

        //TODO: Build an input screen for these settings which will read in the existing settings
        //and then allow them to be changed and then write them back out
        //For moment, leave it hardcoded
        TextView statusTextView = findViewById(R.id.statusTextView);

        try {
            //String MY_FILE_NAME = "mytextfile.txt";

            Context ctx = getApplicationContext();
            VwFileManager fm = new VwFileManager();

            VwSettings settings = new VwSettings();
            settings.setUserName("mfpuseranme3");
            settings.setPassword("mfppw3");
            settings.setBmr(26903);
            settings.setTargetWeight(79.43);
            //fm.writeFile(ctx, SETTINGS_FILE, settings.toString());

            fm.writeSettings(ctx, SETTINGS_FILE, settings);

            statusTextView.setText("Settings updated");
        } catch (Exception ex) {
            statusTextView.setText("Ex: " + ex);

        }



    }


    /**
     * Called when the user taps the Weigh In button
     */
    public void calculateWeight(View view) {
        TextView statusTextView = findViewById(R.id.statusTextView);
        try {

            Context ctx = getApplicationContext();
            VwFileManager fm = new VwFileManager();

            //Now read from this file
            StringBuffer stringBuffer = new StringBuffer();
            fm.readFile(ctx, SETTINGS_FILE, stringBuffer);
            statusTextView.setText(stringBuffer.toString());

            //File f = getFilesDir();
            //statusTextView.setText( f.toString() );

        } catch (FileNotFoundException fofex) {
            statusTextView.setText("Mising Virtual Weight File");

        } catch (Exception ex) {
            statusTextView.setText("Ex: " + ex);

        }



    }


    /** Called when the user taps the Weigh In button */
    public void calculateWeightWORKING(View view) {
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

