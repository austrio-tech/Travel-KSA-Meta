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

import com.example.travelaks.data.model.ActivityItem;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class activity_activitiess_riyadh extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitiess_riyadh);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recycler_activities);
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        Button btnBack = findViewById(R.id.btn_back);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<ActivityItem> activityList = new ArrayList<>();
        ActivityAdapter adapter = new ActivityAdapter(this, activityList);
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance()
            .collection("activities")
            .whereEqualTo("city", "Riyadh")
            .orderBy("rating", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener(querySnapshot -> {
                progressBar.setVisibility(View.GONE);
                activityList.clear();
                activityList.addAll(querySnapshot.toObjects(ActivityItem.class));
                adapter.notifyDataSetChanged();
            })
            .addOnFailureListener(e -> {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Failed to load activities: " + e.getMessage(), Toast.LENGTH_LONG).show();
            });

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                startActivity(new Intent(activity_activitiess_riyadh.this, RiyadhDetailsActivity.class));
                finish();
            });
        }
    }
}
