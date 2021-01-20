package com.ahmedg.tripplannerpro.view;

import android.os.Bundle;
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
import com.ahmedg.tripplannerpro.model.TripModelHistory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HistoryTripAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TripModelHistory> tripModelArrayList = new ArrayList<>();
    private SetOnclickListenerHistory setOnclickListenerHistory;

    public void setOnItemClickListener(SetOnclickListenerHistory setOnclickListener) {
        this.setOnclickListenerHistory = setOnclickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item_history, parent, false);

        return new HistoryTripViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        TripModelHistory tripModelHistory = tripModelArrayList.get(position);
        HistoryTripViewHolder tripViewHolder = (HistoryTripViewHolder) holder;
        tripViewHolder.tripName.setText(tripModelHistory.getTripName());
        tripViewHolder.source.setText(tripModelHistory.getSource());
        tripViewHolder.destination.setText(tripModelHistory.getDestination());
        String x[] = tripModelHistory.getTime().split("_");
        tripViewHolder.time.setText(x[0].toString());
        tripViewHolder.date.setText(x[1].toString());
        if (tripModelHistory.isStatus()) {
            tripViewHolder.status.setVisibility(View.VISIBLE);
            tripViewHolder.status.setImageResource(R.drawable.done);

        } else {
            tripViewHolder.status.setVisibility(View.VISIBLE);
            tripViewHolder.status.setImageResource(R.drawable.ic_baseline_clear_24);
        }
        if (tripModelArrayList.get(position).getDirection().equals("One Direction")) {
            tripViewHolder.direction.setVisibility(View.VISIBLE);
            tripViewHolder.direction.setImageResource(R.drawable.arrow_forward);

        } else {
            tripViewHolder.direction.setVisibility(View.VISIBLE);
            tripViewHolder.direction.setImageResource(R.drawable.arrow_round);
        }

    }

    public List<TripModelHistory> getModelArrayList() {
        return tripModelArrayList;
    }

    public void setDataList(List<TripModelHistory> list) {
        this.tripModelArrayList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tripModelArrayList == null ? 0 : tripModelArrayList.size();
    }

    class HistoryTripViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tripName, source, destination, date, time;
        ImageView status, direction;
        Button btnMaps;

        public HistoryTripViewHolder(@NonNull View itemView) {
            super(itemView);
            tripName = itemView.findViewById(R.id.tridNameTv);
            source = itemView.findViewById(R.id.sourceTripTv);
            destination = itemView.findViewById(R.id.destinationTripTv);
            status = itemView.findViewById(R.id.statusIv);
            date = itemView.findViewById(R.id.dateTv);
            time = itemView.findViewById(R.id.timeTv);
            direction = itemView.findViewById(R.id.arrowDirectionIv);
            status = itemView.findViewById(R.id.statusIv);
            btnMaps = itemView.findViewById(R.id.mapBtn);
            btnMaps.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (setOnclickListenerHistory != null) {
                if (view == btnMaps) {
                    setOnclickListenerHistory.onMapClickListener(getAdapterPosition());
                }

            }
        }
    }

}

interface SetOnclickListenerHistory {
    void onMapClickListener(int index);
}