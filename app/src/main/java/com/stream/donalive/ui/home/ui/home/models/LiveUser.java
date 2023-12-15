package com.stream.donalive.ui.home.ui.home.models;

public class LiveUser {
    private String userId;
    private long uid;
    private String username;
    private String photo;
    private String tag;
    private boolean host;
    private String liveID;
    private String liveType;
    private String liveStatus;
    private long startTime;
    private long endTime;
    private String country_name;

    public LiveUser() {
    }

    public LiveUser(String userId, long uid, String username, String photo, String tag, boolean host, String liveID, String liveType, String liveStatus, long startTime, long endTime, String country_name) {
        this.userId = userId;
        this.uid = uid;
        this.username = username;
        this.photo = photo;
        this.tag = tag;
        this.host = host;
        this.liveID = liveID;
        this.liveType = liveType;
        this.liveStatus = liveStatus;
        this.startTime = startTime;
        this.endTime = endTime;
        this.country_name = country_name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isHost() {
        return host;
    }

    public void setHost(boolean host) {
        this.host = host;
    }

    public String getLiveID() {
        return liveID;
    }

    public void setLiveID(String liveID) {
        this.liveID = liveID;
    }

    public String getLiveType() {
        return liveType;
    }

    public void setLiveType(String liveType) {
        this.liveType = liveType;
    }

    public String getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(String liveStatus) {
        this.liveStatus = liveStatus;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }
}
