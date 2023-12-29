package com.stream.donalive.streaming.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.stream.donalive.R;
import com.stream.donalive.streaming.activity.model.GiftModel;

import java.util.ArrayList;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.CountryViewHolder> {
    private ArrayList<GiftModel> giftList;
    private Context context;

    private int index=-1;

    private GiftAdapter.Select select;

    public interface Select{
        void select(String name,String url);
    }

    public GiftAdapter(Context context, ArrayList<GiftModel> giftList, GiftAdapter.Select select) {
        this.context = context;
        this.giftList = giftList;
        this.select=select;
    }

    @NonNull
    @Override
    public GiftAdapter.CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gift, parent, false);
        return new GiftAdapter.CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftAdapter.CountryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String countryName = giftList.get(position).getName();
        String image = giftList.get(position).getImage();
        holder.textViewCountryName.setText(countryName);
        holder.txt_coin.setText("500");
        Glide.with(context).load(image).into(holder.countryImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                index=position;
                select.select(giftList.get(position).getName(),giftList.get(position).getImage());
                notifyDataSetChanged();

            }
        });

        if (index==position){
            holder.materialCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.app_color1));
        }else {
            holder.materialCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.start_color1));

        }
    }

    @Override
    public int getItemCount() {
        return giftList.size();
    }

    public static class CountryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCountryName,txt_coin;
        ImageView countryImage;
        MaterialCardView materialCardView;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_coin = itemView.findViewById(R.id.txt_coin_gift);
            textViewCountryName = itemView.findViewById(R.id.txt_gift_live_name);
            countryImage = itemView.findViewById(R.id.image_gift);
            materialCardView = itemView.findViewById(R.id.cardViewRoot);
        }
    }
}


