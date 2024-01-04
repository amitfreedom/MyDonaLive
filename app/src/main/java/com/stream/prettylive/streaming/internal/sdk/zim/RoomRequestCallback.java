package com.stream.prettylive.streaming.internal.sdk.zim;

public interface RoomRequestCallback {

    void onRoomRequestSend(int errorCode, String requestID);
}
