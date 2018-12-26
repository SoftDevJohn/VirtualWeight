package com.costigan.virtualweight;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.content.Context;

import com.costigan.virtualweight.db.DbCalorie;
import com.costigan.virtualweight.gson.GsonLocalDateSerializerAdapter;
import com.costigan.virtualweight.gson.GsonLocalDateDeserializerAdapter;
import com.costigan.virtualweight.mvc.CalorieViewModel;
import com.costigan.virtualweight.ui.DatabaseListActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static com.costigan.virtualweight.VwFileManager.SETTINGS_FILE;

public class MainActivity extends AppCompatActivity {
    public static final String WEIGHT_RESULT = "com.costigan.virtualweight.WEIGHT_RESULT";

    enum RetreiveCaloriesStatus {
        SUCCESS, FAIL, UNKNOWN_HOST;
    }

    CalorieCalculator calculator = new CalorieCalculator();
    TextView statusTextView = null;
    private ScreenScraper ss;
    private CalorieViewModel mCalorieViewModel;

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

        ss = MfpScreenScraper.getInstance();
        mCalorieViewModel = ViewModelProviders.of(this).get(CalorieViewModel.class);

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

        // Get a new or existing ViewModel from the ViewModelProvider.
        mCalorieViewModel = ViewModelProviders.of(this).get(CalorieViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mCalorieViewModel.getAllWords().observe(this, new Observer<List<DbCalorie>>() {

            @Override
            public void onChanged(@Nullable final List<DbCalorie> dbCalories) {
                // Update the cached copy of the calories in the adapter.
                //adapter.setWords(calories);
                //Update the display
                int n = 0;
                //statusTextView.setText("Db changed: "+dbCalories.size());
                //TODO testing.
                //TODO
                final VwSettings vws = calculator.getSettings();
                List<Calorie> calories = convertDbCaloriesToCalories(dbCalories);

                TotalCalories total = TotalCalories.toTotalCalories(calories);

                calculator.setTotal(total);

                refreshMainDisplay();
                saveTotalCaloriesState();
                statusTextView.setText("xxxxTC=" + calculator.getTotal());
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.opt_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sync:
                //statusTextView.setText("Sync");
                calculateWeight();
                return true;
            case R.id.settings:
                //statusTextView.setText("Settings");
                changeSettings();
                return true;
            case R.id.history:
                //statusTextView.setText("Settings");
                browseDb();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Save a JSON representation of TotalCalories in internal storage
     * Onlt preserve the fields marked as @Expose
     * When a new session of Vw starts, it will automatically reload this.
     */
    private void saveTotalCaloriesState() {
        Context context = getApplicationContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        TotalCalories tc = calculator.getTotal();
        VwSettings vws = calculator.getSettings();

        if ((tc == null) || (vws == null)) {
            statusTextView.setText("Unable to save state of null calories");
            return;
        }

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(LocalDate.class, new GsonLocalDateSerializerAdapter())
                .create();
        String totalCaloriesAsJson = gson.toJson(tc);

        prefsEditor.putString(TotalCalories.TOTAL_CALORIES_OBJECT, totalCaloriesAsJson);
        prefsEditor.commit();
        statusTextView.setText("Test persistence: " + totalCaloriesAsJson);

    }


    /**
     * Retrive JSON string of TotalCalories object from internal storage
     */
    private void restoreState() {
        Context context = getApplicationContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(LocalDate.class, new GsonLocalDateDeserializerAdapter())
                .create();


        String json = preferences.getString(TotalCalories.TOTAL_CALORIES_OBJECT, "");

        if (json.isEmpty()) {
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
    public void browseDb() {
        Intent intent = new Intent(this, DatabaseListActivity.class);
        startActivityForResult(intent, 1);
    }

    /**
     * Called when the user taps the Weigh In button
     */
    public void changeSettings() {

        //Open the Settings Screem
        Intent intent = new Intent(this, DisplaySettingsActivity.class);
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
                //statusTextView.setText("Cancelled");
            }
        }
    }//onActivityResult

    //getOverweightMessage(80.0,83.5,bmr
    private void setOverweightMessage(TextView owTv, TextView nmTv, TextView rTv, double currentWeight, double targetWeight, double bmr) {

        CalorieCalculator.RecommendationStats stats = calculator.getRecommendationStats();
        String overWeightMsg = String.format("%.2f kg (%.0f kcal or %.0f %%)", Math.abs(stats.overweight), Math.abs(stats.overweightCalories), Math.abs(stats.overweightPercentage));

        if (stats.minutesToNextMeal > 0) {
            String nextMealMsg = String.format("(Within target mealtime: %s)", getTargetDateTimeAsString(stats.minutesToNextMeal));
            nmTv.setText(nextMealMsg);
            nmTv.setTextColor(Color.rgb(255, 155, 0));
        } else {
            nmTv.setText("(Can have next meal when ready)");
            nmTv.setTextColor(Color.GREEN);
        }

        if (stats.overweight > 0) {
            owTv.setText("You are overweight by: " + overWeightMsg);
            owTv.setTextColor(Color.RED);

            String safeDateToTarget = getTargetDateTimeAsString(stats.minutes);
            String fastDateToTarget = getTargetDateTimeAsString(stats.fastingMinutes);

            String recommendationMsg = String.format("To reach target, you need to:" +
                            "\n\t\t1) safely lose weight for %.0f days (%s); or" +
                            "\n\t\t2) fast for %.1f days until %s.",
                    (stats.minutes / 60 / 24), safeDateToTarget, (stats.fastingMinutes / 60 / 24), fastDateToTarget);

            rTv.setText(recommendationMsg);
            rTv.setTextColor(Color.rgb(255, 155, 0));

        } else {
            owTv.setText("You have a buffer of: " + overWeightMsg);
            owTv.setTextColor(Color.GREEN);
            rTv.setText("");
        }

    }

    public String getTargetDateTimeAsString(double minutesToNextMeal) {
        String nextMeal = "";
        DateTime dtNextMeal = DateTime.now().plusMinutes((int) minutesToNextMeal);
        if (dtNextMeal.toLocalDate().equals(LocalDate.now())) {
            nextMeal = "today at " + dtNextMeal.toString("HH:mm");
        } else if (dtNextMeal.toLocalDate().equals(LocalDate.now().plusDays(1))) {
            nextMeal = "tomorrow at " + dtNextMeal.toString("HH:mm");
        } else {
            nextMeal = dtNextMeal.toString("EEE dd/MMM/YYYY HH:mm");
        }
        return nextMeal;
    }

    private List<LocalDate> getListOfDatesUptoToday(LocalDate fromDate) throws Exception {
        LocalDate today = org.joda.time.LocalDate.now();
        return getListOfDatesForRange(fromDate, today);
    }

    /**
     * Create a list of dates for the given parameters.
     *
     * @param fromDate
     * @param toDate
     * @return A list of Dates for the given date range
     * @throws Exception
     */
    private List<LocalDate> getListOfDatesForRange(LocalDate fromDate, LocalDate toDate) throws Exception {
        List<LocalDate> retrievalDates = new ArrayList<LocalDate>();
        for (LocalDate searchDate = fromDate; (searchDate.isBefore(toDate) || searchDate.isEqual(toDate)); searchDate = searchDate.plusDays(1)) {
            retrievalDates.add(searchDate);
        }


        return retrievalDates;

    }


    public void calculateWeight() {

        VirtualWeight vw = new VirtualWeight();
        vw.calcuateWeight();
        WeightResultDto dto = vw.getWeight();

        try {
            //final to allow the child chread to access it
            final VwSettings settings = getVwSettings();
            calculator.setSettings(settings);
            LocalDate dayAfterStartDate = settings.getDayAfterStartDate();

            ((MfpScreenScraper) ss).setUsername(settings.getUserName());
            ((MfpScreenScraper) ss).setPassword(settings.getPassword());

            ///Run this in a seperate thread
            // Otherwise, activity main thread will throw exception.
            Thread okHttpExecuteThread = new Thread() {
                @Override
                public void run() {
                    try {
                        List<LocalDate> dates = getListOfDatesUptoToday(calculator.getSettings().getDayAfterStartDate());
                        final List<Calorie> calorieList = ss.getCaloriesForDateList(dates);

                        //The child tread cannot update the UI  directly, otherwise we get:
                        // Only the original thread that created a view hierarchy can touch its views.
                        //So we need to use this method
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sendChildThreadMessageToMainThread(RetreiveCaloriesStatus.SUCCESS, "", calorieList);
                            }
                        });

                    } catch (final Exception ex) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (ex instanceof UnknownHostException) {
                                    sendChildThreadMessageToMainThread(RetreiveCaloriesStatus.UNKNOWN_HOST, "Ex=" + ex, null);
                                } else {
                                    sendChildThreadMessageToMainThread(RetreiveCaloriesStatus.FAIL, "Ex=" + ex, null);
                                }
                            }
                        });
                    }
                    ;
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
    //private void sendChildThreadMessageToMainThread(RetreiveCaloriesStatus status, String rspMsg, TotalCalories total) {
    private void sendChildThreadMessageToMainThread(RetreiveCaloriesStatus status, String rspMsg, List<Calorie> calorieList) {

        if (status == RetreiveCaloriesStatus.UNKNOWN_HOST) {
            statusTextView.setText("Unable to connect to retrieve calories");
            return;
        }

        final TotalCalories total = TotalCalories.toTotalCalories(calorieList);

        if (total == null) {
            statusTextView.setText("Unable to retrieve calories. (TotalCalories is null)");
            return;
        }

        List<DbCalorie> dbCalorieList = convertCaloriesToDbCalories(calorieList);
        mCalorieViewModel.overwriteAllCalories(dbCalorieList);
/*
        //TODO
        // defer to listener on DB
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
*/
    }


    //TODO: This may not be necessary if we merge Calorie and DbCalorie (as DbCalorie is only a normla POJO)
    List<DbCalorie> convertCaloriesToDbCalories(List<Calorie> calorieList) {
        List<DbCalorie> dbCalories = new ArrayList<DbCalorie>();
        for (Calorie cal : calorieList) {
            DbCalorie dbCal = new DbCalorie(cal.getDateAsMfpString(), cal.getCaloriesIn(), cal.getCaloriesOut());
            dbCalories.add(dbCal);
        }

        return dbCalories;
    }

    //TODO: This may not be necessary if we merge Calorie and DbCalorie (as DbCalorie is only a normla POJO)
    List<Calorie> convertDbCaloriesToCalories(List<DbCalorie> calorieList) {
        List<Calorie> calories = new ArrayList<Calorie>();
        for (DbCalorie dbCal : calorieList) {
            Calorie cal = new Calorie();
            cal.setDate(dbCal.getWord());
            cal.setCaloriesIn(dbCal.getCaloriesIn());
            cal.setCaloriesOut(dbCal.getCaloriesOut());
            calories.add(cal);
        }

        return calories;
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

