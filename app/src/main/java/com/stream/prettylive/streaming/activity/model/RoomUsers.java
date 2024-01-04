package com.stream.prettylive.streaming.activity.model;

public class RoomUsers {
        private String liveId;
        private String userId;
        private String uid;
        private String username;
        private String country_name;
        private String image;
        private String level;
        private String friends;
        private String followers;
        private String following;
        private String coins;
        private long time;

    public RoomUsers() {
    }

    public RoomUsers(String liveId, String userId, String uid, String username, String country_name, String image, String level, String friends, String followers, String following, String coins, long time) {
        this.liveId = liveId;
        this.userId = userId;
        this.uid = uid;
        this.username = username;
        this.country_name = country_name;
        this.image = image;
        this.level = level;
        this.friends = friends;
        this.followers = followers;
        this.following = following;
        this.coins = coins;
        this.time = time;
    }

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
