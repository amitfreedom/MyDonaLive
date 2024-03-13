package com.stream.prettylive.ui.lucky_game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stream.prettylive.databinding.MainGameRowBinding;

import java.util.ArrayList;

public class GamesLuckyAdapter extends RecyclerView.Adapter<GamesLuckyAdapter.GamesHolder> {
    private ArrayList<GamesLuckyModel> gameList;
    Select select;

    interface  Select{
        void selectGame(int gameId);
    }

    public GamesLuckyAdapter(ArrayList<GamesLuckyModel> gameList,Select select) {
        this.gameList = gameList;
        this.select = select;
    }

    @NonNull
    @Override
    public GamesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MainGameRowBinding binding = MainGameRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new GamesHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GamesHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.gamesItemTitle.setText(gameList.get(position).getGameName());
        holder.binding.gamesItemImage.setImageResource(gameList.get(position).getGameImage());
        if (gameList.get(position).getGameID()!=0){
            holder.binding.comingSoon.setVisibility(View.VISIBLE);
        }else {
            holder.binding.comingSoon.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.itemView.getContext();
                int gameListID = gameList.get(position).getGameID();
                select.selectGame(gameListID);
//
            }
        });
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public static class GamesHolder extends RecyclerView.ViewHolder {
        MainGameRowBinding binding;

        public GamesHolder(MainGameRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}