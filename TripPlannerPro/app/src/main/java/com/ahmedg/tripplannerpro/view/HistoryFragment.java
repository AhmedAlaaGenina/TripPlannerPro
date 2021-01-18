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
import android.widget.Toast;

import com.ahmedg.tripplannerpro.R;
import com.ahmedg.tripplannerpro.model.TripDataBase;
import com.ahmedg.tripplannerpro.model.TripModel;
import com.ahmedg.tripplannerpro.model.TripModelHistory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class HistoryFragment extends Fragment {

    private int[] images = {R.drawable.status_cancel, R.drawable.status_done};

    private RecyclerView recyclerView;
    private Context mCtx;
    HistoryTripAdapter historyTripAdapter;
    TripDataBase tripDataBase;
    View view;
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
                            //when delete item from tripDatabase, add tripHistoryDB

                            deleteTripItem(viewHolder);
                            historyTripAdapter.getModelArrayList().remove(viewHolder.getAdapterPosition());
                            historyTripAdapter.notifyDataSetChanged();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getTrips();

                        }
                    }).show();
        }
    };
    public void getTrips() {
        tripDataBase.tripDaoHistory().getAllTrips().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<TripModelHistory>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull List<TripModelHistory> tripModels) {
                        historyTripAdapter.setDataList(tripModels);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(view.getContext(), "Data Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);
        init();
        return view;
    }
    private void getDataFromHistoryDB() {
        tripDataBase.tripDaoHistory().getAllTrips().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<TripModelHistory>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull List<TripModelHistory> tripModels) {
                        historyTripAdapter.setDataList(tripModels);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(view.getContext(), "Data Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void init(){
        recyclerView = view.findViewById(R.id.historyRv);
        tripDataBase = TripDataBase.getInstance(getContext());
        mCtx = getActivity();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mCtx));
        historyTripAdapter = new HistoryTripAdapter();
        getDataFromHistoryDB();
        recyclerView.setAdapter(historyTripAdapter);
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);
    }
    public void deleteTripItem(RecyclerView.ViewHolder viewHolder) {
        tripDataBase.tripDaoHistory().deleteTripHistory(historyTripAdapter.getModelArrayList().get(viewHolder.getAdapterPosition())).subscribeOn(Schedulers.computation())
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
}