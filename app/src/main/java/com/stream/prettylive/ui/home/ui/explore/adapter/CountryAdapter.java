package com.stream.prettylive.ui.home.ui.explore.adapter;

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
import com.stream.prettylive.R;
import com.stream.prettylive.ui.home.ui.explore.models.CountryModel;

import java.util.ArrayList;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {
    private ArrayList<CountryModel> countryList;
    private Context context;

    private int index=-1;

    private Select select;

    public interface Select{
        void select(String name,String url);
    }

    public CountryAdapter(Context context, ArrayList<CountryModel> countryList,Select select) {
        this.context = context;
        this.countryList = countryList;
        this.select=select;
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_country, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String countryName = countryList.get(position).getName();
        String image = countryList.get(position).getImage();
        holder.textViewCountryName.setText(countryName);
        Glide.with(context).load(image).into(holder.countryImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                index=position;
                select.select(countryList.get(position).getName(),countryList.get(position).getImage());
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
        return countryList.size();
    }

    public static class CountryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCountryName;
        ImageView countryImage;
        MaterialCardView materialCardView;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCountryName = itemView.findViewById(R.id.txt_countryName);
            countryImage = itemView.findViewById(R.id.image_flag);
            materialCardView = itemView.findViewById(R.id.cardViewRoot);
        }
    }
}

