package com.stream.prettylive.streaming.internal.sdk.components.express;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.stream.prettylive.R;

public class GiftButton extends ZImageButton{
    public GiftButton(@NonNull Context context) {
        super(context);
    }

    public GiftButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GiftButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void initView() {
        super.initView();
        setImageResource(R.drawable.presents_icon,R.drawable.presents_icon);
    }

    @Override
    public void open() {
        super.open();

        Toast.makeText(getContext(), "open", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void close() {
        super.close();
        Toast.makeText(getContext(), "close", Toast.LENGTH_SHORT).show();

    }
}
