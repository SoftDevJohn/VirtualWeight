package com.costigan.virtualweight;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Context;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

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

       //Network operations should be running in a seperate thread
        //But this hack will allows us to develop and test it here
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //End Hack

        MfpScreenScraper mpfSc = new MfpScreenScraper();
        mpfSc.screenScrape( statusTextView );

    }



    /**
     * Called when the user taps the Weigh In button
     */
    public void changeSettingsWORKS(View view) {

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
    public void TESTINGcalculateWeight(View view) {
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
    public void calculateWeight(View view) {
        TextView statusTextView = findViewById(R.id.statusTextView);

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
        //TextView statusTextView = findViewById(R.id.statusTextView);

        //Network operations should be running in a seperate thread
        //But this hack will allows us to develop and test it here
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //End Hack
        MfpScreenScraper mpfSc = new MfpScreenScraper();
        try {
            TodaysCalories tc = mpfSc.getCaloriesForToday();
            caloriesInTextView.setText( String.valueOf( tc.getCaloriesIn()) );
            caloriesOutTextView.setText( String.valueOf( tc.getCaloriesOut()) );
            netCaloriesTextView.setText( String.valueOf( tc.getNetCalories() )  );
            netWeightTextView.setText( String.valueOf( (double)tc.getNetCalories() / (double)7700 ) );
            double currentWeight = 85.1 + (( tc.getCaloriesIn()-tc.getCaloriesOut()-dto.getBmrSinceMidnight()))/7700;;
            weightResultTextView.setText( String.valueOf( currentWeight) );

            statusTextView.setText("TC="+tc);

        }catch(Exception ex){
            statusTextView.setText("Ex="+ex);
        }




        bmrTextView.setText(dto.getBmrSinceMidnightAsString());





    }


    double calcuateWeight(int weight, double bmr, int in, int out){
        return weight + (in-out-bmr)/7700;

    }


    }

