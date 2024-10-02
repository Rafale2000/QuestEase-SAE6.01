package bdd;

public class Son {
    private int id;
    private byte son;

    public Son(int id, byte son) {
        this.id = id;
        this.son = son;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getSon() {
        return son;
    }

    public void setSon(byte son) {
        this.son = son;
    }
}
