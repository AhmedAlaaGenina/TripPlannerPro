package com.ahmedg.tripplannerpro.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedg.tripplannerpro.R;

import java.util.ArrayList;

public class FloatingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<String> stringArrayList;
    Context context;
    public FloatingAdapter(Context context) {
        stringArrayList = new ArrayList<>();
        this.context = context;
    }
    public void setArrayList(ArrayList<String> stringArrayList){
        this.stringArrayList = stringArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.note_row,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String note = stringArrayList.get(position);
        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.noteTv.setText(note);

    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView noteTv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTv = itemView.findViewById(R.id.noteText);
        }
    }
}

