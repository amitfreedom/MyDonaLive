package com.stream.prettylive.ui.home.ui.home.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.stream.prettylive.databinding.ListGameAllBinding;
import com.stream.prettylive.game.teenpatty.models.GameList;
import com.stream.prettylive.ui.home.ui.home.models.LiveUser;
import com.stream.prettylive.ui.utill.Constant;

import java.util.Objects;

public class GameAllAdapter extends FirestoreAdapter<GameAllAdapter.ViewHolder>{

    public interface OnSelectedGameSelectedListener {

        void onSelectedGameSelected(DocumentSnapshot user);

    }

    private GameAllAdapter.OnSelectedGameSelectedListener mListener;


    public GameAllAdapter(Query query, GameAllAdapter.OnSelectedGameSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public GameAllAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GameAllAdapter.ViewHolder(ListGameAllBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(GameAllAdapter.ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ListGameAllBinding binding;

        public ViewHolder(ListGameAllBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final GameAllAdapter.OnSelectedGameSelectedListener listener) {

            try{
                GameList gameList = snapshot.toObject(GameList.class);
                if (!Objects.equals(gameList.getBanner(), "") && gameList.isActive()){
                    Glide.with(binding.ivImage.getContext())
                            .load(gameList.getBanner())
                            .into(binding.ivImage);
                }
            }catch (Exception e){
                Log.i("error", "bind: "+e);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onSelectedGameSelected(snapshot);
                    }
                }
            });
        }

    }
}
