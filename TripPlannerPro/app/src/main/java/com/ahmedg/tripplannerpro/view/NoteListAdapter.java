package com.ahmedg.tripplannerpro.view;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.ahmedg.tripplannerpro.R;
import com.ahmedg.tripplannerpro.model.TripDataBase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteViewHolder> {
    private ArrayList<String> noteList = new ArrayList<>();


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.tvNoteName.setText(noteList.get(position));

    }

    @Override
    public int getItemCount() {
        return noteList == null ? 0 : noteList.size();
    }

    public void setDataList(ArrayList<String> noteList) {
        this.noteList = noteList;
        notifyDataSetChanged();
    }

    public ArrayList<String> getDataList() {
        return noteList;
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvNoteName;

        @SuppressLint("ResourceType")
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNoteName = itemView.findViewById(R.id.noteText);
        }
    }

}
