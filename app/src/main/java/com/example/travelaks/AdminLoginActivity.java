package com.example.travelaks;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        EditText etEmail = findViewById(R.id.et_admin_email);
        EditText etPassword = findViewById(R.id.et_admin_password);
        Button btnLogin = findViewById(R.id.btn_admin_login);
        Button btnBack = findViewById(R.id.btn_back);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty()) { etEmail.setError("Enter email"); return; }
            if (password.isEmpty()) { etPassword.setError("Enter password"); return; }

            btnLogin.setEnabled(false);
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String uid = authResult.getUser().getUid();
                    db.collection("users").document(uid).get()
                        .addOnSuccessListener(doc -> {
                            String role = doc.getString("role");
                            if ("admin".equals(role)) {
                                Toast.makeText(this, "Welcome, Admin!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AdminLoginActivity.this, Cites.class));
                                finish();
                            } else {
                                mAuth.signOut();
                                btnLogin.setEnabled(true);
                                Toast.makeText(this, "Access denied: not an admin account", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(e -> {
                            btnLogin.setEnabled(true);
                            Toast.makeText(this, "Error checking role: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
                })
                .addOnFailureListener(e -> {
                    btnLogin.setEnabled(true);
                    Toast.makeText(this, "Login failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
        });

        btnBack.setOnClickListener(v -> finish());
    }
}
