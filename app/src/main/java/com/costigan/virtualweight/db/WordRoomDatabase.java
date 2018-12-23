package com.costigan.virtualweight.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Word.class}, version = 1) //List the entites in the database
public abstract class WordRoomDatabase extends RoomDatabase {

    //For each DAO, define a getter
    public abstract WordDao wordDao();

    //Make the class a iungletion to prevent multiple instance of the d opened at once
    private static volatile WordRoomDatabase INSTANCE;

    static WordRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WordRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WordRoomDatabase.class, "word_database")
                            .build();

                }
            }
        }
        return INSTANCE;
    }
    //End of Singleton




}
