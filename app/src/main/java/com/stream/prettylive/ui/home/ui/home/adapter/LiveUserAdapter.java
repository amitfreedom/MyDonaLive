//package com.stream.prettylive.ui.home.ui.home.adapter;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.DiffUtil;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.stream.prettylive.R;
//import com.stream.prettylive.ui.home.ui.home.models.LiveUser;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class LiveUserAdapter extends RecyclerView.Adapter<LiveUserAdapter.LiveUserViewHolder> {
//
//    private List<LiveUser> items = new ArrayList<>();
//
//    public void setItems(List<LiveUser> newItems) {
//        // Use DiffUtil to calculate the difference between old and new lists
//        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ItemDiffCallback(items, newItems));
//        items.clear();
//        items.addAll(newItems);
//        diffResult.dispatchUpdatesTo(this);
//    }
//
//    @NonNull
//    @Override
//    public LiveUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_user_layout, parent, false);
//        return new LiveUserViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull LiveUserViewHolder holder, int position) {
//        LiveUser currentItem = items.get(position);
//        holder.bind(currentItem);
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//    static class LiveUserViewHolder extends RecyclerView.ViewHolder {
//        private TextView itemName;
//
//        LiveUserViewHolder(@NonNull View itemView) {
//            super(itemView);
//            itemName = itemView.findViewById(R.id.item_name);
//        }
//
//        void bind(LiveUser item) {
//            itemName.setText(item.getUsername());
//        }
//    }
//
//    // Inner class for DiffUtil.Callback
//    private static class ItemDiffCallback extends DiffUtil.Callback {
//        private final List<LiveUser> oldList;
//        private final List<LiveUser> newList;
//
//        ItemDiffCallback(List<LiveUser> oldList, List<LiveUser> newList) {
//            this.oldList = oldList;
//            this.newList = newList;
//        }
//
//        @Override
//        public int getOldListSize() {
//            return oldList.size();
//        }
//
//        @Override
//        public int getNewListSize() {
//            return newList.size();
//        }
//
//        @Override
//        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
//            return oldList.get(oldItemPosition).getUserId() == newList.get(newItemPosition).getUserId();
//        }
//
//        @Override
//        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
//            LiveUser oldItem = oldList.get(oldItemPosition);
//            LiveUser newItem = newList.get(newItemPosition);
//            return oldItem.getUserName().equals(newItem.getUserName());
//        }
//    }
//}
