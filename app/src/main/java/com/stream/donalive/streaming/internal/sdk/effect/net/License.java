package com.stream.donalive.streaming.internal.sdk.effect.net;

import com.google.gson.annotations.SerializedName;

public class License {


    @SerializedName("License")
    private String license;

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
