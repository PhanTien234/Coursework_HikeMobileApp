package com.example.HikeAppCW.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.HikeAppCW.R;
import com.example.HikeAppCW.activities.HikeAdapter;
import com.example.HikeAppCW.databases.DatabaseHelper;
import com.example.HikeAppCW.models.Hike;
import com.example.HikeAppCW.models.Observation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HikeFragment extends Fragment implements HikeAdapter.OnClickListener {

    private DatabaseHelper databaseHelper;
    private HikeAdapter hikeAdapter;
    private List<Hike> hikeList;
    private List<Observation> observationList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        databaseHelper = new DatabaseHelper(getContext());

        FloatingActionButton deleteAll = v.findViewById(R.id.deleteAll);
        deleteAll.setOnClickListener(view -> deleteAll());

        RecyclerView recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        hikeList = databaseHelper.getAllHikes();
        hikeAdapter = new HikeAdapter(getContext(), hikeList, this);
        recyclerView.setAdapter(hikeAdapter);

        return v;
    }

    public void onReplaceFrame(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.commit();
    }

    public void setDataFragment(Hike hike, Fragment fragment) {
        Bundle result = new Bundle();
        result.putLong("h_id", hike.getId());
        result.putString("h_name", hike.getName());
        result.putString("h_location", hike.getLocation());
        result.putString("h_date", hike.getDate());
        result.putString("h_parking", hike.getParking());
        result.putString("h_length", hike.getLength());
        result.putString("h_level", hike.getLevel());
        result.putString("h_description", hike.getDescription());
        fragment.setArguments(result);
        getParentFragmentManager().setFragmentResult("h_data", result);
    }

    @Override
    public void onDeleteClick(Hike hike) {
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.trash)
                .setTitle(R.string.delete_hike)
                .setMessage("Are you sure to delete this hike " + hike.getName() + " ?")
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    databaseHelper.deleteHike(hike.getId());
                    hikeList.remove(hike);
                    hikeAdapter.notifyDataSetChanged();
                })
                .setNegativeButton(R.string.cancel, null)
                .setCancelable(true)
                .show();
    }

    public void deleteAll() {
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.trash)
                .setTitle(R.string.delete_hike)
                .setMessage("Are you sure to delete all hikes ?")
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    databaseHelper.deleteAllHikes();
                    hikeList.clear();
                    hikeAdapter.notifyDataSetChanged();
                })
                .setNegativeButton(R.string.cancel, null)
                .setCancelable(true)
                .show();
    }
}
