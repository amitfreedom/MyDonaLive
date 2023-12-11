package com.stream.donalive.streaming.internal.sdk.components.express;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.stream.donalive.R;
import com.stream.donalive.streaming.internal.sdk.ZEGOSDKManager;

public class AudioOutputButton extends ZImageButton {

    public AudioOutputButton(@NonNull Context context) {
        super(context);
    }

    public AudioOutputButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AudioOutputButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initView() {
        super.initView();
        setImageResource(R.drawable.call_icon_speaker, R.drawable.call_icon_built_in);
    }

    @Override
    public void open() {
        super.open();
        ZEGOSDKManager.getInstance().expressService.setAudioRouteToSpeaker(true);
    }

    @Override
    public void close() {
        super.close();
        ZEGOSDKManager.getInstance().expressService.setAudioRouteToSpeaker(false);
    }
}
