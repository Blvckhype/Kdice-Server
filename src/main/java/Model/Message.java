package Model;

public class Message {
    private long playerId = 0;
    private final String action;

    public Message(String action) {
        this.action = action;
    }

    public Message(long playerId, String action) {
        this.playerId = playerId;
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public long getPlayerId() {
        return playerId;
    }

    public boolean isPlayerIdPresent() {
        return playerId != 0;
    }
}
