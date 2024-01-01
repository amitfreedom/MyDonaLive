package com.stream.donalive.streaming.activity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.stream.donalive.databinding.ItemActiveViewersBinding;
import com.stream.donalive.databinding.ItemActiveViewersListBinding;
import com.stream.donalive.streaming.activity.model.RoomUsers;
import com.stream.donalive.ui.home.ui.home.adapter.FirestoreAdapter;
import com.stream.donalive.ui.utill.Constant;

import java.util.Objects;

public class ViewersListAdapter extends FirestoreAdapter<ViewersListAdapter.ViewHolder> {

    public interface OnActiveUserSelectedListener {

        void onActiveUserSelected(DocumentSnapshot user);

    }

    private ViewersListAdapter.OnActiveUserSelectedListener mListener;


    public ViewersListAdapter(Query query, ViewersListAdapter.OnActiveUserSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewersListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewersListAdapter.ViewHolder(ItemActiveViewersListBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(ViewersListAdapter.ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemActiveViewersListBinding binding;

        public ViewHolder(ItemActiveViewersListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final ViewersListAdapter.OnActiveUserSelectedListener listener) {

            RoomUsers roomUsers = snapshot.toObject(RoomUsers.class);

            if (roomUsers!=null){
                binding.txtUsername.setText(roomUsers.getUsername());
                binding.txtUID.setText("ID : "+roomUsers.getUid());
                binding.txtLevel.setText("Lv"+roomUsers.getLevel());

                if (!Objects.equals(roomUsers.getImage(), "")){
                    // Load image
                    Glide.with(binding.imageFlag.getContext())
                            .load(roomUsers.getImage())
                            .into(binding.imageFlag);
                }else {
                    // Load image
                    Glide.with(binding.imageFlag.getContext())
                            .load(Constant.USER_PLACEHOLDER_PATH)
                            .into(binding.imageFlag);
                }
            }


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