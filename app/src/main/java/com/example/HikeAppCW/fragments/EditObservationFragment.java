package com.example.HikeAppCW.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

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

import com.example.HikeAppCW.R;
import com.example.HikeAppCW.databases.DatabaseHelper;
import com.example.HikeAppCW.models.Observation;
import com.google.android.material.button.MaterialButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class EditObservationFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private String name, time, date, weather, comment;
    private long id, hikeId;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_edit_observation, container, false);

        databaseHelper = new DatabaseHelper(getContext());

        id = getArguments().getLong("ob_id");
        name = getArguments().getString("ob_name");
        time = getArguments().getString("ob_time");
        date = getArguments().getString("ob_date");
        weather = getArguments().getString("ob_weather");
        comment = getArguments().getString("ob_comment");
        hikeId = getArguments().getLong("ob_hike_id");

        EditText editObName = v.findViewById(R.id.editObName);
        TextView obTimeEdit = v.findViewById(R.id.obTimeEdit);
        TextView obDateEdit = v.findViewById(R.id.obDateEdit);
        AutoCompleteTextView weatherLevelEdit = v.findViewById(R.id.weatherLevelEdit);
        MultiAutoCompleteTextView editObComment = v.findViewById(R.id.editObComment);

        // Set data from the selected observation
        editObName.setText(name);
        obTimeEdit.setText(time);
        obDateEdit.setText(date);
        weatherLevelEdit.setText(weather);
        editObComment.setText(comment);

        obTimeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        obDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        // Populate the weather dropdown
        String[] weatherLevels = getResources().getStringArray(R.array.weather_list);
        ArrayAdapter<String> adapterWeather = new ArrayAdapter<>(getContext(), R.layout.list_dropdown, weatherLevels);
        weatherLevelEdit.setAdapter(adapterWeather);

        ImageView backObservationFragment = v.findViewById(R.id.backObservationFragment);
        backObservationFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObservationFragment observationFragment = new ObservationFragment();
                Bundle args = new Bundle();
                args.putLong("h_id", hikeId);
                observationFragment.setArguments(args);
                onReplaceFrame(observationFragment);
            }
        });
        MaterialButton editObButton = v.findViewById(R.id.editObButton);
        editObButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateObservation();
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
                        TextView obTimeEdit = v.findViewById(R.id.obTimeEdit);
                        obTimeEdit.setText(formattedTime);
                    }
                },
                hour,
                minute,
                true // 24-hour format
        );

        // Show the TimePickerDialog
        timePickerDialog.show();
    }

    private void showDatePicker() {
        // Get the current date
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        int day = currentDate.getDayOfMonth();

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        LocalDate selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay);
                        updateDate(selectedDate);
                    }
                },
                year,
                month - 1,
                day
        );

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void updateDate(LocalDate selectedDate) {
        TextView obDateEdit = v.findViewById(R.id.obDateEdit);
        String formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        obDateEdit.setText(formattedDate);
    }

    private void updateObservation() {
        EditText editObName = v.findViewById(R.id.editObName);
        TextView obTimeEdit = v.findViewById(R.id.obTimeEdit);
        TextView obDateEdit = v.findViewById(R.id.obDateEdit);
        AutoCompleteTextView weatherLevelEdit = v.findViewById(R.id.weatherLevelEdit);
        MultiAutoCompleteTextView editObComment = v.findViewById(R.id.editObComment);

        String updatedName = editObName.getText().toString();
        String updatedTime = obTimeEdit.getText().toString();
        String updatedDate = obDateEdit.getText().toString();
        String updatedWeather = weatherLevelEdit.getText().toString();
        String updatedComment = editObComment.getText().toString();

        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.updated)
                .setTitle("Update Observations")
                .setMessage("Are you sure to update this observation?")
                .setPositiveButton(R.string.update, (dialog, which) -> {
                    Observation observation = new Observation();
                    observation.setObservation_id(id);
                    observation.setObservation_name(updatedName);
                    observation.setObservation_time(updatedTime);
                    observation.setObservation_date(updatedDate);
                    observation.setObservation_weather(updatedWeather);
                    observation.setObservation_comment(updatedComment);
                    observation.setOb_hike_id(hikeId);

                    databaseHelper.updateObservation(observation);

                    // Optionally, you can navigate back to the ObservationFragment
                    // for the current hike after updating
                    ObservationFragment observationFragment = new ObservationFragment();
                    Bundle args = new Bundle();
                    args.putLong("h_id", hikeId);
                    observationFragment.setArguments(args);
                    onReplaceFrame(observationFragment);

                    dialog.dismiss();
                })
                .setNegativeButton(R.string.cancel, null)
                .setCancelable(true)
                .show();
    }

    public void onReplaceFrame(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.commit();
    }
}
