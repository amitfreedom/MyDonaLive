package com.stream.donalive.streaming.activity.model;

public class GiftModel {
    String image;
    String name;
    String beans;

    public GiftModel() {
    }

    public GiftModel(String image, String name, String beans) {
        this.image = image;
        this.name = name;
        this.beans = beans;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeans() {
        return beans;
    }

    public void setBeans(String beans) {
        this.beans = beans;
    }
}
