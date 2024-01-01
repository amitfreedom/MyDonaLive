package com.stream.donalive.streaming.gift;

import android.media.MediaPlayer;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAParser.ParseCompletion;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.stream.donalive.R;

public class SVGAAnimation implements GiftAnimation {

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference ref = firebaseDatabase.getReference().child("userInfo");
    private String otherUserId;
    private String liveType;

    private String animationFileName = "kingset.svga";
//    private String animationFileName = "star.svga";
//    private String animationFileName = "sports-star.svga";
//    private String animationFileName = "mp3_to_long.svga";
    private ViewGroup parentView;

    public SVGAAnimation(ViewGroup animationViewParent,String otherUserId,String liveType) {
        this.parentView = animationViewParent;
        this.otherUserId=otherUserId;
        this.liveType = liveType;
        SVGAParser.Companion.shareParser().init(animationViewParent.getContext());

    }

    @Override
    public void startPlay(String fileName,String gift_count) {
        MediaPlayer mediaPlayer = MediaPlayer.create(parentView.getContext(), R.raw.car_cound);

        SVGAParser.Companion.shareParser().decodeFromAssets(fileName, new ParseCompletion() {
            @Override
            public void onComplete(@NonNull SVGAVideoEntity svgaVideoEntity) {
                SVGAImageView svgaImageView = new SVGAImageView(parentView.getContext());
                svgaImageView.setLoops(Integer.parseInt(gift_count));
                parentView.addView(svgaImageView);
                svgaImageView.setVideoItem(svgaVideoEntity);
                svgaImageView.stepToFrame(0, true);

                svgaImageView.setCallback(new SVGACallback() {
                    @Override
                    public void onPause() {

                    }

                    @Override
                    public void onFinished() {
                        mediaPlayer.stop();
                        parentView.removeView(svgaImageView);
                        ref.child(otherUserId).child(liveType).child(otherUserId).child("gifts").removeValue();
                    }

                    @Override
                    public void onRepeat() {

                    }

                    @Override
                    public void onStep(int frame, double v) {
                        if (frame == 2) {
                            // Start playing the sound
//                            mediaPlayer.start();
                        }
                    }
                });
            }

            @Override
            public void onError() {

            }
        }, null);
    }
}
