package com.example.travelaks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;



import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // هذا هو التصحيح الأهم
        setContentView(R.layout.activity_home);   // اسم الملف = activity_home.xml

        // ربط الأزرار (تأكد من أسماء الـ ID في الـ XML)
        Button btnLogin = findViewById(R.id.btn_login);
        Button btnSignup = findViewById(R.id.btn_signup);
        Button btnHelpDesk = findViewById(R.id.btn_helpdesk);
        Button btnAdmin = findViewById(R.id.btn_admin);

        // تسجيل الدخول
        btnLogin.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, LoginActivity.class))
        );

        // إنشاء حساب
        btnSignup.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, SignUpActivity.class))
        );

        // مكتب المساعدة
        btnHelpDesk.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, HelpDeskActivity.class))
        );

        // دخول الإدارة
        btnAdmin.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, AdminLoginActivity.class))
        );
    }
}