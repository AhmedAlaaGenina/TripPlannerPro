package com.ahmedg.tripplannerpro.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ahmedg.tripplannerpro.R;
import com.ahmedg.tripplannerpro.model.TripDataBase;
import com.ahmedg.tripplannerpro.model.TripModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
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
            loadFragment(new ProfileFragment());

        } else if (id == R.id.sync) {
            //sync Data With Firebase......
            tripDataBase.tripDao().getTrips().subscribeOn(Schedulers.computation())
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