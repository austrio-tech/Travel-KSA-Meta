package com.example.travelaks;   // غيرها حسب اسم باكج مشروعك

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class activity_hotel_riyadh extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_riyadh);   // تأكد أن اسم الملف هو activity_hotels_riyadh

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // زر Back
        Button btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                Intent intent = new Intent(activity_hotel_riyadh.this, RiyadhDetailsActivity.class);
                startActivity(intent);
                finish();
            });
        }
    }
}