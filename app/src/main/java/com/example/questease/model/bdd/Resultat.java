package com.example.questease.model.bdd;

public class Resultat {
    private int idUtilisateur;
    private boolean isTresor;
    private boolean isEpreuve1;
    private boolean isEpreuve2;
    private boolean isEpreuve3;
    private boolean isEpreuve4;

    public Resultat(int idUtilisateur, boolean isTresor, boolean isEpreuve1, boolean isEpreuve2, boolean isEpreuve3, boolean isEpreuve4) {
        this.idUtilisateur = idUtilisateur;
        this.isTresor = isTresor;
        this.isEpreuve1 = isEpreuve1;
        this.isEpreuve2 = isEpreuve2;
        this.isEpreuve3 = isEpreuve3;
        this.isEpreuve4 = isEpreuve4;
    }

    public boolean isTresor() {
        return isTresor;
    }

    public void setTresor(boolean tresor) {
        isTresor = tresor;
    }

    public boolean isEpreuve1() {
        return isEpreuve1;
    }

    public void setEpreuve1(boolean epreuve1) {
        isEpreuve1 = epreuve1;
    }

    public boolean isEpreuve2() {
        return isEpreuve2;
    }

    public void setEpreuve2(boolean epreuve2) {
        isEpreuve2 = epreuve2;
    }

    public boolean isEpreuve3() {
        return isEpreuve3;
    }

    public void setEpreuve3(boolean epreuve3) {
        isEpreuve3 = epreuve3;
    }

    public boolean isEpreuve4() {
        return isEpreuve4;
    }

    public void setEpreuve4(boolean epreuve4) {
        isEpreuve4 = epreuve4;
    }
}
