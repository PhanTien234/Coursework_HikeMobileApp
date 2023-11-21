package com.example.HikeAppCW.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.HikeAppCW.R;
import com.example.HikeAppCW.databases.DatabaseHelper;
import com.example.HikeAppCW.models.Hike;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.Locale;

public class AddHikeFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private String parking;
    private View v;
    private TextView dateHike;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add, container, false);

        // Initialize your SQLite database helper
        databaseHelper = new DatabaseHelper(getContext());

        String[] item = getResources().getStringArray(R.array.level_list);
        AutoCompleteTextView autoCompleteTextView = v.findViewById(R.id.levelHike);
        ArrayAdapter<String> adapterItem = new ArrayAdapter<>(getContext(), R.layout.list_dropdown, item);
        autoCompleteTextView.setAdapter(adapterItem);

        dateHike = v.findViewById(R.id.dateHike);
        dateHike.setOnClickListener(view -> showDatePickerDialog());

        MaterialButton addButton = v.findViewById(R.id.btnAdd);
        addButton.setOnClickListener(view -> getData());

        return v;
    }

    private void getData() {
        EditText getName, getLocation, getLength, getLevel, getDescription;
        TextView getDate, nameCf, locationCf, dateCf, parkingCf, lengthCf, levelCf, descriptionCf;
        MaterialButton cfCancel, cfYes;
        RadioButton radioYes, radioNo;

        getName = v.findViewById(R.id.nameHike);
        getDate = v.findViewById(R.id.dateHike);
        getLocation = v.findViewById(R.id.locationHike);
        getLength = v.findViewById(R.id.lengthHike);
        getLevel = v.findViewById(R.id.levelHike);
        getDescription = v.findViewById(R.id.descriptionHike);

        radioYes = v.findViewById(R.id.radioYes);
        radioNo = v.findViewById(R.id.radioNo);

        String name = getName.getText().toString();
        String location = getLocation.getText().toString();
        String length = getLength.getText().toString();
        String level = getLevel.getText().toString();
        String description = getDescription.getText().toString();
        String date = getDate.getText().toString();

        parking = radioYes.isChecked() ? radioYes.getText().toString() : radioNo.getText().toString();

        if (name.isEmpty() || location.isEmpty() || length.isEmpty() || level.isEmpty() || date.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {

            AlertDialog.Builder confirmDialog = new AlertDialog.Builder(getContext());
            View viewDialog = getLayoutInflater().inflate(R.layout.alert_dialog, null);

            nameCf = viewDialog.findViewById(R.id.cfName);
            locationCf = viewDialog.findViewById(R.id.cfLocation);
            dateCf = viewDialog.findViewById(R.id.cfDate);
            parkingCf = viewDialog.findViewById(R.id.cfParking);
            lengthCf = viewDialog.findViewById(R.id.cfLength);
            levelCf = viewDialog.findViewById(R.id.cfLevel);
            descriptionCf = viewDialog.findViewById(R.id.cfDescription);

            cfCancel = viewDialog.findViewById(R.id.btnCancerCf);
            cfYes = viewDialog.findViewById(R.id.btnYesCf);

            nameCf.setText("Name: " + name);
            locationCf.setText("Location: " + location);
            dateCf.setText("Date of the Hike: " + date);
            parkingCf.setText("Parking: " + parking);
            lengthCf.setText("Length of the Hike: " + length);
            levelCf.setText("Difficulty level: " + level);
            descriptionCf.setText("Description: " + description);

            confirmDialog.setView(viewDialog);
            AlertDialog dialog = confirmDialog.create();
            dialog.show();
            cfCancel.setOnClickListener(view -> {
                dialog.dismiss();
            });

            cfYes.setOnClickListener(view -> {
                Hike hike = new Hike();
                hike.setName(name);
                hike.setLocation(location);
                hike.setDate(date);
                hike.setParking(parking);
                hike.setLength(length);
                hike.setLevel(level);
                hike.setDescription(description);

                // Use the SQLiteDatabase instance to insert the hike
                databaseHelper.insertHike(hike);
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

                onReplaceFrame(new HikeFragment());
            });
        }
    }

    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getChildFragmentManager(), "Date Picker");
    }

    public void onReplaceFrame(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.layoutFrames, fragment);
        ft.commit();
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Display the date in the TextView
            String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month + 1, year);
            ((AddHikeFragment) getParentFragment()).updateDate(selectedDate);
        }
    }

    public void updateDate(String selectedDate) {
        dateHike.setText(selectedDate);
    }
}
