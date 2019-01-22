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
    private int rounds = 0;
    private boolean sent;
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
                    playerList.get(remaining - 1).getClientSocket().setSoTimeout(2 * 1000);
                    playerList.get(playerList.size() - 1).start();
                    generalScore.add(new Score((long) remaining, 0));
                    CommandGenerator.connected(playerList.get(remaining - 1));
                    remaining++;
                }

                int startPlayer;
                if (playerList.stream().allMatch(Player::isReady)) {
                    startPlayer = setUpGame(playerList);
                    currentPlayer = startPlayer;
                    CommandGenerator.startMessage(playerList, currentPlayer);
                    CommandGenerator.boardInfo(this.game, playerList);
                } else
                    continue;
                Thread.sleep(1000);
                int turns = 1;
                int looser;
                String command;

                while (turns <= 10) {
                    if (playerList.get(currentPlayer - 1).isReady() && !sent) {
                        //playerList.get(currentPlayer - 1).getClientOut().writeBytes(MOVE);
                        playerList.get(currentPlayer - 1).schedule(new Message(MOVE));
                        sent = true;
                    }
                    //System.out.println(messages.size());
                    if (!messages.isEmpty() && playerList.get(currentPlayer - 1).isReady()) {
                        assert messages.peek() != null;
                        command = messages.peek().getAction();
                        if (command.startsWith("ATAK")) {
                            messages.remove();
                            CommandGenerator.attackInfo(playerList, command);
                            int[] attack = CommandParser.validateAttack(command);
                            AttackResult[] attackResults = gameHelper.attack(attack, playerList.get(currentPlayer - 1));
                            if (attackResults != null) {
                                //playerList.get(currentPlayer - 1).getClientOut().writeBytes(OK);
                                //playerList.get(currentPlayer - 1).schedule(new Message(OK));
                                playerList.get(currentPlayer - 1).getClientOut().flush();
                                CommandGenerator.attackResult(attackResults, playerList);
                                looser = gameHelper.checkPlayerFields(playerList, game, attackResults[1].getId());
                                if (looser != -1) {
                                    CommandGenerator.endTurnForPlayer(playerList.get(looser), turns, playerPositionInTurn);
                                    generalScore.get(looser + 1).setSum(generalScore.get(looser + 1).getSum() + playerPositionInTurn);
                                    playerPositionInTurn--;
                                }
                            } else {
                                playerList.get(currentPlayer - 1).getClientOut().writeBytes(ERROR);
                                playerList.get(currentPlayer - 1).getClientOut().flush();
                            }
                        }
                        if (command.equals("PASS\n")) {
                            messages.remove();
                            playerList.get(currentPlayer - 1).getClientOut().writeBytes(OK);
                            playerList.get(currentPlayer - 1).getClientOut().flush();
                            this.sent = false;
                        }
                        controlMove(startPlayer);

                        if (rounds == 100 || !checkActivePlayers(playerList)) {
                            CommandGenerator.endTurn(playerList, turns, playerPositionInTurn);
                            turns++;
                            startPlayer = setUpGame(playerList);
                            currentPlayer = startPlayer;
                            CommandGenerator.boardInfo(game, playerList);
                        }
                    }
                }
                if (rounds == 10) {
                    Collections.sort(generalScore);
                    CommandGenerator.endGame(playerList, generalScore);
                    playerList.forEach(player -> {
                        try {
                            player.getClientSocket().close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    break;
                }
            }
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

    private void controlMove(int startPlayer) throws IOException {
        this.currentPlayer++;
        if (this.currentPlayer % 6 == 0) {
            this.currentPlayer = 1;
        }
        if (this.currentPlayer == startPlayer) {
            this.rounds++;
            this.gameHelper.addCubes(game, playerList);
            CommandGenerator.endRound(playerList);
            CommandGenerator.boardInfo(game, playerList);
        }
    }

    private int setUpGame(List<Player> playerList) {
        int startPlayer = new Random().nextInt(5 + 1 - 1) + 1;
        this.rounds = 0;
        this.playerPositionInTurn = 5;
        for (Player player : playerList) {
            player.setReady(true);
        }
        this.game = new Game();
        this.gameHelper = new GameHelper(this.game);


        return startPlayer;
    }

    public boolean schedule(Message message) {
        return this.messages.add(message);
    }

    public Queue<Message> getMessages() {
        return messages;
    }
}