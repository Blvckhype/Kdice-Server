package Model;

import java.util.Random;

public class  Game {

    private Field[][] board = new Field[5][5];

    public Game() {
        for (int i = 0 ; i < 5 ; i++) {
            for (int j = 0 ; j < 5 ; j++) {
                this.board[i][j] = new Field();
            }
        }
        setUpGame();
    }

    private void setUpGame() {
        for (int i = 0 ; i < 5 ; i++) {
            for (int j = 0 ; j < 2 ; j++) {
                randomPlayers(i);
            }
        }
        for (int i = 0 ; i < 5 ; i++)
            randomCubeGroup();
    }

    private void randomPlayers(int i) {
        int x, y;
        do {
            y = new Random().nextInt(5);
            x = new Random().nextInt(5);
        } while (this.board[y][x].getOwner() != 0);
        this.board[y][x].setCubeCount(2);
        this.board[y][x].setOwner(i + 1);
    }

    private void randomCubeGroup() {
        int x, y;
        do {
            y = new Random().nextInt(5);
            x = new Random().nextInt(5);
        } while (this.board[y][x].getOwner() != 0 || this.board[y][x].getCubeCount() != 0);
        this.board[y][x].setCubeCount(new Random().nextInt(5) + 1);
        System.out.println();
    }

    public Field[][] getBoard() {
        return board;
    }

    public Field getFieldInfo(int x, int y) {
        return this.board[x][y];
    }

}
