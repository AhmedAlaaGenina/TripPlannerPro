package com.ahmedg.tripplannerpro.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ahmedg.tripplannerpro.R;
import com.ahmedg.tripplannerpro.model.TripDataBase;

import java.util.ArrayList;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UpdateNotesFragment extends Fragment {
    private static final String NOTES_KEY_EDT = "edtNotes";
    View view;
    Bundle bundleNoteRoom;
    ArrayList<String> list;
    NoteListAdapter noteListAdapter;
    RecyclerView recyclerView;
    TextView tvEmpty;
    Button btnAddNote, btnSaveNotes;
    EditText edTextNote;
    TripDataBase tripDataBase;
    int id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_update_notes, container, false);
        if (savedInstanceState == null) {
            list = new ArrayList<>();
        } else {
            list = savedInstanceState.getStringArrayList(NOTES_KEY_EDT);
        }
        noteListAdapter = new NoteListAdapter();
        btnSaveNotes = view.findViewById(R.id.btnSaveUpdataNotes);
        btnAddNote = view.findViewById(R.id.btnAddUpdateNote);
        edTextNote = view.findViewById(R.id.etNoteUpdataText);
        tvEmpty = view.findViewById(R.id.tv_empty);
        tripDataBase = TripDataBase.getInstance(getContext());

        list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.rcvThisNotes);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);
        bundleNoteRoom = this.getArguments();
        if (bundleNoteRoom != null) {
            list = bundleNoteRoom.getStringArrayList(HomeFragment.NOTE_ROOM);
            noteListAdapter.setDataList(list);
            id = bundleNoteRoom.getInt(HomeFragment.GET_ID);
            Log.i("TAG", "onCreateView: "+id);
        }
        recyclerView.setAdapter(noteListAdapter);
        if (list == null || list.isEmpty()) {
            if (list == null) {
                list = new ArrayList<>();
            }
            tvEmpty.setVisibility(View.VISIBLE);
        }
        btnSaveNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripDataBase.tripDao().updateNotes(id, list)
                        .subscribeOn(Schedulers.computation())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                            }

                            @Override
                            public void onComplete() {

                            }

                            @Override
                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                            }
                        });
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(NOTES_KEY_EDT, list);
    }
}