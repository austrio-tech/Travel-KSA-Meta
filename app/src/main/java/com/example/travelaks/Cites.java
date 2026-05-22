package com.example.travelaks;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;

public class Cites extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cites);

        // City cards
        CardView cardRiyadh  = findViewById(R.id.card_riyadh);
        CardView cardJeddah  = findViewById(R.id.card_jeddah);
        CardView cardMakkah  = findViewById(R.id.card_makkah);
        CardView cardMadinah = findViewById(R.id.card_madinah);

        cardRiyadh.setOnClickListener(v  -> startActivity(new Intent(this, RiyadhDetailsActivity.class)));
        cardJeddah.setOnClickListener(v  -> startActivity(new Intent(this, JeddahDetailsActivity.class)));
        cardMakkah.setOnClickListener(v  -> startActivity(new Intent(this, MakkahDetailsActivity.class)));
        cardMadinah.setOnClickListener(v -> startActivity(new Intent(this, MadinahDetailsActivity.class)));

        // Logout
        findViewById(R.id.btn_logout).setOnClickListener(v ->
            new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout", (d, w) -> {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(this, HomeActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                })
                .setNegativeButton("Cancel", null)
                .show()
        );
    }
}
