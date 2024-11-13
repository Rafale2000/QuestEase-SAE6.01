package com.example.questease.Model.BDD;

public class ChoseATrouverPrixJuste {
    private int id;
    private String nom;
    private String cheminImage;
    private int valeur;

    public ChoseATrouverPrixJuste(int id, String nom, String cheminImage, int valeur) {
        this.id = id;
        this.nom = nom;
        this.cheminImage = cheminImage;
        this.valeur = valeur;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCheminImage() {
        return cheminImage;
    }

    public void setCheminImage(String cheminImage) {
        this.cheminImage = cheminImage;
    }

    public int getValeur() {
        return valeur;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }
}
