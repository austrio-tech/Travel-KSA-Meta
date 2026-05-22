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

import com.example.travelaks.data.model.Attraction;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class Tourist_Attractions_Riyadh extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_attractions_riyadh);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recycler_attractions);
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        Button btnBack = findViewById(R.id.btn_back);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Attraction> attractionList = new ArrayList<>();
        AttractionAdapter adapter = new AttractionAdapter(this, attractionList);
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance()
            .collection("attractions")
            .whereEqualTo("city", "Riyadh")
            .orderBy("rating", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener(querySnapshot -> {
                progressBar.setVisibility(View.GONE);
                attractionList.clear();
                attractionList.addAll(querySnapshot.toObjects(Attraction.class));
                adapter.notifyDataSetChanged();
            })
            .addOnFailureListener(e -> {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Failed to load attractions: " + e.getMessage(), Toast.LENGTH_LONG).show();
            });

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(Tourist_Attractions_Riyadh.this, RiyadhDetailsActivity.class));
            finish();
        });
    }
}
