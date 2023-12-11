package com.stream.donalive.streaming.internal.sdk.effect.net;



public interface IGetLicenseCallback {
    void onGetLicense(int code,String message, License license);
}
