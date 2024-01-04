package com.stream.prettylive.streaming.components.audioroom;

import java.util.HashMap;
import java.util.Map;

import im.zego.zegoexpress.entity.ZegoRoomExtraInfo;

public class RTCRoomExtraInfo {

    private Map<String, String> valueMap = new HashMap<>();

    public static final String HOST = "host";
    public static final String LOCK_SEAT = "lockseat";

    public RTCRoomExtraInfo(ZegoRoomExtraInfo roomExtraInfo) {

    }

    private void parse() {

    }

    public void addOrUpdateValuePair(String key, String value) {
        valueMap.put(key, value);
    }
}
