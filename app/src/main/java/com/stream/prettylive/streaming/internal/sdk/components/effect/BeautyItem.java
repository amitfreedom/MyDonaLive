package com.stream.prettylive.streaming.internal.sdk.components.effect;

import android.graphics.drawable.Drawable;

import com.stream.prettylive.streaming.internal.sdk.ZEGOSDKManager;
import com.stream.prettylive.streaming.internal.sdk.effect.bean.BeautyAbility;
import com.stream.prettylive.streaming.internal.sdk.effect.bean.BeautyType;

public class BeautyItem {

    public String name;
    public Drawable drawable;
    public BeautyAbility beautyAbility;

    public BeautyItem(String name, Drawable drawable, BeautyType beautyType) {
        this.name = name;
        this.drawable = drawable;
        this.beautyAbility = ZEGOSDKManager.getInstance().effectsService.getBeautyAbility(beautyType);
    }
}
