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
import com.stream.donalive.global.AppConstants;
import com.stream.donalive.ui.home.ui.home.models.LiveUser;
import com.stream.donalive.ui.home.ui.home.models.Restaurant;
import com.stream.donalive.ui.utill.Constant;
import com.stream.donalive.ui.utill.RestaurantUtil;

import java.util.Objects;

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

            LiveUser restaurant = snapshot.toObject(LiveUser.class);

            if (Objects.equals(restaurant.getLiveType(), "0")){
                binding.liveType.setText("Video Live");
            }

            if (Objects.equals(restaurant.getLiveType(), "1")){
                binding.liveType.setText("Audio Party");
            }

            // Load image
            Glide.with(binding.restaurantItemImage.getContext())
                    .load(Constant.USER_PLACEHOLDER_PATH)
                    .into(binding.restaurantItemImage);

            binding.restaurantItemName.setText(restaurant.getUsername());

//            binding.restaurantItemRating.setRating((float) restaurant.getAvgRating());
//            binding.restaurantItemCity.setText(restaurant.getCity());
//            binding.restaurantItemCategory.setText(restaurant.getCategory());
//            binding.restaurantItemPrice.setText(RestaurantUtil.getPriceString(restaurant));

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
