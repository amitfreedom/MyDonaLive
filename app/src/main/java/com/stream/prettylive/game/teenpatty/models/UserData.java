package com.stream.prettylive.game.teenpatty.models;


public class UserData {
    private long MyPotA;
    private long MyPotB;
    private long MyPotC;
    private boolean LOG;
    private long YourWager;
    private long Coins;
    private long receiveCoin;
    private String userName;
    private String image;
    private long uid;
    private String userId;

    // Default constructor required for Firestore
    public UserData() {
    }

    public UserData(long myPotA, long myPotB, long myPotC, boolean LOG, long yourWager, long coins, long receiveCoin, String userName, String image, long uid, String userId) {

        MyPotA = myPotA;
        MyPotB = myPotB;
        MyPotC = myPotC;
        this.LOG = LOG;
        YourWager = yourWager;
        Coins = coins;
        this.receiveCoin = receiveCoin;
        this.userName = userName;
        this.image = image;
        this.uid = uid;
        this.userId = userId;
    }


    public long getMyPotA() {
        return MyPotA;
    }

    public void setMyPotA(long myPotA) {
        MyPotA = myPotA;
    }

    public long getMyPotB() {
        return MyPotB;
    }

    public void setMyPotB(long myPotB) {
        MyPotB = myPotB;
    }

    public long getMyPotC() {
        return MyPotC;
    }

    public void setMyPotC(long myPotC) {
        MyPotC = myPotC;
    }

    public boolean isLOG() {
        return LOG;
    }

    public void setLOG(boolean LOG) {
        this.LOG = LOG;
    }

    public long getYourWager() {
        return YourWager;
    }

    public void setYourWager(long yourWager) {
        YourWager = yourWager;
    }

    public long getCoins() {
        return Coins;
    }

    public void setCoins(long coins) {
        Coins = coins;
    }

    public long getReceiveCoin() {
        return receiveCoin;
    }

    public void setReceiveCoin(long receiveCoin) {
        this.receiveCoin = receiveCoin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

