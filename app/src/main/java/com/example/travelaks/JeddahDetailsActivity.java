package com.example.travelaks;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class JeddahDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeddah_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button btnAskAi = findViewById(R.id.btn_ask_ai);
        if (btnAskAi != null) {
            btnAskAi.setOnClickListener(v -> {
                Intent intent = new Intent(this, AiChatActivity.class);
                intent.putExtra("city_name", "Jeddah");
                startActivity(intent);
            });
        }
        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            finish(); // يغلق الصفحة الحالية ويرجع للصفحة السابقة
        });

    }
}