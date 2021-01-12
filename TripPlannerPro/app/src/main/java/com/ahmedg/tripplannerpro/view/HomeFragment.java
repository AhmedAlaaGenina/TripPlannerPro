package com.ahmedg.tripplannerpro.view;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ahmedg.tripplannerpro.R;
import com.ahmedg.tripplannerpro.model.TripDataBase;
import com.ahmedg.tripplannerpro.model.TripModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class HomeFragment extends Fragment {
    public static final String ADD_TRIP_FRAGMENT = "AddTripFragment";

    private int[] images = {R.drawable.status_cancel, R.drawable.status_done};
    private String[] tripsName = {"ITI ", "Journey"};
    private String[] source = {"Zagizag ", "Mansoura"};
    private String[] destination = {"Mansoura ", "Zagizag"};
    private RecyclerView recyclerView;
    HomeTripAdapter homeTripAdapter;
    private ArrayList<TripModel> tripModels;
    private Context mCtx;
    TripDataBase tripDataBase;

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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        tripModels = new ArrayList<>();
        tripDataBase = TripDataBase.getInstance(getContext());

//        for (int i = 0 ; i < images.length ; i++){
//            tripModels.add(new TripModel(tripsName[i],source[i],destination[i],true));
//        }

        recyclerView = view.findViewById(R.id.homeRv);
        mCtx = getActivity();

        //recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mCtx));
        homeTripAdapter = new HomeTripAdapter();
        recyclerView.setAdapter(homeTripAdapter);

        tripDataBase.tripDao().getTrips().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<TripModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull List<TripModel> tripModels) {
                        homeTripAdapter.setDataList(tripModels);
                        Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(view.getContext(), "Data Error", Toast.LENGTH_SHORT).show();
                    }
                });



        return view;
    }
}