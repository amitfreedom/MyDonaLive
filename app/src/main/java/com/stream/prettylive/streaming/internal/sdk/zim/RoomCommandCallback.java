package com.stream.prettylive.streaming.internal.sdk.zim;

public interface RoomCommandCallback {

    void onSendRoomCommand(int errorCode, String errorMessage);
}
