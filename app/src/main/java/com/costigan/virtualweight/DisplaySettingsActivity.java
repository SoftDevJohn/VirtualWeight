package com.costigan.virtualweight;

import android.app.Activity;
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

        String message = settings.getUserName();
        EditText et = findViewById(R.id.userNameEditText);
        et.setText(message);


    }

    public void updateSettings(View view) {
        Intent returnIntent = new Intent();

        EditText et = findViewById(R.id.userNameEditText);
        VwSettings updatedSettings = new VwSettings();
        updatedSettings.setUserName(et.getText().toString());
        returnIntent.putExtra("VW_SETTINGS_RETURNED",updatedSettings);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();

    }


    }
