package com.example.travelaks;   // غيرها لاسم باكج مشروعك

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Tourist_Attractions_Riyadh extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_attractions_riyadh);   // اسم ملف الـ XML

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // زر Back → يرجع إلى صفحة تفاصيل الرياض
        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(Tourist_Attractions_Riyadh.this, RiyadhDetailsActivity.class);
            startActivity(intent);
            finish();   // يغلق الصفحة الحالية
        });
    }
}