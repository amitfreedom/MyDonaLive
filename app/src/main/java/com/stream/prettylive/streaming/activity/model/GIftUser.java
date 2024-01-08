package com.stream.prettylive.streaming.activity.model;

public class GIftUser {
    String image;
    String mainStreamID;
    String uid;
    String userId;
    String userName;

    public GIftUser() {
    }

    public GIftUser(String image, String mainStreamID, String uid, String userId, String userName) {
        this.image = image;
        this.mainStreamID = mainStreamID;
        this.uid = uid;
        this.userId = userId;
        this.userName = userName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMainStreamID() {
        return mainStreamID;
    }

    public void setMainStreamID(String mainStreamID) {
        this.mainStreamID = mainStreamID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
