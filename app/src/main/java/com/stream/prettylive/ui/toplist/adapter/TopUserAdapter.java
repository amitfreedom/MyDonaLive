package com.stream.prettylive.ui.toplist.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.stream.prettylive.databinding.ListTopUserBinding;
import com.stream.prettylive.ui.home.ui.home.adapter.FirestoreAdapter;
import com.stream.prettylive.ui.home.ui.profile.models.UserDetailsModel;
import com.stream.prettylive.ui.utill.Constant;

import java.util.Objects;

public class TopUserAdapter extends FirestoreAdapter<TopUserAdapter.ViewHolder> {
    public interface OnUserSelectedListener {
        void onUserSelected(DocumentSnapshot user);
    }

    private TopUserAdapter.OnUserSelectedListener mListener;


    public TopUserAdapter(Query query, TopUserAdapter.OnUserSelectedListener listener) {
        super(query);
        mListener = listener;
    }




    @NonNull
    @Override
    public TopUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TopUserAdapter.ViewHolder(ListTopUserBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(TopUserAdapter.ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener,position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ListTopUserBinding binding;

        public ViewHolder(ListTopUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @SuppressLint("SetTextI18n")
        public void bind(final DocumentSnapshot snapshot,
                         final OnUserSelectedListener listener, int position) {

           try{
               UserDetailsModel userDetailsModel = snapshot.toObject(UserDetailsModel.class);


               if (Objects.equals(userDetailsModel.getImage(), "")){
                   Glide.with(binding.ivProfileImage.getContext())
                           .load(Constant.USER_PLACEHOLDER_PATH)
                           .into(binding.ivProfileImage);
               }else {
                   Glide.with(binding.ivProfileImage.getContext())
                           .load(userDetailsModel.getImage())
                           .into(binding.ivProfileImage);
               }
               binding.txtSn.setText(String.valueOf(position+1));
               binding.txtUserName.setText(userDetailsModel.getUsername());
               binding.txtUserUid.setText("Lv "+userDetailsModel.getLevel());
               binding.txtTotalCoin.setText(userDetailsModel.getDiamond());
//               binding.txtUserUid.setText("ID : "+userDetailsModel.getUid()+"receive coin :"+userDetailsModel.getDiamond());


               itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       if (listener != null) {
                           listener.onUserSelected(snapshot);
                       }
                   }
               });
           }catch (Exception e){

           }
        }

    }
}
