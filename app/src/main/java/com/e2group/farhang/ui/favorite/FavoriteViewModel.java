package com.e2group.farhang.ui.favorite;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.e2group.farhang.Model.App;
import com.e2group.farhang.Model.Word;
import com.e2group.farhang.Model.WordDao;
import com.e2group.farhang.Model.WordDatabase;

public class FavoriteViewModel extends AndroidViewModel {
    WordDao wordDao;
    WordDatabase wordDatabase = App.getInstance().getWordDatabase();
    public LiveData<PagedList<Word>> favorite;
    public LiveData<PagedList<Word>> listUpdate;
    public FavoriteViewModel(@NonNull Application context) {
        super(context);
        this.wordDao = wordDatabase.wordDao();
    }

    public void getFavList(){
        favorite = new LivePagedListBuilder<>(wordDao.getFavList(),20).build();
    }

//    public void updateList(){
//        listUpdate = new LivePagedListBuilder<>(wordDao.upAll(),20).build();
//
//    }
}
