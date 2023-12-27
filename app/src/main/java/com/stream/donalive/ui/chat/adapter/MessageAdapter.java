package com.stream.donalive.ui.chat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.stream.donalive.databinding.ItemInroomMessageBinding;
import com.stream.donalive.ui.chat.model.Message;
import com.stream.donalive.ui.home.ui.home.adapter.FirestoreAdapter;

import java.util.Objects;

public class MessageAdapter extends FirestoreAdapter<MessageAdapter.ViewHolder> {

    public interface OnMessageUserSelectedListener {

        void onMessageUserSelected(DocumentSnapshot user);

    }

    private MessageAdapter.OnMessageUserSelectedListener mListener;


    public MessageAdapter(Query query, MessageAdapter.OnMessageUserSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageAdapter.ViewHolder(ItemInroomMessageBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(MessageAdapter.ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemInroomMessageBinding binding;

        public ViewHolder(ItemInroomMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final MessageAdapter.OnMessageUserSelectedListener listener) {

            Message message = snapshot.toObject(Message.class);

//            if (Objects.equals(restaurant.getLiveType(), "0")){
//                binding.liveType.setText("Video Live");
//            }
//
//            if (Objects.equals(restaurant.getLiveType(), "1")){
//                binding.liveType.setText("Audio Party");
//            }
//
//            if (Objects.equals(restaurant.getPhoto(), "")){
//                // Load image
//                Glide.with(binding.restaurantItemImage.getContext())
//                        .load(Constant.USER_PLACEHOLDER_PATH)
//                        .into(binding.restaurantItemImage);
//            }else {
//                // Load image
//                Glide.with(binding.restaurantItemImage.getContext())
//                        .load(restaurant.getPhoto())
//                        .into(binding.restaurantItemImage);
//            }


            binding.tvInroomMessage.setText(message.getMessageText());

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onMessageUserSelected(snapshot);
                    }
                }
            });
        }

    }
}