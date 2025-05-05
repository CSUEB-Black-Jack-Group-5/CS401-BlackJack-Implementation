package networking;

import java.io.Serializable;
import game.Card;
import game.CardHand;
import game.Shoe;
import game.Table;

public class Message implements Serializable {
    /* Login Method */
    public static class Login {
        public static class Request extends Message {
            private String username;
            private String password;

            public Request(String username, String password) {
                this.username = username;
                this.password = password;
            }

            public String getUsername() {
                return username;
            }

            public String getPassword() {
                return password;
            }
        }
        public static class Response extends Message {
            private boolean status;
            private AccountType type;

            public Response(boolean status, AccountType type) {
                this.status = status;
                this.type = type;
            }

            public boolean getStatus() {
                return status;
            }

            public AccountType getType() {
                return type;
            }
        }
    }
    /* Create Account Method */
    public static class CreateAccount {
        public static class Request extends Message {
            private String username;
            private String password;

            public Request(String username, String password) {
                this.username = username;
                this.password = password;
            }

            public String getUsername() {
                return username;
            }

            public String getPassword() {
                return password;
            }
        }
        public static class Response extends Message {
            private boolean status;

            public Response(boolean status) {
                this.status = status;
            }

            public boolean getStatus() {
                return status;
            }
        }
    }
    /* Join Table Method */     // not properly UMLed yet; this is my attempt at implementation
    public static class JoinTable {
        public static class Request extends Message {
            private int userId;
            private int tableId;

            public Request(Integer userId, int tableId) {
                this.userId = userId;
                this.tableId = tableId;
            }

            public Integer getUserId() {
                return userId;
            }

            public int getTableId() {
                return tableId;
            }
        }
        public static class Response extends Message {
            private boolean status;

            public Response(boolean status) {
                this.status = status;
            }

            public boolean getStatus() {
                return status;
            }
        }
    }
    /* Create Table Method */
    public static class CreateTable {
        public static class Request extends Message {
            private String dealerId;    // how come dealerId is a string but tableId is an int according to UML?

            public Request(String dealerId) {
                this.dealerId = dealerId;
            }

            public String getDealerId() {
                return dealerId;
            }
        }
        public static class Response extends Message {
            private boolean status;
            private int tableId;        // how come tableId is an int but dealerId is a string according to UML?

            public Response(boolean status, int tableId) {
                this.status = status;
                this.tableId = tableId;
            }

            public boolean getStatus() {
                return status;
            }

            public int getTableId() {
                return tableId;
            }
        }
    }
    /* Deal Method */
    public static class Deal {
        public static class Request extends Message {
            private int tableId; // what table is this from

            public Request(int tableId) {
                this.tableId = tableId;
            }

            public int getTableId() {
                return tableId;
            }
        }
        public static class Response extends Message {
            private boolean status;

            public Response(boolean status) {
                this.status = status;
            }

            public boolean getStatus() {
                return this.status;
            }
        }
    }
    /* Hit Method */            // not properly UMLed yet; this is my attempt at implementation
    public static class Hit {
        public static class Request extends Message {
            private int playerId;
            private Shoe deck;

            public Request(int playerId, Shoe deck) {
                this.playerId = playerId;
                this.deck = deck;
            }

            public int getPlayerId() {
                return playerId;
            }

            public Shoe getDeck() {
                return deck;
            }
        }
        public static class Response extends Message {
            private Card draw;
            private CardHand hand;
            private boolean status;

            public Response(Card draw, CardHand hand, boolean status) {
                this.draw = draw;
                this.hand = hand;
                this.status = status;
            }

            public Card getDraw() {
                return draw;
            }

            public CardHand getHand() {
                return hand;
            }

            public boolean getStatus() {
                return status;
            }
        }
    }
    /* Stand Method */          // not properly UMLed yet; this is my attempt at implementation
    public static class Stand {
        public static class Request extends Message {
            private int playerId;

            public Request(int playerId) {
                this.playerId = playerId;
            }

            public int getPlayerId() {
                return playerId;
            }
        }
        public static class Response extends Message {
            // unsure if the hand is necessary for stand, but i implemented it just in case
            private CardHand hand;
            private boolean status;

            public Response(CardHand hand, boolean status) {
                this.hand = hand;
                this.status = status;
            }

            public CardHand getHand() {
                return hand;
            }

            public boolean getStatus() {
                return status;
            }
        }
    }
    /* Split Method */          // not properly UMLed yet; this is my attempt at implementation
    public static class Split {
        public static class Request extends Message {
            private int playerId;
            private CardHand hand;

            public Request(int playerId, CardHand hand) {
                this.playerId = playerId;
                this.hand = hand;
            }

            public int getPlayerId() {
                return playerId;
            }

            public CardHand getHand() {
                return hand;
            }
        }
        public static class Response extends Message {
            private CardHand hand;
            private CardHand splitHand;
            private boolean status;

            public Response(CardHand hand, CardHand splitHand, boolean status) {
                this.hand = hand;
                this.splitHand = splitHand;
                this.status = status;
            }

            public CardHand getHand() {
                return hand;
            }

            public CardHand getSplitHand() {
                return splitHand;
            }

