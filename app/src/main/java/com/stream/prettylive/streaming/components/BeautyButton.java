package com.stream.prettylive.streaming.components;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.utils.widget.ImageFilterView;

import com.stream.prettylive.R;

public class BeautyButton extends ImageFilterView {


    public BeautyButton(@NonNull Context context) {
        super(context);
        initView();
    }

    public BeautyButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BeautyButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setScaleType(ScaleType.CENTER);
        setImageResource(R.drawable.beauty_btn);
        setBackgroundColor(getResources().getColor(R.color.bottom_btn_background));
        setRoundPercent(1.0f);
    }
}
