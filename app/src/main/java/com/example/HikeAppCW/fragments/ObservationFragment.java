package com.example.HikeAppCW.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.HikeAppCW.R;
import com.example.HikeAppCW.activities.ObservationAdapter;
import com.example.HikeAppCW.databases.DatabaseHelper;
import com.example.HikeAppCW.models.Observation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class ObservationFragment extends Fragment implements ObservationAdapter.OnObservationClickListener {
    private DatabaseHelper databaseHelper;
    List<Observation> observations;
    ObservationAdapter observationAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_observation, container, false);

        databaseHelper = new DatabaseHelper(getActivity().getApplicationContext());

        long hike_id = getArguments().getLong("h_id");
        Bundle result = new Bundle();
        result.putLong("hike_id", hike_id);

        RecyclerView obRecyclerView = v.findViewById(R.id.observationRecyclerView);
        obRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FloatingActionButton newOb = v.findViewById(R.id.newObservation);
        newOb.setOnClickListener(view -> {
            Fragment fragment = new AddObservationFragment();
            fragment.setArguments(result);
            onObservationReplaceFrame(fragment);
        });
        FloatingActionButton deleteAllObservations = v.findViewById(R.id.deleteAllObserva);
        deleteAllObservations.setOnClickListener(view -> deleteAllObsers());

        observations = databaseHelper.getObservationsForHike(hike_id);
        observationAdapter = new ObservationAdapter(getContext(), observations, this);
        obRecyclerView.setAdapter(observationAdapter);

        ImageView backHikeFragment1 = v.findViewById(R.id.backHikeFragment1);
        backHikeFragment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onObservationReplaceFrame(new HikeFragment());
            }
        });
        return v;
    }

    public void onObservationReplaceFrame(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.commit();
    }

    public void setObservationDataFragment(Observation observation, Fragment fragment) {
        Bundle result = new Bundle();
        result.putLong("ob_id", observation.observation_id);
        result.putString("ob_name", observation.observation_name);
        result.putString("ob_time", observation.observation_time);
        result.putString("ob_date", observation.observation_date);
        result.putString("ob_weather", observation.observation_weather);
        result.putString("ob_comment", observation.observation_comment);
        result.putLong("ob_hike_id", observation.ob_hike_id);
        fragment.setArguments(result);
    }

    public void onObservationDeleteClick(Observation observation) {
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.trash)
                .setTitle(R.string.delete_ob)
                .setMessage("Are you sure to delete this observation " + observation.observation_name + " ?")
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    databaseHelper.deleteObservation(observation);
                    observations.remove(observation);
                    observationAdapter.notifyDataSetChanged();
                })
                .setNegativeButton(R.string.cancel, null)
                .setCancelable(true)
                .show();
    }

    public void deleteAllObsers() {
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.trash)
                .setTitle(R.string.delete_hike)
                .setMessage("Are you sure to delete all observations?")
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    databaseHelper.deleteAllObservations();
                    observations.clear();
                    observationAdapter.notifyDataSetChanged();
                })
                .setNegativeButton(R.string.cancel, null)
                .setCancelable(true)
                .show();
    }
}
