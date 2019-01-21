package Model;

public class Field {

    private int owner;
    private int cubeCount;

    Field() {
        this.owner = 0;
        this.cubeCount = 0;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getCubeCount() {
        return cubeCount;
    }

    public void setCubeCount(int cubeCount) {
        this.cubeCount = cubeCount;
    }
}
