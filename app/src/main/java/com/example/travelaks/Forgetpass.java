package com.example.travelaks;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Forgetpass extends AppCompatActivity {

    private EditText etEmail, etConfirmPassword;
    private Button btnReset, btnBack;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnReset = findViewById(R.id.btn_reset);
        btnBack = findViewById(R.id.btn_back);

        etEmail.setHint("Enter your email address");
        etEmail.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        etConfirmPassword.setVisibility(View.GONE);

        btnReset.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();

            if (email.isEmpty()) {
                etEmail.setError("Please enter your email");
                return;
            }

            btnReset.setEnabled(false);
            mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this,
                        "Reset link sent to " + email + ". Check your inbox.",
                        Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    btnReset.setEnabled(true);
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
        });

        btnBack.setOnClickListener(v -> finish());
    }
}
