package com.stream.prettylive.streaming.internal.sdk.effect.net;



public interface IGetLicenseCallback {
    void onGetLicense(int code,String message, License license);
}
