package com.stream.donalive.ui.home.ui.reels;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;
import com.stream.donalive.R;

public class VideoPlayerFragment extends Fragment {

    private SimpleExoPlayer player;
    private PlayerView playerView;
    private String videoUrl;

    public VideoPlayerFragment(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_player, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playerView = view.findViewById(R.id.playerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    private void initializePlayer() {
        player = new SimpleExoPlayer.Builder(requireContext()).build();
        playerView.setPlayer(player);
        player.setPlayWhenReady(true);
        player.setMediaItem(MediaItem.fromUri(videoUrl));
        player.prepare();
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
