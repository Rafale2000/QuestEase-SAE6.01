package com.example.questease.Model.BDD;

public class Utilisateur {
    private int id; // This will be auto-incremented by the database
    private String username;
    private int xp;
    private String email;
    private String resultatPasse;

    // Constructor
    public Utilisateur(int id, String firstName, int xp, String res, String e) {
        this.id = id;
        this.username = firstName;
        this.xp = xp;
        this.resultatPasse = res;
        this.email = e;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResultatPasse() {
        return resultatPasse;
    }

    public void setResultatPasse(String resultatPasse) {
        this.resultatPasse = resultatPasse;
    }

    public Utilisateur() {
        this.username = "john";
        this.xp = 0;
    }

    // Getters and Setters for all fields
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }
}
