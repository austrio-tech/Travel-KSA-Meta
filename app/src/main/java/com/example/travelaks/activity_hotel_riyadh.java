package com.example.travelaks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelaks.data.model.Hotel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class activity_hotel_riyadh extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_riyadh);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recycler_hotels);
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        Button btnBack = findViewById(R.id.btn_back);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Hotel> hotelList = new ArrayList<>();
        HotelAdapter adapter = new HotelAdapter(this, hotelList);
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance()
            .collection("hotels")
            .whereEqualTo("city", "Riyadh")
            .orderBy("rating", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener(querySnapshot -> {
                progressBar.setVisibility(View.GONE);
                hotelList.clear();
                hotelList.addAll(querySnapshot.toObjects(Hotel.class));
                adapter.notifyDataSetChanged();
            })
            .addOnFailureListener(e -> {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Failed to load hotels: " + e.getMessage(), Toast.LENGTH_LONG).show();
            });

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                startActivity(new Intent(activity_hotel_riyadh.this, RiyadhDetailsActivity.class));
                finish();
            });
        }
    }
}
