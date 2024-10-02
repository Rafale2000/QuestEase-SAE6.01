package Model.bdd;


import java.util.ArrayList;

public class Indice {

    private int id;

    private String indice;

    private ArrayList<Integer> sonMot;

    public Indice(int id, String indice, ArrayList<Integer> sonMot) {
        this.id = id;
        this.indice = indice;
        this.sonMot = sonMot;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }
}
