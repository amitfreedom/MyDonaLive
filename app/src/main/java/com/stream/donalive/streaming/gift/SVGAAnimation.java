package com.stream.donalive.streaming.gift;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAParser.ParseCompletion;
import com.opensource.svgaplayer.SVGASoundManager;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.stream.donalive.R;

public class SVGAAnimation implements GiftAnimation {

    private String animationFileName = "kingset.svga";
//    private String animationFileName = "car.svga";
//    private String animationFileName = "sports-car.svga";
//    private String animationFileName = "mp3_to_long.svga";
    private ViewGroup parentView;

    public SVGAAnimation(ViewGroup animationViewParent) {
        this.parentView = animationViewParent;
        SVGAParser.Companion.shareParser().init(animationViewParent.getContext());
//        SVGASoundManager.INSTANCE.init();
//        SVGASoundManager.INSTANCE.release();

    }

    @Override
    public void startPlay() {
//        SVGASoundManager.INSTANCE.setVolume(2f, svgaVideoEntity);
        MediaPlayer mediaPlayer = MediaPlayer.create(parentView.getContext(), R.raw.car_cound);

        SVGAParser.Companion.shareParser().decodeFromAssets(animationFileName, new ParseCompletion() {
            @Override
            public void onComplete(@NonNull SVGAVideoEntity svgaVideoEntity) {
                SVGAImageView svgaImageView = new SVGAImageView(parentView.getContext());
                svgaImageView.setLoops(1);
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
