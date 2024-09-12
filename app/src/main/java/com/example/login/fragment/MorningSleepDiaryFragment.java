package com.example.login.fragment;
import android.content.Context;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.widget.EditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.example.login.R;


public class MorningSleepDiaryFragment extends Fragment {
    private EditText et1, et2, et3, et4, et5;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_morning_sleep_diary, container, false);

        et1 = view.findViewById(R.id.et1);
        et2 = view.findViewById(R.id.et2);
        et3 = view.findViewById(R.id.et3);
        et4 = view.findViewById(R.id.et4);
        et5 = view.findViewById(R.id.et5);

        Button saveButton = view.findViewById(R.id.morning_diary_save);
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
        return !et1.getText().toString().trim().isEmpty() &&
                !et2.getText().toString().trim().isEmpty() &&
                !et3.getText().toString().trim().isEmpty() &&
                !et4.getText().toString().trim().isEmpty() &&
                !et5.getText().toString().trim().isEmpty();
    }

    private void saveDiaryEntry() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SleepDiary", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("et1", et1.getText().toString().trim());
        editor.putString("et2", et2.getText().toString().trim());
        editor.putString("et3", et3.getText().toString().trim());
        editor.putString("et4", et4.getText().toString().trim());
        editor.putString("et5", et5.getText().toString().trim());

        editor.apply();
    }
}


