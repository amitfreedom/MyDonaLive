package com.stream.prettylive.streaming.activity.adapter;

import static com.google.firebase.database.DatabaseKt.getSnapshots;

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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GiftViewUserAdapter extends FirestoreAdapter<GiftViewUserAdapter.ViewHolder> {

    private boolean selectAll = false;
    private List<String> selectedIds = new ArrayList<>();
    @SuppressLint("NotifyDataSetChanged")
    public void selectAll() {
        selectAll = true;
        mListener.SelectedUser("1");
        notifyDataSetChanged();

    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearSelection() {
        selectAll = false;
        selectedIds.clear(); // Clear the set of selected IDs
        notifyDataSetChanged();
    }




    private int selectedIndex =-1;

    public interface OnActiveUserSelectedListener {

        void onActiveUserSelected(DocumentSnapshot user);
        void SelectedUser(String user);

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
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                String itemId = "1234";

                if (selectAll) {


//                    mListener.SelectedUser(selectedIds);

                    notifyDataSetChanged();
                } else {
                    selectedIndex = position;
                    if (mListener != null) {
                        mListener.onActiveUserSelected(getSnapshot(position));
                    }
                    notifyDataSetChanged();
                }

            }
        });


        // Highlight the item if "Select All" is active
        if (selectAll) {
            holder.binding.selectedView.setVisibility(View.VISIBLE);
        } else {
            // Regular selection logic
            if (selectedIndex == position) {
                holder.binding.selectedView.setVisibility(View.VISIBLE);
            } else {
                holder.binding.selectedView.setVisibility(View.GONE);
            }
        }



//        if (selectedIndex==position){
//            holder.binding.selectedView.setVisibility(View.VISIBLE);
//        }else {
//            holder.binding.selectedView.setVisibility(View.GONE);
//        }

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

    public List<String> getSelectedIds() {
        return selectedIds;
    }


}
