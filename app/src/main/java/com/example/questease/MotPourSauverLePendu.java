package com.example.questease;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MotPourSauverLePendu {
    ArrayList<Set<String>> mot;
    String motATrouver;
    boolean trouver = false;

    MotPourSauverLePendu(String str) {
        System.out.println(str);
        this.motATrouver = str;
        this.mot = new ArrayList();

        for(int i = 0; i < str.length(); ++i) {
            System.out.println(str.charAt(i));
            this.mot.add(new HashSet());
        }

    }

    public void replace(int indice, char let) {
        ((Set)this.mot.get(indice)).clear();
        ((Set)this.mot.get(indice)).add(String.valueOf(let));
    }

    public boolean find(char lettreChoisie) {
        boolean flag = false;

        for(int i = 0; i < this.motATrouver.length(); ++i) {
            if (lettreChoisie == this.motATrouver.charAt(i)) {
                this.replace(i, lettreChoisie);
                flag = true;
            }
        }

        return flag;
    }

    public boolean ConditionDeVictoire() {
        boolean flag = true;
        Iterator var2 = this.mot.iterator();

        Set set;
        do {
            if (!var2.hasNext()) {
                return flag;
            }

            set = (Set)var2.next();
        } while(!set.isEmpty());

        return false;
    }

    public String afficherMot() {
        StringBuilder affichage = new StringBuilder();

        for(int i = 0; i < this.motATrouver.length(); ++i) {
            if (((Set)this.mot.get(i)).isEmpty()) {
                affichage.append("_");
            } else {
                affichage.append(this.motATrouver.charAt(i));
            }
        }

        return affichage.toString();
    }

    public ArrayList<Set<String>> getMot() {
        return this.mot;
    }

    public void setMot(ArrayList<Set<String>> mot) {
        this.mot = mot;
    }

    public String getMotATrouver() {
        return this.motATrouver;
    }

    public void setMotATrouver(String motATrouver) {
        this.motATrouver = motATrouver;
    }

    public boolean isTrouver() {
        return this.trouver;
    }

    public void setTrouver(boolean trouver) {
        this.trouver = trouver;
    }
}
