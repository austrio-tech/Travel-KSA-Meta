package com.example.travelaks;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelaks.data.model.ActivityItem;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private final Context context;
    private final List<ActivityItem> activities;

    public ActivityAdapter(Context context, List<ActivityItem> activities) {
        this.context = context;
        this.activities = activities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActivityItem item = activities.get(position);
        holder.tvName.setText(item.getName());
        holder.tvRating.setText(item.getRating() + " ★  •  " + item.getCategory());
        holder.tvHours.setText("🕐 " + item.getOpenTime() + " – " + item.getCloseTime());
        holder.tvDescription.setText(item.getDescription());
        holder.tvLocation.setText("📍 " + item.getLocation());

        int resId = context.getResources().getIdentifier(
            item.getImageUrl(), "drawable", context.getPackageName());
        if (resId != 0) holder.ivImage.setImageResource(resId);

        holder.btnMap.setOnClickListener(v ->
            openMap(item.getLatitude(), item.getLongitude(), item.getName()));
    }

    @Override
    public int getItemCount() { return activities.size(); }

    private void openMap(double lat, double lng, String name) {
        String url = "https://www.google.com/maps/search/?api=1&query="
            + lat + "," + lng + "&query_place_id=" + Uri.encode(name);
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvRating, tvHours, tvDescription, tvLocation;
        Button btnMap;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage       = itemView.findViewById(R.id.iv_activity_image);
            tvName        = itemView.findViewById(R.id.tv_activity_name);
            tvRating      = itemView.findViewById(R.id.tv_activity_rating);
            tvHours       = itemView.findViewById(R.id.tv_activity_hours);
            tvDescription = itemView.findViewById(R.id.tv_activity_description);
            tvLocation    = itemView.findViewById(R.id.tv_activity_location);
            btnMap        = itemView.findViewById(R.id.btn_map);
        }
    }
}
