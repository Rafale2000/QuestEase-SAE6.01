package com.example.questease.model.Jeu;

public class PrixJusteJeu {
    private final int targetNumber;
    private int remainingAttempts;

    public PrixJusteJeu(int initialAttempts, int targetNumber) {
        this.targetNumber = targetNumber;
        this.remainingAttempts = initialAttempts;
    }

    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    public void decreaseRemainingAttemps() {
        remainingAttempts--;
    }

    public Result checkGuess(int guess) {
        if (guess == targetNumber) {
            remainingAttempts = 0; // Game over, player wins
            return Result.CORRECT;
        } else if (guess < targetNumber) {
            remainingAttempts--;
            return Result.TROP_BAS;
        } else {
            remainingAttempts--;
            return Result.TROP_HAUT;
        }
    }

    public enum Result {
        CORRECT,
        TROP_BAS,
        TROP_HAUT
    }
}