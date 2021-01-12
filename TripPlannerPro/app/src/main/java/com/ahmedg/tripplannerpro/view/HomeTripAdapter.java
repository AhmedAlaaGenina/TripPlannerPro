package com.ahmedg.tripplannerpro.view;

import android.app.Activity;
import android.content.Context;
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

public class HomeTripAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TripModel> tripModelArrayList = new ArrayList<>();
    Context context;

    //    public HomeTripAdapter(ArrayList<TripModel> tripModelArrayList) {
//        this.tripModelArrayList = tripModelArrayList;
//    }
    public void setDataList(List<TripModel> list) {
        this.tripModelArrayList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item_home, parent, false);
        this.context = parent.getContext();
        return new HomeTripViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TripModel tripModel = tripModelArrayList.get(position);
        HomeTripViewHolder tripViewHolder = (HomeTripViewHolder) holder;
        tripViewHolder.tripName.setText(tripModel.getTripName());
        tripViewHolder.source.setText(tripModel.getSource());
        tripViewHolder.destination.setText(tripModel.getDestination());
        tripViewHolder.date.setText(tripModel.getDate());
        tripViewHolder.time.setText(tripModel.getTime());
        tripViewHolder.btnNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                (Activity)context.getActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.container, new NoteFragment(), AddTripFragment.NOTE_FRAGMENT)
//                        .addToBackStack(null)
//                        .commit();
            }
        });
        if (tripModel.isStatus()) {
            tripViewHolder.status.setVisibility(View.VISIBLE);
            tripViewHolder.status.setImageResource(R.drawable.status_done);

        } else {
            tripViewHolder.status.setVisibility(View.VISIBLE);
            tripViewHolder.status.setImageResource(R.drawable.status_cancel);
        }

    }

    @Override
    public int getItemCount() {
        return tripModelArrayList.size();
    }

    class HomeTripViewHolder extends RecyclerView.ViewHolder {
        TextView tripName, source, destination, date, time;
        ImageView status;
        Button btnNotes;

        public HomeTripViewHolder(@NonNull View itemView) {
            super(itemView);
            tripName = itemView.findViewById(R.id.tridNameTv);
            source = itemView.findViewById(R.id.sourceTripTv);
            destination = itemView.findViewById(R.id.destinationTripTv);
            date = itemView.findViewById(R.id.dateTv);
            time = itemView.findViewById(R.id.timeTv);
            status = itemView.findViewById(R.id.statusIv);
            btnNotes = itemView.findViewById(R.id.notesBtn);

        }
    }
}
