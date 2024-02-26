package com.stream.prettylive.game.teenpatty.models;

public class GameList {
    String title="";
    String image="";
    String banner="";
    boolean active;

    public GameList() {
    }

    public GameList(String title, String image,String banner, boolean active) {
        this.title = title;
        this.image = image;
        this.banner = banner;
        this.active = active;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
