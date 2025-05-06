package networking;

import java.io.Serializable;
import game.Card;
import game.CardHand;
import game.Shoe;
import game.Table;

/**
 * Message class containing all message types for the Blackjack client-server communication.
 * This implementation provides full support for dealer-player synchronization.
 */
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

    /* Join Table Method */
    public static class JoinTable {
        public static class Request extends Message {
            private int userId;
            private int tableId;

            public Request(int userId, int tableId) {
                this.userId = userId;
                this.tableId = tableId;
            }

            public int getUserId() {
                return userId;
            }

            public int getTableId() {
                return tableId;
            }
        }

        public static class Response extends Message {
            private boolean status;
            private Table table;

            public Response(boolean status, Table table) {
                this.status = status;
                this.table = table;
            }

            public boolean getStatus() {
                return status;
            }

            public Table getTable() {
                return table;
            }

            public int getTableId() {
                return table.getTableId();
            }

            public String getDealerUsername() {
                return table.getDealer().getUsername();
            }

            public int getPlayerCount() {
                return table.getPlayerCount();
            }

            public int getPlayerLimit() {
                return table.getPlayerLimit();
            }
        }
    }

    /* Create Table Method */
    public static class CreateTable {
        public static class Request extends Message {
            private String dealerId;

            public Request(String dealerId) {
                this.dealerId = dealerId;
            }

            public String getDealerId() {
                return dealerId;
            }
        }

        public static class Response extends Message {
            private boolean status;
            private Table table;

            public Response(boolean status, Table table) {
                this.status = status;
                this.table = table;
            }

            public boolean getStatus() {
                return status;
            }

            public Table getTable() {
                return table;
            }

            public int getTableId() {
                return table.getTableId();
            }

            public String getDealerUsername() {
                return table.getDealer().getUsername();
            }

            public int getPlayerCount() {
                return table.getPlayerCount();
            }

            public int getPlayerLimit() {
                return table.getPlayerLimit();
            }
        }
    }

    /* Deal Method */
    public static class Deal {
        public static class Request extends Message {
            private int tableId;

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

    /* Hit Method */
    public static class Hit {
        public static class Request extends Message {
            private String playerName;
            private Shoe deck;
            private int tableId;

            public Request(String playerName, Shoe deck) {
                this.playerName = playerName;
                this.deck = deck;
                this.tableId = 0; // Default value
            }

            public Request(String playerName, Shoe deck, int tableId) {
                this.playerName = playerName;
                this.deck = deck;
                this.tableId = tableId;
            }

            public String getPlayerName() {
                return playerName;
            }

            public Shoe getDeck() {
                return deck;
            }

            public int getTableId() {
                return tableId;
            }
        }

        public static class Response extends Message {
            private Card draw;
            private CardHand hand;
            private boolean status;
            private boolean dealerCard;

            public Response(Card draw, CardHand hand, boolean status) {
                this.draw = draw;
                this.hand = hand;
                this.status = status;
                this.dealerCard = false;
            }

            public Response(Card draw, CardHand hand, boolean status, boolean dealerCard) {
                this.draw = draw;
                this.hand = hand;
                this.status = status;
                this.dealerCard = dealerCard;
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

            public boolean isDealerCard() {
                return dealerCard;
            }
        }
    }

    /* Stand Method */
    public static class Stand {
        public static class Request extends Message {
            private String playerName;
            private int tableId;

            public Request(String playerName) {
                this.playerName = playerName;
                this.tableId = 0; // Default value
            }

            public Request(String playerName, int tableId) {
                this.playerName = playerName;
                this.tableId = tableId;
            }

            public Request(int playerId) {
                this.playerName = "Player " + playerId; // Fallback if only ID provided
                this.tableId = 0;
            }

            public String getPlayerName() {
                return playerName;
            }

            public int getTableId() {
                return tableId;
            }
        }

        public static class Response extends Message {
            private CardHand hand;
            private boolean playerWins;

            public Response(CardHand hand, boolean playerWins) {
                this.hand = hand;
                this.playerWins = playerWins;
            }

            public CardHand getHand() {
                return hand;
            }

            public boolean playerWins() {
                return playerWins;
            }
        }
    }

    /* Split Method */
    public static class Split {
        public static class Request extends Message {
            private int playerId;
            private CardHand hand;
            private String playerName;
            private int tableId;

            public Request(int playerId, CardHand hand) {
                this.playerId = playerId;
                this.hand = hand;
                this.playerName = null;
                this.tableId = 0;
            }

            public Request(String playerName, CardHand hand, int tableId) {
                this.playerName = playerName;
                this.hand = hand;
                this.tableId = tableId;
                this.playerId = 0; // Not using playerId in this constructor
            }

            public int getPlayerId() {
                return playerId;
            }

            public String getPlayerName() {
                return playerName;
            }

            public CardHand getHand() {
                return hand;
            }

            public int getTableId() {
                return tableId;
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

    /* Double Down Method */
    public static class DoubleDown {
        public static class Request extends Message {
            private int playerId;
            private int wager;
            private String playerName;
            private int tableId;

            public Request(int playerId, int wager) {
                this.playerId = playerId;
                this.wager = wager;
                this.playerName = null;
                this.tableId = 0;
            }

            public Request(String playerName, int wager, int tableId) {
                this.playerName = playerName;
                this.wager = wager;
                this.tableId = tableId;
                this.playerId = 0; // Not using playerId in this constructor
            }

            public int getPlayerId() {
                return playerId;
            }

            public String getPlayerName() {
                return playerName;
            }

            public int getWager() {
                return wager;
            }

            public int getTableId() {
                return tableId;
            }
        }

        public static class Response extends Message {
            private int wager;
            private boolean status;
            private Card drawnCard;

            public Response(int wager, boolean status) {
                this.wager = wager;
                this.status = status;
                this.drawnCard = null;
            }

            public Response(int wager, boolean status, Card drawnCard) {
                this.wager = wager;
                this.status = status;
                this.drawnCard = drawnCard;
            }

            public int getWager() {
                return wager;
            }

            public boolean getStatus() {
                return status;
            }

            public Card getDrawnCard() {
                return drawnCard;
            }
        }
    }

    /* Lobby Data Method */
    public static class LobbyData {
        public static class Request extends Message {
            private int dealerId;
            private AccountType type;

            public Request(int dealerId, AccountType type) {
                this.dealerId = dealerId;
                this.type = type;
            }

            public int getDealerId() {
                return dealerId;
            }

            public AccountType getType() {
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
        public static class Request extends Message {
            private int dealerId;

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
            private String playerName;
            private int tableId;

            public Request(int playerId) {
                this.playerId = playerId;
                this.playerName = null;
                this.tableId = 0;
            }

            public Request(String playerName, int tableId) {
                this.playerName = playerName;
                this.tableId = tableId;
                this.playerId = 0;
            }

            public int getPlayerId() {
                return playerId;
            }

            public String getPlayerName() {
                return playerName;
            }

            public int getTableId() {
                return tableId;
            }
        }

        public static class Response extends Message {
            private CardHand playerHand;
            private CardHand dealerHand;

            public Response(CardHand playerHand, CardHand dealerHand) {
                this.playerHand = playerHand;
                this.dealerHand = dealerHand;
            }

            public CardHand getPlayerHand() {
                return playerHand;
            }

            public CardHand getDealerHand() {
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
            private String playerName;
            private int tableId;

            public Request(String playerName, int tableId) {
                this.playerName = playerName;
                this.tableId = tableId;
            }

            public String getPlayerName() {
                return playerName;
            }

            public int getTableId() {
                return tableId;
            }
        }

        public static class Response extends Message {
            private boolean status;
            private String playerName;

            public Response(boolean status) {
                this.status = status;
                this.playerName = null;
            }

            public Response(boolean status, String playerName) {
                this.status = status;
                this.playerName = playerName;
            }

            public boolean getStatus() {
                return status;
            }

            public String getPlayerName() {
                return playerName;
            }
        }
    }

    /* Player Leave Method */
    public static class PlayerLeave {
        public static class Request extends Message {
            private int playerId;
            private int tableId;
            private String playerName;

            public Request(int playerId, int tableId) {
                this.playerId = playerId;
                this.tableId = tableId;
                this.playerName = null;
            }

            public Request(String playerName, int tableId) {
                this.playerName = playerName;
                this.tableId = tableId;
                this.playerId = 0;
            }

            public int getPlayerId() {
                return playerId;
            }

            public String getPlayerName() {
                return playerName;
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

    /* Dealer Leave Method */
    public static class DealerLeave {
        public static class Request extends Message {
            private int dealerId;
            private int tableId;

            public Request(int dealerId, int tableId) {
                this.dealerId = dealerId;
                this.tableId = tableId;
            }

            public int getDealerId() {
                return dealerId;
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

    /* Shuffle Method */
    public static class Shuffle {
        public static class Request extends Message {
            private int tableId;

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
                return status;
            }
        }
    }

    /* Shuffle Complete Method */
    public static class ShuffleComplete {
        public static class Response extends Message {
            private int tableId;

            public Response(int tableId) {
                this.tableId = tableId;
            }

            public int getTableId() {
                return tableId;
            }
        }
    }

    /* Place Bet Method */
    public static class PlaceBet {
        public static class Request extends Message {
            private int playerId;
            private int tableId;
            private int betAmount;
            private String playerName;

            public Request(int playerId, int tableId, int betAmount) {
                this.playerId = playerId;
                this.tableId = tableId;
                this.betAmount = betAmount;
                this.playerName = null;
            }

            public Request(String playerName, int tableId, int betAmount) {
                this.playerName = playerName;
                this.tableId = tableId;
                this.betAmount = betAmount;
                this.playerId = 0;
            }

            public int getPlayerId() {
                return playerId;
            }

            public String getPlayerName() {
                return playerName;
            }

            public int getTableId() {
                return tableId;
            }

            public int getBetAmount() {
                return betAmount;
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

    /* Deal Initial Cards Method */
    public static class DealInitialCards {
        public static class Request extends Message {
            private int tableId;
            private String playerName;
            private int betAmount;

            public Request(int tableId, String playerName, int betAmount) {
                this.tableId = tableId;
                this.playerName = playerName;
                this.betAmount = betAmount;
            }

            public int getTableId() {
                return tableId;
            }

            public String getPlayerName() {
                return playerName;
            }

            public int getBetAmount() {
                return betAmount;
            }
        }

        public static class Response extends Message {
            private boolean status;
            private Card[] dealerCards;
            private Card[] playerCards;
            private String playerName;
            private boolean checkForBlackjack;

            public Response(boolean status, Card[] dealerCards, Card[] playerCards, String playerName) {
                this.status = status;
                this.dealerCards = dealerCards;
                this.playerCards = playerCards;
                this.playerName = playerName;
                this.checkForBlackjack = true;
            }

            public Response(boolean status, Card[] dealerCards, Card[] playerCards,
                            String playerName, boolean checkForBlackjack) {
                this.status = status;
                this.dealerCards = dealerCards;
                this.playerCards = playerCards;
                this.playerName = playerName;
                this.checkForBlackjack = checkForBlackjack;
            }

            public boolean getStatus() {
                return status;
            }

            public Card[] getDealerCards() {
                return dealerCards;
            }

            public Card[] getPlayerCards() {
                return playerCards;
            }

            public String getPlayerName() {
                return playerName;
            }

            public boolean shouldCheckForBlackjack() {
                return checkForBlackjack;
            }
        }
    }

    /* Game State Method */
    public static class GameState {
        public static class Update extends Message {
            private boolean dealerTurn;
            private boolean playerTurn;
            private boolean roundComplete;
            private String resultMessage;

            public Update(boolean dealerTurn) {
                this.dealerTurn = dealerTurn;
                this.playerTurn = !dealerTurn;
                this.roundComplete = false;
                this.resultMessage = null;
            }

            public Update(boolean dealerTurn, boolean playerTurn, boolean roundComplete) {
                this.dealerTurn = dealerTurn;
                this.playerTurn = playerTurn;
                this.roundComplete = roundComplete;
                this.resultMessage = null;
            }

            public Update(boolean dealerTurn, boolean playerTurn, boolean roundComplete, String resultMessage) {
                this.dealerTurn = dealerTurn;
                this.playerTurn = playerTurn;
                this.roundComplete = roundComplete;
                this.resultMessage = resultMessage;
            }

            public boolean isDealerTurn() {
                return dealerTurn;
            }

            public boolean isPlayerTurn() {
                return playerTurn;
            }

            public boolean isRoundComplete() {
                return roundComplete;
            }

            public String getResultMessage() {
                return resultMessage;
            }
        }
    }

    /* Dealer Reveal Method */
    public static class DealerReveal {
        public static class Response extends Message {
            private Card[] dealerCards;
            private int dealerHandValue;
            private int tableId;

            public Response(Card[] dealerCards, int dealerHandValue, int tableId) {
                this.dealerCards = dealerCards;
                this.dealerHandValue = dealerHandValue;
                this.tableId = tableId;
            }

            public Card[] getDealerCards() {
                return dealerCards;
            }

            public int getDealerHandValue() {
                return dealerHandValue;
            }

            public int getTableId() {
                return tableId;
            }
        }
    }

    /* Game Result Method */
    public static class GameResult {
        public static class Response extends Message {
            private boolean playerWins;
            private boolean push;
            private String resultMessage;
            private int winnings;
            private int playerHandValue;
            private int dealerHandValue;
            private String playerName;
            private int tableId;

            public Response(boolean playerWins, boolean push, String resultMessage,
                            int winnings, int playerHandValue, int dealerHandValue,
                            String playerName, int tableId) {
                this.playerWins = playerWins;
                this.push = push;
                this.resultMessage = resultMessage;
                this.winnings = winnings;
                this.playerHandValue = playerHandValue;
                this.dealerHandValue = dealerHandValue;
                this.playerName = playerName;
                this.tableId = tableId;
            }

            public boolean playerWins() {
                return playerWins;
            }

            public boolean isPush() {
                return push;
            }

            public String getResultMessage() {
                return resultMessage;
            }

            public int getWinnings() {
                return winnings;
            }

            public int getPlayerHandValue() {
                return playerHandValue;
            }

            public int getDealerHandValue() {
                return dealerHandValue;
            }

            public String getPlayerName() {
                return playerName;
            }

            public int getTableId() {
                return tableId;
            }
        }
    }

    /* Insurance Method */
    public static class Insurance {
        public static class Request extends Message {
            private String playerName;
            private int tableId;
            private int insuranceAmount;
            private boolean takeInsurance;

            public Request(String playerName, int tableId, int insuranceAmount, boolean takeInsurance) {
                this.playerName = playerName;
                this.tableId = tableId;
                this.insuranceAmount = insuranceAmount;
                this.takeInsurance = takeInsurance;
            }

            public String getPlayerName() {
                return playerName;
            }

            public int getTableId() {
                return tableId;
            }

            public int getInsuranceAmount() {
                return insuranceAmount;
            }

            public boolean takeInsurance() {
                return takeInsurance;
            }
        }

        public static class Response extends Message {
            private boolean dealerHasBlackjack;
            private boolean insurancePaid;
            private int insurancePayoutAmount;

            public Response(boolean dealerHasBlackjack, boolean insurancePaid, int insurancePayoutAmount) {
                this.dealerHasBlackjack = dealerHasBlackjack;
                this.insurancePaid = insurancePaid;
                this.insurancePayoutAmount = insurancePayoutAmount;
            }

            public boolean dealerHasBlackjack() {
                return dealerHasBlackjack;
            }

            public boolean isInsurancePaid() {
                return insurancePaid;
            }

            public int getInsurancePayoutAmount() {
                return insurancePayoutAmount;
            }
        }
    }

    /* Surrender Method */
    public static class Surrender {
        public static class Request extends Message {
            private String playerName;
            private int tableId;

            public Request(String playerName, int tableId) {
                this.playerName = playerName;
                this.tableId = tableId;
            }

            public String getPlayerName() {
                return playerName;
            }

            public int getTableId() {
                return tableId;
            }
        }

        public static class Response extends Message {
            private boolean status;
            private int refundAmount;

            public Response(boolean status, int refundAmount) {
                this.status = status;
                this.refundAmount = refundAmount;
            }

            public boolean getStatus() {
                return status;
            }

            public int getRefundAmount() {
                return refundAmount;
            }
        }
    }
}