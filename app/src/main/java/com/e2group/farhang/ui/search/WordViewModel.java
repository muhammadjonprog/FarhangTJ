package com.e2group.farhang.ui.search;

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

public class WordViewModel extends AndroidViewModel {
    public LiveData<PagedList<Word>> searchList;
    public LiveData<PagedList<Word>> wordList;

    WordDao wordDao;
    WordDatabase wordDatabase = App.getInstance().getWordDatabase();

    public WordViewModel(@NonNull Application application) {
        super(application);
        this.wordDao = wordDatabase.wordDao();
    }

    //Данные поступают из базы данных в PagedList объект
    public void getAll(){
        wordList = new LivePagedListBuilder<>(wordDao.getAll(),30).build();
    }

    public void doSearch(String query){
        searchList = new LivePagedListBuilder<>(wordDao.search(query), 30).build();
    }
}
