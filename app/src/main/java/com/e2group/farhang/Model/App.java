package com.e2group.farhang.Model;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.e2group.farhang.Model.WordDatabase;

import java.util.List;

public class App extends Application {
    public static App instance;
    private WordDatabase wordDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        wordDatabase = Room.databaseBuilder(this,WordDatabase.class,
                "F")
                .createFromAsset("Farhang.db")
                .allowMainThreadQueries()
                .build();
    }

    public static App getInstance(){
        return instance;
    }

    public WordDatabase getWordDatabase() {
        return wordDatabase;
    }

}
