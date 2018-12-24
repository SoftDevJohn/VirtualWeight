package com.costigan.virtualweight.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.costigan.virtualweight.R;
import com.costigan.virtualweight.db.DbCalorie;
import com.costigan.virtualweight.mvc.CalorieListAdapter;
import com.costigan.virtualweight.mvc.CalorieViewModel;

import java.util.List;

import static com.costigan.virtualweight.ui.EditCalorieActivity.EXTRA_CALS_DATE;
import static com.costigan.virtualweight.ui.EditCalorieActivity.EXTRA_CALS_IN;
import static com.costigan.virtualweight.ui.EditCalorieActivity.EXTRA_CALS_OUT;

//import com.example.android.roomwordssample.R;


public class DatabaseListActivity extends AppCompatActivity implements EditCalorieContextMenuListener {

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    private CalorieViewModel mCalorieViewModel;


    //Called from CalorieListAdapter when user click on the edit context menu item from the list
    public void editListRow(String date, int caloriesIn, int caloriesOut) {
        int n = 0;
        Intent intent = new Intent(DatabaseListActivity.this, EditCalorieActivity.class);


        intent.putExtra(EXTRA_CALS_DATE, date);
        intent.putExtra(EXTRA_CALS_IN, caloriesIn);
        intent.putExtra(EXTRA_CALS_OUT, caloriesOut);


        startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_list);

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);


        AdapterView.OnItemSelectedListener listener = null;
        final CalorieListAdapter adapter = new CalorieListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get a new or existing ViewModel from the ViewModelProvider.
        mCalorieViewModel = ViewModelProviders.of(this).get(CalorieViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mCalorieViewModel.getAllWords().observe(this, new Observer<List<DbCalorie>>() {
            @Override
            public void onChanged(@Nullable final List<DbCalorie> calories) {
                // Update the cached copy of the calories in the adapter.
                adapter.setWords(calories);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DatabaseListActivity.this, EditCalorieActivity.class);

                intent.putExtra(EXTRA_CALS_DATE, "2018-12-23"); //TODO get data now, today
                intent.putExtra(EXTRA_CALS_IN, 0);
                intent.putExtra(EXTRA_CALS_OUT, 0);


                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            DbCalorie calorie = new DbCalorie(data.getStringExtra(EditCalorieActivity.EXTRA_REPLY),
                    data.getIntExtra(EXTRA_CALS_IN, 0),
                    data.getIntExtra(EXTRA_CALS_OUT, 0)


            );
            //mCalorieViewModel.insert(calorie);
            mCalorieViewModel.updateOrAdd(calorie);

        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}
