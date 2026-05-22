package com.example.travelaks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
public class Cites extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cites);
        // ربط الكروت
        CardView cardRiyadh = findViewById(R.id.card_riyadh);
        CardView cardJeddah = findViewById(R.id.card_jeddah);
        CardView cardMakkah = findViewById(R.id.card_makkah);
        CardView cardMadinah = findViewById(R.id.card_madinah);
        // الرياض
        cardRiyadh.setOnClickListener(v -> {
            Intent intent = new Intent(Cites.this, RiyadhDetailsActivity.class);
            startActivity(intent);
        });
        // جدة
        cardJeddah.setOnClickListener(v -> {
            Intent intent = new Intent(Cites.this, JeddahDetailsActivity.class);
            startActivity(intent);
        });
        // مكة
        cardMakkah.setOnClickListener(v -> {
            Intent intent = new Intent(Cites.this, MakkahDetailsActivity.class);
            startActivity(intent);
        });
        // المدينة المنورة
        cardMadinah.setOnClickListener(v -> {
            Intent intent = new Intent(Cites.this, MadinahDetailsActivity.class);
            startActivity(intent);
        });
    }
}