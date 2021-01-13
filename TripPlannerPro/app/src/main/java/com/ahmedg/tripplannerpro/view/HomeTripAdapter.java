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

    @NonNull
    @Override
    public HomeTripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeTripViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_item_home, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeTripViewHolder holder, int position) {
        holder.tripName.setText(tripModelArrayList.get(position).getTripName());
        holder.source.setText(tripModelArrayList.get(position).getSource());
        holder.destination.setText(tripModelArrayList.get(position).getDestination());
        holder.date.setText(tripModelArrayList.get(position).getDate());
        holder.time.setText(tripModelArrayList.get(position).getTime());

        if (tripModelArrayList.get(position).isStatus()) {
            holder.status.setVisibility(View.VISIBLE);
            holder.status.setImageResource(R.drawable.status_done);

        } else {
            holder.status.setVisibility(View.VISIBLE);
            holder.status.setImageResource(R.drawable.status_cancel);
        }

    }

    public ArrayList<TripModel> getModelArrayList() {
        return tripModelArrayList;
    }

    @Override
    public int getItemCount() {
        return tripModelArrayList == null ? 0 : tripModelArrayList.size();
    }

    public void setDataList(ArrayList<TripModel> list) {
        this.tripModelArrayList = list;
        notifyDataSetChanged();
    }

    class HomeTripViewHolder extends RecyclerView.ViewHolder {
        TextView tripName, source, destination, date, time;
        ImageView status;

        public HomeTripViewHolder(@NonNull View itemView) {
            super(itemView);
            tripName = itemView.findViewById(R.id.tridNameTv);
            source = itemView.findViewById(R.id.sourceTripTv);
            destination = itemView.findViewById(R.id.destinationTripTv);
            date = itemView.findViewById(R.id.dateTv);
            time = itemView.findViewById(R.id.timeTv);
            status = itemView.findViewById(R.id.statusIv);
        }
    }
}
