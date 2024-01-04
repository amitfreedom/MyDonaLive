package com.stream.prettylive.ui.home.ui.reels;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.stream.prettylive.R;

import java.util.ArrayList;
import java.util.List;

public class VideoPagerAdapter extends RecyclerView.Adapter<VideoPagerAdapter.VideoViewHolder> {

private final Context context;
private final List<String> videoUrls;
//private final List<SimpleExoPlayer> players;

public VideoPagerAdapter(Context context) {
        this.context = context;
        this.videoUrls = getSampleVideoUrls(); // Replace with your video URLs
//        this.players = new ArrayList<>();
        }

@NonNull
@Override
public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video_player, parent, false);
        return new VideoViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.bind(position);
        }

@Override
public int getItemCount() {
        return videoUrls.size();
        }

private List<String> getSampleVideoUrls() {
        // Replace with your video URLs
        List<String> urls = new ArrayList<>();
        urls.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
        urls.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
        return urls;
        }

class VideoViewHolder extends RecyclerView.ViewHolder {



    public VideoViewHolder(@NonNull View itemView) {
        super(itemView);

    }

    public void bind(int position) {
    }
}
}