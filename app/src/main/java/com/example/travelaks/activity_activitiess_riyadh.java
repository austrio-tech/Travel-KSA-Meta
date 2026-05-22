package com.example.travelaks;   // غيرها حسب اسم الباكج في مشروعك

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class activity_activitiess_riyadh extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitiess_riyadh);   // تأكد أن اسم ملف XML صحيح

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // زر Back
        Button btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                Intent intent = new Intent(activity_activitiess_riyadh.this, RiyadhDetailsActivity.class);
                startActivity(intent);
                finish();
            });
        }
    }
}
