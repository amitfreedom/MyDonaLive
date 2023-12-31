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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.stream.donalive.R;
import com.stream.donalive.databinding.ItemGiftBinding;
import com.stream.donalive.streaming.activity.model.GiftModel;
import com.stream.donalive.ui.home.ui.home.adapter.FirestoreAdapter;
import com.stream.donalive.ui.home.ui.home.models.LiveUser;
import com.stream.donalive.ui.utill.Constant;
import com.stream.donalive.ui.utill.Convert;

import java.util.ArrayList;
import java.util.Objects;

public class GiftAdapter extends FirestoreAdapter<GiftAdapter.ViewHolder> {

    private int index=-1;

    public interface OnGiftSelectedListener {

        void onGiftSelected(DocumentSnapshot user);

    }

    private GiftAdapter.OnGiftSelectedListener mListener;


    public GiftAdapter(Query query, GiftAdapter.OnGiftSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public GiftAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GiftAdapter.ViewHolder(ItemGiftBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(GiftAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bind(getSnapshot(position), mListener);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index=position;
                mListener.onGiftSelected(getSnapshot(position));
                notifyDataSetChanged();

            }

        });
        if (index==position){
            holder.binding.cardViewRoot.setCardBackgroundColor(ContextCompat.getColor(holder.binding.cardViewRoot.getContext(), R.color.pink_top));
        }else {
            holder.binding.cardViewRoot.setCardBackgroundColor(ContextCompat.getColor(holder.binding.cardViewRoot.getContext(), R.color.white));

        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemGiftBinding binding;

        public ViewHolder(ItemGiftBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final GiftAdapter.OnGiftSelectedListener listener) {

            GiftModel restaurant = snapshot.toObject(GiftModel.class);

//            if (Objects.equals(restaurant.getLiveType(), "0")){
//                binding.liveType.setText("Video Live");
//            }
//
//            if (Objects.equals(restaurant.getLiveType(), "1")){
//                binding.liveType.setText("Audio Party");
//            }

            if (Objects.equals(restaurant.getImage(), "")){
                // Load image
                Glide.with(binding.imageGift.getContext())
                        .load(Constant.USER_PLACEHOLDER_PATH)
                        .into(binding.imageGift);
            }else {
                // Load image
                Glide.with(binding.imageGift.getContext())
                        .load(restaurant.getImage())
                        .into(binding.imageGift);
            }


            binding.txtGiftLiveName.setText(restaurant.getGiftName());
            binding.txtCoinGift.setText(new Convert().prettyCount(Integer.parseInt(restaurant.getPrice())));



//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (listener != null) {
//                        index=position;
//                        listener.onGiftSelected(snapshot);
//                    }
//                }
//            });
        }

    }
}


