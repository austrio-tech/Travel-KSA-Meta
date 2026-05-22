package com.example.travelaks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HelpDeskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_desk);

        // ربط كل الأسئلة والإجابات بنفس الطريقة
        setupExpandable(R.id.layout1, R.id.ans1);
        setupExpandable(R.id.layout2, R.id.ans2);
        setupExpandable(R.id.layout3, R.id.ans3);
        setupExpandable(R.id.layout4, R.id.ans4);
        setupExpandable(R.id.layout5, R.id.ans5);
        setupExpandable(R.id.layout6, R.id.ans6);

        // زر الرجوع (Back) يروح لصفحة Home
        findViewById(R.id.btn_back).setOnClickListener(v -> {
            Intent intent = new Intent(HelpDeskActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish(); // تقفل صفحة Help Desk
        });
    }

    // دالة واحدة تشتغل على أي سؤال + جواب
    private void setupExpandable(int layoutId, int answerId) {
        LinearLayout layout = findViewById(layoutId);
        TextView answer = findViewById(answerId);

        layout.setOnClickListener(v -> {
            if (answer.getVisibility() == View.GONE) {
                answer.setVisibility(View.VISIBLE);
            } else {
                answer.setVisibility(View.GONE);
            }
        });
    }
}