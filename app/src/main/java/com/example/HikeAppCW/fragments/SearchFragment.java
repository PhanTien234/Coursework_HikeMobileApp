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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.HikeAppCW.R;
import com.example.HikeAppCW.activities.HikeAdapter;
import com.example.HikeAppCW.databases.AppDatabase;
import com.example.HikeAppCW.databases.DatabaseHelper;
import com.example.HikeAppCW.models.Hike;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class SearchFragment extends Fragment implements HikeAdapter.OnClickListener {
    private DatabaseHelper databaseHelper;
    private HikeAdapter hikeAdapter;
    private List<Hike> hikeList;
    String name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        databaseHelper = new DatabaseHelper(getContext());

        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        MaterialButton searchBtn = v.findViewById(R.id.searchButton);
        searchBtn.setOnClickListener(view -> {
            EditText searchText = v.findViewById(R.id.searchByName);

            if (searchText.getText().toString().trim().isEmpty()) {
                // Search text is empty, display a Toast
                Toast.makeText(getContext(), "Please type the hike you want to search", Toast.LENGTH_SHORT).show();
            } else {
                // Search text is not empty, perform the search
                name = "%" + searchText.getText().toString() + "%";
                hikeList = databaseHelper.searchHikeName(name);

                if (hikeList.isEmpty()) {
                    // Show a message when no results are found
                    recyclerView.setVisibility(View.GONE);
                    TextView noResultsMessage = v.findViewById(R.id.noResultsMessage);
                    noResultsMessage.setVisibility(View.VISIBLE);
                } else {
                    // Hide the message if there are results
                    recyclerView.setVisibility(View.VISIBLE);
                    TextView noResultsMessage = v.findViewById(R.id.noResultsMessage);
                    noResultsMessage.setVisibility(View.GONE);
                    hikeAdapter = new HikeAdapter(getContext(), hikeList, this);
                    recyclerView.setAdapter(hikeAdapter);
                }
            }
        });

        return v;
    }



    public void onReplaceFrame(Fragment fragment){
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

}