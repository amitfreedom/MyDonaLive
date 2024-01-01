package com.stream.donalive.streaming.activity.model;

public class GiftModel {

    String fileName;
    String giftId;
    String giftName;
    String gift_type;
    String image;
    String price;
    long timestamp;

    public GiftModel() {
    }

    public GiftModel(String fileName,String giftId, String giftName, String gift_type, String image, String price, long timestamp) {
        this.fileName = fileName;
        this.giftId = giftId;
        this.giftName = giftName;
        this.gift_type = gift_type;
        this.image = image;
        this.price = price;
        this.timestamp = timestamp;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getGift_type() {
        return gift_type;
    }

    public void setGift_type(String gift_type) {
        this.gift_type = gift_type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
