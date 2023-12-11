package com.stream.donalive.streaming.internal.sdk.effect.bean;

import java.util.Arrays;
import java.util.List;

public enum BeautyGroup {
    BASIC, /////
    ADVANCED, /////
    FILTERS, ////
    MAKEUPS,////
    LIPSTICK, ////
    BLUSHER, ////
    EYELASHES, ////
    EYELINER, ////
    EYESHADOW, ////
    COLORED_CONTACTS, ////
    STYLE_MAKEUP, ////
    STICKERS, ////
    BACKGROUND;

    public static final List<BeautyGroup> makeups = Arrays.asList(LIPSTICK, BLUSHER, EYELASHES, EYELINER, EYESHADOW,
        COLORED_CONTACTS);

    public boolean isMakeup() {
        return makeups.contains(this);
    }
}
