package com.example.questease.model.bdd;

public class MotCryptex {
    private int id;
    private String mot;
    private int diff;
    private int idIndice; // Ajouté
    private Indice indice;

    public MotCryptex(int id, String mot, int diff, Indice indice) {
        this.id = id;
        this.mot = mot;
        this.diff = diff;
        this.indice = indice;
        this.idIndice = indice != null ? indice.getId() : 0; // Si l'indice est passé
    }

    public MotCryptex(int id, String mot, int diff, int idIndice) {
        this.id = id;
        this.mot = mot;
        this.diff = diff;
        this.idIndice = idIndice;
        this.indice = null; // Pour éviter toute confusion
    }

    public int getIdIndice() {
        return idIndice;
    }

    public void setIdIndice(int idIndice) {
        this.idIndice = idIndice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Indice getIndice() {
        return indice;
    }

    public void setIndice(Indice indice) {
        this.indice = indice;
    }

    // Autres getters et setters
}
