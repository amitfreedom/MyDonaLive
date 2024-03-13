package com.stream.prettylive.ui.lucky_game;

import java.io.Serializable;

public class GamesLuckyModel implements Serializable {
    private int gameID;
    private String gameName;
    private int gameImage;

    public GamesLuckyModel(int gameID, String gameName, int gameImage) {
        this.gameID = gameID;
        this.gameName = gameName;
        this.gameImage = gameImage;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getGameImage() {
        return gameImage;
    }

    public void setGameImage(int gameImage) {
        this.gameImage = gameImage;
    }
}

