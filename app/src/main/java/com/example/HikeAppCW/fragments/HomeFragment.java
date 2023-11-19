package com.example.HikeAppCW.fragments;

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

import com.example.HikeAppCW.R;
import com.example.HikeAppCW.activities.HikeAdapter;
import com.example.HikeAppCW.databases.AppDatabase;
import com.example.HikeAppCW.models.Hike;
import com.example.HikeAppCW.models.Observation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HomeFragment extends Fragment implements HikeAdapter.OnClickListener{

    private AppDatabase appDatabase;
    private HikeAdapter hikeAdapter;
    private List<Hike> hikeList;
    private List<Observation> observationList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        appDatabase = Room.databaseBuilder(getActivity(), AppDatabase.class, "hike_database_db")
                .allowMainThreadQueries().build();

        FloatingActionButton deleteAll = (FloatingActionButton) v.findViewById(R.id.deleteAll);
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAll();
            }
        });

        RecyclerView recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        hikeList = appDatabase.hikeDao().getAllHike();
        hikeAdapter = new HikeAdapter(getContext(),hikeList, this);
        recyclerView.setAdapter(hikeAdapter);

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
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.trash)
                .setTitle(R.string.delete_hike)
                .setMessage("Are you sure to delete all hikes ?")
                .setPositiveButton(R.string.delete, (dialog, which)->{
                    appDatabase.hikeDao().deleteAll();
                    hikeList.removeAll(hikeList);

                    hikeAdapter.notifyDataSetChanged();
                })
                .setNegativeButton(R.string.cancel, null)
                .setCancelable(true)
                .show();
    }


}