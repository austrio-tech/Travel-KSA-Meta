package com.example.travelaks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import java.util.List;

public class AdminManageUsersActivity extends AppCompatActivity {

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
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Manage Users");
        toolbar.setNavigationOnClickListener(v -> finish());

        // Users are read-only (Add via normal signup) — hide FAB
        findViewById(R.id.fab_add).setVisibility(View.GONE);

        RecyclerView rv = findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter();
        rv.setAdapter(adapter);

        loadData();
    }

    private void loadData() {
        ProgressBar pb = findViewById(R.id.progress_bar);
        pb.setVisibility(View.VISIBLE);
        db.collection("users").get()
            .addOnSuccessListener(snap -> {
                pb.setVisibility(View.GONE);
                items.clear();
                items.addAll(snap.getDocuments());
                adapter.notifyDataSetChanged();
            })
            .addOnFailureListener(e -> {
                pb.setVisibility(View.GONE);
                Toast.makeText(this, "Load failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            });
    }

    private void showEditDialog(DocumentSnapshot doc) {
        Spinner spinner = new Spinner(this);
        ArrayAdapter<String> sa = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_dropdown_item,
            new String[]{"user", "admin"});
        spinner.setAdapter(sa);
        String current = doc.getString("role");
        spinner.setSelection("admin".equals(current) ? 1 : 0);

        new AlertDialog.Builder(this)
            .setTitle("Change Role — " + doc.getString("email"))
            .setView(spinner)
            .setPositiveButton("Save", (d, w) -> {
                String newRole = (String) spinner.getSelectedItem();
                db.collection("users").document(doc.getId())
                    .update("role", newRole)
                    .addOnSuccessListener(v -> { Toast.makeText(this, "Role updated", Toast.LENGTH_SHORT).show(); loadData(); })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void confirmDelete(DocumentSnapshot doc) {
        new AlertDialog.Builder(this)
            .setTitle("Delete User")
            .setMessage("Remove " + doc.getString("email") + " from Firestore?\n(Firebase Auth account is not deleted.)")
            .setPositiveButton("Delete", (d, w) ->
                db.collection("users").document(doc.getId()).delete()
                    .addOnSuccessListener(v -> { Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show(); loadData(); })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show()))
            .setNegativeButton("Cancel", null)
            .show();
    }

    class Adapter extends RecyclerView.Adapter<Adapter.VH> {
        class VH extends RecyclerView.ViewHolder {
            TextView title, subtitle;
            VH(View v) {
                super(v);
                title    = v.findViewById(R.id.tv_row_title);
                subtitle = v.findViewById(R.id.tv_row_subtitle);
                v.findViewById(R.id.btn_edit).setOnClickListener(ev ->
                    showEditDialog(items.get(getAdapterPosition())));
                v.findViewById(R.id.btn_delete).setOnClickListener(ev ->
                    confirmDelete(items.get(getAdapterPosition())));
            }
        }

        @NonNull @Override
        public VH onCreateViewHolder(@NonNull ViewGroup p, int t) {
            return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_admin_row, p, false));
        }

        @Override
        public void onBindViewHolder(@NonNull VH h, int pos) {
            DocumentSnapshot d = items.get(pos);
            h.title.setText(d.getString("email") != null ? d.getString("email") : d.getId());
            h.subtitle.setText("Role: " + d.getString("role"));
        }

        @Override public int getItemCount() { return items.size(); }
    }
}
