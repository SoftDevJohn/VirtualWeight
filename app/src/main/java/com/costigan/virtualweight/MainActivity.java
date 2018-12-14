package com.costigan.virtualweight;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.LocalDate;

import java.io.FileNotFoundException;

import static com.costigan.virtualweight.VwFileManager.SETTINGS_FILE;

public class MainActivity extends AppCompatActivity {
    public static final String WEIGHT_RESULT = "com.costigan.virtualweight.WEIGHT_RESULT";

    CalorieCalculator calculator = new CalorieCalculator();
    TextView statusTextView = null;

    //https://www.livestrong.com/article/519568-does-slim-fast-curb-your-appetite/?ajax=1&is=1
    //3 X 650 meals + 2 x 200 snacks
    private static final int MAX_MEAL_CALORIES = 650;
    TextView weightResultTextView = null;
    TextView bmrTextView = null;
    TextView caloriesOutTextView = null;
    TextView caloriesInTextView = null;
    TextView netCaloriesTextView = null;
    TextView netWeightTextView = null;
    TextView owTv = null;
    TextView nmTv = null;
    TextView rTv = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        statusTextView = findViewById(R.id.statusTextView);
        weightResultTextView = findViewById(R.id.weightResultTextView);
        bmrTextView = findViewById(R.id.bmrTextView);
        caloriesOutTextView = findViewById(R.id.caloriesOutTextView);
        caloriesInTextView = findViewById(R.id.caloriesInTextView);
        netCaloriesTextView = findViewById(R.id.netCaloriesTextView);
        netWeightTextView = findViewById(R.id.netWeightTextView);
        owTv = findViewById(R.id.overWeightTextView);
        nmTv = findViewById(R.id.nextMealTextView);
        rTv = findViewById(R.id.recomendationTextView);

