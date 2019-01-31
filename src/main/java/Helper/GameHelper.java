package Helper;

import Model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class GameHelper {

    private Game game;
    private Random random;

    GameHelper(Game game) {
        this.game = game;
        this.random = new Random();
    }

    AttackResult[] attack(int[] positions, Player player) {
        int attackerSum = 0, opponentSum = 0;
        AttackResult[] attackResults = new AttackResult[2];
        attackResults[0] = new AttackResult(game.getBoard()[positions[0]][positions[1]].getOwner(), game.getBoard()[positions[0]][positions[1]].getCubeCount());
        attackResults[1] = new AttackResult(game.getBoard()[positions[2]][positions[3]].getOwner(), game.getBoard()[positions[2]][positions[3]].getCubeCount());
//        if (game.getBoard()[positions[2]][positions[3]].getOwner() == 0
//                && game.getBoard()[positions[0]][positions[1]].getCubeCount() >= 2 && game.getBoard()[positions[0]][positions[1]].getOwner() == 0 && checkNeighbors(positions)) {
//            for (int i = 0; i < game.getBoard()[positions[0]][positions[1]].getCubeCount(); i++) {
//                int value = random.nextInt(6) + 1;
//                attackerSum += attackerSum + value;
//                attackResults[0].setCubeRolls(String.valueOf(value));
//            }
//            game.getBoard()[positions[2]][positions[3]].setCubeCount(game.getBoard()[positions[0]][positions[1]].getCubeCount() - 1);
//            game.getBoard()[positions[2]][positions[3]].setOwner((int) player.getId());
//            game.getBoard()[positions[0]][positions[1]].setCubeCount(1);
//            attackResults[0].setWinner(game.getBoard()[positions[0]][positions[1]].getOwner());
//            attackResults[1].setWinner(game.getBoard()[positions[0]][positions[1]].getOwner());
//        }
//        if (game.getBoard()[positions[0]][positions[1]].getOwner() == player.getId()
//                && game.getBoard()[positions[0]][positions[1]].getCubeCount() >= 2 && checkNeighbors(positions)) {
        for (int i = 0; i < game.getBoard()[positions[0]][positions[1]].getCubeCount(); i++) {
            int value = random.nextInt(6) + 1;
            attackerSum += attackerSum + value;
            attackResults[0].setCubeRolls(String.valueOf(value));
        }
        for (int i = 0; i < attackResults[1].getCubeAmount(); i++) {
            int value = random.nextInt(6) + 1;
            opponentSum += opponentSum + value;
            attackResults[1].setCubeRolls(String.valueOf(value));
        }
        if (attackerSum > opponentSum) {
            game.getBoard()[positions[2]][positions[3]].setCubeCount(game.getBoard()[positions[0]][positions[1]].getCubeCount() - 1);
            game.getBoard()[positions[2]][positions[3]].setOwner((int) player.getId());
            game.getBoard()[positions[0]][positions[1]].setCubeCount(1);
            attackResults[0].setWinner(game.getBoard()[positions[0]][positions[1]].getOwner());
            attackResults[1].setWinner(game.getBoard()[positions[0]][positions[1]].getOwner());
            return attackResults;
        } else {
            game.getBoard()[positions[0]][positions[1]].setCubeCount(1);
            attackResults[0].setWinner(game.getBoard()[positions[2]][positions[3]].getOwner());
            attackResults[1].setWinner(game.getBoard()[positions[2]][positions[3]].getOwner());
            return attackResults;
        }
//        }
//        return null;
    }

    private boolean checkNeighbors(int[] positions) {
        return (Math.abs(positions[0] - positions[2]) == 1 || Math.abs(positions[1] - positions[3]) == 1);
    }

    int checkPlayerFields(List<Player> playerList, Game game, long owner) {
        if (owner != 0) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (game.getBoard()[i][j].getOwner() == (int) owner)
                        return -1;
                }
            }
            playerList.get((int) owner - 1).setReady(false);
            return (int) owner - 1;
        }
        return -1;
    }


    void addCubes(Game game, List<Player> playerList) throws InterruptedException {
        for (Player player : playerList) {
            if (player.isReady()) {
                List<Point> myPoints = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        Field field = game.getFieldInfo(i, j);
                        if (field.getOwner() == player.getId())
                            myPoints.add(new Point(i, j));
                    }
                }
                if (myPoints.size() >= 1) {
                    for (int i = 0; i < myPoints.size(); i++) {
                        int value = new Random().nextInt(myPoints.size());
                        if (game.getBoard()[myPoints.get(value).getX()][myPoints.get(value).getY()].getCubeCount() < 8)
                            game.getBoard()[myPoints.get(value).getX()][myPoints.get(value).getY()].setCubeCount(game.getBoard()[myPoints.get(value).getX()][myPoints.get(value).getY()].getCubeCount() + 1);
                    }
                }
            }
        }
    }


}
