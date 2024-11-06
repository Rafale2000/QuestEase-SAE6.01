package Controller;


import Model.Jeu.PrixJusteJeu;

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

    public String CheckGuess(int valeur) {
        return this.prixJusteJeu.checkGuess(valeur).name();
    }
}
