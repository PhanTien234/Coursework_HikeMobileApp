package com.example.HikeAppCW.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.HikeAppCW.R;
import com.example.HikeAppCW.fragments.EditObservationFragment;
import com.example.HikeAppCW.models.Observation;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.MyObservationViewHolder> {
    Context context;
    List<Observation> observations;
    private OnObservationClickListener onObservationClickListener;

    public ObservationAdapter(Context context, List<Observation> observations, OnObservationClickListener onObservationClickListener) {
        this.context = context;
        this.observations = observations;
        this.onObservationClickListener = onObservationClickListener;
    }

    @NonNull
    @Override
    public MyObservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.observation_card_item, parent, false);
        return new MyObservationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyObservationViewHolder holder, int position) {
        Observation observation = observations.get(position);
        holder.observationName.setText(observation.observation_name);


        holder.edit.setOnClickListener(view -> {
            Fragment fragment = new EditObservationFragment();
            onObservationClickListener.setObservationDataFragment(observation, fragment);
            onObservationClickListener.onObservationReplaceFrame(fragment);
        });

        holder.delete.setOnClickListener(view -> {
            onObservationClickListener.onObservationDeleteClick(observation);
        });


    }

    @Override
    public int getItemCount() {
        return observations.size();
    }

    public interface OnObservationClickListener{
        void onObservationDeleteClick(Observation observation);
        void onObservationReplaceFrame(Fragment fragment);
        void setObservationDataFragment(Observation observation, Fragment fragment);

    }

    public static class MyObservationViewHolder extends RecyclerView.ViewHolder{

        TextView observationName;

        MaterialButton edit, delete;
        public MyObservationViewHolder(@NonNull View itemView) {
            super(itemView);
            observationName = itemView.findViewById(R.id.cardObservationName);
            edit = itemView.findViewById(R.id.cardObservationEdit);
            delete = itemView.findViewById(R.id.cardObservationDelete);
        }
    }
}

