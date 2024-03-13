package com.stream.prettylive.ui.lucky_game.HeadsOrTails;

import java.util.Random;

public class Coin {
    public int flip() {
        Random random = new Random();
        return random.nextInt(2) + 1;
    }
}

