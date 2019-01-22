package Helper;

import Model.*;

import java.io.IOException;
import java.util.List;

class CommandGenerator {

    static void connected(Player player) throws IOException {
        player.getClientOut().writeBytes("POLACZONO\n");
        player.getClientOut().flush();
    }

    static void startMessage(List<Player> playerList, int startPlayer) throws IOException {
        for (Player player : playerList) {
            player.getClientOut().writeBytes("START " + player.getId() + " " + startPlayer + "\n");
            player.getClientOut().flush();
        }
    }

    static void boardInfo(Game game, List<Player> playerList) throws IOException {
        for (Player player : playerList) {
            if (player.isReady()) {
                for (int x = 0; x < 5; x++) {
                    for (int y = 0; y < 5; y++) {
                        Field field = game.getFieldInfo(x, y);
                        player.getClientOut().writeBytes("PLANSZA " + (x + 1) + " " + (y + 1) + " " + field.getOwner() + " " + field.getCubeCount() + "\n");
                        player.getClientOut().flush();
                    }
                }
            }
        }
    }

    static void attackResult(AttackResult[] attackResults, List<Player> playerList) throws IOException {
        StringBuilder attackerRolls = new StringBuilder();
        StringBuilder opponentRolls = new StringBuilder();
        for (int i = 0; i < attackResults[0].getCubeRolls().length; i++) {
            attackerRolls.append(attackResults[0].getCubeRolls()[i]).append(" ");
        }
        for (int i = 0; i < attackResults[1].getCubeRolls().length; i++)
            opponentRolls.append(attackResults[1].getCubeRolls()[i]).append(" ");
        for (Player player : playerList) {
            if (player.isReady()) {
                player.getClientOut().writeBytes("WYNIK " + attackResults[0].getId() + " " + attackResults[0].getCubeAmount() + " " + attackerRolls +
                        attackResults[1].getId() + " " + attackResults[1].getCubeAmount() + " " + opponentRolls + attackResults[0].getWinner() + "\n");
                player.getClientOut().flush();
            }
        }
    }

    static void attackInfo(List<Player> playerList, String command) throws IOException {
        for (Player player : playerList) {
            if (player.isReady()) {
                System.out.println(command);
                player.getClientOut().writeBytes(command);
                player.getClientOut().flush();
            }
        }
    }

    static void endRound(List<Player> playerList) throws IOException {
        for (Player player : playerList) {
            if (player.isReady()) {
                player.getClientOut().writeBytes("KONIEC RUNDY\n");
                player.getClientOut().flush();
            }
        }
    }

    //FOR ALL
    static void endTurn(List<Player> playerList, int turn, int place) throws IOException {
        for (Player player : playerList) {
            if (player.isReady()) {
                player.getClientOut().writeBytes("TURA " + turn + " " + place-- + "\n");
                player.getClientOut().flush();
            }
        }
    }

    static void endTurnForPlayer(Player player, int turn, int place) throws IOException {
        player.getClientOut().writeBytes("TURA " + turn + " " + place + "\n");
        player.getClientOut().flush();
        player.setReady(false);
    }

    //TODO
    static void endGame(List<Player> playerList, List<Score> generalScore) throws IOException {
        String generalClassification = "";
        for (int i = 0; i < 5; i++) {
            generalClassification = generalClassification + playerList.get(i).getNickname() + " " + generalScore.get(i).getSum() + " ";
        }
        for (Player player : playerList) {
            player.getClientOut().writeBytes("KONIEC " + generalClassification + "\n");
            player.getClientOut().flush();

        }
    }
}
