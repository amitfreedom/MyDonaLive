package com.stream.donalive.streaming.internal.business.call;

public interface ReceiveCallListener {

    void onReceiveNewCall(String requestID, String inviter, String userName, int type);
}
