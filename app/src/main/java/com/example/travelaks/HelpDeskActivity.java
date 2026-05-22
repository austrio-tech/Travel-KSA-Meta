package com.example.travelaks;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HelpDeskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_desk);

        RecyclerView recyclerView = findViewById(R.id.recycler_faqs);
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<FaqItem> faqs = new ArrayList<>();
        FaqAdapter adapter = new FaqAdapter(faqs);
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance()
            .collection("faqs")
            .orderBy("order")
            .get()
            .addOnSuccessListener(snap -> {
                progressBar.setVisibility(View.GONE);
                for (QueryDocumentSnapshot doc : snap) {
                    Boolean visible = doc.getBoolean("isVisible");
                    if (Boolean.FALSE.equals(visible)) continue; // skip hidden FAQs client-side
                    String q = doc.getString("question");
                    String a = doc.getString("answer");
                    if (q != null && a != null) faqs.add(new FaqItem(q, a));
                }
                adapter.notifyDataSetChanged();
            })
            .addOnFailureListener(e -> {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Could not load FAQs: " + e.getMessage(), Toast.LENGTH_LONG).show();
            });

        findViewById(R.id.btn_back).setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            finish();
        });
    }

    // ── Data class ────────────────────────────────────────────
    static class FaqItem {
        final String question, answer;
        boolean expanded = false;
        FaqItem(String q, String a) { question = q; answer = a; }
    }

    // ── Adapter ───────────────────────────────────────────────
    static class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.VH> {
        private final List<FaqItem> items;
        FaqAdapter(List<FaqItem> items) { this.items = items; }

        @NonNull @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_faq, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH h, int pos) {
            FaqItem item = items.get(pos);
            h.tvQuestion.setText(item.question);
            h.tvAnswer.setText(item.answer);
            h.tvAnswer.setVisibility(item.expanded ? View.VISIBLE : View.GONE);
            h.tvArrow.setText(item.expanded ? "▲" : "▼");

            h.itemView.setOnClickListener(v -> {
                item.expanded = !item.expanded;
                notifyItemChanged(pos);
            });
        }

        @Override public int getItemCount() { return items.size(); }

        static class VH extends RecyclerView.ViewHolder {
            TextView tvQuestion, tvAnswer, tvArrow;
            VH(View v) {
                super(v);
                tvQuestion = v.findViewById(R.id.tv_question);
                tvAnswer   = v.findViewById(R.id.tv_answer);
                tvArrow    = v.findViewById(R.id.tv_arrow);
            }
        }
    }
}
