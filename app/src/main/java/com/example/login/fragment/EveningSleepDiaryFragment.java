package com.example.login.fragment;
import android.content.Context;
import android.app.FragmentManager;
import android.os.Bundle;
import android.widget.EditText;
import  androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.example.login.R;
import android.content.SharedPreferences;
public class EveningSleepDiaryFragment extends Fragment {
    private EditText etn1, etn2, etn3, etn4, etn5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evening_sleep_diary, container, false);
        etn1 = view.findViewById(R.id.etn1);
        etn2 = view.findViewById(R.id.etn2);
        etn3 = view.findViewById(R.id.etn3);
        etn4 = view.findViewById(R.id.etn4);
        etn5 = view.findViewById(R.id.etn5);


        Button saveButton = view.findViewById(R.id.evening_diary_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInputValid()) {
                    saveDiaryEntry();
                    Toast.makeText(getActivity(), "日记保存成功", Toast.LENGTH_SHORT).show();

                    // Return to HomeFragment
                    getFragmentManager().popBackStack("HomeFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else {
                    Toast.makeText(getActivity(), "填写信息不完整，保存失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private boolean isInputValid() {
        return !etn1.getText().toString().trim().isEmpty() &&
                !etn2.getText().toString().trim().isEmpty() &&
                !etn3.getText().toString().trim().isEmpty() &&
                !etn4.getText().toString().trim().isEmpty() &&
                !etn5.getText().toString().trim().isEmpty();
    }

    private void saveDiaryEntry() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SleepDiary", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("etn1", etn1.getText().toString().trim());
        editor.putString("etn2", etn2.getText().toString().trim());
        editor.putString("etn3", etn3.getText().toString().trim());
        editor.putString("etn4", etn4.getText().toString().trim());
        editor.putString("etn5", etn5.getText().toString().trim());

        editor.apply();
    }
}



