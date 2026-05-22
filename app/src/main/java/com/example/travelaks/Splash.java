package com.example.travelaks;   // تأكدي أن هذا هو اسم الباكيج عندك

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // تأخير 3 ثواني ثم الانتقال إلى صفحة اللوجن
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Splash.this, HomeActivity.class);
            startActivity(intent);
            finish();   // إغلاق السبلاش حتى لا يرجع له المستخدم
        }, 3000); // 3000 = 3 ثواني
    }
}