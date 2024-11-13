package com.example.questease.Model.BDD.jeu;

import java.util.Random;
import java.util.Scanner;

public class PrixJust {
    int nbr;
    int pv;

    PrixJust(int pv){
        Random random = new Random();
        this.pv = pv;
        nbr = random.nextInt(100);
    }

    public boolean lunch(){
        while(pv>0){
            Scanner sc = new Scanner(System.in);
            System.out.println("Veuillez saisir le nombre :");
            int nouveauNombre = sc.nextInt();
            if(nouveauNombre>nbr){
                pv-=1;
                System.out.println("c'est trop haut");
                System.out.println("il te reste "+ this.pv+" cout");
            } else if (nouveauNombre < nbr) {
                pv-=1;
                System.out.println("c'est trop bas");
                System.out.println("il te reste "+ this.pv+" cout");

            } else{
                pv = 0;
                System.out.println("c'est le bon nombre, tient 1 euro");
                return true;
            }
        }
        return false;
    }
}
