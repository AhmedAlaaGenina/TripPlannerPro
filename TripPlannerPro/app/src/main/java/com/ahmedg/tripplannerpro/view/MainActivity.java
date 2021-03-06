package com.ahmedg.tripplannerpro.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ahmedg.tripplannerpro.R;
import com.ahmedg.tripplannerpro.model.TripDataBase;
import com.ahmedg.tripplannerpro.model.TripModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    public static final String ADD_TRIP_FRAGMENT = "AddNewTripFragment";

    private BottomNavigationView bottomNavigationView;
    private static final String TAG = "MainActivity";
    HomeFragment homeFragment;
    FloatingActionButton btnAddNewTrip;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef, newRef;
    TripDataBase tripDataBase;
    AlarmManager alarmManager;
    AlertReceiver alertReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference().child("trips");
        newRef = myRef.push();
        tripDataBase = TripDataBase.getInstance(getApplicationContext());
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alertReceiver = new AlertReceiver();

        if (savedInstanceState != null) {
            return;
        } else {
            loadFragment(new HomeFragment());
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                setUpSelectFragment(id);
                return true;
            }
        });
        btnAddNewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new AddTripFragment(), ADD_TRIP_FRAGMENT)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void init() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        btnAddNewTrip = findViewById(R.id.btnAddNewTrip);
        if (!Settings.canDrawOverlays(getApplicationContext())) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 0);
        }
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        //put log here
        Log.e(TAG, fragment.getClass().getName());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    private void setUpSelectFragment(int id) {
        if (id == R.id.history) {
            loadFragment(new HistoryFragment());

        } else if (id == R.id.home) {
            loadFragment(new HomeFragment());

        } else if (id == R.id.account) {
            //loadFragment(new ProfileFragment());
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));


        } else if (id == R.id.sync) {
            //sync Data With Firebase......
            tripDataBase.tripDao().getAllTrips().subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<List<TripModel>>() {
                        @Override
                        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                        }

                        @Override
                        public void onSuccess(@io.reactivex.annotations.NonNull List<TripModel> tripModels) {
                            newRef.setValue(tripModels);
                            Toast.makeText(MainActivity.this, "Sync Done!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {

        if (bottomNavigationView.getSelectedItemId() == R.id.home) {
            super.onBackPressed();
            finish();
        } else if (bottomNavigationView.getSelectedItemId() == R.id.history) {
            bottomNavigationView.setSelectedItemId(R.id.home);
        } else if (bottomNavigationView.getSelectedItemId() == R.id.account) {
            bottomNavigationView.setSelectedItemId(R.id.history);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        getSupportFragmentManager().putFragment(outState, TAG, homeFragment);
    }


}