package Model;

public class AttackResult {

    private long id;
    private int cubeAmount;
    private int[] cubeRolls;
    private long winner;

    public AttackResult(long id, int cubeAmount) {
        this.id = id;
        this.cubeAmount = cubeAmount;
        this.cubeRolls = new int[this.cubeAmount];
        this.winner = -1;
    }

    public long getId() {
        return id;
    }

    public int getCubeAmount() {
        return cubeAmount;
    }

    public int[] getCubeRolls() {
        return cubeRolls;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCubeAmount(int cubeAmount) {
        this.cubeAmount = cubeAmount;
    }

    public void setCubeRolls(int[] cubeRolls) {
        this.cubeRolls = cubeRolls;
    }

    public long getWinner() {
        return winner;
    }

    public void setWinner(long winner) {
        this.winner = winner;
    }
}
