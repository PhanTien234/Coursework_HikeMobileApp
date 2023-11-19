package com.example.testcw.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.testcw.R;
import com.example.testcw.activities.HikeAdapter;
import com.example.testcw.databases.AppDatabase;
import com.example.testcw.models.Hike;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class SearchFragment extends Fragment implements HikeAdapter.OnClickListener {
    private AppDatabase appDatabase;
    private HikeAdapter hikeAdapter;
    private List<Hike> hikeList;
    String name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        appDatabase = Room.databaseBuilder(getActivity(), AppDatabase.class, "hike_database_db")
                .allowMainThreadQueries().build();

        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        MaterialButton searchBtn = (MaterialButton) v.findViewById(R.id.searchButton);
        searchBtn.setOnClickListener(view -> {
            EditText searchText = v.findViewById(R.id.searchByName);
            name = "%" + searchText.getText().toString() + "%";
            hikeList = appDatabase.hikeDao().searchHikeName(name);
            hikeAdapter = new HikeAdapter(getContext(),hikeList, this);
            recyclerView.setAdapter(hikeAdapter);
        });

        return v;
    }


    public void onReplaceFrame(Fragment fragment){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.commit();
    }

    public void setDataFragment(Hike hike, Fragment fragment){
        Bundle result = new Bundle();
        result.putLong("h_id",hike.hike_id);
        result.putString("h_name",hike.name);
        result.putString("h_location",hike.location);
        result.putString("h_date",hike.date);
        result.putString("h_parking",hike.parking);
        result.putString("h_length",hike.length);
        result.putString("h_level",hike.level);
        result.putString("h_description",hike.description);
        fragment.setArguments(result);
        getParentFragmentManager().setFragmentResult("h_data",result);
    }

    @Override
    public void onDeleteClick(Hike hike){
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.trash)
                .setTitle(R.string.delete_hike)
                .setMessage("Are you sure to delete this hike "+hike.name+" ?")
                .setPositiveButton(R.string.delete, (dialog, which)->{
                    appDatabase.hikeDao().deleteHike(hike);
                    hikeList.remove(hike);
                    hikeAdapter.notifyDataSetChanged();
                })
                .setNegativeButton(R.string.cancel, null)
                .setCancelable(true)
                .show();
    }

    public void deleteAll(){
    }

}