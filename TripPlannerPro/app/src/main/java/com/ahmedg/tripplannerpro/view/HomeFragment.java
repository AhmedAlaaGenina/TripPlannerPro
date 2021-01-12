package com.ahmedg.tripplannerpro.view;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahmedg.tripplannerpro.R;
import com.ahmedg.tripplannerpro.model.TripModel;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private int[] images = {R.drawable.status_cancel, R.drawable.status_done};
    private String[] tripsName = {"ITI ", "Journey"};
    private String[] source = {"Zagizag ", "Mansoura"};
    private String[] destination = {"Mansoura ", "Zagizag"};
    private RecyclerView recyclerView;
    private ArrayList<TripModel> tripModels;
    private Context mCtx;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        tripModels = new ArrayList<>();
        for (int i = 0 ; i < images.length ; i++){
            tripModels.add(new TripModel(tripsName[i],source[i],destination[i],true));
        }
        recyclerView = view.findViewById(R.id.homeRv);
        mCtx = getActivity();
        //recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mCtx));
        HomeTripAdapter homeTripAdapter = new HomeTripAdapter(tripModels);
        recyclerView.setAdapter(homeTripAdapter);
        return view;
    }
}