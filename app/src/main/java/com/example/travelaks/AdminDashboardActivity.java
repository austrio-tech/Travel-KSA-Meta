package com.example.travelaks;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminDashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private FirebaseFirestore db;

    // Stat card data: {color, icon, label, collection}
    private static final Object[][] CARDS = {
        {"#2563EB", "👥", "Users",       "users",       AdminManageUsersActivity.class},
        {"#059669", "🏙️", "Cities",      "cities",      AdminManageCitiesActivity.class},
        {"#D97706", "🏨", "Hotels",      "hotels",      AdminManageHotelsActivity.class},
        {"#7C3AED", "🏛️", "Attractions", "attractions", AdminManageAttractionsActivity.class},
        {"#DC2626", "🎭", "Activities",  "activities",  AdminManageActivitiesActivity.class},
        {"#0891B2", "❓", "FAQs",        "faqs",        AdminManageFaqsActivity.class},
    };

    private static final int[] CARD_IDS = {
        R.id.card_users, R.id.card_cities, R.id.card_hotels,
        R.id.card_attractions, R.id.card_activities, R.id.card_faqs
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_dashboard);

        toolbar.setNavigationOnClickListener(v ->
            drawerLayout.openDrawer(GravityCompat.START));

        setupStatCards();

        findViewById(R.id.btn_seed).setOnClickListener(v -> confirmSeed());
        findViewById(R.id.btn_open_app).setOnClickListener(v -> {
            startActivity(new Intent(this, Cites.class));
            finish();
        });
    }

    private void setupStatCards() {
        for (int i = 0; i < CARD_IDS.length; i++) {
            View cardView = findViewById(CARD_IDS[i]);
            Object[] data = CARDS[i];

            String color      = (String) data[0];
            String icon       = (String) data[1];
            String label      = (String) data[2];
            String collection = (String) data[3];
            Class<?> target   = (Class<?>) data[4];

            cardView.setBackgroundColor(android.graphics.Color.parseColor(color));
            ((TextView) cardView.findViewById(R.id.tv_card_icon)).setText(icon);
            ((TextView) cardView.findViewById(R.id.tv_card_label)).setText(label);

            TextView tvCount = cardView.findViewById(R.id.tv_card_count);
            tvCount.setText("…");

            db.collection(collection).get()
                .addOnSuccessListener(snap -> tvCount.setText(String.valueOf(snap.size())))
                .addOnFailureListener(e -> tvCount.setText("–"));

            cardView.setOnClickListener(v ->
                startActivity(new Intent(this, target)));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupStatCards(); // refresh counts when returning from a manage screen
    }

    private void confirmSeed() {
        new AlertDialog.Builder(this)
            .setTitle("Seed Firebase Data")
            .setMessage("This will upload all default cities, hotels, attractions, activities, and FAQs. Existing data will be skipped if collections are non-empty.")
            .setPositiveButton("Seed Now", (d, w) -> runSeeder())
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void runSeeder() {
        Toast.makeText(this, "Seeding... please wait", Toast.LENGTH_LONG).show();
        FirebaseDataSeeder.seedAll((success, message) ->
            runOnUiThread(() -> {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                if (success) setupStatCards();
            })
        );
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        int id = item.getItemId();
        if      (id == R.id.nav_dashboard)   { /* already here */ }
        else if (id == R.id.nav_users)       startActivity(new Intent(this, AdminManageUsersActivity.class));
        else if (id == R.id.nav_cities)      startActivity(new Intent(this, AdminManageCitiesActivity.class));
        else if (id == R.id.nav_hotels)      startActivity(new Intent(this, AdminManageHotelsActivity.class));
        else if (id == R.id.nav_attractions) startActivity(new Intent(this, AdminManageAttractionsActivity.class));
        else if (id == R.id.nav_activities)  startActivity(new Intent(this, AdminManageActivitiesActivity.class));
        else if (id == R.id.nav_faqs)        startActivity(new Intent(this, AdminManageFaqsActivity.class));
        else if (id == R.id.nav_seed)        confirmSeed();
        else if (id == R.id.nav_open_app)  { startActivity(new Intent(this, Cites.class)); finish(); }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
