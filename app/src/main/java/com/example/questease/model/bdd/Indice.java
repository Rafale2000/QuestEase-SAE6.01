package com.example.questease.model.bdd;


public class Indice {

    private int id;

    private String hint;

    public Indice(int id, String hint) {
        this.id = id;
        this.hint = hint;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHint() {
        return hint;
    }

    public void setIndice(String hint) {
        this.hint = hint;
    }
}
