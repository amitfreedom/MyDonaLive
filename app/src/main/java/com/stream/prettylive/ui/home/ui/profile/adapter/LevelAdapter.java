package com.stream.prettylive.ui.home.ui.profile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stream.prettylive.R;
import com.stream.prettylive.ui.home.ui.profile.models.Level;

import java.util.List;

// LevelAdapter.java
public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.ViewHolder> {
    private List<Level> levels;

    public LevelAdapter(List<Level> levels) {
        this.levels = levels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_level, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Level level = levels.get(position);
            holder.levelTextView.setText("Level"+level.getCoin());
            holder.levelCoin.setText(String.valueOf(level.getLevelNumber()));


    }

    @Override
    public int getItemCount() {
        return levels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView levelTextView,levelCoin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            levelTextView = itemView.findViewById(R.id.levelTextView);
            levelCoin = itemView.findViewById(R.id.levelCoin);
        }
    }
}

