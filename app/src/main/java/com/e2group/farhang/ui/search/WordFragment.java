package com.e2group.farhang.ui.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e2group.farhang.Adapter.WordAdapter;
import com.e2group.farhang.R;
import com.e2group.farhang.View.AboutActivity;

public class WordFragment extends Fragment implements AutoCompleteTextView.OnEditorActionListener {
    private Button btnU, btnQ, btnJ, btnH, btnI, btnG;
    private ImageView ivInfo;
    private WordViewModel wordViewModel;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private AutoCompleteTextView autoCompleteTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_word, container, false);
        initData(root);
        linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        final WordAdapter wordAdapter = new WordAdapter(getContext());
        recyclerView.setAdapter(wordAdapter);

        wordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        wordViewModel.getAll();
        wordViewModel.wordList.observe(getViewLifecycleOwner(), wordAdapter::submitList);
        autoCompleteTextView.setOnEditorActionListener(this);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                final Handler handler = new Handler();
                String query = autoCompleteTextView.getText().toString().trim();
                if (query.isEmpty()) {
                    wordViewModel.getAll();
                    wordViewModel.wordList.observe(getViewLifecycleOwner(), wordAdapter::submitList);
                } else {
                    handler.postDelayed(() -> {
                        wordViewModel.doSearch(query);
                        wordViewModel.searchList.observe(getViewLifecycleOwner(), wordAdapter::submitList);
                    }, 300);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        View.OnClickListener onClickListener = view -> {
            switch (view.getId()) {
                case R.id.btnH:
                    Editable h = autoCompleteTextView.getText();
                    h.insert(autoCompleteTextView.getSelectionStart(), "ҳ");
                    break;
                case R.id.btnG:
                    Editable g = autoCompleteTextView.getText();
                    g.insert(autoCompleteTextView.getSelectionStart(), "ғ");
                    break;
                case R.id.btnU:
                    Editable u = autoCompleteTextView.getText();
                    u.insert(autoCompleteTextView.getSelectionStart(), "ӯ");
                    break;
                case R.id.btnI:
                    Editable i = autoCompleteTextView.getText();
                    i.insert(autoCompleteTextView.getSelectionStart(), "ӣ");
                    break;
                case R.id.btnJ:
                    Editable j = autoCompleteTextView.getText();
                    j.insert(autoCompleteTextView.getSelectionStart(), "ҷ");
                    break;
                case R.id.btnQ:
                    Editable q = autoCompleteTextView.getText();
                    q.insert(autoCompleteTextView.getSelectionStart(), "қ");
                    break;
            }
        };
        btnH.setOnClickListener(onClickListener);
        btnQ.setOnClickListener(onClickListener);
        btnG.setOnClickListener(onClickListener);
        btnI.setOnClickListener(onClickListener);
        btnU.setOnClickListener(onClickListener);
        btnJ.setOnClickListener(onClickListener);

        ivInfo.setOnClickListener(view -> startActivity(new Intent(getContext(), AboutActivity.class)));

        return root;
    }

    public void initData(View root) {
        recyclerView = root.findViewById(R.id.recyclerViewListWord);
        autoCompleteTextView = root.findViewById(R.id.actSearchWord);
        ivInfo = root.findViewById(R.id.iv_info);
        btnG = root.findViewById(R.id.btnG);
        btnI = root.findViewById(R.id.btnI);
        btnH = root.findViewById(R.id.btnH);
        btnJ = root.findViewById(R.id.btnJ);
        btnQ = root.findViewById(R.id.btnQ);
        btnU = root.findViewById(R.id.btnU);
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            closeKeyboard(getContext());
            return true;
        }
        return false;
    }

    private static void closeKeyboard(Context ctx) {
        InputMethodManager inputMethodManager = (InputMethodManager) ctx.
                getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}