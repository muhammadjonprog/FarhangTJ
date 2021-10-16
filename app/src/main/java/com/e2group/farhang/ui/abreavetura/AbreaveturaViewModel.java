package com.e2group.farhang.ui.abreavetura;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.e2group.farhang.Model.Abbreavetura;
import com.e2group.farhang.Model.App;
import com.e2group.farhang.Model.WordDao;
import com.e2group.farhang.Model.WordDatabase;

public class AbreaveturaViewModel extends AndroidViewModel {
    public LiveData<PagedList<Abbreavetura>> abbList;
    public LiveData<PagedList<Abbreavetura>> searchAbb;

    WordDao wordDao;
    WordDatabase wordDatabase = App.getInstance().getWordDatabase();

    public AbreaveturaViewModel(@NonNull Application application) {
        super(application);
        this.wordDao = wordDatabase.wordDao();
    }

    public void getAllAb(){
        abbList = new LivePagedListBuilder<>(wordDao.getAllAb(),20).build();
    }

    public void doSearchAbb(String query){
        searchAbb = new LivePagedListBuilder<>(wordDao.searchAb(query),20).build();
    }
}
