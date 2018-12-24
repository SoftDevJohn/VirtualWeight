package com.costigan.virtualweight.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * A basic class representing an entity that is a row in a one-column database table.
 *
 * @ Entity - You must annotate the class as an entity and supply a table name if not class name.
 * @ PrimaryKey - You must identify the primary key.
 * @ ColumnInfo - You must supply the column name if it is different from the variable name.
 *
 * See the documentation for the full rich set of annotations.
 * https://developer.android.com/topic/libraries/architecture/room.html
 */

@Entity(tableName = "calorie_table")
public class DbCalorie {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "word")
    private String mWord;

    @NonNull
    @ColumnInfo(name = "calories_in")
    private Integer caloriesIn;

    @NonNull
    @ColumnInfo(name = "calories_out")
    private Integer caloriesOut;

    public DbCalorie(@NonNull String word, @NonNull Integer caloriesIn, @NonNull Integer caloriesOut) {
        this.mWord = word;
        this.caloriesIn = caloriesIn;
        this.caloriesOut = caloriesOut;
    }

    @NonNull
    public String getWord() {
        return this.mWord;
    }

    public Integer getCaloriesIn() {
        return this.caloriesIn;
    }

    public Integer getCaloriesOut() {
        return this.caloriesOut;
    }



}