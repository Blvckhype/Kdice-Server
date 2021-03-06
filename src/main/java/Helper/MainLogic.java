package Helper;

import Model.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class MainLogic {

    private List<Player> playerList;
    private final static int MAX_PLAYERS = 5;
    private final static String OK = "OK\n";
    private final static String ERROR = "ERROR\n";
    private final static String MOVE = "TWOJ RUCH\n";
    private Game game;
    private GameHelper gameHelper;
    private ArrayList<Score> generalScore = new ArrayList<>();
    private int playerPositionInTurn;
    private int currentPlayer;

    @SuppressWarnings("InfiniteLoopStatement")
    public synchronized void startServer(ServerSocket serverSocket) throws IOException, InterruptedException {
        playerList = Collections.synchronizedList(new ArrayList<>());
        int remaining = 1;

        while (true) {

            if (playerList.size() < MAX_PLAYERS) {
                while (playerList.size() < 5) {
                    Socket connectionSocket = serverSocket.accept();
                    playerList.add(new Player(remaining, connectionSocket));
//                    playerList.get(remaining - 1).getClientSocket().setSoTimeout(2 * 1000);
                    playerList.get(remaining - 1).start();
                    generalScore.add(new Score((long) remaining, 0));
                    CommandGenerator.connected(playerList.get(remaining - 1));
                    remaining++;
                }
            }

            if (playerList.stream().allMatch(Player::isReady)) {
                setUpGame(playerList);
                currentPlayer = 1;
                CommandGenerator.startMessage(playerList, currentPlayer);
                CommandGenerator.boardInfo(this.game, playerList);
            } else
                continue;

            int turns = 1;
            int looser, rounds = 1;
            String command;

            while (turns <= 10) {
                for (Player player : playerList) {
                    if (player.isReady()) {
                        CommandGenerator.yourMove(player);
                        do {
                            command = player.getClientIn().readLine(); //ATAK
                            if (command.startsWith("ATA")) {
                                CommandGenerator.answer(player);
                                CommandGenerator.attackInfo(playerList, command, (int) player.getId());
                                int[] attack = CommandParser.validateAttack(command);
                                AttackResult[] attackResults = gameHelper.attack(attack, playerList.get(currentPlayer - 1));
                                CommandGenerator.attackResult(attackResults, playerList);
                                looser = gameHelper.checkPlayerFields(playerList, game, attackResults[1].getId());
                                if (looser != -1 && player.isReady()) {
                                    CommandGenerator.endTurnForPlayer(playerList.get(looser), turns, playerPositionInTurn);
                                    generalScore.get(looser).setSum(playerPositionInTurn);
                                    playerPositionInTurn--;
                                }
                            } else if (command.startsWith("PAS"))
                                CommandGenerator.answer(player);
                        } while (!command.startsWith("PAS"));

                        currentPlayer++;
                        if (currentPlayer == 6) {
                            gameHelper.addCubes(game, playerList);
                            CommandGenerator.endRound(playerList);
                            CommandGenerator.boardInfo(game, playerList);
                            currentPlayer = 1;
                            rounds = rounds + 1;
                        }

                        if (rounds == 100 || !checkActivePlayers(playerList)) {
                            rounds = 1;
                            setRemainingPlayerScore(playerList);
                            CommandGenerator.endTurn(playerList, turns, playerPositionInTurn);
                            turns++;
                            System.out.println("TURA: " + (turns - 1));
                            setUpGame(playerList);
                            CommandGenerator.boardInfo(game, playerList);
                        }

                    } else {
                        currentPlayer++;
                        if (currentPlayer == 6)
                            currentPlayer = 1;
                    }

                    if (turns == 11)
                        CommandGenerator.endGame(playerList, generalScore);
                    break;
                }
            }
            break;
        }
    }

    private boolean checkActivePlayers(List<Player> playerList) {
        int i = 0;
        for (Player player : playerList) {
            if (player.isReady())
                i++;
            if (i >= 2)
                return true;
        }
        return false;
    }

    private void setRemainingPlayerScore(List<Player> playerList) {
        for (Player player : playerList) {
            if (player.isReady()) {
                generalScore.get((int) player.getId() - 1).setSum(playerPositionInTurn);
                playerPositionInTurn--;
            }
        }
    }


    private void setUpGame(List<Player> playerList) {
        this.playerPositionInTurn = 5;
        for (Player player : playerList) {
            player.setReady(true);
        }
        this.game = new Game();
        this.gameHelper = new GameHelper(this.game);

    }
}