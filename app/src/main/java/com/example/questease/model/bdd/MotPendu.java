package com.example.questease.model.bdd;


public class MotPendu {
    private int id;
    private String mot;
    private int diff;
    private int idIndice;

    public MotPendu(int id, String mot, int diff, int idIndice) {
        this.id = id;
        this.mot = mot;
        this.diff = diff;
        this.idIndice = idIndice;
    }

    public int getId() {
        return id;
    }


    public String getMot() {
        return mot;
    }

    public void setMot(String mot) {
        this.mot = mot;
    }

    public int getDiff() {
        return diff;
    }

    public void setDiff(int diff) {
        this.diff = diff;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdIndice() {
        return idIndice;
    }

    public void setIdIndice(int idIndice) {
        this.idIndice = idIndice;
    }
}
