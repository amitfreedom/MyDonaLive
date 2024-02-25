package com.stream.prettylive.game.teenpatty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.stream.prettylive.R;
import com.stream.prettylive.game.teenpatty.models.GameList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.ViewHolder> {
    private List<GameList> listData = new ArrayList<GameList>();
    private Context context;

    Select select;
    public interface Select{
        void onPress(GameList gameList);
    }

    public GameListAdapter(List<GameList> listData, FragmentActivity fragmentActivity,Select select) {
        this.listData = listData;
        this.context = fragmentActivity;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.game_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull GameListAdapter.ViewHolder holder, int position) {
        GameList myListData = listData.get(position);
        try{
            holder.title.setText(myListData.getTitle());
            if (!Objects.equals(myListData.getImage(), "")){
                Glide.with(context).load(myListData.getImage()).into(holder.image);
            }

        }catch (Exception ignored){

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select.onPress(myListData);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            image = (ImageView) itemView.findViewById(R.id.image);
        }

    }
}

