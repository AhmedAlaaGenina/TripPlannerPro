package com.ahmedg.tripplannerpro.view;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedg.tripplannerpro.R;
import com.ahmedg.tripplannerpro.model.TripModel;

import java.util.ArrayList;
import java.util.List;

public class HomeTripAdapter extends RecyclerView.Adapter<HomeTripAdapter.HomeTripViewHolder> {
    private ArrayList<TripModel> tripModelArrayList = new ArrayList<>();
    SetOnclickListener setOnclickListener;
    int pos;

    @NonNull
    @Override
    public HomeTripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeTripViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_item_home, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeTripViewHolder holder, int position) {
        pos = position;
        holder.tripName.setText(tripModelArrayList.get(position).getTripName());
        holder.source.setText(tripModelArrayList.get(position).getSource());
        holder.destination.setText(tripModelArrayList.get(position).getDestination());
        String[] time = tripModelArrayList.get(position).getTime().split("_");
        holder.time.setText(time[0]);
        holder.date.setText(time[1]);

        if (tripModelArrayList.get(position).getDirection().equals("One Direction")) {
            holder.direction.setVisibility(View.VISIBLE);
            holder.direction.setImageResource(R.drawable.arrow_forward);

        } else {
            holder.direction.setVisibility(View.VISIBLE);
            holder.direction.setImageResource(R.drawable.arrow_round);
        }

    }

    public void setOnItemClickListener(SetOnclickListener setOnclickListener) {
        this.setOnclickListener = setOnclickListener;
    }

    public ArrayList<TripModel> getModelArrayList() {
        return tripModelArrayList;
    }

    public TripModel getItem(int index) {
        return tripModelArrayList.get(index);
    }

    public int getId() {
        return pos;
    }

    @Override
    public int getItemCount() {
        return tripModelArrayList == null ? 0 : tripModelArrayList.size();
    }

    public void setDataList(ArrayList<TripModel> list) {
        this.tripModelArrayList = list;
        notifyDataSetChanged();
    }

    class HomeTripViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tripName, source, destination, date, time;
        ImageView direction;

        Button btnNotes, btnStart;

        public HomeTripViewHolder(@NonNull View itemView) {
            super(itemView);
            tripName = itemView.findViewById(R.id.tridNameTv);
            source = itemView.findViewById(R.id.sourceTripTv);
            destination = itemView.findViewById(R.id.destinationTripTv);
            date = itemView.findViewById(R.id.dateTv);
            time = itemView.findViewById(R.id.timeTv);
            btnNotes = itemView.findViewById(R.id.notesBtn);
            direction = itemView.findViewById(R.id.arrowDirectionIv);
            btnStart = itemView.findViewById(R.id.btnStart);
            itemView.setOnClickListener(this);
            itemView.setClickable(true);
            btnNotes.setOnClickListener(this);
            btnStart.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (setOnclickListener != null) {
                if (v == itemView) {
                    setOnclickListener.onItemClickListener(getAdapterPosition());
                }
                if (v == btnNotes) {
                    setOnclickListener.onNoteClickListener(getAdapterPosition());
                }
                if (v == btnStart) {
                    setOnclickListener.onStartClickListener(getAdapterPosition());
                }
            }
        }
    }
}

interface SetOnclickListener {
    void onItemClickListener(int index);

    void onNoteClickListener(int index);

    void onStartClickListener(int index);
}