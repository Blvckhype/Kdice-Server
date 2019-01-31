package Model;

public class AttackResult {

    private long id;
    private int cubeAmount;
    private String cubeRolls;
    private long winner;

    public AttackResult(long id, int cubeAmount) {
        this.id = id;
        this.cubeAmount = cubeAmount;
        this.cubeRolls = "";
        this.winner = -1;
    }

    public long getId() {
        return id;
    }

    public int getCubeAmount() {
        return cubeAmount;
    }

    public String getCubeRolls() {
        return cubeRolls;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCubeAmount(int cubeAmount) {
        this.cubeAmount = cubeAmount;
    }

    public void setCubeRolls(String cubeRolls) {
        if (this.cubeRolls.length() == 0)
            this.cubeRolls = cubeRolls;
        else
            this.cubeRolls = this.cubeRolls + " " + cubeRolls;
    }

    public long getWinner() {
        return winner;
    }

    public void setWinner(long winner) {
        this.winner = winner;
    }
}
