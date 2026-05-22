package com.example.travelaks;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminManageFaqsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private final List<DocumentSnapshot> items = new ArrayList<>();
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage);
        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Manage FAQs");
        toolbar.setNavigationOnClickListener(v -> finish());

        RecyclerView rv = findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter();
        rv.setAdapter(adapter);

        findViewById(R.id.fab_add).setOnClickListener(v -> showDialog(null));
        loadData();
    }

    private void loadData() {
        ProgressBar pb = findViewById(R.id.progress_bar);
        pb.setVisibility(View.VISIBLE);
        db.collection("faqs").orderBy("order", Query.Direction.ASCENDING).get()
            .addOnSuccessListener(snap -> {
                pb.setVisibility(View.GONE);
                items.clear();
                items.addAll(snap.getDocuments());
                adapter.notifyDataSetChanged();
            })
            .addOnFailureListener(e -> { pb.setVisibility(View.GONE);
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show(); });
    }

    private void showDialog(DocumentSnapshot existing) {
        Context ctx = this;
        int pad = dpToPx(16);

        EditText etQuestion = field(ctx, "Question",    existing, "question");
        EditText etAnswer   = field(ctx, "Answer",      existing, "answer");
        etAnswer.setMinLines(3);
        EditText etOrder    = field(ctx, "Order (1, 2…)", existing, "order");

        CheckBox cbVisible  = new CheckBox(ctx);
        cbVisible.setText("Visible to users");
        cbVisible.setChecked(existing == null || Boolean.TRUE.equals(existing.getBoolean("isVisible")));

        LinearLayout layout = new LinearLayout(ctx);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(pad, pad, pad, pad);
        layout.addView(etQuestion);
        layout.addView(etAnswer);
        layout.addView(etOrder);
        layout.addView(cbVisible);

        ScrollView sv = new ScrollView(ctx);
        sv.addView(layout);

        new AlertDialog.Builder(ctx)
            .setTitle(existing == null ? "Add FAQ" : "Edit FAQ")
            .setView(sv)
            .setPositiveButton("Save", (d, w) -> {
                Map<String, Object> data = new HashMap<>();
                data.put("question",  etQuestion.getText().toString().trim());
                data.put("answer",    etAnswer.getText().toString().trim());
                data.put("order",     parseInt(etOrder));
                data.put("isVisible", cbVisible.isChecked());

                if (existing == null)
                    db.collection("faqs").add(data)
                        .addOnSuccessListener(r -> { Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show(); loadData(); })
                        .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show());
                else
                    db.collection("faqs").document(existing.getId()).update(data)
                        .addOnSuccessListener(r -> { Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show(); loadData(); })
                        .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show());
            })
            .setNegativeButton("Cancel", null).show();
    }

    private void confirmDelete(DocumentSnapshot doc) {
        new AlertDialog.Builder(this)
            .setTitle("Delete FAQ")
            .setMessage("Delete this FAQ?")
            .setPositiveButton("Delete", (d, w) ->
                db.collection("faqs").document(doc.getId()).delete()
                    .addOnSuccessListener(v -> { Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show(); loadData(); })
                    .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show()))
            .setNegativeButton("Cancel", null).show();
    }

    private EditText field(Context ctx, String hint, DocumentSnapshot doc, String key) {
        EditText et = new EditText(ctx);
        et.setHint(hint);
        et.setPadding(16, 16, 16, 16);
        if (doc != null) { Object v = doc.get(key); et.setText(v != null ? v.toString() : ""); }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = dpToPx(8);
        et.setLayoutParams(lp);
        return et;
    }

    private int parseInt(EditText et) {
        try { return Integer.parseInt(et.getText().toString().trim()); } catch (Exception e) { return 0; }
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    class Adapter extends RecyclerView.Adapter<Adapter.VH> {
        class VH extends RecyclerView.ViewHolder {
            TextView title, subtitle;
            VH(View v) {
                super(v);
                title    = v.findViewById(R.id.tv_row_title);
                subtitle = v.findViewById(R.id.tv_row_subtitle);
                v.findViewById(R.id.btn_edit).setOnClickListener(ev -> showDialog(items.get(getAdapterPosition())));
                v.findViewById(R.id.btn_delete).setOnClickListener(ev -> confirmDelete(items.get(getAdapterPosition())));
            }
        }
        @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int t) {
            return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_admin_row, p, false));
        }
        @Override public void onBindViewHolder(@NonNull VH h, int pos) {
            DocumentSnapshot d = items.get(pos);
            h.title.setText(d.getString("question"));
            Boolean vis = d.getBoolean("isVisible");
            h.subtitle.setText("Order: " + d.getLong("order") + " • " + (Boolean.TRUE.equals(vis) ? "Visible" : "Hidden"));
        }
        @Override public int getItemCount() { return items.size(); }
    }
}
