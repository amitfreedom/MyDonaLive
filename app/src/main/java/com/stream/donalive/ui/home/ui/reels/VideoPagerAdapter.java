package com.stream.donalive.ui.home.ui.reels;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.stream.donalive.R;

import java.util.ArrayList;
import java.util.List;

public class VideoPagerAdapter extends RecyclerView.Adapter<VideoPagerAdapter.VideoViewHolder> {

private final Context context;
private final List<String> videoUrls;
private final List<SimpleExoPlayer> players;

public VideoPagerAdapter(Context context) {
        this.context = context;
        this.videoUrls = getSampleVideoUrls(); // Replace with your video URLs
        this.players = new ArrayList<>();
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

    private final PlayerView playerView;
    private final SimpleExoPlayer player;

    public VideoViewHolder(@NonNull View itemView) {
        super(itemView);
        playerView = itemView.findViewById(R.id.playerView);
        player = new SimpleExoPlayer.Builder(context).build();
        playerView.setPlayer(player);
    }

    public void bind(int position) {
        MediaItem mediaItem = MediaItem.fromUri(videoUrls.get(position));
        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true);
    }
}
}