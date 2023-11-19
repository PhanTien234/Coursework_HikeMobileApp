package com.example.HikeAppCW.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
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

import com.example.HikeAppCW.R;
import com.example.HikeAppCW.databases.AppDatabase;
import com.example.HikeAppCW.models.Hike;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;

public class EditHikeFragment extends Fragment {
    private AppDatabase appDatabase;
    private String name, location, date, parking, length, level, description;
    private long id;
    EditText editName, editLocation,editLevel, editLength,  editDescription;
    TextView editDate;
    RadioButton editYes, editNo;

    private View v;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_edit_hike, container, false);

        appDatabase = Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class, "hike_database_db")
                .allowMainThreadQueries() // For simplicity, don't use this in production
                .build();
        editName = v.findViewById(R.id.editHikeName);
        editLocation = v.findViewById(R.id.editHikeLocation);
        editLength = v.findViewById(R.id.editHikeLength);
        editLevel = v.findViewById(R.id.editHikeLevel);
        editDescription = v.findViewById(R.id.editHikeDescription);
        editDate = v.findViewById(R.id.editHikeDate);
        editYes = v.findViewById(R.id.radioYesEdit);
        editNo = v.findViewById(R.id.radioNoEdit);

        getDataHike();

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getChildFragmentManager(),"Date Picker");
            }
        });

        MaterialButton updateBtn = v.findViewById(R.id.updateButton);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateButton();
            }
        });
        return v;
    }

    private void getDataHike(){

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
                if (parking.equals("Yes")){
                    editYes.setChecked(true);
                } else {
                    editNo.setChecked(true);
                }
                id = result.getLong("h_id");

                String[] itemList = getResources().getStringArray(R.array.level_list);
                AutoCompleteTextView autoCompleteTextView = v.findViewById(R.id.editHikeLevel);
                ArrayAdapter<String> adapterItem = new ArrayAdapter<String>(getContext(), R.layout.dropdown_list, itemList);
                autoCompleteTextView.setAdapter(adapterItem);

            }
        });

    }

    public void updateButton(){
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.updated)
                .setTitle(R.string.update_hike)
                .setMessage("Are you sure to update this hikes ?")
                .setPositiveButton(R.string.update, (dialog, which)->{
                    appDatabase.hikeDao().updateHike(editHike());
                    onReplaceFrame(new HomeFragment());
                })
                .setNegativeButton(R.string.cancel, null)
                .setCancelable(true)
                .show();

    }
    public void onReplaceFrame(Fragment fragment){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.commit();
    }

    private Hike editHike(){
        name = editName.getText().toString();
        location = editLocation.getText().toString();
        date = editDate.getText().toString();
        if (editYes.isChecked()){
            parking = "Yes";
        } else {
            parking = "No";
        }
        length = editLength.getText().toString();
        level = editLevel.getText().toString();
        description = editDescription.getText().toString();

        Hike hike = new Hike(id, name , location, date, parking, length, level, description);
        appDatabase.hikeDao().updateHike(hike);

        return hike;
    }
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
        {
            LocalDate d = LocalDate.now();
            int year = d.getYear();
            int month = d.getMonthValue();
            int day = d.getDayOfMonth();
            return new DatePickerDialog(getActivity(), this, year, --month, day);}
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day){
            ((EditHikeFragment)getParentFragment()).formatDOB(year, ++month, day);
        }

    }
    public void formatDOB(int year, int month, int day){

        String dayZero , monthZero;
        if (day<10){
            dayZero="0";
        } else {
            dayZero="";
        }
        if (month<10){
            monthZero="0";
        } else {
            monthZero="";
        }
        TextView date = getView().findViewById(R.id.editHikeDate);
        date.setText(dayZero+day+"/"+monthZero+month+"/"+year);
    }

}