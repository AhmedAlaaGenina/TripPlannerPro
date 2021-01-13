package com.ahmedg.tripplannerpro.view;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class HomeFragment extends Fragment {
    public static final String ADD_TRIP_FRAGMENT = "AddTripFragment";
    Button btnNotes;
    private int[] images = {R.drawable.status_cancel, R.drawable.status_done};
    private String[] tripsName = {"ITI ", "Journey"};
    private String[] source = {"Zagizag ", "Mansoura"};
    private String[] destination = {"Mansoura ", "Zagizag"};
    private RecyclerView recyclerView;
    HomeTripAdapter homeTripAdapter;
    private Context mCtx;
    TripDataBase tripDataBase;
    View view;


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        tripDataBase = TripDataBase.getInstance(getContext());
        recyclerView = view.findViewById(R.id.homeRv);
        mCtx = getActivity();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mCtx));
        homeTripAdapter = new HomeTripAdapter();
        recyclerView.setAdapter(homeTripAdapter);
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);
        tripDataBase.tripDao().getTrips().subscribeOn(Schedulers.computation())
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
        return view;
    }

    ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@androidx.annotation.NonNull RecyclerView recyclerView, @androidx.annotation.NonNull RecyclerView.ViewHolder viewHolder, @androidx.annotation.NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@androidx.annotation.NonNull RecyclerView.ViewHolder viewHolder, int direction) {

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

            homeTripAdapter.getModelArrayList().remove(viewHolder.getAdapterPosition());
            homeTripAdapter.notifyDataSetChanged();

        }
    };
}