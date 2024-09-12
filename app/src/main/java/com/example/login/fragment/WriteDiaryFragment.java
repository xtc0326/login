package com.example.login.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.login.R;




/// WriteDiaryFragment.java

    public class WriteDiaryFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_write_diary, container, false);

            Button morningDiaryButton = view.findViewById(R.id.button_morning);
            Button eveningDiaryButton = view.findViewById(R.id.button_night);

            morningDiaryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigate to MorningSleepDiaryFragment
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, new MorningSleepDiaryFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });

            eveningDiaryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigate to EveningSleepDiaryFragment
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, new EveningSleepDiaryFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });

            return view;
        }
    }




