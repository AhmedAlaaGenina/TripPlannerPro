package com.ahmedg.tripplannerpro.view;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmedg.tripplannerpro.R;

import java.util.ArrayList;

public class NoteFragment extends Fragment {
    public static final String NOTES_KEY = "NOTES_KEY";
    View view;
    NoteListAdapter noteListAdapter;
    RecyclerView recyclerView;
    Button btnAddNote, btnSaveNotes;
    EditText edTextNote;
    ArrayList<String> list;
    ArrayList<String> eList;
    TextView tvEmpty;
    Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_note, container, false);
        noteListAdapter = new NoteListAdapter();
        recyclerView = view.findViewById(R.id.rcvNotes);
        btnAddNote = view.findViewById(R.id.btnAddNote);
        btnSaveNotes = view.findViewById(R.id.btnSaveNotes);
        edTextNote = view.findViewById(R.id.etNoteText);
        tvEmpty = view.findViewById(R.id.tv_empty);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        eList = new ArrayList<>();
        eList.add("No Notes");
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(noteListAdapter);
        bundle = new Bundle();

        if (list.size() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
        }


        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edTextNote.getText().toString())) {
                    String note = edTextNote.getText().toString();
                    list.add(note);
                    noteListAdapter.setDataList(list);
                    if (!list.isEmpty()) {
                        bundle.putStringArrayList(NOTES_KEY, list);
                    } else {
                        bundle.putStringArrayList(NOTES_KEY, eList);
                    }
                    edTextNote.setText("");
                    tvEmpty.setVisibility(View.GONE);
                    Log.i("TAG", "onCreateView: " + list.toString());
                } else {
                    Toast.makeText(getActivity(), "Text Is Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnSaveNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTripFragment tripFragment = new AddTripFragment();
                tripFragment.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, tripFragment, HomeFragment.ADD_TRIP_FRAGMENT)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            list.remove(viewHolder.getAdapterPosition());
            noteListAdapter.notifyDataSetChanged();
        }
    };
}