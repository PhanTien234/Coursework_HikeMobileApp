package com.example.HikeAppCW.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.HikeAppCW.R;
import com.example.HikeAppCW.databases.DatabaseHelper;
import com.example.HikeAppCW.models.Hike;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.Locale;

public class EditHikeFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private String name, location, date, parking, length, level, description;
    private long id;
    EditText editName, editLocation, editLevel, editLength, editDescription;
    TextView editDate;
    RadioButton editYes, editNo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_edit_hike, container, false);

        databaseHelper = new DatabaseHelper(getActivity().getApplicationContext());

        editName = v.findViewById(R.id.editHikeName);
        editLocation = v.findViewById(R.id.editHikeLocation);
        editLength = v.findViewById(R.id.editHikeLength);
        editLevel = v.findViewById(R.id.editHikeLevel);
        editDescription = v.findViewById(R.id.editHikeDescription);
        editDate = v.findViewById(R.id.editHikeDate);
        editYes = v.findViewById(R.id.editRadioYes);
        editNo = v.findViewById(R.id.editRadioNo);

        getDataHike();

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getChildFragmentManager(), "Date Picker");
            }
        });

        MaterialButton updateBtn = v.findViewById(R.id.updateButton);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateButton();
            }
        });

        ImageView backHikeFragment = v.findViewById(R.id.backHikeFragment);
        backHikeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onReplaceFrame(new HikeFragment());
            }
        });
        return v;
    }

    private void getDataHike() {

        getParentFragmentManager().setFragmentResultListener("h_data", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                name = result.getString("h_name");
                editName.setText(name);

                location = result.getString("h_location");
                editLocation.setText(location);

                date = result.getString("h_date");
                editDate.setText(date);

                length = result.getString("h_length");
                editLength.setText(length);

                level = result.getString("h_level");
                editLevel.setText(level);

                description = result.getString("h_description");
                editDescription.setText(description);

                parking = result.getString("h_parking");
                if (parking.equals("Yes")) {
                    editYes.setChecked(true);
                } else {
                    editNo.setChecked(true);
                }
                id = result.getLong("h_id");

                String[] itemList = getResources().getStringArray(R.array.level_list);
                AutoCompleteTextView autoCompleteTextView = getView().findViewById(R.id.editHikeLevel);
                ArrayAdapter<String> adapterItem = new ArrayAdapter<String>(getContext(), R.layout.list_dropdown, itemList);
                autoCompleteTextView.setAdapter(adapterItem);

            }
        });

    }
    public void updateButton() {
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.shield)
                .setTitle(R.string.update_hike)
                .setMessage("Are you sure to update this hike?")
                .setPositiveButton(R.string.update, (dialog, which) -> {
                    Hike updatedHike = editHike(); // Get the updated hike
                    databaseHelper.updateHike(updatedHike); // Update the database with the new data
                    onReplaceFrame(new HikeFragment());
                })
                .setNegativeButton(R.string.cancel, null)
                .setCancelable(true)
                .show();
    }

    public void onReplaceFrame(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.layoutFrames, fragment);
        ft.commit();
    }

    private Hike editHike() {
        name = editName.getText().toString();
        location = editLocation.getText().toString();
        date = editDate.getText().toString();
        if (editYes.isChecked()) {
            parking = "Yes";
        } else {
            parking = "No";
        }
        length = editLength.getText().toString();
        level = editLevel.getText().toString();
        description = editDescription.getText().toString();

        Hike hike = new Hike(name, location, date, parking, length, level, description);
        hike.setId(id); // Set the ID of the existing hike
        return hike;
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(requireContext(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Display the date in the TextView
            String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month + 1, year);
            ((EditHikeFragment) getParentFragment()).updateDate(selectedDate);
        }
    }
    public void updateDate(String selectedDate) {
        editDate.setText(selectedDate);
    }

}
