package com.stream.prettylive.ui.home.ui.home.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.stream.prettylive.R;
import com.stream.prettylive.databinding.ItemRestaurantBinding;
import com.stream.prettylive.ui.home.ui.home.models.Restaurant;
import com.stream.prettylive.ui.utill.RestaurantUtil;


public class RestaurantAdapter extends FirestoreAdapter<RestaurantAdapter.ViewHolder> {

    public interface OnRestaurantSelectedListener {

        void onRestaurantSelected(DocumentSnapshot restaurant);

    }

    private OnRestaurantSelectedListener mListener;

    public RestaurantAdapter(Query query, OnRestaurantSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ItemRestaurantBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemRestaurantBinding binding;

        public ViewHolder(ItemRestaurantBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnRestaurantSelectedListener listener) {

            Restaurant restaurant = snapshot.toObject(Restaurant.class);
            Resources resources = itemView.getResources();

            // Load image
            Glide.with(binding.restaurantItemImage.getContext())
                    .load(restaurant.getPhoto())
                    .into(binding.restaurantItemImage);

            binding.restaurantItemName.setText(restaurant.getName());
//            binding.restaurantItemRating.setRating((float) restaurant.getAvgRating());
            binding.restaurantItemCity.setText(restaurant.getCity());
            binding.restaurantItemCategory.setText(restaurant.getCategory());
//            binding.restaurantItemNumRatings.setText(resources.getString(R.string.fmt_num_ratings,
//                    restaurant.getNumRatings()));
            binding.restaurantItemPrice.setText(RestaurantUtil.getPriceString(restaurant));

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onRestaurantSelected(snapshot);
                    }
                }
            });
        }

    }
}
