package com.ahmedg.tripplannerpro.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ahmedg.tripplannerpro.R;
import com.ahmedg.tripplannerpro.model.TripDataBase;
import com.ahmedg.tripplannerpro.model.TripModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class HomeFragment extends Fragment implements SetOnclickListener{
    public static final String ADD_TRIP_FRAGMENT = "AddTripFragment";
    Button btnNotes;
    private int[] images = {R.drawable.status_cancel, R.drawable.status_done};
    private RecyclerView recyclerView;
    HomeTripAdapter homeTripAdapter;
    private Context mCtx;
    TripDataBase tripDataBase;
    DatabaseReference myRef, newRef;
    FirebaseDatabase firebaseDatabase;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        init();
        getTrips();
        return view;
    }

    ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@androidx.annotation.NonNull RecyclerView recyclerView, @androidx.annotation.NonNull RecyclerView.ViewHolder viewHolder, @androidx.annotation.NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@androidx.annotation.NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            new AlertDialog.Builder(getActivity()).setMessage("Do You Want to Delete this Trip ?!")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            deleteTripItem(viewHolder);
                            homeTripAdapter.getModelArrayList().remove(viewHolder.getAdapterPosition());
                            homeTripAdapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getTrips();

                        }
                    }).show();
        }
    };

    private void init() {
        tripDataBase = TripDataBase.getInstance(getContext());
        recyclerView = view.findViewById(R.id.homeRv);
        mCtx = getActivity();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mCtx));
        homeTripAdapter = new HomeTripAdapter();
        homeTripAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(homeTripAdapter);
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference().child("trips");
        newRef = myRef.push();
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);
    }

    public void deleteTripItem(RecyclerView.ViewHolder viewHolder) {
        tripDataBase.tripDao().deleteTrip(homeTripAdapter.getModelArrayList().get(viewHolder.getAdapterPosition())).subscribeOn(Schedulers.computation())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    public void getTrips() {
        tripDataBase.tripDao().getAllTrips().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<TripModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull List<TripModel> tripModels) {
                        homeTripAdapter.setDataList((ArrayList<TripModel>) tripModels);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(view.getContext(), "Data Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onItemClickListener(View v,int index) {
//        tripDataBase.tripDao().getTripById(index).
        Toast.makeText(v.getContext(), "Item Clicked", Toast.LENGTH_SHORT).show();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new AddTripFragment(), ADD_TRIP_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onNoteClickListener(View v,int index) {
        Toast.makeText(v.getContext(), "Notes Clicked", Toast.LENGTH_SHORT).show();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new NoteFragment(), ADD_TRIP_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onStartClickListener(View v,int index) {
        Toast.makeText(v.getContext(), "Start Clicked", Toast.LENGTH_SHORT).show();
    }
}