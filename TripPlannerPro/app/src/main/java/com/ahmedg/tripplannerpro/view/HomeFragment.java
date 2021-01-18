package com.ahmedg.tripplannerpro.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ahmedg.tripplannerpro.R;
import com.ahmedg.tripplannerpro.model.TripDataBase;
import com.ahmedg.tripplannerpro.model.TripModel;
import com.ahmedg.tripplannerpro.model.TripModelHistory;
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


public class HomeFragment extends Fragment implements SetOnclickListener {
    public static final String ADD_TRIP_FRAGMENT = "AddTripFragment";
    public static final String NOTE_ROOM = "noteRoom";
    public static final String GET_TRIP = "getTrip";
    public static final String GET_ID ="GetID" ;
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
                            setItemInHistoryDB(viewHolder);
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
        tripDataBase.tripDao().deleteTrip(homeTripAdapter.getModelArrayList().get(viewHolder.getAdapterPosition()))
                .subscribeOn(Schedulers.computation())
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

    public void getTripsById(int ID) {
        tripDataBase.tripDao().getTripById(homeTripAdapter.getModelArrayList().get(ID).getId())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<TripModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull TripModel tripModel) {
                        Log.i("TAG", "onSuccess: " + tripModel.getNotes());
                        Bundle bundleSendNoteRoom = new Bundle();
                        bundleSendNoteRoom.putParcelable(GET_TRIP, tripModel);
                        bundleSendNoteRoom.putInt(GET_ID,homeTripAdapter.getModelArrayList().get(ID).getId());
                        UpdateFragment updateFragment = new UpdateFragment();
                        updateFragment.setArguments(bundleSendNoteRoom);
                        Log.i("TAG", "initAddTrip: " + bundleSendNoteRoom);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, updateFragment, GET_TRIP)
                                .addToBackStack(null)
                                .commit();                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }
                });
    }

    public void getNotes(int ID) {
        tripDataBase.tripDao().getTripById(homeTripAdapter.getModelArrayList().get(ID).getId())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<TripModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull TripModel tripModel) {
                        Log.i("TAG", "onSuccess: " + tripModel.getNotes());
                        Bundle bundleSendNoteRoom = new Bundle();
                        bundleSendNoteRoom.putStringArrayList(NOTE_ROOM, tripModel.getNotes());
                        bundleSendNoteRoom.putInt(GET_ID,homeTripAdapter.getModelArrayList().get(ID).getId());
                        UpdateNotesFragment updateNotesFragment = new UpdateNotesFragment();
                        updateNotesFragment.setArguments(bundleSendNoteRoom);
                        Log.i("TAG", "initAddTrip: " + bundleSendNoteRoom);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, updateNotesFragment, AddTripFragment.NOTE_FRAGMENT)
                                .addToBackStack(null)
                                .commit();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }
                });
    }

    private void setItemInHistoryDB(RecyclerView.ViewHolder viewHolder) {
        Log.v("HistoryDb ","delete");
        TripModel tripModel = homeTripAdapter.getItem(viewHolder.getAdapterPosition());
        Log.v("HistoryDb ",tripModel.getTripName());
        tripDataBase.tripDaoHistory().insertTripHistory(new TripModelHistory(tripModel.getTripName(),
                tripModel.getSource(), tripModel.getDestination(), false,
                tripModel.getDate(), tripModel.getTime(), tripModel.getDirection(),
                tripModel.getRepetition(), tripModel.getNotes())).subscribeOn(Schedulers.computation())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.v("HistoryDb ","onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.v("HistoryDb ","onComplete");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.v("HistoryDb ","error");
                    }
                });
    }

    private void setItemInHistoryDB(int index) {
        Log.v("HistoryDb ","start");
        TripModel tripModel = homeTripAdapter.getItem(index);
        tripDataBase.tripDaoHistory().insertTripHistory(new TripModelHistory(tripModel.getTripName(),
                tripModel.getSource(), tripModel.getDestination(), true,
                tripModel.getDate(), tripModel.getTime(), tripModel.getDirection(),
                tripModel.getRepetition(), tripModel.getNotes())).subscribeOn(Schedulers.computation())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.v("HistoryDb ","onSubscribe1");
                    }

                    @Override
                    public void onComplete() {
                        Log.v("HistoryDb ","onComplete1");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.v("HistoryDb ","error1");
                    }
                });
    }

    public void deleteTripItem(int index) {
        tripDataBase.tripDao().deleteTrip(homeTripAdapter.getModelArrayList().get(index)).subscribeOn(Schedulers.computation())
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


    @Override
    public void onItemClickListener(int index) {
        getTripsById(index);
    }

    @Override
    public void onNoteClickListener(int index) {
        getNotes(index);
    }


    @Override
    public void onStartClickListener(int index) {
        Toast.makeText(getActivity(), "Start Clicked", Toast.LENGTH_SHORT).show();
        setItemInHistoryDB(index);
        deleteTripItem(index);
        homeTripAdapter.getModelArrayList().remove(index);
        homeTripAdapter.notifyDataSetChanged();
    }
}