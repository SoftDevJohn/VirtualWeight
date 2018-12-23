package com.costigan.virtualweight.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

/**
 * A Repository is a class that abstracts access to multiple data sources.
 * The Repository is not part of the Architecture Components libraries,
 * but is a suggested best practice for code separation and architecture
 */
public class WordRepository {

    //Varibles fr DAO and list of words
    private WordDao mWordDao;

    //private List<Word> mAllWords;
    //Wrapped with live data so we can automatically observe changes
    private LiveData<List<Word>> mAllWords;

    //JC add wrapper LiveData

    /**
     *
     * constructor that gets a handle to the database and initializes the member variables.
     *
     */
    WordRepository(Application application) {
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();
        mAllWords = mWordDao.getAllWords();
    }

    public void insert (Word word) {
        new insertAsyncTask(mWordDao).execute(word);
    }

    private static class insertAsyncTask extends AsyncTask<Word, Void, Void> {

        private WordDao mAsyncTaskDao;

        insertAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Word... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    LiveData<List<Word>> getAllWords() { return mAllWords; }
}
