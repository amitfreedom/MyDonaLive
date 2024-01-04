package com.stream.prettylive.ui.follow;

import java.util.ArrayList;

public class TestUser {
    private String userId;
    private String username;
    private ArrayList<String> following;
    private ArrayList<String> followers;

    public TestUser() {
    }

    public TestUser(String userId, String username, ArrayList<String> following, ArrayList<String> followers) {
        this.userId = userId;
        this.username = username;
        this.following = following;
        this.followers = followers;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getFollowing() {
        return following;
    }

    public void setFollowing(ArrayList<String> following) {
        this.following = following;
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
    }
}
