package com.example.questease.Model.bdd;

public class Son {
    private int id;
    private String cheminSon;
    private int IdIndice;

    public Son(int id, String cheminSon, int idIndice) {
        this.id = id;
        this.cheminSon = cheminSon;
        IdIndice = idIndice;
    }


    public int getIdIndice() {
        return IdIndice;
    }

    public void setIdIndice(int idIndice) {
        IdIndice = idIndice;
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

    public void setSon(String son) {
        this.cheminSon = cheminSon;
    }
}
