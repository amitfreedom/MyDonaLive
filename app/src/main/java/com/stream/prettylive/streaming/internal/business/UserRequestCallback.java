package com.stream.prettylive.streaming.internal.business;

public interface UserRequestCallback {

    void onUserRequestSend(int errorCode, String requestID);
}
