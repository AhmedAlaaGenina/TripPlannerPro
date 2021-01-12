package com.ahmedg.tripplannerpro.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.ahmedg.tripplannerpro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        loadFragment(new HomeFragment());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                setUpSelectFragment(id);
                 return true;
            }
        });

    }

    private void init() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        //put log here
        Log.e(TAG, fragment.getClass().getName());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contanier, fragment);
        transaction.commit();
    }
    private void setUpSelectFragment(int id){
        if (id == R.id.history){
            loadFragment(new HistoryFragment());

        }else if(id == R.id.home){
            loadFragment(new HomeFragment());

        }else if(id == R.id.account){
            loadFragment(new ProfileFragment());

        }
    }

    @Override
    public void onBackPressed() {

        if(bottomNavigationView.getSelectedItemId() == R.id.home){
            super.onBackPressed();
         finish();
        }else if(bottomNavigationView.getSelectedItemId() == R.id.history){
            bottomNavigationView.setSelectedItemId(R.id.home);
        }else if(bottomNavigationView.getSelectedItemId() == R.id.account){
            bottomNavigationView.setSelectedItemId(R.id.history);
        }


    }
}