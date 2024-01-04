package com.stream.prettylive.streaming.internal.business.call;

public interface ReceiveCallListener {

    void onReceiveNewCall(String requestID, String inviter, String userName, int type);
}
