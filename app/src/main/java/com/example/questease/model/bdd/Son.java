package com.example.questease.model.bdd;

public class Son {
    private int id;
    private final String cheminSon;
    private final int idIndice;

    public Son(int id, String cheminSon, int idIndice) {
        this.id = id;
        this.cheminSon = cheminSon;
        this.idIndice = idIndice;
    }


    public int getIdIndice() {
        return idIndice;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSon() {
        return this.cheminSon;
    }

}
