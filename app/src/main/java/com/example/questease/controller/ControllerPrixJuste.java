package com.example.questease.controller;


import com.example.questease.model.jeu.PrixJusteJeu;

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

    public void reduceRemainingAttempts() {
        prixJusteJeu.decreaseRemainingAttemps();
    }

    public String CheckGuess(int valeur) {
        return this.prixJusteJeu.checkGuess(valeur).name();
    }
}
