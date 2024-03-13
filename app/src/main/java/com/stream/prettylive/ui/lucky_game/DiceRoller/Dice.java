package com.stream.prettylive.ui.lucky_game.DiceRoller;

import java.util.Random;

public class Dice {
    private int numSides;

    public Dice(int numSides) {
        this.numSides = numSides;
    }

    public int roll() {
        Random random = new Random();
        return random.nextInt(numSides) + 1;
    }
}

