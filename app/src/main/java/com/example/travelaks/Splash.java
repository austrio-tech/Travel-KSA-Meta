package com.example.travelaks;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // If a regular user is already signed in, skip HomeActivity and go straight to content
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                startActivity(new Intent(this, Cites.class));
            } else {
                startActivity(new Intent(this, HomeActivity.class));
            }
            finish();
        }, 3000);
    }
}
