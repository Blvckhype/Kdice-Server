package Model;

import Helper.MainLogic;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Player extends Thread {

    private long id;
    private Socket clientSocket;
    private InputStream clientInput;
    private BufferedReader clientIn;
    private DataOutputStream clientOut;
    private String nickname;
    private boolean isReady;
    private final Queue<Message> messages = new LinkedBlockingQueue<>();

    public Player(long id, Socket clientSocket) throws IOException {
        this.id = id;
        this.clientSocket = clientSocket;
        this.clientInput = this.clientSocket.getInputStream();
        this.clientIn = new BufferedReader(new InputStreamReader(this.clientInput));
        this.clientOut = new DataOutputStream(this.clientSocket.getOutputStream());
        this.isReady = false;
    }

    public void run() {
        try {
            String login = this.clientIn.readLine();
            this.clientOut.writeBytes("OK\n");
            this.clientOut.flush();
            setNickname(login.substring(login.indexOf(" ") + 1));
            this.isReady = true;
            /*while (!isCorrect) {
                String login = this.clientIn.readLine();
                if (!login.equals("") && login.startsWith("LOGIN") && login.length() > 6) {
                    this.clientOut.writeBytes("OK\n");
                    this.clientOut.flush();
                    setNickname(login.substring(login.indexOf(" ") + 1));
                    isCorrect = true;
                    this.isReady = true;
                } else if (!login.equals("") && (!login.startsWith("LOGIN") || login.length() <= 6)) {
                    this.clientOut.writeBytes("ERROR\n");
                    this.clientOut.flush();
                }
            }*/

            MainLogic mainLogic = new MainLogic();

            //noinspection InfiniteLoopStatement
            while (true) {

                String command, message;
                try {
                    if (!this.messages.isEmpty()) {
                        command = messages.peek().getAction();
                        messages.remove();
                        if (command.equals("TWOJ RUCH\n")) {
                            this.clientOut.writeBytes(command);
                            do {
                                message = this.clientIn.readLine();
                                mainLogic.schedule(new Message(message));
                            } while (message.equals("PASS\n"));
                        }
                        if (command.startsWith("OK")) {
                            this.clientOut.writeBytes("OK\n");
                        }
//                        if (command.equals("ERROR\n")) {
//                            this.clientOut.writeBytes("ERROR\n");
//                        }
//                        if (command.startsWith("WYNIK")) {
//                            this.clientOut.writeBytes(command);
//                        }
//                        message = this.clientIn.readLine();
//                        if (message.startsWith("ATAK")) {
//                            mainLogic.schedule(new Message(command));
////                            System.out.println(command);
//                        }
//                        if (message.startsWith("PASS")) {
//                            mainLogic.schedule(new Message(command));
//                        }
                    }
                } catch (SocketException ex) {
                    ex.printStackTrace();
                    break;
                } catch (SocketTimeoutException st) {
                    this.isReady = false;
                }
            }
        } catch (SocketTimeoutException st) {
            this.isReady = false;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                this.clientSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public long getId() {
        return id;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public BufferedReader getClientIn() {
        return clientIn;
    }

    public DataOutputStream getClientOut() {
        return clientOut;
    }

    public InputStream getClientInput() {
        return clientInput;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean schedule(Message message) {
        return messages.add(message);
    }
}
