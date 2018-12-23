package com.costigan.virtualweight.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface WordDao {

    @Insert
    void insert(Word word);

    @Query("DELETE FROM word_table")
    void deleteAll();

    /**
     *  Wrap with "List<Word> getAllWords();" LiveData so it automatically observes when
     *  the data changes so that you can update it.
     * @return
     */
    @Query("SELECT * from word_table ORDER BY word ASC")
    //List<Word> getAllWords();
    LiveData<List<Word>> getAllWords();
}