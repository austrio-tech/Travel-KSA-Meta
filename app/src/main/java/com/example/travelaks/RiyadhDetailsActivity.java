package com.example.travelaks;

import static com.example.travelaks.R.id.btnHotels;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RiyadhDetailsActivity extends AppCompatActivity {

    private static final String TAG = "RiyadhDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riyadh_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // محاولة العثور على الزر
        Button btnAskAi = findViewById(R.id.btn_ask_ai);

        if (btnAskAi != null) {
            Log.d(TAG, "✅ The button was successfully found.");
            Toast.makeText(this, "The button is there", Toast.LENGTH_SHORT).show();

            btnAskAi.setOnClickListener(v -> {
                Log.d(TAG, "The AI button was pressed");
                Toast.makeText(this, "Chat is opening...", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RiyadhDetailsActivity.this, AiChatActivity.class);
                intent.putExtra("city_name", "Riyadh");
                startActivity(intent);
            });
        } else {
            Log.e(TAG, "❌ Big mistake: The btn_ask_ai button is not in the layout!");
            Toast.makeText(this, "The button is not on the page!", Toast.LENGTH_LONG).show();
        }
        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            finish(); // يغلق الصفحة الحالية ويرجع للصفحة السابقة
        });
        // داخل onCreate() في RiyadhDetailsActivity.java

        @SuppressLint("WrongViewCast") Button btnHotels = findViewById(R.id.btnHotels);     // ← غير هذا الـ ID حسب اسم الزر أو الكارت في الـ XML

        btnHotels.setOnClickListener(v -> {
            Intent intent = new Intent(RiyadhDetailsActivity.this, activity_hotel_riyadh.class);
            startActivity(intent);
        });



    }
}