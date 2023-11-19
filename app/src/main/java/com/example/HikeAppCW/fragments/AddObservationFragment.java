package com.example.HikeAppCW.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.HikeAppCW.R;
import com.example.HikeAppCW.databases.AppDatabase;
import com.example.HikeAppCW.models.Observation;
import com.google.android.material.button.MaterialButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class AddObservationFragment extends Fragment {

    private AppDatabase appDatabase;
    private long hikeId;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_observation, container, false);

        appDatabase = Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class, "hike_database_db")
                .allowMainThreadQueries() // For simplicity, don't use this in production
                .build();

        hikeId = getArguments().getLong("hike_id");

        AutoCompleteTextView weatherLevel = v.findViewById(R.id.weatherLevel);
        String[] weatherLevels = getResources().getStringArray(R.array.weather_list);
        ArrayAdapter<String> adapterWeather = new ArrayAdapter<>(getContext(), R.layout.dropdown_list, weatherLevels);
        weatherLevel.setAdapter(adapterWeather);

        TextView obDate = v.findViewById(R.id.obDate);
        obDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getChildFragmentManager(),"Date Picker1");
            }
        });

        TextView obTime = v.findViewById(R.id.obTime);
        obTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        MaterialButton addObButton = v.findViewById(R.id.addObButton);
        addObButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getObData();
            }
        });

        return v;
    }

    private void showTimePicker() {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        // Update the TextView with the selected time
                        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                        TextView obTime = v.findViewById(R.id.obTime);
                        obTime.setText(formattedTime);
                    }
                },
                hour,
                minute,
                true // 24-hour format
        );

        // Show the TimePickerDialog
        timePickerDialog.show();
    }

    private void getObData() {
        EditText obName = v.findViewById(R.id.obName);
        TextView obTime = v.findViewById(R.id.obTime);
        TextView obDate = v.findViewById(R.id.obDate);
        MultiAutoCompleteTextView obComment = v.findViewById(R.id.obComment);
        AutoCompleteTextView weatherLevel = v.findViewById(R.id.weatherLevel);

        String name = obName.getText().toString();
        String time = obTime.getText().toString();
        String date = obDate.getText().toString();
        String selectedWeather = weatherLevel.getText().toString();
        String comment = obComment.getText().toString();

        if (name.isEmpty() || time.isEmpty() || date.isEmpty() || selectedWeather.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder confirmDialog = new AlertDialog.Builder(getContext());
            View viewDialog = getLayoutInflater().inflate(R.layout.alert_new_observation, null);

            TextView nameCf = viewDialog.findViewById(R.id.confirmNameOb);
            TextView timeCf = viewDialog.findViewById(R.id.confirmTimeOb);
            TextView dateCf = viewDialog.findViewById(R.id.confirmDateOb);
            TextView weatherCf = viewDialog.findViewById(R.id.confirmWeatherOb);
            TextView commentCf = viewDialog.findViewById(R.id.confirmDescriptionOb);

            MaterialButton cfCancel = viewDialog.findViewById(R.id.confirmCancel);
            MaterialButton cfYes = viewDialog.findViewById(R.id.confirmYes);

            nameCf.setText("Name: " + name);
            timeCf.setText("Time: " + time);
            dateCf.setText("Date: " + date);
            weatherCf.setText("Weather: " + selectedWeather);
            commentCf.setText("Comment: " + comment);

            confirmDialog.setView(viewDialog);
            AlertDialog dialog = confirmDialog.create();
            dialog.show();

            cfCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            cfYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Observation observation = new Observation();
                    observation.observation_name = name;
                    observation.observation_time = time;
                    observation.observation_date = date;
                    observation.observation_weather = selectedWeather;
                    observation.observation_comment = comment;
                    observation.ob_hike_id = hikeId;

                    appDatabase.observationDao().insertObservation(observation);
                    Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    // Navigate back to the ObservationFragment for the current hike
                    ObservationFragment observationFragment = new ObservationFragment();
                    Bundle args = new Bundle();
                    args.putLong("h_id", hikeId);
                    observationFragment.setArguments(args);

                    onReplaceFrame(observationFragment);
                }
            });
        }
    }

    public void onReplaceFrame(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.commit();
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            LocalDate currentDate = LocalDate.now();
            int year = currentDate.getYear();
            int month = currentDate.getMonthValue();
            int day = currentDate.getDayOfMonth();

            return new DatePickerDialog(getActivity(), this, year, month - 1, day);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            LocalDate selectedDate = LocalDate.of(year, month + 1, day);
            ((AddObservationFragment) getParentFragment()).updateDate(selectedDate);
        }
    }

    public void updateDate(LocalDate selectedDate) {
        TextView obDate = v.findViewById(R.id.obDate);
        String formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        obDate.setText(formattedDate);
    }
}
