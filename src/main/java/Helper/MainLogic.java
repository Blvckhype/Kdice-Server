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
    private static final Queue<Message> messages = new LinkedBlockingQueue<>();

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

            Thread.sleep(1000);
            int turns = 1;
            int looser, rounds = 1;
            String command;

            while (turns <= 10) {
                for (Player player : playerList) {
                    if (player.isReady()) {
                        player.getClientOut().writeBytes(MOVE);
                        player.getClientOut().flush();
                        do {
                            command = player.getClientIn().readLine(); //ATAK
                            if (command.startsWith("ATA")) {
                                player.getClientOut().writeBytes(OK);
                                player.getClientOut().flush();
                                CommandGenerator.attackInfo(playerList, command, currentPlayer);
                                int[] attack = CommandParser.validateAttack(command);
                                AttackResult[] attackResults = gameHelper.attack(attack, playerList.get(currentPlayer - 1));
                                CommandGenerator.attackResult(attackResults, playerList);
                                looser = gameHelper.checkPlayerFields(playerList, game, attackResults[1].getId());
                                if (looser != -1) {
                                   CommandGenerator.endTurnForPlayer(playerList.get(looser), turns, playerPositionInTurn);
               //                     generalScore.get(looser + 1).setSum(generalScore.get(looser + 1).getSum() + playerPositionInTurn);
                                    playerPositionInTurn--;
                                }
                            } else if (command.startsWith("PAS"))
                                player.getClientOut().writeBytes(OK);
                        } while (!command.startsWith("PAS"));

                        currentPlayer++;
                        if (currentPlayer == 6) {
                            gameHelper.addCubes(game, playerList);
                            CommandGenerator.endRound(playerList);
                            CommandGenerator.boardInfo(game, playerList);
                            currentPlayer = 1;
                            rounds = rounds + 1;
                            //playerList.stream().forEach(x -> System.out.println(x.isReady()));
                        }

                        if (rounds == 100 || !checkActivePlayers(playerList)) {
                            rounds = 1;
                            CommandGenerator.endTurn(playerList, turns, playerPositionInTurn);
                            turns++;
                            System.out.println("TURA: " + (turns - 1));
                            setUpGame(playerList);
                            CommandGenerator.boardInfo(game, playerList);
                        }

                    } else
                        currentPlayer++;

                    if (turns == 11)
                        break;
                }
                //setUpGame(playerList);
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


    private void setUpGame(List<Player> playerList) {
        this.playerPositionInTurn = 5;
        for (Player player : playerList) {
            player.setReady(true);
        }
        this.game = new Game();
        this.gameHelper = new GameHelper(this.game);

    }

//    public boolean schedule(Message message) {
//        return this.messages.add(message);
//    }
//
//    public Queue<Message> getMessages() {
//        return messages;
//    }
}