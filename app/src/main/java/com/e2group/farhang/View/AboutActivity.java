package com.e2group.farhang.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.e2group.farhang.R;

public class AboutActivity extends AppCompatActivity {
    TextView tvVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        tvVersion = findViewById(R.id.tvVersion);

        //получаем версия приложения
        try {
            PackageInfo packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(),0);
            String version = packageInfo.versionName;
            int verCode = packageInfo.versionCode;
            tvVersion.setText(String.valueOf(version));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}