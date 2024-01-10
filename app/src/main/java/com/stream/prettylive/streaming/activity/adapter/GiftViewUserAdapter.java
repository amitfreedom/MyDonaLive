package com.stream.prettylive.streaming.activity.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.stream.prettylive.databinding.ItemActiveViewersBinding;
import com.stream.prettylive.databinding.ItemGiftUserBinding;
import com.stream.prettylive.streaming.activity.LiveAudioRoomActivity;
import com.stream.prettylive.streaming.activity.model.GIftUser;
import com.stream.prettylive.streaming.activity.model.RoomUsers;
import com.stream.prettylive.ui.home.ui.home.adapter.FirestoreAdapter;
import com.stream.prettylive.ui.utill.Constant;

import java.util.Objects;

public class GiftViewUserAdapter extends FirestoreAdapter<GiftViewUserAdapter.ViewHolder> {

    private int selectedIndex =-1;

    public interface OnActiveUserSelectedListener {

        void onActiveUserSelected(DocumentSnapshot user);

    }

    private GiftViewUserAdapter.OnActiveUserSelectedListener mListener;


    public GiftViewUserAdapter(Query query, GiftViewUserAdapter.OnActiveUserSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public GiftViewUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GiftViewUserAdapter.ViewHolder(ItemGiftUserBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(GiftViewUserAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bind(getSnapshot(position), mListener);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedIndex=position;
                if (mListener != null) {
                    mListener.onActiveUserSelected(getSnapshot(position));
                    notifyDataSetChanged();
                }
            }
        });



        if (selectedIndex==position){
            holder.binding.selectedView.setVisibility(View.VISIBLE);
        }else {
            holder.binding.selectedView.setVisibility(View.GONE);
        }

//        if (position == 0 && selectedIndex==-1) {
//            selectedIndex = 0;
//            holder.binding.selectedView.setVisibility(View.VISIBLE);
//            if (mListener != null) {
//                mListener.onActiveUserSelected(getSnapshot(position));
//            }        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemGiftUserBinding binding;

        public ViewHolder(ItemGiftUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final GiftViewUserAdapter.OnActiveUserSelectedListener listener) {

            GIftUser roomUsers = snapshot.toObject(GIftUser.class);

//            binding.name.setText(roomUsers.getUsername());
//            binding.name.setText(roomUsers.getUsername());

            if (!Objects.equals(roomUsers.getImage(), "")){
                // Load image
                Glide.with(binding.ivViewer.getContext())
                        .load(roomUsers.getImage())
                        .into(binding.ivViewer);
            }else {
                // Load image
                Glide.with(binding.ivViewer.getContext())
                        .load(Constant.USER_PLACEHOLDER_PATH)
                        .into(binding.ivViewer);
            }
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (listener != null) {
//                        listener.onActiveUserSelected(snapshot);
//                    }
//                }
//            });
        }

    }
}
