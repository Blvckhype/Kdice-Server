package Model;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class Player extends Thread {

    private long id;
    private Socket clientSocket;
    private InputStream clientInput;
    private BufferedReader clientIn;
    private DataOutputStream clientOut;
    private String nickname;
    private boolean isReady;

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
        } catch (SocketException ex) {
            ex.printStackTrace();
        } catch (SocketTimeoutException st) {
            this.isReady = false;
        } catch (IOException e) {
            e.printStackTrace();
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

}
