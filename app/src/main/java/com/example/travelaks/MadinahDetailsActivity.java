package com.example.travelaks;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class MadinahDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_madinah_details);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        loadDescription("madinah");

        findViewById(R.id.btnHotels).setOnClickListener(v ->
            openSection(activity_hotel_riyadh.class, "Madinah"));
        findViewById(R.id.btnAttractions).setOnClickListener(v ->
            openSection(Tourist_Attractions_Riyadh.class, "Madinah"));
        findViewById(R.id.btnEvents).setOnClickListener(v ->
            openSection(activity_activitiess_riyadh.class, "Madinah"));
        findViewById(R.id.btn_ask_ai).setOnClickListener(v -> {
            Intent i = new Intent(this, AiChatActivity.class);
            i.putExtra("city_name", "Madinah");
            startActivity(i);
        });
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    private void loadDescription(String cityId) {
        TextView tv = findViewById(R.id.tv_description);
        FirebaseFirestore.getInstance().collection("cities").document(cityId).get()
            .addOnSuccessListener(doc -> {
                if (doc.exists() && tv != null) {
                    String desc = doc.getString("description");
                    if (desc != null) tv.setText(desc);
                }
            });
    }

    private void openSection(Class<?> target, String city) {
        Intent i = new Intent(this, target);
        i.putExtra("CITY_NAME", city);
        startActivity(i);
    }
}
