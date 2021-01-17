package com.ahmedg.tripplannerpro.view;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;

public class DemoActivity extends FragmentActivity {
    AlertDialog.Builder builder;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Trip Is Here ").setTitle("Trip Planner");
        builder.setMessage("Time Trip!")
                .setCancelable(false)
                .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("TAG", "onClick: " + AddTripFragment.lat + "," +  AddTripFragment.logt);
                        Uri location = Uri.parse("google.navigation:q=" + AddTripFragment.logt + "," + AddTripFragment.lat);
                        Intent intent2 = new Intent(Intent.ACTION_VIEW, location);
                        intent2.setPackage("com.google.android.apps.maps");
                        startActivity(intent2);
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

//        CustomDialoge dialoge = new CustomDialoge();
//
//        dialoge.show(getSupportFragmentManager(), "wesam");
}



