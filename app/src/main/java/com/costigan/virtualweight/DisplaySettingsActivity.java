package com.costigan.virtualweight;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DisplaySettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_settings);

        Intent intent = getIntent();
        VwSettings settings = (VwSettings)intent.getSerializableExtra("VW_SETTINGS");

        EditText userEt = findViewById(R.id.usernameEditText);
        EditText pwEt = findViewById(R.id.passwordEditText);
        EditText bmrEt = findViewById(R.id.bmrEditText);
        EditText swEt = findViewById(R.id.startWeightEditText);
        EditText sdEt = findViewById(R.id.startDateEditText);
        EditText twEt = findViewById(R.id.targetWeightEditText);

        userEt.setText(settings.getUserName());
        pwEt.setText(settings.getPassword());
        bmrEt.setText(String.valueOf( settings.getBmr() ));
        swEt.setText(String.valueOf( settings.getStartWeight() ));
        sdEt.setText(String.valueOf( settings.getStartDate() ));
        twEt.setText(String.valueOf( settings.getTargetWeight() ));




    }

    public void updateSettings(View view) {
        Intent returnIntent = new Intent();

        EditText et = findViewById(R.id.usernameEditText);

        VwSettings updatedSettings = new VwSettings();


        EditText userEt = findViewById(R.id.usernameEditText);
        EditText pwEt = findViewById(R.id.passwordEditText);
        EditText bmrEt = findViewById(R.id.bmrEditText);
        EditText swEt = findViewById(R.id.startWeightEditText);
        EditText sdEt = findViewById(R.id.startDateEditText);
        EditText twEt = findViewById(R.id.targetWeightEditText);

        updatedSettings.setUserName(userEt.getText().toString());
        updatedSettings.setPassword(pwEt.getText().toString());
        updatedSettings.setBmr(Integer.parseInt(bmrEt.getText().toString()) );
        updatedSettings.setStartWeight( Double.parseDouble(swEt.getText().toString()));
        //Handle date
        updatedSettings.setStartDateASYYYYMMMDD(sdEt.getText().toString());
        updatedSettings.setTargetWeight(Double.parseDouble(twEt.getText().toString()));











        returnIntent.putExtra("VW_SETTINGS_RETURNED",updatedSettings);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();

    }


    }
