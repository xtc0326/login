package com.example.login.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import androidx.fragment.app.Fragment;

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

    private EditText diaryEditText;
    private Button saveDiaryButton;
    private Spinner hourSpinner, minuteSpinner, ampmSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_write_diary, container, false);

        diaryEditText = view.findViewById(R.id.diary_edit_text);
        saveDiaryButton = view.findViewById(R.id.save_diary_button);

        hourSpinner = view.findViewById(R.id.hour_spinner);
        minuteSpinner = view.findViewById(R.id.minute_spinner);
        ampmSpinner = view.findViewById(R.id.ampm_spinner);

        // 设置时间选择器
        ArrayAdapter<CharSequence> hourAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.hours_array, android.R.layout.simple_spinner_item);
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hourSpinner.setAdapter(hourAdapter);

        ArrayAdapter<CharSequence> minuteAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.minutes_array, android.R.layout.simple_spinner_item);
        minuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minuteSpinner.setAdapter(minuteAdapter);

        ArrayAdapter<CharSequence> ampmAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.ampm_array, android.R.layout.simple_spinner_item);
        ampmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ampmSpinner.setAdapter(ampmAdapter);

        saveDiaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String diaryText = diaryEditText.getText().toString();
                String time = hourSpinner.getSelectedItem().toString() + ":" +
                        minuteSpinner.getSelectedItem().toString() + " " +
                        ampmSpinner.getSelectedItem().toString();
                saveDiary(diaryText, time);
                getActivity().onBackPressed();  // 返回首页
            }
        });

        return view;
    }

    private void saveDiary(String diaryText, String time) {
        // 在这里实现保存日记的逻辑，例如保存到数据库或SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SleepDiary", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("diary", diaryText);
        editor.putString("time", time);
        editor.apply();

        Toast.makeText(getActivity(), "日记已保存", Toast.LENGTH_SHORT).show();
    }
}
