package com.stream.donalive.ui.home.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.stream.donalive.databinding.ListLiveGiftHistoryBinding;

public class LiveHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

private Context mContext;

public static class CountryHolder extends RecyclerView.ViewHolder {
    public ListLiveGiftHistoryBinding viewBinding;

    public CountryHolder(ListLiveGiftHistoryBinding viewBinding) {
        super(viewBinding.getRoot());
        this.viewBinding = viewBinding;
    }
}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListLiveGiftHistoryBinding binding = ListLiveGiftHistoryBinding.inflate(inflater, parent, false);
        CountryHolder holder = new CountryHolder(binding);
        mContext = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Uncomment the following lines once you have the actual data model
        // CountryHolder userHolder = (CountryHolder) holder;
        // Glide.with(mContext).load(userList.get(position).getUserImage()).into(userHolder.viewBinding.ivImages);
    }

    @Override
    public int getItemCount() {
        return 15;
    }
}
