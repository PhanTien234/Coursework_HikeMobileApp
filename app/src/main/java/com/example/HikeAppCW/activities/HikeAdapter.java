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
import com.example.HikeAppCW.fragments.EditHikeFragment;
import com.example.HikeAppCW.fragments.ObservationFragment;
import com.example.HikeAppCW.models.Hike;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.MyViewHolder> {
    Context context;
    List<Hike> hikes;
    private OnClickListener onClickListener;

    public HikeAdapter(Context context, List<Hike> hikes, OnClickListener onClickListener) {
        this.context = context;
        this.hikes = hikes;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_card, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Hike hike = hikes.get(position);
        holder.nameCard.setText(hike.getName());

        holder.nameCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new EditHikeFragment();
                onClickListener.setDataFragment(hike, fragment);
                onClickListener.onReplaceFrame(fragment);
            }
        });

        holder.more.setOnClickListener(view -> {
            Fragment fragment = new ObservationFragment();
            onClickListener.setDataFragment(hike, fragment);
            onClickListener.onReplaceFrame(fragment);
        });

        holder.delete.setOnClickListener(view -> {
            onClickListener.onDeleteClick(hike);
        });
    }

    @Override
    public int getItemCount() {
        return hikes.size();
    }

    public interface OnClickListener {
        void onDeleteClick(Hike hike);

        void onReplaceFrame(Fragment fragment);

        void setDataFragment(Hike hike, Fragment fragment);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameCard;
        MaterialButton more, delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameCard = itemView.findViewById(R.id.nameCard);
            more = itemView.findViewById(R.id.moreCard);
            delete = itemView.findViewById(R.id.deleteCard);
        }
    }
}
