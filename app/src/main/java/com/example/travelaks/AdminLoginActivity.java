package com.example.travelaks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginActivity extends AppCompatActivity {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    static final String PREFS_NAME = "admin_session";
    static final String KEY_LOGGED_IN = "is_logged_in";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Session check — skip login if already authenticated
        if (getPrefs().getBoolean(KEY_LOGGED_IN, false)) {
            goToDashboard();
            return;
        }

        setContentView(R.layout.activity_admin_login);

        EditText etUsername = findViewById(R.id.et_admin_email);
        EditText etPassword = findViewById(R.id.et_admin_password);
        Button btnLogin = findViewById(R.id.btn_admin_login);
        Button btnBack = findViewById(R.id.btn_back);

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty()) { etUsername.setError("Enter username"); return; }
            if (password.isEmpty()) { etPassword.setError("Enter password"); return; }

            if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
                // Persist session
                getPrefs().edit().putBoolean(KEY_LOGGED_IN, true).apply();
                goToDashboard();
            } else {
                etPassword.setError("Invalid credentials");
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private SharedPreferences getPrefs() {
        return getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    }

    private void goToDashboard() {
        startActivity(new Intent(this, AdminDashboardActivity.class));
        finish();
    }
}
