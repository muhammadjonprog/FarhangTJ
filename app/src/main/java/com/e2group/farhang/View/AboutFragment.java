package com.e2group.farhang.View;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.e2group.farhang.R;
public class AboutFragment extends Fragment {
    TextView tvVersion;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        tvVersion = view.findViewById(R.id.tvVersion);

        //получаем версия приложения
        try {
            PackageInfo packageInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(),0);
            String version = packageInfo.versionName;
            int verCode = packageInfo.versionCode;
            tvVersion.setText(String.valueOf(version));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return view;
    }
}