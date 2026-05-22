package com.example.travelaks;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class Forgetpass extends AppCompatActivity {

    private EditText etNewPassword, etConfirmPassword;
    private Button btnReset, btnBack;

    // شرط قوي لكلمة المرور: 8 أحرف + حرف كبير + حرف صغير + رقم + رمز
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass);

        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnReset = findViewById(R.id.btn_reset);
        btnBack = findViewById(R.id.btn_back);

        btnReset.setOnClickListener(v -> {
            String newPass = etNewPassword.getText().toString().trim();
            String confirmPass = etConfirmPassword.getText().toString().trim();

            if (newPass.isEmpty()) {
                etNewPassword.setError("Please enter new password");
                return;
            }

            if (confirmPass.isEmpty()) {
                etConfirmPassword.setError("Please confirm your password");
                return;
            }

            if (!newPass.equals(confirmPass)) {
                etConfirmPassword.setError("Passwords do not match");
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // التحقق من قوة كلمة المرور
            if (!isStrongPassword(newPass)) {
                etNewPassword.setError("Password must be at least 8 characters and contain:\n• Uppercase letter\n• Lowercase letter\n• Number\n• Special character (@#$%^&+=!)");
                Toast.makeText(this, "Weak password", Toast.LENGTH_LONG).show();
                return;
            }

            // إذا نجح كل شيء
            Toast.makeText(this, "Password has been reset successfully!", Toast.LENGTH_LONG).show();

            finish(); // يرجع لصفحة اللوجن
        });

        btnBack.setOnClickListener(v -> finish());
    }

    // دالة التحقق من قوة كلمة المرور
    private boolean isStrongPassword(String password) {
        return Pattern.compile(PASSWORD_PATTERN).matcher(password).matches();
    }
}