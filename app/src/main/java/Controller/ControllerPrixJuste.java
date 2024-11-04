package Controller;

import Model.jeu.PrixJusteJeu;

/**
 * controller qui fait le lien entre l'application et le jeu
 */
public class ControllerPrixJuste {
    private PrixJusteJeu prixJusteJeu;

    public ControllerPrixJuste(PrixJusteJeu prixJusteJeu) {
        this.prixJusteJeu = prixJusteJeu;
    }

    public int getRemainingAttempts() {
        return prixJusteJeu.getRemainingAttempts();
    }

    public PrixJusteJeu.Result checkGuess(int valeur) {
        return this.prixJusteJeu.checkGuess(valeur);
    }
}
