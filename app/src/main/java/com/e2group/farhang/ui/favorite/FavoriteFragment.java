package com.e2group.farhang.ui.favorite;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.e2group.farhang.Adapter.FavoriteAdapter;
;
import com.e2group.farhang.Model.App;
import com.e2group.farhang.Model.Word;
import com.e2group.farhang.Model.WordDatabase;
import com.e2group.farhang.R;


public class FavoriteFragment extends Fragment {
    private FavoriteViewModel favoriteViewModel;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ImageView deleteAll, ivEmptyList;
    private WordDatabase wordDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_fav, container, false);
        wordDatabase = App.getInstance().getWordDatabase();
        recyclerView = root.findViewById(R.id.recyclerView2);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        deleteAll = root.findViewById(R.id.iv_DeleteAll);
        ivEmptyList = root.findViewById(R.id.ivEmptyList);

        final FavoriteAdapter favoriteAdapter = new FavoriteAdapter(getContext());
        recyclerView.setAdapter(favoriteAdapter);

        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        favoriteViewModel.getFavList();
        favoriteViewModel.favorite.observe(getViewLifecycleOwner(), favoriteAdapter::submitList);
//        favoriteViewModel.favorite.observe(getViewLifecycleOwner(), new Observer<PagedList<Word>>() {
//            @Override
//            public void onChanged(PagedList<Word> words) {
//                favoriteAdapter.submitList(words);
//            }
//        });
//        if (favoriteAdapter.getCurrentList().getDataSource().is){
//            ivEmptyList.setVisibility(View.VISIBLE);
//        }else {
//            ivEmptyList.setVisibility(View.INVISIBLE);
//
//        }
        deleteAll.setOnClickListener(view -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setMessage(R.string.dialog_message);
            dialog.setPositiveButton(R.string.btn_yes, (dialogInterface, i) ->
            wordDatabase.wordDao().updateAll());

            dialog.setNegativeButton(R.string.btn_no, (dialogInterface, i) -> {
            });
            dialog.show();
        });

        return root;
    }
}