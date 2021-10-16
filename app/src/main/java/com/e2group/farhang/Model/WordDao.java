package com.e2group.farhang.Model;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WordDao {
    @Query("SELECT * FROM Word")
    DataSource.Factory<Integer, Word> getAll();


    @Query("SELECT * FROM Word WHERE value  LIKE :sWord || '%'")
    DataSource.Factory<Integer, Word> search(String sWord);

    @Query("SELECT * FROM Abbreavetura")
    DataSource.Factory<Integer, Abbreavetura> getAllAb();

    @Query("SELECT * FROM Abbreavetura WHERE word  LIKE :sAbb || '%'")
    DataSource.Factory<Integer, Abbreavetura> searchAb(String sAbb);

  @Query("UPDATE Word SET favorite = 0 ")
  void updateAll();

  @Update
  void updateFavList (Word word);

    @Query("SELECT * FROM Word WHERE favorite = 1")
    DataSource.Factory<Integer,Word> getFavList();
}
