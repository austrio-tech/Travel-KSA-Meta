package com.example.travelaks;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginActivity extends AppCompatActivity {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        EditText etUsername = findViewById(R.id.et_admin_email);
        EditText etPassword = findViewById(R.id.et_admin_password);
        Button btnLogin = findViewById(R.id.btn_admin_login);
        Button btnBack = findViewById(R.id.btn_back);

        etUsername.setHint("Username");

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty()) { etUsername.setError("Enter username"); return; }
            if (password.isEmpty()) { etPassword.setError("Enter password"); return; }

            if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
                showAdminPanel();
            } else {
                etPassword.setError("Invalid credentials");
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void showAdminPanel() {
        new AlertDialog.Builder(this)
            .setTitle("Admin Panel")
            .setMessage("Welcome, Admin! What would you like to do?")
            .setPositiveButton("Seed Firebase Data", (dialog, which) -> runSeeder())
            .setNegativeButton("Open App", (dialog, which) -> {
                startActivity(new Intent(AdminLoginActivity.this, Cites.class));
                finish();
            })
            .setCancelable(false)
            .show();
    }

    private void runSeeder() {
        Toast.makeText(this, "Seeding data... please wait.", Toast.LENGTH_LONG).show();

        FirebaseDataSeeder.seedAll((success, message) ->
            runOnUiThread(() -> {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                if (success) {
                    new AlertDialog.Builder(this)
                        .setTitle("Seeding Complete")
                        .setMessage(message + "\n\nAll cities, hotels, attractions, activities, and FAQs have been uploaded to Firebase.")
                        .setPositiveButton("Open App", (d, w) -> {
                            startActivity(new Intent(AdminLoginActivity.this, Cites.class));
                            finish();
                        })
                        .show();
                }
            })
        );
    }
}
