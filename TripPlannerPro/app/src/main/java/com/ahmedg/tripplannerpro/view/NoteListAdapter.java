package com.ahmedg.tripplannerpro.view;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.ahmedg.tripplannerpro.R;

import java.util.ArrayList;

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
        return noteList.size();
    }

    public void setDataList(ArrayList<String> noteList) {
        this.noteList = noteList;
        notifyDataSetChanged();
    }
    public ArrayList<String> getDataList(){
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
