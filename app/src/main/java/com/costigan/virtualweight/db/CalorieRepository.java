package com.costigan.virtualweight.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */

public class CalorieRepository {

    private CalorieDao mCalorieDao;
    private LiveData<List<DbCalorie>> mAllWords;

    // Note that in order to unit test the CalorieRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public CalorieRepository(Application application) {
        CalorieRoomDatabase db = CalorieRoomDatabase.getDatabase(application);
        mCalorieDao = db.wordDao();
        mAllWords = mCalorieDao.getAlphabetizedWords();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<DbCalorie>> getAllWords() {
        return mAllWords;
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    public void insert(DbCalorie calorie) {
        new insertAsyncTask(mCalorieDao).execute(calorie);
    }


    final Executor executor = Executors.newFixedThreadPool(2);
    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    public void update(final DbCalorie calorie) {
        //new UpdateAsyncTask(mCalorieDao).execute(calorie);
        //new insertAsyncTask(mCalorieDao).execute(calorie);
        executor.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        mCalorieDao.update(calorie);
                    }
                }
        );

    }

    public void deleteAll() {
        //new UpdateAsyncTask(mCalorieDao).execute(calorie);
        //new insertAsyncTask(mCalorieDao).execute(calorie);
        executor.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        mCalorieDao.deleteAll();
                    }
                }
        );

    }


    private static class insertAsyncTask extends AsyncTask<DbCalorie, Void, Void> {

        private CalorieDao mAsyncTaskDao;

        insertAsyncTask(CalorieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final DbCalorie... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
