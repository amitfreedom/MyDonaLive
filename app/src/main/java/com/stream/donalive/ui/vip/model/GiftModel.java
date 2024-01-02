package com.stream.donalive.ui.vip.model;

public class GiftModel {
    String vipId;
    String fileName;
    String title;
    String image;
    String beans;

    public GiftModel() {
    }

    public GiftModel(String vipId, String fileName, String title, String image, String beans) {
        this.vipId = vipId;
        this.fileName = fileName;
        this.title = title;
        this.image = image;
        this.beans = beans;
    }

    public String getVipId() {
        return vipId;
    }

    public void setVipId(String vipId) {
        this.vipId = vipId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public String getBeans() {
        return beans;
    }

    public void setBeans(String beans) {
        this.beans = beans;
    }
}
