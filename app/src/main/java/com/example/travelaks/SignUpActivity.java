package com.example.travelaks;   // ← غيرها حسب package مشروعك

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class SignUpActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private MaterialButton btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnContinue = findViewById(R.id.btn_continue);

        btnContinue.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // التحقق من الحقول الفارغة
            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Please enter your email");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                etPassword.setError("Please enter your password");
                return;
            }

            if (password.length() < 6) {
                etPassword.setError("The password should be 8 curracter ");
                return;
            }

            // كل شيء صحيح → ينتقل لصفحة المدن ولا يرجع
            Intent intent = new Intent(SignUpActivity.this, Cites.class);
            startActivity(intent);

            finish();                    // مهم: عشان ما يرجع لصفحة Sign Up
            finishAffinity();            // اختياري: يغلق كل الصفحات السابقة

            Toast.makeText(this, "Account is done!", Toast.LENGTH_SHORT).show();
        });
    }
}