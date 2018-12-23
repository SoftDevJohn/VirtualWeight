package com.costigan.virtualweight.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class WordViewModel extends AndroidViewModel {

    //reference to repository
    private WordRepository mRepository;

    //Ccahe list of words
    private LiveData<List<Word>> mAllWords;

    public WordViewModel (Application application) {
        super(application);
        mRepository = new WordRepository(application);
        mAllWords = mRepository.getAllWords();
    }

    //Completely hide the implementation from the UI
    LiveData<List<Word>> getAllWords() { return mAllWords; }
    public void insert(Word word) { mRepository.insert(word); }




}
