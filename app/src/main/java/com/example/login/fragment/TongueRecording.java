package com.example.login.fragment;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.login.R;
import com.example.login.TakePhotoActivity;
import com.example.login.dialog.UploadFileDialog;


public class TongueRecording extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tongue_recording, container, false);

        Button button = view.findViewById(R.id.bt_enter14); //Uploadshexiang
        Button button1 = view.findViewById(R.id.bt_enter15);//ConsultDoctorButton
        Button button2 = view.findViewById(R.id.bt_enter16);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the TakePhotoActivity
                Intent intent = new Intent(getActivity(), UploadFileDialog.class);
                startActivity(intent);
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the ConsultDoctor
                Intent intent = new Intent(getActivity(), TakePhotoActivity.class);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the TakePhotoActivity
                Intent intent = new Intent(getActivity(), TakePhotoActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }
}