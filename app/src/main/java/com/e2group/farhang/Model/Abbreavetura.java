package com.e2group.farhang.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Abbreavetura {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String word;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }


}
