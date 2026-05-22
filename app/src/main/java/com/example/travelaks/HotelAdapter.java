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

import com.example.travelaks.data.model.Hotel;

import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHolder> {

    private final Context context;
    private final List<Hotel> hotels;

    public HotelAdapter(Context context, List<Hotel> hotels) {
        this.context = context;
        this.hotels = hotels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hotel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hotel hotel = hotels.get(position);
        holder.tvName.setText(hotel.getName());
        holder.tvPhone.setText("📞 " + hotel.getPhone());
        holder.tvRating.setText(hotel.getRating() + " ★  •  " + hotel.getCategory());
        holder.tvDescription.setText(hotel.getDescription());

        int resId = context.getResources().getIdentifier(
            hotel.getImageUrl(), "drawable", context.getPackageName());
        if (resId != 0) holder.ivImage.setImageResource(resId);

        holder.btnMap.setOnClickListener(v ->
            openMap(hotel.getLatitude(), hotel.getLongitude(), hotel.getName()));
    }

    @Override
    public int getItemCount() { return hotels.size(); }

    private void openMap(double lat, double lng, String name) {
        String url = "https://www.google.com/maps/search/?api=1&query="
            + lat + "," + lng + "&query_place_id=" + Uri.encode(name);
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvPhone, tvRating, tvDescription;
        Button btnMap;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage       = itemView.findViewById(R.id.iv_hotel_image);
            tvName        = itemView.findViewById(R.id.tv_hotel_name);
            tvPhone       = itemView.findViewById(R.id.tv_hotel_phone);
            tvRating      = itemView.findViewById(R.id.tv_hotel_rating);
            tvDescription = itemView.findViewById(R.id.tv_hotel_description);
            btnMap        = itemView.findViewById(R.id.btn_map);
        }
    }
}
