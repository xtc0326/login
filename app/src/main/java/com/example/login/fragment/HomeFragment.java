package com.example.login.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.login.R;
import com.example.login.UsageStatsActivity;


public class HomeFragment extends Fragment {
    private Button UsageStatsButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button WriteDiaryButton = view.findViewById(R.id.bt_enter1);
        Button TongueRecordingButton = view.findViewById(R.id.bt_enter2);
        Button UsageStatsButton = view.findViewById(R.id.bt_enter4);
        Button MindFulnessButton = view.findViewById(R.id.bt_enter6);
        WriteDiaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace current fragment with WriteDiaryFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new WriteDiaryFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        TongueRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace current fragment with TongueRecordingFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new TongueRecording());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        UsageStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个新的 Intent 以启动 UsageStatsActivity
                Intent intent = new Intent(getActivity(), UsageStatsActivity.class);
                startActivity(intent);
            }
        });

        MindFulnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace current fragment with WriteDiaryFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new MindFulnessFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}


