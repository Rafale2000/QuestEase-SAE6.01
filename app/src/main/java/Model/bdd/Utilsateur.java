package Model.bdd;

import java.util.ArrayList;

public class Utilsateur {
    private int id; // This will be auto-incremented by the database
    private String firstName;
    private String lastName;
    private int xp;
    private ArrayList<Integer> resultatPasse;

    // Constructor
    public Utilsateur(String firstName, String lastName, int xp) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.xp = xp;
    }

    public Utilsateur(){
        this.lastName = "Doe";
        this.firstName = "john";
        this.xp = 0;
    }

    // Getters and Setters for all fields
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }
}
