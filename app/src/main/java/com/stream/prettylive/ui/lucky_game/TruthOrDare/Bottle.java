package com.stream.prettylive.ui.lucky_game.TruthOrDare;

import java.util.Random;

public class Bottle {
    public int turn() {
        Random random = new Random();
        return random.nextInt(5400 - 1800 + 1) + 1800;
    }
}

