package com.example.travelaks;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminManageCitiesActivity extends AppCompatActivity {

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
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Manage Cities");
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
        db.collection("cities").get()
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

        EditText etName   = field(ctx, "Name",        existing, "name");
        EditText etDesc   = field(ctx, "Description", existing, "description");
        EditText etImg    = field(ctx, "Image (drawable name)", existing, "imageUrl");
        EditText etRegion = field(ctx, "Region",      existing, "region");
        EditText etLat    = field(ctx, "Latitude",    existing, "latitude");
        EditText etLng    = field(ctx, "Longitude",   existing, "longitude");

        LinearLayout layout = new LinearLayout(ctx);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(pad, pad, pad, pad);
        for (EditText et : new EditText[]{etName, etDesc, etImg, etRegion, etLat, etLng})
            layout.addView(et);

        ScrollView sv = new ScrollView(ctx);
        sv.addView(layout);

        new AlertDialog.Builder(ctx)
            .setTitle(existing == null ? "Add City" : "Edit City")
            .setView(sv)
            .setPositiveButton("Save", (d, w) -> {
                Map<String, Object> data = new HashMap<>();
                data.put("name",        etName.getText().toString().trim());
                data.put("description", etDesc.getText().toString().trim());
                data.put("imageUrl",    etImg.getText().toString().trim());
                data.put("country",     "Saudi Arabia");
                data.put("region",      etRegion.getText().toString().trim());
                data.put("latitude",    parseDouble(etLat));
                data.put("longitude",   parseDouble(etLng));

                if (existing == null) {
                    db.collection("cities").add(data)
                        .addOnSuccessListener(r -> { Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show(); loadData(); })
                        .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show());
                } else {
                    db.collection("cities").document(existing.getId()).update(data)
                        .addOnSuccessListener(r -> { Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show(); loadData(); })
                        .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show());
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void confirmDelete(DocumentSnapshot doc) {
        new AlertDialog.Builder(this)
            .setTitle("Delete City")
            .setMessage("Delete \"" + doc.getString("name") + "\"?")
            .setPositiveButton("Delete", (d, w) ->
                db.collection("cities").document(doc.getId()).delete()
                    .addOnSuccessListener(v -> { Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show(); loadData(); })
                    .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show()))
            .setNegativeButton("Cancel", null).show();
    }

    private EditText field(Context ctx, String hint, DocumentSnapshot doc, String key) {
        EditText et = new EditText(ctx);
        et.setHint(hint);
        et.setPadding(16, 16, 16, 16);
        if (doc != null) {
            Object val = doc.get(key);
            et.setText(val != null ? val.toString() : "");
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = dpToPx(8);
        et.setLayoutParams(lp);
        return et;
    }

    private double parseDouble(EditText et) {
        try { return Double.parseDouble(et.getText().toString().trim()); } catch (Exception e) { return 0; }
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
            h.title.setText(d.getString("name"));
            h.subtitle.setText(d.getString("region"));
        }
        @Override public int getItemCount() { return items.size(); }
    }
}
