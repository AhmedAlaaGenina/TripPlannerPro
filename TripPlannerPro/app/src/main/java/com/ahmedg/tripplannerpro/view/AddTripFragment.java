package com.ahmedg.tripplannerpro.view;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
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
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


import io.reactivex.CompletableObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddTripFragment extends Fragment {
    public static final int PLACE_API_START = 100;
    public static final int PLACE_API_END = 101;
    public static final String NOTE_FRAGMENT = "NOTE_FRAGMENT";
    public static final String NOTE_BACK = "NOTE_BACK";

    View view;
    Calendar calendar;
    Button btnAddThisTrip, btnAddNotes;
    EditText edtTxtNameTrip, edtTxtStartPoint, edtTxtEndPoint;
    TextView txtViewDate, txtViewTime;
    Spinner spinnerTime, spinnerDirection;
    String tripName, repetitionWord, directionWord, startPoint, endPoint, date, time;
    int myHour, myMinute;
    int myYear, myMonth, myDay;
    Point latLngStart;
    Point latLngEnd;
    String[] repetition = {"daily", "weekly", "monthly", "Other"};
    String[] direction = {"One Direction", "Round Trip"};
    TripDataBase tripDataBase;
    ArrayList<String> notesList;
    Bundle bundle;
    public AlarmManager alarmManager;
    AlertReceiver alertReceiver;
    double lat;
    double logt;
    double lato;
    double longto;
    long alarm_time;
    int iDay;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_trip, container, false);
        init();
        //Fragment Note
        btnAddNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundleNoteBack = new Bundle();
                bundleNoteBack.putStringArrayList(NOTE_BACK, notesList);
                NoteFragment noteFragment = new NoteFragment();
                noteFragment.setArguments(bundleNoteBack);
                Log.i("TAG", "initAddTrip: " + bundleNoteBack);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, noteFragment, NOTE_FRAGMENT)
                        .addToBackStack(null)
                        .commit();
            }
        });
        //Repetition and direction
        getDirection();
        getRepetition();

        //Start End Points
        edtTxtStartPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlaceApi(PLACE_API_START);
            }
        });
        edtTxtEndPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlaceApi(PLACE_API_END);
            }
        });


        //Date and Time
        txtViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime();
            }
        });
        txtViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });


        //Save Data in RoomDB
        btnAddThisTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edtTxtNameTrip.getText().toString()) &&
                        !TextUtils.isEmpty(edtTxtStartPoint.getText().toString()) &&
                        !TextUtils.isEmpty(edtTxtEndPoint.getText().toString()) &&
                        !TextUtils.isEmpty(txtViewDate.getText().toString()) &&
                        !TextUtils.isEmpty(txtViewTime.getText().toString())
                ) {
                    tripName = edtTxtNameTrip.getText().toString();
                    startPoint = edtTxtStartPoint.getText().toString();
                    endPoint = edtTxtEndPoint.getText().toString();
                    time = txtViewTime.getText().toString();
                    date = txtViewDate.getText().toString();
                    String x = time + "_" + date;
                    setAlarm(myHour, myMinute, myDay, myMonth, myYear);
                    insertNewTrip(tripName, startPoint, endPoint
                            , x, directionWord, repetitionWord, notesList, lat, logt, lato, longto, alarm_time);
                    edtTxtNameTrip.setText("");
                    edtTxtStartPoint.setText("");
                    edtTxtEndPoint.setText("");
                    txtViewDate.setText("");
                    txtViewTime.setText("");
                } else {
                    Toast.makeText(view.getContext(), "Please Add All Data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void insertNewTrip(String tripName, String startPoint, String endPoint,
                               String time, String directionWord, String repetitionWord,
                               ArrayList<String> notesList, double lat, double logt, double lato, double longto, long alarm_time) {
        tripDataBase.tripDao().insertTrip(new TripModel(tripName, startPoint, endPoint
                , time, directionWord, repetitionWord, notesList, lat, logt, lato, longto, alarm_time))
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

    private void getDate() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                view.setMinDate(Calendar.getInstance().getTimeInMillis());
                myYear = year;
                myMonth = month + 1;
                myDay = dayOfMonth;
                txtViewDate.setText(myDay + "/" + myMonth + "/" + myYear);
            }
        }, myYear, myMonth, myDay);
        datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        datePickerDialog.show();
    }

    private void getTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.DAY_OF_MONTH, iDay);
                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                c.set(Calendar.MINUTE, minute);
                if (c.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()) {
                    myHour = hourOfDay;
                    myMinute = minute;
                    txtViewTime.setText(DateFormat.format("hh:mm aa", c));
                    Log.i("TAG", "onTimeSet: " + myYear + " " + myMonth + " " + myDay + " " + myHour + " " + myMinute);
                } else {
                    Toast.makeText(getContext(), "Enter Valida Time", Toast.LENGTH_SHORT).show();
                }
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

    private void getRepetition() {
        //Spinners 2
        spinnerDirection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        spinnerDirection.setAdapter(dataAdapter1);
    }

    private void getDirection() {
        //Spinners 1
        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        spinnerTime.setAdapter(dataAdapter);
    }

    private void init() {
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
        notesList = new ArrayList<>();
        bundle = this.getArguments();
        iDay = calendar.get(Calendar.DAY_OF_MONTH);
        PlaceAutocompleteFragment autocompleteFragment;
        //   calendar.set(myYear, myMonth, myDay, myHour, myMinute);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alertReceiver = new AlertReceiver();

        if (bundle != null) {
            notesList = bundle.getStringArrayList(NoteFragment.NOTES_KEY);
        }
        edtTxtStartPoint.setFocusable(false);
        edtTxtEndPoint.setFocusable(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddTripFragment.PLACE_API_START && resultCode == getActivity().RESULT_OK) {
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);
            edtTxtStartPoint.setText(selectedCarmenFeature.text());
            latLngStart = (Point) selectedCarmenFeature.geometry();
            Toast.makeText(getContext(), "Start Point : " + latLngStart.longitude() + " " + latLngStart.latitude(), Toast.LENGTH_SHORT).show();
            longto = latLngStart.longitude();
            lato = latLngStart.latitude();
        }
        if (requestCode == AddTripFragment.PLACE_API_END && resultCode == getActivity().RESULT_OK) {
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);
            edtTxtEndPoint.setText(selectedCarmenFeature.text());
            latLngEnd = (Point) selectedCarmenFeature.geometry();
            Toast.makeText(getContext(), "Start Point : " + latLngEnd.longitude() + " " + latLngEnd.latitude(), Toast.LENGTH_SHORT).show();
            lat = latLngEnd.longitude();
            logt = latLngEnd.latitude();
        }
        if (requestCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getContext(), status.getStatusCode(), Toast.LENGTH_SHORT).show();
            Log.i("TAG", "onActivityResult: Done 3");
        }
    }

    public void setAlarm(int hour, int min, int day, int month, int year) {
        Intent intentA = new Intent(getContext(), AlertReceiver.class);
        Random r = new Random();
        int i1 = r.nextInt(99);
        PendingIntent pendingIntentA = PendingIntent.getBroadcast(getContext(), i1, intentA, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DATE, day);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.SECOND, 0);
        alarm_time = calendar.getTimeInMillis();
        System.out.println(alarm_time);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarm_time, pendingIntentA);
        Toast.makeText(getContext(), "Alarm set", Toast.LENGTH_LONG).show();
        getActivity().registerReceiver(alertReceiver, new IntentFilter());
    }

}
