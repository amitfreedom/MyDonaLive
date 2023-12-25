package com.stream.donalive.ui.vip.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.annotations.NotNull;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.stream.donalive.R;

import java.util.List;

public class SVGAListAdapter extends RecyclerView.Adapter<SVGAListAdapter.SVGAViewHolder> {

    private List<String> vipFileList; // Replace with your SVGA file paths or URLs
    private Context context;

    public SVGAListAdapter(Context context, List<String> vipFileList) {
        this.context = context;
        this.vipFileList = vipFileList;
    }

    @NonNull
    @Override
    public SVGAViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.svga_list_item, parent, false);
        return new SVGAViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SVGAViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return vipFileList.size();
    }

    public static class SVGAViewHolder extends RecyclerView.ViewHolder {
        ImageView svgaImageView;

        public SVGAViewHolder(@NonNull View itemView) {
            super(itemView);
            svgaImageView = itemView.findViewById(R.id.vipImage);
        }
    }
}
