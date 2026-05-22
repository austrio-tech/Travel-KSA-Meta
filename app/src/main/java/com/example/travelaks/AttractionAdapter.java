package com.example.travelaks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelaks.data.model.Attraction;

import java.util.List;

public class AttractionAdapter extends RecyclerView.Adapter<AttractionAdapter.ViewHolder> {

    private final Context context;
    private final List<Attraction> attractions;

    public AttractionAdapter(Context context, List<Attraction> attractions) {
        this.context = context;
        this.attractions = attractions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_attraction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Attraction attraction = attractions.get(position);
        holder.tvName.setText(attraction.getName());
        holder.tvCategory.setText(attraction.getCategory() + " • " + attraction.getRating() + " ★");
        holder.tvDescription.setText(attraction.getDescription());

        int resId = context.getResources().getIdentifier(
            attraction.getImageUrl(), "drawable", context.getPackageName());
        if (resId != 0) {
            holder.ivImage.setImageResource(resId);
        }
    }

    @Override
    public int getItemCount() { return attractions.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvCategory, tvDescription;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_attraction_image);
            tvName = itemView.findViewById(R.id.tv_attraction_name);
            tvCategory = itemView.findViewById(R.id.tv_attraction_category);
            tvDescription = itemView.findViewById(R.id.tv_attraction_description);
        }
    }
}
