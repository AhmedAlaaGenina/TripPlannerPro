package com.ahmedg.tripplannerpro.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedg.tripplannerpro.R;
import com.ahmedg.tripplannerpro.model.TripModel;

import java.util.ArrayList;

public class HistoryTripAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<TripModel> tripModelArrayList = new ArrayList<>();

    int pos;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item_history, parent, false);

        return new HistoryTripViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        pos = position;
        TripModel tripModel = tripModelArrayList.get(position);
        HistoryTripViewHolder tripViewHolder = (HistoryTripViewHolder) holder;
        tripViewHolder.tripName.setText(tripModel.getTripName());
        tripViewHolder.source.setText(tripModel.getSource());
        tripViewHolder.destination.setText(tripModel.getDestination());
        if (tripModel.isStatus()) {
            tripViewHolder.status.setVisibility(View.VISIBLE);
            tripViewHolder.status.setImageResource(R.drawable.status_done);

        } else {
            tripViewHolder.status.setVisibility(View.VISIBLE);
            tripViewHolder.status.setImageResource(R.drawable.status_cancel);
        }

    }

    public void setDataHistory(TripModel list) {
        tripModelArrayList.add(pos, list);
        Log.i("TAG", "setDataList: "+list);
    }

    @Override
    public int getItemCount() {
        return tripModelArrayList == null ? 0 : tripModelArrayList.size();
    }

    class HistoryTripViewHolder extends RecyclerView.ViewHolder {
        TextView tripName, source, destination;
        ImageView status;

        public HistoryTripViewHolder(@NonNull View itemView) {
            super(itemView);
            tripName = itemView.findViewById(R.id.tridNameTv);
            source = itemView.findViewById(R.id.sourceTripTv);
            destination = itemView.findViewById(R.id.destinationTripTv);
            status = itemView.findViewById(R.id.statusIv);
        }
    }
}
