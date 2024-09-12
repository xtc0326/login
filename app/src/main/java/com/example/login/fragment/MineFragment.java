package com.example.login.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.login.R;
import com.example.login.UsageStatsActivity;


public class MineFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        LinearLayout NotificationLinearLayout = view.findViewById(R.id.notification);
        ImageButton PersonalDataButton = view.findViewById(R.id.bt_personal_data);
        NotificationLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new MassageNotationFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        PersonalDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.hide(MineFragment.this);
                //
                transaction.replace(R.id.fragment_container,new PersonalDataFragment());
                // 将该事务添加到回退栈
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}

