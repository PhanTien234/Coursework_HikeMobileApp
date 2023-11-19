package com.example.testcw.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testcw.R;
import com.example.testcw.activities.ObservationAdapter;
import com.example.testcw.databases.AppDatabase;
import com.example.testcw.models.Observation;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ObservationFragment extends Fragment implements ObservationAdapter.OnObservationClickListener {
    private AppDatabase appDatabase;
    List<Observation> observations;
    ObservationAdapter observationAdapter;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_observation, container, false);

        appDatabase = Room.databaseBuilder(getActivity(), AppDatabase.class, "hike_database_db")
                .allowMainThreadQueries().build();

        long hike_id = getArguments().getLong("h_id");
        Bundle result = new Bundle();
        result.putLong("hike_id",hike_id);

        RecyclerView obRecyclerView = v.findViewById(R.id.observationRecyclerView);
        obRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FloatingActionButton newOb = v.findViewById(R.id.newObservation);
        newOb.setOnClickListener(view -> {
            Fragment fragment = new AddObservationFragment();
            fragment.setArguments(result);
            onObservationReplaceFrame(fragment);
        });

        observations = appDatabase.observationDao().getAllObservation(hike_id);
        observationAdapter = new ObservationAdapter(getContext(), observations, this);
        obRecyclerView.setAdapter(observationAdapter);

        return v;
    }



    public void onObservationReplaceFrame(Fragment fragment){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.commit();
    }

    public void setObservationDataFragment(Observation observation, Fragment fragment){
        Bundle result = new Bundle();
        result.putLong("ob_id",observation.observation_id);
        result.putString("ob_name",observation.observation_name);
        result.putString("ob_time",observation.observation_time);
        result.putString("ob_date",observation.observation_date);
        result.putString("ob_weather",observation.observation_weather);
        result.putString("ob_comment",observation.observation_comment);
        result.putLong("ob_hike_id",observation.ob_hike_id);
        fragment.setArguments(result);
    }

    public void onObservationDeleteClick(Observation observation){
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.trash)
                .setTitle(R.string.delete_ob)
                .setMessage("Are you sure to delete this observation "+observation.observation_name+" ?")
                .setPositiveButton(R.string.delete, (dialog, which)->{
                    appDatabase.observationDao().deleteObservation(observation);
                    observations.remove(observation);
                    observationAdapter.notifyDataSetChanged();
                })
                .setNegativeButton(R.string.cancel, null)
                .setCancelable(true)
                .show();
    }
}