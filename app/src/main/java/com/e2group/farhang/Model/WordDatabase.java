package com.e2group.farhang.Model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Word.class,Abbreavetura.class}, version = 1,exportSchema = false)
public abstract class WordDatabase extends RoomDatabase {
    public abstract WordDao wordDao();
}
