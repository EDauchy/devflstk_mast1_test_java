package com.example;

import java.util.ArrayList;
import java.util.List;

public class Frame {
    private int score;
    private final boolean lastFrame;
    private final IGenerateur generateur;
    private final List<Roll> rolls;

    public Frame(IGenerateur generateur, boolean lastFrame) {
        this.lastFrame = lastFrame;
        this.generateur = generateur;
        this.rolls = new ArrayList<>();
    }

    public int getScore() {
        return score;
    }

    public boolean makeRoll() {
        if (!canMakeRoll()) {
            return false;
        }

        int pins = generateur.randomPin(getMaxPinsForNextRoll());
        rolls.add(new Roll(pins));
        score += pins;
        return true;
    }

    private boolean canMakeRoll() {
        if (rolls.isEmpty()) {
            return true;
        }

        if (!lastFrame) {
            if (isStrike(0)) {
                return false;
            }
            return rolls.size() < 2;
        }

        if (rolls.size() >= 3) {
            return false;
        }

        if (rolls.size() == 2) {
            return isStrike(0) || isSpare();
        }

        return true;
    }

    private int getMaxPinsForNextRoll() {
        if (!lastFrame) {
            if (rolls.isEmpty()) {
                return 10;
            }
            return 10 - rolls.get(0).getPins();
        }

        if (rolls.isEmpty() || (rolls.size() == 1 && isStrike(0))) {
            return 10;
        }

        if (rolls.size() == 1) {
            return 10 - rolls.get(0).getPins();
        }

        return 10;
    }

    private boolean isStrike(int rollIndex) {
        return rolls.get(rollIndex).getPins() == 10;
    }

    private boolean isSpare() {
        return rolls.size() >= 2
                && rolls.get(0).getPins() + rolls.get(1).getPins() == 10;
    }
}
