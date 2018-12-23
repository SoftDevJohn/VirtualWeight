package com.costigan.virtualweight.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//import com.example.android.roomwordssample.R;
import com.costigan.virtualweight.R;


/**
 * Activity for entering a word.
 */

public class EditCalorieActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";
    public static final String EXTRA_CALS_DATE = "com.example.android.wordlistsql.REPLY_DATE";
    public static final String EXTRA_CALS_IN = "com.example.android.wordlistsql.REPLY_CALS_IN";
    public static final String EXTRA_CALS_OUT = "com.example.android.wordlistsql.REPLY_CALS_OUT";

    //private EditText mEditWordView;
    private EditText newCaloriesDateTextView;
    private EditText newCaloriesInTextView;
    private EditText newCaloriesOutTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_calorie);
        newCaloriesDateTextView = findViewById(R.id.date);
        newCaloriesInTextView = findViewById(R.id.newCaloriesInTextView);
        newCaloriesOutTextView = findViewById(R.id.newCaloriesOutTextView);



        Intent intent = getIntent();
        String date = intent.getStringExtra(EXTRA_CALS_DATE);
        int calsIn = intent.getIntExtra(EXTRA_CALS_IN,0);
        int calsOut  = intent.getIntExtra(EXTRA_CALS_OUT,0);


        newCaloriesDateTextView.setText(date);
        newCaloriesInTextView.setText(Integer.toString(calsIn));
        newCaloriesOutTextView.setText(Integer.toString(calsOut));

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(newCaloriesDateTextView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String word = newCaloriesDateTextView.getText().toString();
                    //String word = mEditWordView.getText().toString();
                    Integer caloriesIn = Integer.parseInt(newCaloriesInTextView.getText().toString());
                    Integer caloriesOut = Integer.parseInt(newCaloriesOutTextView.getText().toString());

                    replyIntent.putExtra(EXTRA_REPLY, word);
                    replyIntent.putExtra(EXTRA_CALS_IN, caloriesIn );
                    replyIntent.putExtra(EXTRA_CALS_OUT, caloriesOut );
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}