        restoreState();
        refreshMainDisplay();

    }

    /**
     * Save a JSON representation of TotalCalories in internal storage
     * Onlt preserve the fields marked as @Expose
     * When a new session of Vw starts, it will automatically reload this.
     */
    private void saveTotalCaloriesState(){
        Context context = getApplicationContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        TotalCalories tc = calculator.getTotal();
        VwSettings vws = calculator.getSettings();

        if( (tc == null) || (vws == null )){
            statusTextView.setText("nulaaaaa");
            return;
        }

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String totalCaloriesAsJson = gson.toJson(tc);
        String settingsAsJson = gson.toJson(vws);

        prefsEditor.putString(TotalCalories.TOTAL_CALORIES_OBJECT, totalCaloriesAsJson);
        prefsEditor.commit();
        statusTextView.setText("Test persistence: " + totalCaloriesAsJson);
    }


    /**
     * Retrive JSON string of TotalCalories object from internal storage
     */
    private void restoreState(){
        Context context = getApplicationContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = preferences.getString(TotalCalories.TOTAL_CALORIES_OBJECT, "");
        if( json.isEmpty()){
            //Object not found, so dont do anything
            return;
        }
        TotalCalories tc = gson.fromJson(json, TotalCalories.class);
        try {
            VwSettings vws = getVwSettings();
            calculator.setTotal(tc);
            calculator.setSettings(vws);
        } catch (Exception e) {
            e.printStackTrace();
        }

        statusTextView.setText("Test persistence retrieved: " + tc.toString());
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
            VwSettings settings = new VwSettings();
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
            LocalDate startDate = org.joda.time.LocalDate.parse("2018-01-01", Calorie.DATE_FORMATTER);
            settings.setStartDate(startDate);
            settings.setStartWeight(90.0);
            settings.setTargetWeight(80.0);
            //fm.writeFile(ctx, SETTINGS_FILE, settings.toString());
            try {
                fm.writeSettings(ctx, SETTINGS_FILE, settings);
            } catch (Exception ex) {
                statusTextView.setText("Unable to create the default configuration file: " + ex);
            }
            statusTextView.setText("Default configuration file created. Press Settings again.");

        } catch (Exception ex) {
            statusTextView.setText("Ex: " + ex);

        }
    }

    //Handle the results from the Update settings dialog
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                //String result=intent.getStringExtra("result");
                VwSettings settings = (VwSettings) intent.getSerializableExtra("VW_SETTINGS_RETURNED");
                calculator.setSettings(settings);
                statusTextView.setText("Settings are:" + settings.toString());
                Context ctx = getApplicationContext();
                VwFileManager fm = new VwFileManager();
                try {
                    fm.writeSettings(ctx, SETTINGS_FILE, settings);
                } catch (Exception ex) {
                    statusTextView.setText("Ex: " + ex);
                }
                refreshMainDisplay();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                statusTextView.setText("Cancelled");
            }
        }
    }//onActivityResult

    //getOverweightMessage(80.0,83.5,bmr
    private void setOverweightMessage(TextView owTv, TextView nmTv, TextView rTv, double currentWeight, double targetWeight, double bmr) {

        CalorieCalculator.RecommendationStats stats = calculator.getRecommendationStats();
        String overWeightMsg = String.format("%.2f kg (%.0f kcal or %.0f %%)", Math.abs(stats.overweight), Math.abs(stats.overweightCalories), Math.abs(stats.overweightPercentage));

        if (stats.minutesToNextMeal > 0) {
            String nextMeal = stats.now.plusMinutes((int) stats.minutesToNextMeal).toString("EEE dd/MMM/YYYY HH:mm");
            String nextMealMsg = String.format("(Within target mealtime: %s)", nextMeal);
            nmTv.setText(nextMealMsg);
            nmTv.setTextColor(Color.rgb(255, 155, 0));
        } else {
            nmTv.setText("(Can have next meal when ready)");
            nmTv.setTextColor(Color.GREEN);
        }

        if (stats.overweight > 0) {
            owTv.setText("You are overweight by: " + overWeightMsg);
            owTv.setTextColor(Color.RED);

            String safeDateToTarget = stats.now.plusDays((int) stats.days).toString("dd/MMM/YYYY");
            String fastDateToTarget = stats.now.plusMinutes((int) stats.fastingMinutes).toString("EEE dd/MMM/YYYY HH:mm");
            String recommendationMsg = String.format("To reach target, you need to:" +
                            "\n\t\t1) safely lose weight for %.0f days (%s); or" +
                            "\n\t\t2) fast for %.1f days until %s.",
                    stats.days, safeDateToTarget, (stats.fastingMinutes / 60 / 24), fastDateToTarget);

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

        VirtualWeight vw = new VirtualWeight();
        vw.calcuateWeight();
        WeightResultDto dto = vw.getWeight();
        final ScreenScraper ss = new MfpScreenScraper();

        try {
            //final to allow the child chread to access it
            final VwSettings settings = getVwSettings();
            calculator.setSettings(settings);
            LocalDate dayAfterStartDate = settings.getDayAfterStartDate();

            ///Run this in a seperate thread
            // Otherwise, activity main thread will throw exception.
            Thread okHttpExecuteThread = new Thread() {
                @Override
                public void run() {
                    try {
                        final TotalCalories total = ss.getTotalCaloriesDateToToday(calculator.getSettings().getUserName(), calculator.getSettings().getPassword(), calculator.getSettings().getDayAfterStartDate());

                        //The child tread cannot update the UI  directly, otherwise we get:
                        // Only the original thread that created a view hierarchy can touch its views.
                        //So we need to use this method
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sendChildThreadMessageToMainThread("", total);
                            }
                        });

                    } catch (final Exception ex) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sendChildThreadMessageToMainThread("Ex=" + ex, null);
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

    @NonNull
    private VwSettings getVwSettings() throws Exception {
        final VwFileManager fm = new VwFileManager();

        //Now read from this file
        StringBuffer stringBuffer = new StringBuffer();
        Context ctx = getApplicationContext();
        fm.readFile(ctx, VwFileManager.SETTINGS_FILE, stringBuffer);
        return new VwSettings(stringBuffer.toString().trim());
    }

    // Send message from child thread to activity main thread.
    // Because can not modify UI controls in child thread directly.
    private void sendChildThreadMessageToMainThread(String rspMsg, TotalCalories total) {
        final VwSettings vws = calculator.getSettings();
        calculator.setTotal(total);

        statusTextView.setText("Response: " + rspMsg + "TC=" + calculator.getTotal());
        if (calculator.getTotal().getStatus() == TotalCalories.SUCCESS) {
            refreshMainDisplay();
            saveTotalCaloriesState();
            statusTextView.setText("TC=" + calculator.getTotal());
        } else {
            statusTextView.setText("Unable to retreive calories. Check internet connection and login credentials");
        }
    }

    // Send message from child thread to activity main thread.
    // Because can not modify UI controls in child thread directly.
    private void refreshMainDisplay() {

        if (calculator.getTotal() == null) {
            //No calories retrieved to do calculation
            statusTextView.setText("No calories retrieved to do calculation");
            return;
        }
        double netWeight = calculator.getNetWeight();
        weightResultTextView.setText(String.valueOf(netWeight));
        bmrTextView.setText(String.valueOf(calculator.getBmrSinceMidnight()));
        caloriesInTextView.setText(String.valueOf(calculator.getTotal().getTotalCaloriesIn()));
        caloriesOutTextView.setText(String.valueOf(calculator.getTotal().getTotalCaloriesOut()));
        netCaloriesTextView.setText(String.valueOf(calculator.getNetCalories()));
        netWeightTextView.setText(String.valueOf(calculator.getNetWeightChange()));
        setOverweightMessage(owTv, nmTv, rTv, netWeight, calculator.getSettings().getTargetWeight(), calculator.getSettings().getBmr());
    }

    double calcuateWeight(int weight, double bmr, int in, int out) {
        return weight + (in - out - bmr) / 7700;
    }
}

