package com.stream.prettylive.ui.search.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import com.stream.prettylive.databinding.ItemLoginUserBinding;
import com.stream.prettylive.ui.home.ui.home.adapter.FirestoreAdapter;
import com.stream.prettylive.ui.home.ui.profile.models.UserDetailsModel;
import com.stream.prettylive.ui.utill.Constant;

import java.util.Objects;

public class SearchUserAdapter extends FirestoreAdapter<SearchUserAdapter.ViewHolder>{
    public interface OnUserSelectedListener {
        void onUserSelected(DocumentSnapshot user);
    }

    private SearchUserAdapter.OnUserSelectedListener mListener;


    public SearchUserAdapter(Query query, SearchUserAdapter.OnUserSelectedListener listener) {
        super(query);
        mListener = listener;
    }




    @NonNull
    @Override
    public SearchUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchUserAdapter.ViewHolder(ItemLoginUserBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(SearchUserAdapter.ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemLoginUserBinding binding;

        public ViewHolder(ItemLoginUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final SearchUserAdapter.OnUserSelectedListener listener) {

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
               binding.txtUserName.setText(userDetailsModel.getUsername());
               binding.txtUserUid.setText("ID : "+userDetailsModel.getUid());


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onUserSelected(snapshot);
                    }
                }
            });
        }

    }
}
