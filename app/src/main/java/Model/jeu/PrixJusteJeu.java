package Model.jeu;

import java.util.Random;

public class PrixJusteJeu {
    private final int targetNumber;
    private int remainingAttempts;

    public PrixJusteJeu(int initialAttempts) {
        Random random = new Random();
        this.targetNumber = random.nextInt(100) + 1; // Generate number between 1 and 100
        this.remainingAttempts = initialAttempts;
    }

    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    public Result checkGuess(int guess) {
        if (guess == targetNumber) {
            remainingAttempts = 0; // Game over, player wins
            return Result.CORRECT;
        } else if (guess < targetNumber) {
            remainingAttempts--;
            return Result.TOO_LOW;
        } else {
            remainingAttempts--;
            return Result.TOO_HIGH;
        }
    }

    public enum Result {
        CORRECT,
        TOO_LOW,
        TOO_HIGH
    }
}