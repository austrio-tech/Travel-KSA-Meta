package com.example.travelaks;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import android.widget.EditText;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private MaterialButton btnContinue;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Session check — skip signup screen if already signed in
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(SignUpActivity.this, Cites.class));
            finishAffinity();
            return;
        }

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnContinue = findViewById(R.id.btn_continue);

        btnContinue.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Please enter your email");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                etPassword.setError("Please enter your password");
                return;
            }
            if (password.length() < 6) {
                etPassword.setError("Password must be at least 6 characters");
                return;
            }

            btnContinue.setEnabled(false);
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String uid = authResult.getUser().getUid();
                    Map<String, Object> userDoc = new HashMap<>();
                    userDoc.put("email", email);
                    userDoc.put("createdAt", Timestamp.now());
                    userDoc.put("lastLoginAt", Timestamp.now());
                    userDoc.put("role", "user");

                    db.collection("users").document(uid).set(userDoc)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this, Cites.class));
                            finishAffinity();
                        })
                        .addOnFailureListener(e -> {
                            btnContinue.setEnabled(true);
                            Toast.makeText(this, "Error saving profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
                })
                .addOnFailureListener(e -> {
                    btnContinue.setEnabled(true);
                    Toast.makeText(this, "Sign up failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
        });
    }
}
