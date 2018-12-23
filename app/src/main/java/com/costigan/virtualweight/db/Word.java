package com.costigan.virtualweight.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * See example https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#3
 *
 */
@Entity(tableName = "word_table")
public class Word {

    @PrimaryKey //Every entity needs a primary key.
    @NonNull
    @ColumnInfo(name = "word") //the column name if db, if yiu want it differnt from mWord
    private String mWord;

    public Word(@NonNull String word) {this.mWord = word;}

    public String getWord(){return this.mWord;}

}
