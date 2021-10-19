package com.e2group.farhang.ui.abreavetura;

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

import com.e2group.farhang.Adapter.AbbAdapter;
import com.e2group.farhang.R;
import com.e2group.farhang.View.AboutActivity;


public class AbrFragment extends Fragment implements AutoCompleteTextView.OnEditorActionListener {
    private Button btnU, btnQ, btnJ, btnH, btnI, btnG;
    ImageView ivInfo;
    private AbreaveturaViewModel abreaveturaViewModel;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private AutoCompleteTextView autoCompleteTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View root = inflater.inflate(R.layout.fragment_abbrev, container, false);
        initData(root);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        final AbbAdapter abbAdapter = new AbbAdapter(getContext());
        recyclerView.setAdapter(abbAdapter);

        abreaveturaViewModel = new ViewModelProvider(this).get(AbreaveturaViewModel.class);
        abreaveturaViewModel.getAllAb();
        abreaveturaViewModel.abbList.observe(getViewLifecycleOwner(), abbAdapter::submitList);

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
                    abreaveturaViewModel.getAllAb();
                    abreaveturaViewModel.abbList.observe(getViewLifecycleOwner(), abbAdapter::submitList);
                } else {
                    handler.postDelayed(() -> {
                        abreaveturaViewModel.doSearchAbb(query);
                        abreaveturaViewModel.searchAbb.observe(getViewLifecycleOwner(), abbAdapter::submitList);
                    }, 300);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        View.OnClickListener onClickListener = view -> {
            switch (view.getId()) {
                case R.id.btnHA:
                    Editable h = autoCompleteTextView.getText();
                    h.insert(autoCompleteTextView.getSelectionStart(), "ҳ");
                    break;
                case R.id.btnGA:
                    Editable g = autoCompleteTextView.getText();
                    g.insert(autoCompleteTextView.getSelectionStart(), "ғ");
                    break;
                case R.id.btnUA:
                    Editable u = autoCompleteTextView.getText();
                    u.insert(autoCompleteTextView.getSelectionStart(), "ӯ");
                    break;
                case R.id.btnIA:
                    Editable i = autoCompleteTextView.getText();
                    i.insert(autoCompleteTextView.getSelectionStart(), "ӣ");
                    break;
                case R.id.btnJA:
                    Editable j = autoCompleteTextView.getText();
                    j.insert(autoCompleteTextView.getSelectionStart(), "ҷ");
                    break;
                case R.id.btnQA:
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
        recyclerView = root.findViewById(R.id.recyclerViewA);
        autoCompleteTextView = root.findViewById(R.id.actSearchAbb);
        ivInfo = root.findViewById(R.id.iv_infoA);
        btnG = root.findViewById(R.id.btnGA);
        btnI = root.findViewById(R.id.btnIA);
        btnH = root.findViewById(R.id.btnHA);
        btnJ = root.findViewById(R.id.btnJA);
        btnQ = root.findViewById(R.id.btnQA);
        btnU = root.findViewById(R.id.btnUA);
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