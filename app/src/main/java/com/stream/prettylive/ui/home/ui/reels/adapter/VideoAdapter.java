package com.stream.prettylive.ui.home.ui.reels.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stream.prettylive.R;
import com.stream.prettylive.ui.home.ui.reels.models.VideoModel;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {
    Context context;
    List<VideoModel>videoList;

    public VideoAdapter(Context context, List<VideoModel> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_video_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.setData(videoList.get(position));

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private VideoView videoView;
        private TextView header,desc;
        private ProgressBar videoProgressBar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView=itemView.findViewById(R.id.videoView);
            header=itemView.findViewById(R.id.header);
            desc=itemView.findViewById(R.id.desc);
            videoProgressBar=itemView.findViewById(R.id.videoProgressBar);

        }
        void setData(VideoModel videoModel){
            videoView.setVideoPath(videoModel.getUrl());
            header.setText(videoModel.getTitle());
            desc.setText(videoModel.getDesc());

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    videoProgressBar.setVisibility(View.GONE);
                    mediaPlayer.start();
                }
            });

            videoView.setOnClickListener(view -> {
                if (videoView.isPlaying()){
                    videoView.pause();
                }else {
                    videoView.start();
                }
            });

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
        }
    }
}
