package com.example.travelaks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;

public class Cites extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cites);

        CardView cardRiyadh  = findViewById(R.id.card_riyadh);
        CardView cardJeddah  = findViewById(R.id.card_jeddah);
        CardView cardMakkah  = findViewById(R.id.card_makkah);
        CardView cardMadinah = findViewById(R.id.card_madinah);

        cardRiyadh.setOnClickListener(v  -> startActivity(new Intent(this, RiyadhDetailsActivity.class)));
        cardJeddah.setOnClickListener(v  -> startActivity(new Intent(this, JeddahDetailsActivity.class)));
        cardMakkah.setOnClickListener(v  -> startActivity(new Intent(this, MakkahDetailsActivity.class)));
        cardMadinah.setOnClickListener(v -> startActivity(new Intent(this, MadinahDetailsActivity.class)));

        // Search bar — filter city cards by name
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                String q = newText.toLowerCase().trim();
                cardRiyadh.setVisibility( matches(q, "riyadh")          ? View.VISIBLE : View.GONE);
                cardJeddah.setVisibility( matches(q, "jeddah")          ? View.VISIBLE : View.GONE);
                cardMakkah.setVisibility( matches(q, "makkah", "mecca") ? View.VISIBLE : View.GONE);
                cardMadinah.setVisibility(matches(q, "madinah", "medina", "madina") ? View.VISIBLE : View.GONE);
                return true;
            }
        });

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

    private boolean matches(String query, String... names) {
        if (query.isEmpty()) return true;
        for (String name : names) {
            if (name.contains(query)) return true;
        }
        return false;
    }
}
