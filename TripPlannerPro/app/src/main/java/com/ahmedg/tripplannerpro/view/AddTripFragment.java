package com.ahmedg.tripplannerpro.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ahmedg.tripplannerpro.R;
import com.ahmedg.tripplannerpro.model.TripDataBase;
import com.ahmedg.tripplannerpro.model.TripModel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


import io.reactivex.CompletableObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

public class AddTripFragment extends Fragment {
    private static final int PLACE_API_START = 100;
    private static final int PLACE_API_END = 101;
    private static final String NOTE_FRAGMENT = "NOTE_FRAGMENT";
    private static final String MAIN_FRAGMENT = "MAIN_FRAGMENT";

    View view;
    Calendar calendar;
    Button btnAddThisTrip, btnAddNotes;
    EditText edtTxtNameTrip, edtTxtStartPoint, edtTxtEndPoint;
    TextView txtViewDate, txtViewTime;
    Spinner spinnerTime, spinnerDirection;
    String tripName, repetitionWord, directionWord, startPoint, endPoint, date, time;
    int myHour, myMinute;
    int myYear, myMonth, myDay;
    LatLng latLngStart, latLngEnd;
    String[] repetition = {"daily", "weekly", "monthly", "Other"};
    String[] direction = {"One Direction", "Round Trip"};
    TripDataBase tripDataBase;
    ArrayList<String> notesList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_trip, container, false);
        edtTxtNameTrip = view.findViewById(R.id.edtTextNameOfTrip);
        edtTxtStartPoint = view.findViewById(R.id.edtTxtStartPointOfTrip);
        edtTxtEndPoint = view.findViewById(R.id.edtTxtEndPointOfTrip);
        txtViewDate = view.findViewById(R.id.txtViewDate);
        txtViewTime = view.findViewById(R.id.txtViewTime);
        btnAddNotes = view.findViewById(R.id.btnAddNotes);
        btnAddThisTrip = view.findViewById(R.id.btnAddThisTrip);
        spinnerTime = view.findViewById(R.id.spinnerTime);
        spinnerDirection = view.findViewById(R.id.spinnerDiraction);
        tripDataBase = TripDataBase.getInstance(getContext());
        calendar = Calendar.getInstance();
        myYear = calendar.get(calendar.YEAR);
        myMonth = calendar.get(calendar.MONTH);
        myDay = calendar.get(calendar.DAY_OF_MONTH);
        Bundle bundle = this.getArguments();


        //Fragment Note
        btnAddNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new NoteFragment(), NOTE_FRAGMENT)
                        .addToBackStack(null)
                        .commit();
            }
        });


        //Spinners 1
        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                directionWord = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + directionWord, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, direction);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(dataAdapter);

        //Spinners 2
        spinnerDirection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                repetitionWord = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + repetitionWord, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, repetition);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDirection.setAdapter(dataAdapter1);


        //Place Api
        Places.initialize(getContext(), "AIzaSyAJkMEOXZvxmudhM4_qCoXQBZ_RJgjvJBU");
//        edtTxtStartPoint.setFocusable(false);
//        edtTxtEndPoint.setFocusable(false);


        //Start End Points
        edtTxtStartPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                        .build(v.getContext());
                startActivityForResult(intent, PLACE_API_START);
            }
        });
        edtTxtEndPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                        .build(v.getContext());
                startActivityForResult(intent, PLACE_API_END);
            }
        });


        //Date and Time
        txtViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        myHour = hourOfDay;
                        myMinute = minute;
                        calendar.set(0, 0, 0, myHour, myMinute);
                        txtViewTime.setText(DateFormat.format("hh:mm aa", calendar));
                    }
                }, 12, 0, false
                );
                timePickerDialog.updateTime(myHour, myMinute);
                timePickerDialog.show();
            }
        });
        txtViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myYear = year;
                        myMonth = month + 1;
                        myDay = dayOfMonth;
                        txtViewDate.setText(myDay + "/" + myMonth + "/" + myYear);
                    }
                }, myYear, myMonth, myDay);
                datePickerDialog.show();
            }
        });


        //Save Data in RoomDB
        btnAddThisTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripName = edtTxtNameTrip.getText().toString();
                startPoint = edtTxtStartPoint.getText().toString();
                endPoint = edtTxtEndPoint.getText().toString();
                date = txtViewDate.getText().toString();
                time = txtViewTime.getText().toString();
                if (bundle!= null) {
                    notesList = bundle.getStringArrayList(NoteFragment.NOTES_KEY);
                }
                tripDataBase.tripDao().insertTrip(new TripModel(tripName, startPoint, endPoint, true
                        , date, time, directionWord, repetitionWord, notesList))
                        .subscribeOn(Schedulers.computation())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onComplete() {
                                Toast.makeText(view.getContext(), "Trip Add", Toast.LENGTH_SHORT).show();
                                Log.i("TAG", "onComplete: Trip Add");

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Toast.makeText(view.getContext(), "Trip Not Add", Toast.LENGTH_SHORT).show();
                                Log.i("TAG", "onError: Not Add");

                            }
                        });
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new HomeFragment(), MAIN_FRAGMENT)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddTripFragment.PLACE_API_START && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            edtTxtStartPoint.setText(place.getAddress());
            latLngStart = place.getLatLng();
            Log.i("TAG", "onActivityResult: Done 1");
        }
        if (requestCode == AddTripFragment.PLACE_API_END && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            edtTxtEndPoint.setText(place.getAddress());
            latLngEnd = place.getLatLng();
            Log.i("TAG", "onActivityResult: Done 2");

        }
        if (requestCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getContext(), status.getStatusCode(), Toast.LENGTH_SHORT).show();
            Log.i("TAG", "onActivityResult: Done 3");

        }
    }

}