package com.example.travelaks;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Session check — skip login screen if already signed in
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, Cites.class));
            finish();
            return;
        }

        EditText etEmail = findViewById(R.id.et_email);
        EditText etPassword = findViewById(R.id.et_password);
        Button btnContinue = findViewById(R.id.btn_continue);
        Button btnBack = findViewById(R.id.btn_back);
        TextView tvForgotPassword = findViewById(R.id.tv_forgot);

        btnContinue.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty()) {
                etEmail.setError("Please enter your email");
                return;
            }
            if (password.isEmpty()) {
                etPassword.setError("Please enter your password");
                return;
            }

            btnContinue.setEnabled(false);
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String uid = authResult.getUser().getUid();
                    db.collection("users").document(uid)
                        .update("lastLoginAt", Timestamp.now());

                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, Cites.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    btnContinue.setEnabled(true);
                    etPassword.setError("Incorrect email or password");
                    Toast.makeText(this, "Login failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
        });

        tvForgotPassword.setOnClickListener(v ->
            startActivity(new Intent(LoginActivity.this, Forgetpass.class)));

        btnBack.setOnClickListener(v -> finish());
    }
}
