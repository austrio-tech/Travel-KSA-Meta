package com.example.travelaks;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelaks.data.model.Hotel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class activity_hotel_riyadh extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_riyadh);

        String cityName = getIntent().getStringExtra("CITY_NAME");
        if (cityName == null) cityName = "Riyadh";

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tvTitle = findViewById(R.id.tv_toolbar_title);
        if (tvTitle != null) tvTitle.setText("Hotels in " + cityName);

        RecyclerView recyclerView = findViewById(R.id.recycler_hotels);
        ProgressBar progressBar = findViewById(R.id.progress_bar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Hotel> hotelList = new ArrayList<>();
        HotelAdapter adapter = new HotelAdapter(this, hotelList);
        recyclerView.setAdapter(adapter);

        final String city = cityName;
        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance()
            .collection("hotels")
            .whereEqualTo("city", city)
            .get()
            .addOnSuccessListener(snap -> {
                progressBar.setVisibility(View.GONE);
                hotelList.clear();
                hotelList.addAll(snap.toObjects(Hotel.class));
                Collections.sort(hotelList, (a, b) -> Double.compare(b.getRating(), a.getRating()));
                adapter.notifyDataSetChanged();
            })
            .addOnFailureListener(e -> {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Failed to load hotels: " + e.getMessage(), Toast.LENGTH_LONG).show();
            });

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }
}
