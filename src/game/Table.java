package game;

import java.time.LocalDateTime;
import java.time.Duration;

public class Table1 {
    protected static int idCount = 0;
    private int tableId;
    private Dealer dealer;
    private Player[] players;
    private boolean[] ready; // do i need this?
    private static final int PLAYER_LIMIT = 6;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int duration;
    private static Timer turnTimer;
    private int playerCount;
    private boolean isActive;

    // how do we make sure that dealer only spawns one table?
    // maybe flag in the dealer class that triggers if they have a table open already?
    // actually i don't even know if we want this feature ^^ we'll deal with it later if we do want this
    public Table1(Dealer dealer){
        this.tableId = idCount++;
        this.dealer = dealer;
        this.players = new Player[PLAYER_LIMIT];
        this.ready = new boolean[PLAYER_LIMIT];
        this.startTime = LocalDateTime.now();
        this.playerCount = 0;
        this.isActive = true;
    }

    public int getTableId() {
        return tableId;
    }

    public Dealer getDealer() {
        return this.dealer;
    }

    public Player[] getPlayers() {
        return this.players;
    }

    public boolean[] getReady() {
        return ready;
    }

    // idk if we need this, but it's in the UML so
    // also as far as i know, getUsername is not a getter in the Dealer class
    public String getDealerName() {
        return this.dealer.getUsername();
    }

    public int getPlayerCount() {
        return this.playerCount;
    }

    public int getPlayerLimit() {
        return PLAYER_LIMIT;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public boolean isFull() {
        if (playerCount >= PLAYER_LIMIT) {
            return true;
        } else {
            return false;
        }
    }

// im actually not sure how to handle player moves but :sob: this is my attempt at hit handling

    resetTable()


    public void closeTable() {
        this.endTime = LocalDateTime.now();
        Duration elapsedTime = Duration.between(startTime, endTime);
        this.duration = (int) elapsedTime.toMinutes();
    }

    @Override
    public String toString() {
        return "Dealer: " + this.dealer.getUsername() + "\nTable Id: " + this.tableId + "\n";
    }

    /*public static void join(Player player) {
        this.players[this.playerCount] = player;
        this.ready[this.playerCount] = false;
        this.playerCount++;
    }

    public static void disconnect(Player player) {

    }

    public static void startGame() {
        Game newGame = new Game();
        this.games[this.gameCount] = newGame;
    }

    // include a method that allows for easy logging
    // returns the current state of the table
    public static String toString() {
        return // INSERT FORMAT HERE
    }*/
}