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
import androidx.room.Room;

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
import com.example.HikeAppCW.databases.AppDatabase;
import com.example.HikeAppCW.models.Hike;
import com.google.android.material.button.MaterialButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddHikeFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddHikeFragment extends Fragment {

    private AppDatabase appDatabase;
    private String parking;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_add, container, false);

         appDatabase = Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class, "hike_database_db")
                .allowMainThreadQueries() // For simplicity, don't use this in production
                .build();

        String[] item = getResources().getStringArray(R.array.level_list);
        AutoCompleteTextView autoCompleteTextView = v.findViewById(R.id.hikeLevel);
        ArrayAdapter<String> adapterItem = new ArrayAdapter<String>(getContext(),R.layout.dropdown_list, item);
        autoCompleteTextView.setAdapter(adapterItem);

         TextView dateHike = v.findViewById(R.id.hikeDate);
         dateHike.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getChildFragmentManager(),"Date Picker");
             }
         });


       MaterialButton addButton = (MaterialButton) v.findViewById(R.id.addButton);
       addButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                getData();
           }
       });

       return v;
    }

    private void getData(){
        EditText getName, getLocation, getLength, getLevel, getDescription;
        TextView getDate, nameCf, locationCf, dateCf,parkingCf, lengthCf, levelCf, descriptionCf;
        MaterialButton cfCancel, cfYes;
        RadioButton radioYes, radioNo;

        getName = getView().findViewById(R.id.hikeName);
        getDate = getView().findViewById(R.id.hikeDate);
        getLocation = getView().findViewById(R.id.hikeLocation);
        getLength = getView().findViewById(R.id.hikeLength);
        getLevel = getView().findViewById(R.id.hikeLevel);
        getDescription = getView().findViewById(R.id.hikeDescription);

        radioYes = getView().findViewById(R.id.radioYes);
        radioNo = getView().findViewById(R.id.radioNo);

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

            nameCf = viewDialog.findViewById(R.id.confirmName);
            locationCf = viewDialog.findViewById(R.id.confirmLocation);
            dateCf = viewDialog.findViewById(R.id.confirmDate);
            parkingCf = viewDialog.findViewById(R.id.confirmParking);
            lengthCf = viewDialog.findViewById(R.id.confirmLength);
            levelCf = viewDialog.findViewById(R.id.confirmLevel);
            descriptionCf = viewDialog.findViewById(R.id.confirmDescription);

            cfCancel = viewDialog.findViewById(R.id.confirmCancel);
            cfYes = viewDialog.findViewById(R.id.confirmYes);

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
                hike.name = name;
                hike.location = location;
                hike.date = date;
                hike.parking = parking;
                hike.length = length;
                hike.level = level;
                hike.description = description;

                appDatabase.hikeDao().insertHike(hike);
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

                onReplaceFrame(new HomeFragment());
            });
        }
    }
    public void onReplaceFrame(Fragment fragment){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.commit();
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
        {
            LocalDate d = LocalDate.now();
            int year = d.getYear();
            int month = d.getMonthValue();
            int day = d.getDayOfMonth();
            return new DatePickerDialog(getActivity(), this, year, --month, day);}
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day){
            LocalDate dob = LocalDate.of(year, ++month, day);
            ((AddHikeFragment)getParentFragment()).updateDateOfBirth(dob);
        }
    }

    public void updateDateOfBirth(LocalDate dob){
        TextView dobControl = getView().findViewById(R.id.hikeDate);
        // Format the date in "dd/MM/yyyy" format
        String formattedDate = dob.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        dobControl.setText(formattedDate);
    }

}