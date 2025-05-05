package game;


import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Table {
    protected static int idCount = 0;
    private int tableId;
    private Dealer dealer;
    private Player[] players;
    private int playerCount;
    private static final int PLAYER_LIMIT = 6;
    private static Timer turnTimer;
    private LocalDateTime startTime;    // extrapolation from GameData class; not sure if needed
    private LocalDateTime endTime;      // extrapolation from GameData class; not sure if needed
    private int timeLive;               // extrapolation from GameData class; not sure if needed
    private boolean isActive;           // this is a variable used to determine whether
                                        // the table is within a game or not
    // thought it would be helpful for handling incoming connections during a game

    // how do we make sure that dealer only spawns one table?
    // maybe flag in the dealer class that triggers if they have a table open already?
    // actually i don't even know if we want this feature ^^ we'll deal with it later if we do want this
    /* CONSTRUCTOR */
    public Table(){
        this.tableId = idCount++;
        this.players = new Player[PLAYER_LIMIT];
        this.playerCount = 0;
        this.turnTimer = new Timer(60);
        this.startTime = LocalDateTime.now();
        this.isActive = false;
    }

    /* GETTERS */
    public int getTableId() {
        return tableId;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public Player[] getPlayers() {
        return players;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public int getPlayerLimit() {
        return PLAYER_LIMIT;
    }

    public static Timer getTurnTimer() {
        return turnTimer;
    }

    // checks if the table is in a game
    public boolean isActive() {
        return this.isActive;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    /* CHECKS IF THE TABLE IS FULL */
    public boolean isFull() {
        if (playerCount >= PLAYER_LIMIT) {
            return true;
        } else {
            return false;
        }
    }

    /* CHECKS IF THE ENTIRE TABLE IS READY */
    public boolean tableReady() {
        boolean tableReady = true;
        // loops through the player list
        for (int i = 0; i < playerCount; i++) {
            // if a player isn't ready, the table is not ready
            if (!players[i].isReady()) {
               tableReady = false;
            }
        }
        return tableReady;
    }

    /* ADDS A PLAYER TO THE ARRAY */
    public void addPlayer(Player player) {
        // if the table is not full
        if (!isFull()) {
            // adds a player
            players[playerCount] = player;
            playerCount++;
        }
    }

    /* REMOVES A PLAYER FROM THE ARRAY AND SHIFTS ANY PLAYERS AFTER FORWARD */
    public void removePlayer(Player player) {
        // is the player in the array?
        int match = -1;		// will hold the index for the player if found
        // goes through the entire array looking for the player
        for (int i = 0; i < playerCount; i++) {
            // if there is a matching player
            if (players[i].getUsername().equals(player.getUsername())) {
                match = i;	// stores the index
                break;		// and exits the loop
            }
        }

        /* if the player exists within the array
         * makes the player object inaccessible and available for garbage collection */
        if (match > -1) {
            // overwrites the "deleted" element with the next one over
            for (int i = match; i < playerCount-1; i++) {
                players[i] = players[i + 1];
            }
            playerCount--;			// changes the player count to be accurate
            players[playerCount] = null;	/* not that it matters much
             * since it's inaccessible from the public interface, but
             * deallocates the original last element of the past array */
        }

        // v THIS IS JUST HERE FOR TESTING PURPOSES, I'LL REMOVE THIS WHEN THE FINAL DESIGN IS COMPLETE!!
        // if there was no player removed
        if (match == -1) {
            System.out.print("Player Remove Operation Failed.");
        }
    }

    /* MAIN GAMEPLAY LOOP */
    public void startGame() {
        // should I assume that readyCheck is triggered beforehand and remove this?
        if (!tableReady()) {
            return;
        }

        // game is active
        isActive = true;

        // creates a list of players to deal to using the player array
        List<Player> dealToPlayers = new ArrayList<>();
        for (Player element : players) {
            if (element != null) {
                dealToPlayers.add(element);
            }
        }

        // deals two cards to each player
        dealer.deal(dealToPlayers);
        // deal() method takes in a list of players,
        // hence the array to list conversion

        // for each player's turn
        for (int i = 0; i < playerCount; i++) {
            // start the timer
            turnTimer.startTimer();

            // keep track of the timer to make sure it doesn't go over the duration
            while(!turnTimer.isTimeUp() /* OR --> insert condition for player has made a move */ ) {
                /* I HAVE NO IDEA IF PLAYER MOVES ARE HANDLED HERE OR NOT!!! */
                /* not sure if blackjack check is run here or not either */
            }

            // if time is up or player has made a move
            // stops the timer
            turnTimer.stopTimer();
            // and resets it for the next player
            turnTimer.resetTimer();
        }

        /* INSERT DEALER SEQUENCE AT THE END + CALL PAYOUT */

        // game is no longer active
        isActive = false;

        resetTable();
    }

    /* RESETS THE TABLE STATE */
    public void resetTable() {

        // resets the timer just in case
        turnTimer.resetTimer();

        // all players ready state is set to false and reset their hands
        for (int i = 0; i < playerCount; i++) {
            players[i].setReady(false);
            players[i].resetHand();
        }

        // resets the dealer's hand
        dealer.resetHand();

        // not sure if i want to call shuffle at the end or not

    }

    /* IF DEALER LEAVES, CALL THIS METHOD TO LOG TABLE DATA */
    public void closeSession() {
        this.endTime = LocalDateTime.now();
        Duration elapsedTime = Duration.between(startTime, endTime);
        this.timeLive = (int) elapsedTime.toMinutes();

        /* INSERT DATABASE METHOD HERE */
        // clearTableData();?

        // goes back to the lobby closeTable method
    }

    /* THIS IS FOR TESTING PURPOSES; RESETS TO THE CONSTRUCTOR STATE */
    public void clearTableData() {
        for (int i = 0; i < playerCount; i++) {
            removePlayer(players[i]);
        }
        playerCount = 0;
        turnTimer.resetTimer();
        isActive = false;
    }

    @Override
    // include a method that allows for easy logging / testing
    // returns the current state of the table
    // INSERT FORMAT HERE
    public String toString() {
        String data = "Table Id: " + this.tableId + "\nDealer: " +
                this.dealer.getUsername() + "\nPlayers:\n";
        if (playerCount == 0) {
            data += "NONE\n";
        } else {
            for (int i = 0; i < playerCount; i++) {
                data += "[" + (i + 1) + "] " + players[i].getUsername() + "\n\tReady Status: ";
                if (players[i].isReady()) {
                    data += "READY\n";
                } else {
                    data += "NOT READY\n";
                }
            }
        }
        data += "\n";

        return data;
    }
}

