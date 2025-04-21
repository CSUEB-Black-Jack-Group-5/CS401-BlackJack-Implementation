package networking;

import java.io.Serializable;

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
            private AccountType type;       // this doesn't exist yet

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
    /* Hit Method */    // not properly UMLed yet
    public static class Hit {
        public static class Request extends Message {}
        public static class Response extends Message {}
    }
    /* Stand Method */  // not properly UMLed yet
    public static class Stand {
        public static class Request extends Message {}
        public static class Response extends Message {}
    }
    /* Leave Method */  // not properly UMLed yet
    public static class Leave {
        public static class Request extends Message {}
        public static class Response extends Message {}
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
    /* Table Data Method */
    public static class TableData {
        public static class Request extends Message {}
        public static class Response extends Message {}
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
}
