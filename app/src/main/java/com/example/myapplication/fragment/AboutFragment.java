package com.example.myapplication.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.myapplication.BuildConfig;
import com.example.myapplication.R;

public class AboutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        
        // 动态设置版本号
        TextView tvVersion = view.findViewById(R.id.tvVersion);
        if (tvVersion != null) {
            String versionName = BuildConfig.VERSION_NAME;
            tvVersion.setText("版本 " + versionName);
        }
        
        return view;
    }

}


