package com.stream.donalive.ui.home.ui.profile.models;

public class UserDetailsModel {
    private String userId="";
    private long uid=0;
    private String username="";
    private String email="";
    private String phone="";
    private String countryCode="";
    private String country_name="";
    private String loginType="";
    private String image="";
    private String regId="";
    private String deviceId="";
    private String beans="";
    private String coins="";
    private String level="";
    private String diamond="";
    private String latitude;
    private String longitude;
    private String friends="";
    private String followers="";
    private String following="";
    private long loginTime=0;


    public UserDetailsModel() {
    }

    public UserDetailsModel(String userId, long uid, String username, String email, String phone, String countryCode, String country_name, String loginType, String image, String regId, String deviceId, String beans, String coins, String level, String diamond, String latitude, String longitude, String friends, String followers, String following, long loginTime) {
        this.userId = userId;
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.countryCode = countryCode;
        this.country_name = country_name;
        this.loginType = loginType;
        this.image = image;
        this.regId = regId;
        this.deviceId = deviceId;
        this.beans = beans;
        this.coins = coins;
        this.level = level;
        this.diamond = diamond;
        this.latitude = latitude;
        this.longitude = longitude;
        this.friends = friends;
        this.followers = followers;
        this.following = following;
        this.loginTime = loginTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getBeans() {
        return beans;
    }

    public void setBeans(String beans) {
        this.beans = beans;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDiamond() {
        return diamond;
    }

    public void setDiamond(String diamond) {
        this.diamond = diamond;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }
}
