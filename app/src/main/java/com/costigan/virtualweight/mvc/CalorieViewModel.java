package com.costigan.virtualweight.mvc;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.costigan.virtualweight.db.DbCalorie;
import com.costigan.virtualweight.db.CalorieRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 */

public class CalorieViewModel extends AndroidViewModel {

    private CalorieRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<DbCalorie>> mAllWords;

    public CalorieViewModel(Application application) {
        super(application);
        mRepository = new CalorieRepository(application);
        mAllWords = mRepository.getAllWords();
    }

    public LiveData
            <List<DbCalorie>> getAllWords() {
        return mAllWords;
    }

    public void insert(DbCalorie calorie) {
        mRepository.insert(calorie);
    }

    public void update(DbCalorie calorie) {
        mRepository.update(calorie);

    }

/*
    private boolean contains(DbCalorie calorie) {
        LiveData<List<DbCalorie>> mAllWords = mRepository.getAllWords();
        List<DbCalorie> calories = mAllWords.getValue();
        if( calories == null ){
            return false;
        }
        //calories.stream().filter(o -> o.getWord().equals(calorie.getWord())).findFirst().isPresent();
        for (int n = 0; n < calories.size(); n++) {
            DbCalorie c = calories.get(n);
            if (c.getWord().equals(calorie.getWord())) {
                return true;
            }
        }

        // Java 8
        //return calories.stream().anyMatch(c -> c.getWord().equals(calorie.getWord()) );


        return false;
    }

    public void updateOrAdd(final DbCalorie calorie) {
        if (contains(calorie)) {
            mRepository.update(calorie);
        } else {
            mRepository.insert(calorie);
        }
    }
*/
    public void overwriteAllCalories(final List<DbCalorie> dbCalorieList){
        mRepository.overwriteAllCalories(dbCalorieList);
        /*
        mRepository.deleteAll();
        for(DbCalorie calorie : dbCalorieList) {
            updateOrAdd(calorie);
        }
        */
    }

}