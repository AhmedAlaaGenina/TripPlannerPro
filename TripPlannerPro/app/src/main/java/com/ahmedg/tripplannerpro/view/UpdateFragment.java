package com.ahmedg.tripplannerpro.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ahmedg.tripplannerpro.R;
import com.ahmedg.tripplannerpro.model.TripDataBase;
import com.ahmedg.tripplannerpro.model.TripModel;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import java.util.Calendar;

import io.reactivex.CompletableObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UpdateFragment extends Fragment {
    private static final int PLACE_API_S = 77;
    private static final int PLACE_API_E = 78;
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
    int myHour, myMinute;
    int myYear, myMonth, myDay;
    Calendar calendar;


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
        calendar = Calendar.getInstance();
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
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime();
            }
        });
        tvData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });

        edtStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlaceApi(PLACE_API_S);
            }
        });

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
                                Toast.makeText(view.getContext(), "All Data is Updated", Toast.LENGTH_SHORT).show();

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

    private void getDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                view.setMinDate(System.currentTimeMillis());
                myYear = year;
                myMonth = month + 1;
                myDay = dayOfMonth;
                tvData.setText(myDay + "/" + myMonth + "/" + myYear);
            }
        }, myYear, myMonth, myDay);
        datePickerDialog.show();
    }

    private void getTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myHour = hourOfDay;
                myMinute = minute;
                calendar.set(0, 0, 0, myHour, myMinute);
                tvTime.setText(DateFormat.format("hh:mm aa", calendar));
                Log.i("TAG", "onTimeSet: " + myYear + " " + myMonth + " " + myDay + " " + myHour + " " + myMinute);
            }
        }, 12, 0, false
        );
        timePickerDialog.updateTime(myHour, myMinute);
        timePickerDialog.show();
    }

    private void setPlaceApi(int placeApiRequest) {
        Intent intent = new PlaceAutocomplete.IntentBuilder()
                .accessToken(getString(R.string.mapbox_access_token))
                .placeOptions(PlaceOptions.builder()
                        .backgroundColor(Color.parseColor("#EEEEEE"))
                        .limit(10)
                        .build(PlaceOptions.MODE_CARDS))
                .build(getActivity());
        startActivityForResult(intent, placeApiRequest);
    }
}