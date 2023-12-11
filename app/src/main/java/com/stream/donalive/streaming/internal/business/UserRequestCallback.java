package com.stream.donalive.streaming.internal.business;

public interface UserRequestCallback {

    void onUserRequestSend(int errorCode, String requestID);
}
