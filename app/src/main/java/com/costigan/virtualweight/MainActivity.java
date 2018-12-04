package com.costigan.virtualweight;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Context;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
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

        //Open the Settings Screem
        Intent intent = new Intent(this, DisplaySettingsActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        Context ctx = getApplicationContext();
        VwFileManager fm = new VwFileManager();
        try {
            //String MY_FILE_NAME = "mytextfile.txt";


            VwSettings settings = new VwSettings();
            //settings.setUserName("MyUsrName");
            fm.getSettingsFromFile(ctx, SETTINGS_FILE, settings);

            String message = "My message";
            intent.putExtra("VW_SETTINGS", settings);
            //startActivity(intent);
            startActivityForResult(intent, 1);
        } catch (FileNotFoundException fnfex) {
            ///Create a default configuration file
            VwSettings settings = new VwSettings();
            settings.setUserName("mfpuseranme");
            settings.setPassword("mfppw");
            settings.setBmr(2000);

            //The last official way-in, on subsequent days the weight is calculated based on caloroes
            LocalDate startDate = org.joda.time.LocalDate.parse("2018-01-01", TodaysCalories.DATE_FORMATTER);
            settings.setStartDate(startDate);
            settings.setStartWeight(90.0);
            settings.setTargetWeight(80.0);
            //fm.writeFile(ctx, SETTINGS_FILE, settings.toString());
            try {
                fm.writeSettings(ctx, SETTINGS_FILE, settings);
            } catch (Exception ex) {
                TextView statusTextView = findViewById(R.id.statusTextView);
                statusTextView.setText("Unable to create the default configuration file: " + ex);
            }
            TextView statusTextView = findViewById(R.id.statusTextView);
            statusTextView.setText("Default configuration file created. Press Settings again.");

        } catch (Exception ex) {
            TextView statusTextView = findViewById(R.id.statusTextView);
            statusTextView.setText("Ex: " + ex);

        }


    }

    //Handle the results from the Update settings dialog
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        TextView statusTextView = findViewById(R.id.statusTextView);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                //String result=intent.getStringExtra("result");
                VwSettings settings = (VwSettings) intent.getSerializableExtra("VW_SETTINGS_RETURNED");
                statusTextView.setText("Settings are:" + settings.toString());
                Context ctx = getApplicationContext();
                VwFileManager fm = new VwFileManager();
                try {
                    fm.writeSettings(ctx, SETTINGS_FILE, settings);
                } catch (Exception ex) {
                    statusTextView.setText("Ex: " + ex);
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                statusTextView.setText("Cancelled");
            }
        }
    }//onActivityResult

    //getOverweightMessage(80.0,83.5,bmr
    private void setOverweightMessage(TextView owTv, TextView rTv, double currentWeight, double targetWeight, double bmr) {
        String fs;

        double overweight = currentWeight - targetWeight;

        double overweightCalories = overweight * 7700;
        double overweightPercentage = (overweight / targetWeight) * 100;
        double maxSafeWeightLoss = 750; //kcal = 0.7 kg / week = 1.7 lbs week
        double days = overweightCalories / maxSafeWeightLoss;
        double fastingDays = overweightCalories / bmr;


        DateTime now = DateTime.now();
        DateTime dt = now.plusDays((int) days);


        String overWeightMsg = String.format("%.2f kg (%.0f kcal or %.0f %%)", Math.abs(overweight), Math.abs(overweightCalories), Math.abs(overweightPercentage));


        if (overweight > 0) {
            owTv.setText("You are overweight by: " + overWeightMsg);
            owTv.setTextColor(Color.RED);

            String safeDateToTarget = now.plusDays((int) days).toString("dd/MMM/YYYY");
            String fastDateToTarget = now.plusDays((int) fastingDays).toString("dd/MMM/YYYY HH:MM");
            String recommendationMsg = String.format("To reach target, you need to:\n\t1) safely lose weight for %.0f days (%s); or\n\t\t2) fast for %.1fys until %s.", days, safeDateToTarget, fastingDays, fastDateToTarget);

            rTv.setText(recommendationMsg);
            rTv.setTextColor(Color.rgb(255,155,0));

        } else {
            owTv.setText("You have a buffer of: " + overWeightMsg);
            owTv.setTextColor(Color.GREEN);
            rTv.setText("");
        }

    }


    /**
     * Called when the user taps the Weigh In button
     */
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
        TextView owTv = findViewById(R.id.overWeightTextView);
        TextView rTv = findViewById(R.id.recomendationTextView);

        //TextView statusTextView = findViewById(R.id.statusTextView);

        //Network operations should be running in a seperate thread
        //But this hack will allows us to develop and test it here
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //End Hack
        MfpScreenScraper mpfSc = new MfpScreenScraper();
        try {
            VwFileManager fm = new VwFileManager();

            //Now read from this file
            StringBuffer stringBuffer = new StringBuffer();
            Context ctx = getApplicationContext();
            fm.readFile(ctx, VwFileManager.SETTINGS_FILE, stringBuffer);
            VwSettings vws = new VwSettings(stringBuffer.toString().trim());

            LocalDate dayAfterStartDate = vws.getDayAfterStartDate();
            TotalCalories total = mpfSc.getTotalCaloriesDateToToday(vws.getUserName(), vws.getPassword(), vws.getDayAfterStartDate());

            if (total.getStatus() == TotalCalories.SUCCESS) {
                double netWeight = total.getNetWeight(vws.getBmr(), vws.getStartWeight());
                weightResultTextView.setText(String.valueOf(netWeight));
                bmrTextView.setText(String.valueOf(total.getBmrSinceMidnight(vws.getBmr())));


                caloriesInTextView.setText(String.valueOf(total.getTotalCaloriesIn()));
                caloriesOutTextView.setText(String.valueOf(total.getTotalCaloriesOut()));
                netCaloriesTextView.setText(String.valueOf(total.getNetCalories(vws.getBmr())));
                netWeightTextView.setText(String.valueOf(total.getNetWeightChange(vws.getBmr())));

                setOverweightMessage(owTv, rTv, netWeight, vws.getTargetWeight(), vws.getBmr());


                statusTextView.setText("TC=" + total);
            } else {
                statusTextView.setText("Unable to retreive calories. Check internet connection and login credentials");
            }

        } catch (Exception ex) {
            statusTextView.setText("Ex=" + ex);
        }
    }

    double calcuateWeight(int weight, double bmr, int in, int out) {
        return weight + (in - out - bmr) / 7700;
    }
}

