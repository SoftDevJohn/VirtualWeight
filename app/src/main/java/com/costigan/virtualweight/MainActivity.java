package com.costigan.virtualweight;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Context;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.io.FileNotFoundException;

import static com.costigan.virtualweight.VwFileManager.SETTINGS_FILE;

public class MainActivity extends AppCompatActivity {
    public static final String WEIGHT_RESULT = "com.costigan.virtualweight.WEIGHT_RESULT";


    //https://www.livestrong.com/article/519568-does-slim-fast-curb-your-appetite/?ajax=1&is=1
    //3 X 650 meals + 2 x 200 snacks
    private static final int MAX_MEAL_CALORIES = 650;


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
    private void setOverweightMessage(TextView owTv, TextView nmTv, TextView rTv, double currentWeight, double targetWeight, double bmr) {

        double overweight = currentWeight - targetWeight;
        double overweightCalories = overweight * 7700;
        double overweightPercentage = (overweight / targetWeight) * 100;
        double maxSafeWeightLoss = 750; //kcal = 0.7 kg / week = 1.7 lbs week
        double days = overweightCalories / maxSafeWeightLoss;
        double fastingMinutes = (overweightCalories / bmr) * 60 * 24;

        double minutesToNextMeal = ((overweightCalories + MAX_MEAL_CALORIES) / bmr) * 60 * 24;


        DateTime now = DateTime.now();
        String nowStr = now.toString("EEE dd/MMM/YYYY HH:mm");

        DateTime dt = now.plusDays((int) days);


        String overWeightMsg = String.format("%.2f kg (%.0f kcal or %.0f %%)", Math.abs(overweight), Math.abs(overweightCalories), Math.abs(overweightPercentage));


        if( minutesToNextMeal >0 ){
            String nextMeal = now.plusMinutes((int) minutesToNextMeal).toString("EEE dd/MMM/YYYY HH:mm");
            String nextMealMsg = String.format("(Within target mealtime: %s)", nextMeal);
            nmTv.setText(nextMealMsg);
            nmTv.setTextColor(Color.rgb(255, 155, 0));
        }else{
            nmTv.setText("(Can have next meal when ready)");
            nmTv.setTextColor(Color.GREEN);
        }

        if (overweight > 0) {
            owTv.setText("You are overweight by: " + overWeightMsg);
            owTv.setTextColor(Color.RED);

            String safeDateToTarget = now.plusDays((int) days).toString("dd/MMM/YYYY");
            String fastDateToTarget = now.plusMinutes((int) fastingMinutes).toString("EEE dd/MMM/YYYY HH:mm");
            String recommendationMsg = String.format("To reach target, you need to:" +
                    "\n\t\t1) safely lose weight for %.0f days (%s); or" +
                    "\n\t\t2) fast for %.1f days until %s.",
                    days, safeDateToTarget, (fastingMinutes/60/24), fastDateToTarget);

            rTv.setText(recommendationMsg);
            rTv.setTextColor(Color.rgb(255, 155, 0));

        } else {
            owTv.setText("You have a buffer of: " + overWeightMsg);
            owTv.setTextColor(Color.GREEN);
            rTv.setText("");
        }

    }


    //Testing threading
    public void calculateWeight(View view) {
        TextView statusTextView = findViewById(R.id.statusTextView);

        VirtualWeight vw = new VirtualWeight();
        vw.calcuateWeight();
        WeightResultDto dto = vw.getWeight();


        final ScreenScraper ss = new MfpScreenScraper();


        try {

            //final to allow the child chread to access it
            final VwFileManager fm = new VwFileManager();

            //Now read from this file
            StringBuffer stringBuffer = new StringBuffer();
            Context ctx = getApplicationContext();
            fm.readFile(ctx, VwFileManager.SETTINGS_FILE, stringBuffer);
            final VwSettings vws = new VwSettings(stringBuffer.toString().trim());

            LocalDate dayAfterStartDate = vws.getDayAfterStartDate();

            ///Run this in a seperate thread

            // Otherwise, activity main thread will throw exception.
            Thread okHttpExecuteThread = new Thread() {
                @Override
                public void run() {
                    try {
                        final TotalCalories total = ss.getTotalCaloriesDateToToday(vws.getUserName(), vws.getPassword(), vws.getDayAfterStartDate());

                        //The child tread cannot update the UI  directly, otherwise we get:
                        // Only the original thread that created a view hierarchy can touch its views.
                        //So we need to use this method
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sendChildThreadMessageToMainThread("", vws, total);
                            }
                        });


                    } catch (final Exception ex) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sendChildThreadMessageToMainThread("Ex=" + ex, null, null);
                            }
                        });

                    }


                }
            };
            statusTextView.setText("Retrieving calories, please wait...");
            okHttpExecuteThread.start();

        } catch (Exception ex) {
            statusTextView.setText("Ex=" + ex);
        }
    }

    // Send message from child thread to activity main thread.
    // Because can not modify UI controls in child thread directly.
    private void sendChildThreadMessageToMainThread(String rspMsg, final VwSettings vws, TotalCalories total) {
        TextView weightResultTextView = findViewById(R.id.weightResultTextView);
        TextView bmrTextView = findViewById(R.id.bmrTextView);
        TextView caloriesOutTextView = findViewById(R.id.caloriesOutTextView);
        TextView caloriesInTextView = findViewById(R.id.caloriesInTextView);
        TextView netCaloriesTextView = findViewById(R.id.netCaloriesTextView);
        TextView netWeightTextView = findViewById(R.id.netWeightTextView);
        TextView owTv = findViewById(R.id.overWeightTextView);
        TextView nmTv = findViewById(R.id.nextMealTextView);
        TextView rTv = findViewById(R.id.recomendationTextView);

        TextView statusTextView = findViewById(R.id.statusTextView);
        statusTextView.setText("Response: " + rspMsg + "TC=" + total);
        if (total.getStatus() == TotalCalories.SUCCESS) {
            double netWeight = total.getNetWeight(vws.getBmr(), vws.getStartWeight());
            weightResultTextView.setText(String.valueOf(netWeight));
            bmrTextView.setText(String.valueOf(total.getBmrSinceMidnight(vws.getBmr())));
            caloriesInTextView.setText(String.valueOf(total.getTotalCaloriesIn()));
            caloriesOutTextView.setText(String.valueOf(total.getTotalCaloriesOut()));
            netCaloriesTextView.setText(String.valueOf(total.getNetCalories(vws.getBmr())));
            netWeightTextView.setText(String.valueOf(total.getNetWeightChange(vws.getBmr())));
            setOverweightMessage(owTv, nmTv,rTv, netWeight, vws.getTargetWeight(), vws.getBmr());
            statusTextView.setText("TC=" + total);
     } else {
            statusTextView.setText("Unable to retreive calories. Check internet connection and login credentials");
        }
    }

    double calcuateWeight(int weight, double bmr, int in, int out) {
        return weight + (in - out - bmr) / 7700;
    }
}

