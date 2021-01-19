package com.ahmedg.tripplannerpro.view;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;

import com.ahmedg.tripplannerpro.model.TripDataBase;
import com.ahmedg.tripplannerpro.model.TripModel;
import com.ahmedg.tripplannerpro.model.TripModelHistory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DemoActivity extends FragmentActivity {
    AlertDialog.Builder builder;
    TripDataBase tripDataBase;
    double latt, longt;
    TripModel tripModelToHistory;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tripDataBase = TripDataBase.getInstance(getApplicationContext());
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        calendar.set(0, 0, 0, hour, min);
        String time = DateFormat.format("hh:mm aa", calendar).toString();
        Calendar calendar1 = Calendar.getInstance();
        int month = calendar1.get(Calendar.MONTH) + 1;
        int year = calendar1.get(Calendar.YEAR);
        int day = calendar1.get(Calendar.DATE);
        String date = time + "_" + day + "/" + month + "/" + year;
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Trip Is Here ").setTitle("Trip Planner");
        builder.setMessage("Time Trip!")
                .setCancelable(false)
                .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getLatLonByAlarmTime(date);

//                        Uri location = Uri.parse("google.navigation:q=" + longt + "," + latt);
//                        Intent intent2 = new Intent(Intent.ACTION_VIEW, location);
//                        intent2.setPackage("com.google.android.apps.maps");
//                        startActivity(intent2);
                        finish();
//                        Toast.makeText(getApplicationContext(), "you choose yes action for alertbox",
//                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Snooze", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());
                        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
                        notificationHelper.getManager().notify(1, nb.build());
                        finish();
//                        Toast.makeText(getApplicationContext(), "you choose no action for alertbox",
//                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        finish();
                        dialog.cancel();
                        System.exit(0);
//                        Toast.makeText(getApplicationContext(), "you choose no action for alertbox",
//                                Toast.LENGTH_SHORT).show();
                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Trip Planner");
        alert.show();
    }

    public void getLatLonByAlarmTime(String date) {
        Log.i("TAG", "getLatLonByAlarmTime: " + date);
        tripDataBase.tripDao().getTripByAlarmTime(date)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<TripModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull TripModel tripModel) {
                        Log.i("TAG", "onSuccess: " + tripModel.getId());
                        tripModelToHistory = tripModel;
                        latt = tripModel.getLat();
                        longt = tripModel.getLongt();
                        setItemInHistoryDB();
                        deleteTripItem();
                        Uri location = Uri.parse("google.navigation:q=" + longt + "," + latt);
                        Intent intent2 = new Intent(Intent.ACTION_VIEW, location);
                        intent2.setPackage("com.google.android.apps.maps");
                        startActivity(intent2);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("TAG", "onError: Date Lat lont Error : " + e);
                    }
                });
    }

    private void setItemInHistoryDB() {
        Log.v("HistoryDb ", "start");

        tripDataBase.tripDaoHistory().insertTripHistory(new TripModelHistory(tripModelToHistory.getTripName(),
                tripModelToHistory.getSource(), tripModelToHistory.getDestination(), true,
                tripModelToHistory.getTime(), tripModelToHistory.getDirection(),
                tripModelToHistory.getRepetition(), tripModelToHistory.getNotes()))
                .subscribeOn(Schedulers.computation())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.v("HistoryDb ", "onSubscribe1");
                    }

                    @Override
                    public void onComplete() {
                        Log.v("HistoryDb ", "onComplete1");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.v("HistoryDb ", "error1");
                    }
                });
    }

    public void deleteTripItem() {
        tripDataBase.tripDao().deleteTrip(tripModelToHistory)
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
//        CustomDialoge dialoge = new CustomDialoge();
//
//        dialoge.show(getSupportFragmentManager(), "wesam");
}



