package com.costigan.virtualweight.mvc;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.costigan.virtualweight.db.Calorie;
import com.costigan.virtualweight.db.CalorieRepository;

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
    private LiveData<List<Calorie>> mAllWords;

    public CalorieViewModel(Application application) {
        super(application);
        mRepository = new CalorieRepository(application);
        mAllWords = mRepository.getAllWords();
    }

    public LiveData<List<Calorie>> getAllWords() {
        return mAllWords;
    }

    public void insert(Calorie calorie) {
        mRepository.insert(calorie);
    }

    public void update(Calorie calorie) {
        mRepository.update(calorie);

    }

    public boolean contains(Calorie calorie) {
        LiveData<List<Calorie>> mAllWords = mRepository.getAllWords();
        List<Calorie> calories = mAllWords.getValue();
        //calories.stream().filter(o -> o.getWord().equals(calorie.getWord())).findFirst().isPresent();
        for (int n = 0; n < calories.size(); n++) {
            Calorie c = calories.get(n);
            if (c.getWord().equals(calorie.getWord())) {
                return true;
            }
        }
        return false;
    }

    public void updateOrAdd(final Calorie calorie) {
        if (contains(calorie)) {
            mRepository.update(calorie);
        } else {
            mRepository.insert(calorie);
        }
    }


}