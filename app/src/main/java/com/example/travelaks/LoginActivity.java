package com.example.travelaks;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etEmail = findViewById(R.id.et_email);
        EditText etPassword = findViewById(R.id.et_password);
        Button btnContinue = findViewById(R.id.btn_continue);
        Button btnBack = findViewById(R.id.btn_back);
        TextView tvForgotPassword = findViewById(R.id.tv_forgot);

        // CONTINUE Button
        btnContinue.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Check if email is empty
            if (email.isEmpty()) {
                etEmail.setError("Please enter your email");
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if password is empty
            if (password.isEmpty()) {
                etPassword.setError("Please enter your password");
                Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Test Login Credentials
            if (email.equals("user@travelaks.com") && password.equals("123456")) {

                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, Cites.class);
                startActivity(intent);
                finish(); // Close Login screen

            } else {
                // Wrong password
                Toast.makeText(this, "Incorrect password", Toast.LENGTH_LONG).show();
                etPassword.setError("Incorrect password");
            }
        });

        // Forgot Password
        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, Forgetpass.class);
            startActivity(intent);
        });

        // Back Button
        btnBack.setOnClickListener(v -> finish());
    }
}