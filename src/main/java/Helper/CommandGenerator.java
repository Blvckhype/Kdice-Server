package Helper;

import Model.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

class CommandGenerator {

    final static Logger logger = Logger.getLogger(CommandGenerator.class);


    static void connected(Player player) throws IOException {
        player.getClientOut().writeBytes("POLACZONO\n");
        logger.info("DO: " + player.getClientSocket().getLocalAddress() + " WYSLANO POLACZONO");
        player.getClientOut().flush();
    }

    static void answer(Player player) throws IOException {
        player.getClientOut().writeBytes("OK\n");
        logger.info("DO: " + player.getClientSocket().getLocalAddress() + " WYSLANO TWOJ RUCH");
        player.getClientOut().flush();
    }

    static void yourMove(Player player) throws IOException {
        player.getClientOut().writeBytes("TWOJ RUCH\n");
        logger.info("DO: " + player.getClientSocket().getLocalAddress() + " WYSLANO OK");
        player.getClientOut().flush();
    }

    static void startMessage(List<Player> playerList, int startPlayer) throws IOException {
        for (Player player : playerList) {
            player.getClientOut().writeBytes("START " + player.getId() + " " + startPlayer + "\n");
            logger.info("DO: " + player.getClientSocket().getLocalAddress() + " WYSLANO START " + player.getId() + " " + startPlayer );
            player.getClientOut().flush();
        }
    }

    static void boardInfo(Game game, List<Player> playerList) throws IOException {
        for (Player player : playerList) {
            if (player.isReady()) {
                for (int x = 0; x < 5; x++) {
                    for (int y = 0; y < 5; y++) {
                        Field field = game.getFieldInfo(x, y);
                        String boardInfo = "PLANSZA " + (x + 1) + " " + (y + 1) + " " + field.getOwner() + " " + field.getCubeCount();
                        player.getClientOut().writeBytes(boardInfo + "\n");
                        logger.info("DO: " + player.getClientSocket().getLocalAddress() + " WYSLANO " + boardInfo);
                        player.getClientOut().flush();
                    }
                }
            }
        }
    }

    static void attackResult(AttackResult[] attackResults, List<Player> playerList) throws IOException {
        for (Player player : playerList) {
            if (player.isReady()) {
                String result;
                if (attackResults[1].getId() == 0) {
                    result = "WYNIK " + attackResults[0].getId() + " " + attackResults[0].getCubeAmount() + " " + attackResults[0].getCubeRolls() +
                            " " + attackResults[1].getId() + " " + attackResults[1].getCubeAmount() + " " + attackResults[1].getCubeRolls() + attackResults[0].getWinner() + "\n";
                } else {
                    result = "WYNIK " + attackResults[0].getId() + " " + attackResults[0].getCubeAmount() + " " + attackResults[0].getCubeRolls() +
                            " " + attackResults[1].getId() + " " + attackResults[1].getCubeAmount() + " " + attackResults[1].getCubeRolls() + " " + attackResults[0].getWinner() + "\n";
                }
                player.getClientOut().writeBytes(result);
                logger.info("DO: " + player.getClientSocket().getLocalAddress() + " WYSLANO " + result);
                player.getClientOut().flush();
            }
        }
    }

    static void attackInfo(List<Player> playerList, String command, int id) throws IOException {
        for (Player player : playerList) {
            if (player.isReady()) { // && player.getId() != id
                player.getClientOut().writeBytes(command + "\n");
                logger.info("DO: " + player.getClientSocket().getLocalAddress() + " WYSLANO " + command);
                player.getClientOut().flush();
            }
        }
    }

    static void endRound(List<Player> playerList) throws IOException {
        for (Player player : playerList) {
            if (player.isReady()) {
                player.getClientOut().writeBytes("KONIEC RUNDY\n");
                logger.info("DO: " + player.getClientSocket().getLocalAddress() + " WYSLANO KONIEC RUNDY");
                logger.info("-----------------------------------------------------------------------------");
                player.getClientOut().flush();
            }
        }
    }

    static void endTurn(List<Player> playerList, int turn, int place) throws IOException {
        for (Player player : playerList) {
            if (player.isReady()) {
                player.getClientOut().writeBytes("TURA " + turn + " " + place-- + "\n");
                logger.info("DO: " + player.getClientSocket().getLocalAddress() + " WYSLANO TURA " + turn + " " + place--);
                player.getClientOut().flush();
            }
        }
    }

    static void endTurnForPlayer(Player player, int turn, int place) throws IOException {
        player.getClientOut().writeBytes("TURA " + turn + " " + place + "\n");
        logger.info("DO: " + player.getClientSocket().getLocalAddress() + " WYSLANO TURA " + turn + " " + place);
        player.getClientOut().flush();
        player.setReady(false);
    }

    static void endGame(List<Player> playerList, List<Score> generalScore) throws IOException {
        Collections.sort(generalScore);
        String generalClassification = "";
        for (int i = 0; i < 5; i++) {
            generalClassification = generalClassification + playerList.get(i).getNickname() + " " + generalScore.get(i).getSum() + " ";
        }
        for (Player player : playerList) {
            player.getClientOut().writeBytes("KONIEC " + generalClassification + "\n");
            logger.info("DO: " + player.getClientSocket().getLocalAddress() + " WYSLANO KONIEC " + generalClassification);
            player.getClientOut().flush();

        }
    }
}
