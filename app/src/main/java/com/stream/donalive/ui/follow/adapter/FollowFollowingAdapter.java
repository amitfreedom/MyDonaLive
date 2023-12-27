package com.stream.donalive.ui.follow.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.stream.donalive.R;
import com.stream.donalive.ui.home.ui.profile.models.UserDetailsModel;
import com.stream.donalive.ui.utill.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FollowFollowingAdapter extends RecyclerView.Adapter<FollowFollowingAdapter.MyViewHolder> {
    List<UserDetailsModel> userDetailsList = new ArrayList<>();

    private Select select;

    public FollowFollowingAdapter(List<UserDetailsModel> userDetailsList, Select select) {
        this.userDetailsList = userDetailsList;
        this.select = select;
    }

    public interface Select{
        void onSelectUser(UserDetailsModel detailsModel);
    }

    @NonNull
    @Override
    public FollowFollowingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_follow_user, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowFollowingAdapter.MyViewHolder holder, int position) {
        UserDetailsModel userDetailsModel = userDetailsList.get(position);
        if (Objects.equals(userDetailsModel.getImage(), "")){
            Glide.with(holder.profileImage.getContext())
                    .load(Constant.USER_PLACEHOLDER_PATH)
                    .into(holder.profileImage);
        }else {
            Glide.with(holder.profileImage.getContext())
                    .load(userDetailsModel.getImage())
                    .into(holder.profileImage);
        }
        holder.name.setText(userDetailsModel.getUsername());
        holder.id.setText("ID : "+userDetailsModel.getUid());

        holder.itemView.setOnClickListener(v -> {
            select.onSelectUser(userDetailsModel);
        });

    }

    @Override
    public int getItemCount() {
        return userDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView profileImage;
        private TextView name,id;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.iv_profile_image);
            name = itemView.findViewById(R.id.txt_userName);
            id = itemView.findViewById(R.id.txt_userUid);
        }
    }
}
