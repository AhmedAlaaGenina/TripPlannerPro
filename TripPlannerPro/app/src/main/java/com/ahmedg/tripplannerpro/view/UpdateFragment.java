package com.ahmedg.tripplannerpro.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.Spinner;
import android.widget.TextView;

import com.ahmedg.tripplannerpro.R;
import com.ahmedg.tripplannerpro.model.TripDataBase;
import com.ahmedg.tripplannerpro.model.TripModel;

import io.reactivex.CompletableObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UpdateFragment extends Fragment {
    View view;
    EditText edtName, edtStart, edtEnd;
    TextView tvTime, tvData;
    Spinner sTime, sDirection;
    Bundle bundle;
    TripModel tripModel;
    String[] repetition = {"daily", "weekly", "monthly", "Other"};
    String[] direction = {"One Direction", "Round Trip"};
    String repetitionWord, directionWord;
    Button btnUpdate;
    TripDataBase tripDataBase;
    int id;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update, container, false);
        edtName = view.findViewById(R.id.edtTxtName);
        edtStart = view.findViewById(R.id.edtTxtStartPoint);
        edtEnd = view.findViewById(R.id.edtTxtEndPoint);
        tvTime = view.findViewById(R.id.tvTime);
        tvData = view.findViewById(R.id.tvDate);
        sTime = view.findViewById(R.id.tvspinnerTime);
        sDirection = view.findViewById(R.id.tvspinnerDiraction);
        btnUpdate = view.findViewById(R.id.btnUpdateTrip);
        tripDataBase = TripDataBase.getInstance(getContext());
        bundle = this.getArguments();
        if (bundle != null) {
            tripModel = bundle.getParcelable(HomeFragment.GET_TRIP);
            id = bundle.getInt(HomeFragment.GET_ID);
            Log.i("TAG", "onCreateView: " + id);
        }
        edtName.setText(tripModel.getTripName());
        edtStart.setText(tripModel.getSource());
        edtEnd.setText(tripModel.getDestination());
        String time[] = tripModel.getTime().split("_");
        tvTime.setText(time[0]);
        tvData.setText(time[1]);
        String x = time[0] + "_" + time[1];
        getDirection();
        getRepetition();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripDataBase.tripDao().update(id,
                        edtName.getText().toString(),
                        edtStart.getText().toString(),
                        edtEnd.getText().toString(),
                        x,
                        directionWord, repetitionWord
                ).subscribeOn(Schedulers.computation())
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
        });

        return view;
    }

    private void getRepetition() {
        //Spinners 2
        sDirection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                repetitionWord = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, repetition);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sDirection.setAdapter(dataAdapter1);
    }

    private void getDirection() {
        //Spinners 1
        sTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                directionWord = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, direction);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sTime.setAdapter(dataAdapter);
    }

}