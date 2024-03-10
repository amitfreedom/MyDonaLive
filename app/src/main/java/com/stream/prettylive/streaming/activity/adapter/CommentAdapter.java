package com.stream.prettylive.streaming.activity.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.stream.prettylive.databinding.ListLiveChatBinding;
import com.stream.prettylive.streaming.activity.model.ChatMessageModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context context;
    private List<ChatMessageModel> chatMessages;


    public CommentAdapter(Context context, List<ChatMessageModel> chatMessages) {
        this.context = context;
        this.chatMessages = chatMessages;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(ListLiveChatBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        if (chatMessages.get(position).getGift().equalsIgnoreCase("")) {
            holder.chatBinding.rlGifts.setVisibility(View.GONE);
            holder.chatBinding.txtUserNameAndMessage.setText(chatMessages.get(position).getName() + ": " + chatMessages.get(position).getMessage());
        } else {
            holder.chatBinding.rlGifts.setVisibility(View.VISIBLE);
            holder.chatBinding.txtUserNameAndMessage.setText(chatMessages.get(position).getName() + ": " + "Send Gifts");
//            Glide.with(context).load(chatMessages.get(position).gift).listener(new RequestListener<Drawable>() {
//                @Override
//                public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                    holder.chatBinding.progress.setVisibility(View.GONE);
//                    return false;
//                }
//
//                @Override
//                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                    holder.chatBinding.progress.setVisibility(View.GONE);
//                    return false;
//                }
//            }).into(holder.chatBinding.imgGift);
        }

//        Glide.with(context).load(chatMessages.get(position).image).into(holder.chatBinding.imgProfile);

    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ListLiveChatBinding chatBinding;

        public ViewHolder(@NonNull @NotNull ListLiveChatBinding itemView) {
            super(itemView.getRoot());
            chatBinding = itemView;
        }
    }
}

