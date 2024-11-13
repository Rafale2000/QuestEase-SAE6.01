package com.example.questease;

import com.example.questease.Model.Jeu.PrixJusteJeu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PrixJusteTest {
    static PrixJusteJeu prix;
    @BeforeAll
    static void setUpBeforeAll() {
        prix = new PrixJusteJeu(5,50);
    }

    @Test
    void testSuperieur() {
        System.out.println("Running test 1");
        Assertions.assertEquals(PrixJusteJeu.Result.TROP_HAUT, prix.checkGuess(75));
        Assertions.assertEquals(4, prix.getRemainingAttempts());
    }

    @Test
    void testInferieur(){
        System.out.println("Running test 2");
        Assertions.assertEquals(PrixJusteJeu.Result.TROP_BAS, prix.checkGuess(40));

    }
    @Test
    void testEgal(){
        System.out.println("Running test 3");
        Assertions.assertEquals(PrixJusteJeu.Result.CORRECT, prix.checkGuess(50));
    }
}
