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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.HikeAppCW.R;
import com.example.HikeAppCW.databases.DatabaseHelper;
import com.example.HikeAppCW.models.Observation;
import com.google.android.material.button.MaterialButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class AddObservationFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private long hikeId;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_observation_fragment, container, false);
        databaseHelper = new DatabaseHelper(getContext());

        hikeId = getArguments().getLong("hike_id");

        AutoCompleteTextView weatherLevel = view.findViewById(R.id.weatherLevel);
        String[] weatherLevels = getResources().getStringArray(R.array.weather_list);
        ArrayAdapter<String> adapterWeather = new ArrayAdapter<>(getContext(), R.layout.list_dropdown, weatherLevels);
        weatherLevel.setAdapter(adapterWeather);

        TextView obDate = view.findViewById(R.id.dateObser);
        obDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getChildFragmentManager(),"Date Picker1");
            }
        });

        TextView obTime = view.findViewById(R.id.timeObser);
        obTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        MaterialButton addObButton = view.findViewById(R.id.btnAddObser);
        addObButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getObData();
            }
        });

        ImageView backObservationFragment2 = view.findViewById(R.id.backObservationFragment2);
        backObservationFragment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObservationFragment observationFragment = new ObservationFragment();
                Bundle args = new Bundle();
                args.putLong("h_id", hikeId);
                observationFragment.setArguments(args);
                onReplaceFrame(observationFragment);
            }
        });

        return view;
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
                        TextView obTime = view.findViewById(R.id.timeObser);
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
        EditText obName = view.findViewById(R.id.nameObser);
        TextView obTime = view.findViewById(R.id.timeObser);
        TextView obDate = view.findViewById(R.id.dateObser);
        MultiAutoCompleteTextView obComment = view.findViewById(R.id.commentObser);
        AutoCompleteTextView weatherLevel = view.findViewById(R.id.weatherLevel);

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

            TextView nameCf = viewDialog.findViewById(R.id.cfNameObser);
            TextView timeCf = viewDialog.findViewById(R.id.cfTimeObser);
            TextView dateCf = viewDialog.findViewById(R.id.cfDateObser);
            TextView weatherCf = viewDialog.findViewById(R.id.cfWeatherObser);
            TextView commentCf = viewDialog.findViewById(R.id.cfDescriptionObser);

            MaterialButton cfCancel = viewDialog.findViewById(R.id.btnCancerCf);
            MaterialButton cfYes = viewDialog.findViewById(R.id.btnYesCf);

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
                    observation.setObservationName(name);
                    observation.setObservationTime(time);
                    observation.setObservationDate(date);
                    observation.setObservationWeather(selectedWeather);
                    observation.setObservationComment(comment);
                    observation.setObHikeId(hikeId);

                    databaseHelper.insertObservation(observation);
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
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.layoutFrames, fragment);
        fragmentTransaction.commit();
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
        TextView obDate = view.findViewById(R.id.dateObser);
        String formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        obDate.setText(formattedDate);
    }
}
