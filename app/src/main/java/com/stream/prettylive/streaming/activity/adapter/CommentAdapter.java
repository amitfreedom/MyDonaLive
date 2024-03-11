package com.stream.prettylive.streaming.activity.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.stream.prettylive.R;
import com.stream.prettylive.databinding.ListLiveChatBinding;
import com.stream.prettylive.streaming.activity.model.ChatMessageModel;
import com.stream.prettylive.streaming.components.message.barrage.RoundBackgroundColorSpan;
import com.stream.prettylive.streaming.internal.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        if (chatMessages.get(position).getGift().equalsIgnoreCase("")) {

            try{
                holder.chatBinding.rlGifts.setVisibility(View.GONE);
                String lv = "Lv"+chatMessages.get(position).getLevel();
                Context context = holder.itemView.getContext();
                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                RoundBackgroundColorSpan backgroundColorSpan = new RoundBackgroundColorSpan(context,
                        ContextCompat.getColor(context, R.color.pink_top),
                        ContextCompat.getColor(context, android.R.color.white));
                // Create a SpannableStringBuilder
                SpannableStringBuilder builder = new SpannableStringBuilder();
                builder.append(lv);
                builder.append(" ");


                AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(Utils.sp2px(10, displayMetrics));
                builder.setSpan(absoluteSizeSpan, 0, lv.length(),
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                builder.setSpan(backgroundColorSpan, 0, lv.length(),
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                // Append the name with red color
                int nameStart = builder.length();
                builder.append(chatMessages.get(position).getName());
                int nameEnd = builder.length();
                builder.setSpan(new ForegroundColorSpan(Color.parseColor("#fb4f90")), nameStart, nameEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// Append a space
                builder.append(" ");

// Append the message with white color
                int messageStart = builder.length();
                builder.append(chatMessages.get(position).getMessage());
                int messageEnd = builder.length();
                builder.setSpan(new ForegroundColorSpan(Color.WHITE), messageStart, messageEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                holder.chatBinding.txtUserName.setText(builder);
//                holder.chatBinding.txtUserMessage.setText(chatMessages.get(position).getMessage());
//                holder.chatBinding.txtLevel.setText("Lv"+chatMessages.get(position).getLevel());
                if (!Objects.equals(chatMessages.get(position).getImage(), "")) {
                    Glide.with(context).load(chatMessages.get(position).getImage()).into(holder.chatBinding.imgProfile);
                }else {
                    Glide.with(context).load(R.drawable.ic_user1).into(holder.chatBinding.imgProfile);
                }
            }catch (Exception e){
                Log.i("error", "onBindViewHolder: "+e);
            }
        } else {
            holder.chatBinding.rlGifts.setVisibility(View.VISIBLE);
            holder.chatBinding.txtUserMessage.setText(chatMessages.get(position).getName() + ": " + "Send Gifts");
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

