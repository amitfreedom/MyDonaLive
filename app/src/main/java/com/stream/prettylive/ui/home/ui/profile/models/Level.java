package com.stream.prettylive.ui.home.ui.profile.models;

public class Level {
    private int levelNumber;
    private String coin;

    public Level(int levelNumber, String coin) {
        this.levelNumber = levelNumber;
        this.coin = coin;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }
}
