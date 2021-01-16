package com.ahmedg.tripplannerpro.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            finish();
        }else{
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
            finish();
        }

    }
}