            public boolean getStatus() {
                return status;
            }
        }
    }
    /* Double Down Method */    // not properly UMLed yet; this is my attempt at implementation
    public static class DoubleDown {
        public static class Request extends Message {
            private int playerId;
            private int wager;

            public Request(int playerId, int wager) {
                this.playerId = playerId;
                this.wager = wager;
            }

            public int getPlayerId() {
                return playerId;
            }

            public int getWager() {
                return wager;
            }
        }
        public static class Response extends Message {
            private int wager;
            private boolean status;

            public Response(int wager, boolean status) {
                this.wager = wager;
                this.status = status;
            }

            public int getWager() {
                return wager;
            }

            public boolean getStatus() {
                return status;
            }
        }
    }
    /* Lobby Data Method */
    public static class LobbyData {
        public static class Request extends Message {
            private int dealerId;       // added dealerId for getDealerId method in UML
            private AccountType type;

            public Request(int dealerId, AccountType type) {
                this.dealerId = dealerId;
                this.type = type;
            }

            public int getDealerId() {
                return dealerId;
            }

            public AccountType getType() {  // added getType method
                return type;
            }
        }
        public static class Response extends Message {
            private Table[] tables;
            private int playerCount;
            private int dealerCount;

            public Response(Table[] tables, int playerCount, int dealerCount) {
                this.tables = tables;
                this.playerCount = playerCount;
                this.dealerCount = dealerCount;
            }

            public Table[] getTables() {
                return tables;
            }

            public int getPlayerCount() {
                return playerCount;
            }

            public int getDealerCount() {
                return dealerCount;
            }
        }
    }
    /* Table Data Method */     // not properly UMLed yet; this is my attempt at implementation
    public static class TableData {
        public static class Request extends Message {
            private int dealerId;       // unsure who's requesting the table data?

            public Request(int dealerId) {
                this.dealerId = dealerId;
            }

            public int getDealerId() {
                return dealerId;
            }
        }
        public static class Response extends Message {
            private int dealerId;
            private int[] playerIds;
            private int playersJoined;

            public Response(int dealerId, int[] playerIds, int playersJoined) {
                this.dealerId = dealerId;
                this.playerIds = playerIds;
                this.playersJoined = playersJoined;
            }

            public int getDealerId() {
                return dealerId;
            }

            public int[] getPlayerIds() {
                return playerIds;
            }

            public int getPlayersJoined() {
                return playersJoined;
            }
        }
    }
    /* Game Data Method */
    public static class GameData {
        public static class Request extends Message {
            private int playerId;

            public Request(int playerId) {
                this.playerId = playerId;
            }

            public int getPlayerId() {      // added getPlayerId method
                return playerId;
            }
        }
        public static class Response extends Message {
            private CardHand playerHand;
            private CardHand dealerHand;

            public Response(CardHand playerHand, CardHand dealerHand) {
                this.playerHand = playerHand;
                this.dealerHand = dealerHand;
            }

            public CardHand getPlayerHand() {   // added getPlayerHand method
                return playerHand;
            }

            public CardHand getDealerHand() {   // added getDealerHand method
                return dealerHand;
            }
        }
    }
    /* Clock Sync Method */
    public static class ClockSync {
        public static class Request extends Message {
            public Request() {
            }
        }
        public static class Response extends Message {
            private float clockTime;

            public Response(float clockTime) {
                this.clockTime = clockTime;
            }

            public float getClockTime() {
                return clockTime;
            }
        }
    }
    /* Player Ready Method */
    public static class PlayerReady {
        public static class Request extends Message {
            private String username;

            public Request(String username) {
                this.username = username;
            }

            public String getUsername() {
                return username;
            }
        }
        public static class Response extends Message {
            private boolean status;

            public Response(boolean status) {
                this.status = status;
            }

            public boolean getStatus() {
                return status;
            }
        }
    }
    /* Player Leave Method */
    public static class PlayerLeave {
        public static class Request extends Message {
            private int playerId;
            private int tableId;

            public Request(int playerId, int tableId) {
                this.playerId = playerId;
                this.tableId = tableId;
            }

            public int getPlayerId() {  // added getPlayerId method
                return playerId;
            }

            public int getTableId() {   // added getTableId method
                return tableId;
            }
        }
        public static class Response extends Message {
            private boolean status;

            public Response(boolean status) {
                this.status = status;
            }

            public boolean getStatus() {    // added getStatus method
                return status;
            }
        }
    }
    /* Dealer Leave Method */
    public static class DealerLeave {
        public static class Request extends Message {
            private int dealerId;
            private int tableId;

            public Request(int dealerId, int tableId) {
                this.dealerId = dealerId;
                this.tableId = tableId;
            }

            public int getDealerId() {  // added getDealerId method
                return dealerId;
            }

            public int getTableId() {   // added getTableId method
                return tableId;
            }
        }
        public static class Response extends Message {
            private boolean status;

            public Response(boolean status) {
                this.status = status;
            }

            public boolean getStatus() {    // added getStatus method
                return status;
            }
        }
    }
    /* Player Bet Method */
    public static class Bet {
        public static class Request extends Message {
            public Request() {}
        }

        public static class Response extends Message {
            public final int amount;

            public Response(int amount) {
                this.amount = amount;
            }
            public int getAmount(){
                return this.amount;
            }
        }
    }
}
