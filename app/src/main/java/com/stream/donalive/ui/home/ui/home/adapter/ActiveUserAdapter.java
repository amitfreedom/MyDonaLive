package com.stream.donalive.ui.home.ui.home.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.stream.donalive.databinding.ItemActiveUserBinding;
import com.stream.donalive.databinding.ItemRestaurantBinding;
import com.stream.donalive.ui.home.ui.home.models.Restaurant;
import com.stream.donalive.ui.utill.RestaurantUtil;

public class ActiveUserAdapter extends FirestoreAdapter<ActiveUserAdapter.ViewHolder>{

    public interface OnActiveUserSelectedListener {

        void onActiveUserSelected(DocumentSnapshot user);

    }

    private ActiveUserAdapter.OnActiveUserSelectedListener mListener;


    public ActiveUserAdapter(Query query, ActiveUserAdapter.OnActiveUserSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ActiveUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ActiveUserAdapter.ViewHolder(ItemActiveUserBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(ActiveUserAdapter.ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemActiveUserBinding binding;

        public ViewHolder(ItemActiveUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final ActiveUserAdapter.OnActiveUserSelectedListener listener) {

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
            binding.restaurantItemPrice.setText(RestaurantUtil.getPriceString(restaurant));

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onActiveUserSelected(snapshot);
                    }
                }
            });
        }

    }
}
