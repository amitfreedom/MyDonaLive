package com.stream.donalive.ui.home.ui.home.models;

public class LiveUser {
    private String live;
    private String userId;
    private String roomId;
    private String userName;


    public LiveUser() {
    }

    public LiveUser(String live, String userId, String roomId, String userName) {
        this.live = live;
        this.userId = userId;
        this.roomId = roomId;
        this.userName = userName;
    }

    public String getLive() {
        return live;
    }

    public void setLive(String live) {
        this.live = live;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
