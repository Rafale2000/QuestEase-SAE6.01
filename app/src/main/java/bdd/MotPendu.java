package bdd;



public class MotPendu {
    private int id;
    private String mot;
    private int diff;

    public MotPendu(int id,String mot, int diff) {
        this.id = id;
        this.mot = mot;
        this.diff = diff;
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
}
