package com.stream.donalive.streaming.activity.model;

public class PurchageGiftModel {
    private  String fileName;
    private  String giftCoin;
    private  String userId;
    private  String giftId;
    private  String liveType;
    private  String gift_count;
    private  String liveId;

    public PurchageGiftModel() {
    }

    public PurchageGiftModel(String fileName, String giftCoin, String userId, String giftId, String liveType, String gift_count, String liveId) {
        this.fileName = fileName;
        this.giftCoin = giftCoin;
        this.userId = userId;
        this.giftId = giftId;
        this.liveType = liveType;
        this.gift_count = gift_count;
        this.liveId = liveId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getGiftCoin() {
        return giftCoin;
    }

    public void setGiftCoin(String giftCoin) {
        this.giftCoin = giftCoin;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getLiveType() {
        return liveType;
    }

    public void setLiveType(String liveType) {
        this.liveType = liveType;
    }

    public String getGift_count() {
        return gift_count;
    }

    public void setGift_count(String gift_count) {
        this.gift_count = gift_count;
    }

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }
}
