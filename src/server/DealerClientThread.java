package server;

import game.*;
import networking.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * DealerClientThread handles all server-side message interactions for a dealer client.
 * This improved version ensures proper synchronization between dealer and player views.
 */
public class DealerClientThread extends ClientThreadWithHooks {
    private final Server serverRef;
    private final Dealer dealer;
    private Player currentPlayer;

    // Store player hands for comparison (tableId -> playerName -> hand)
    private Map<Integer, Map<String, CardHand>> playerHandsMap = new HashMap<>();

    // Store dealer cards for each table
    private Map<Integer, CardHand> dealerHandsMap = new HashMap<>();

    public DealerClientThread(Socket socket, Server serverRef, ObjectOutputStream writer, ObjectInputStream reader) {
        super(socket, writer, reader);
        this.serverRef = serverRef;
        this.dealer = new Dealer("dealer1", "letmein", 17);  // Temporary dealer login

        System.out.println("Spawned dealer thread");

        /**
         * Handles CreateTable Request — spawns a new table and sends a basic response.
         */
        addMessageHook(Message.CreateTable.Request.class, (req) -> {
            System.out.println("CreateTable Request from dealer: " + dealer.getUsername());

            // Create a new table on the server
            TableThread table = serverRef.spawnTable();
            table.getTable().setDealer(dealer);

            // This is critical - add the dealer to the table's joined users
            table.addClientToTable(this);

            // Set this table as the dealer's active table
            this.setActiveTable(table);

            // Initialize hand tracking for this table
            int tableId = table.getTable().getTableId();
            playerHandsMap.put(tableId, new HashMap<>());
            dealerHandsMap.put(tableId, new CardHand(21));

            System.out.println("Table created with ID: " + tableId + ", dealer: " + dealer.getUsername());

            sendNetworkMessage(new Message.CreateTable.Response(true, table.getTable()));
        });

        /**
         * Handles Deal Request — deals two cards to the current player.
         */
        addMessageHook(Message.Deal.Request.class, (req) -> {
            System.out.println("Deal Request");

            if (currentPlayer != null) {
                dealer.dealCard(currentPlayer);
                dealer.dealCard(currentPlayer);

                // Send updated hand info back to the dealer client
                sendNetworkMessage(new Message.GameData.Response(currentPlayer.getHand(), dealer.getHand()));
            } else {
                System.err.println("No current player set for this dealer.");
            }

            boolean dummyStatus = true;
            sendNetworkMessage(new Message.Deal.Response(dummyStatus));
        });

        // Handle STAND Request with proper synchronization
        addMessageHook(Message.Stand.Request.class, (req) -> {
            System.out.println("Stand Request");

            int tableId = req.getTableId();
            String playerName = req.getPlayerName();

            // Get the player's hand from our tracking map
            CardHand playerHand = null;
            if (playerHandsMap.containsKey(tableId) &&
                    playerHandsMap.get(tableId).containsKey(playerName)) {
                playerHand = playerHandsMap.get(tableId).get(playerName);
            } else {
                playerHand = currentPlayer != null ? currentPlayer.getHand() : new CardHand(21);
            }

            // Get dealer's hand
            CardHand dealerHand = dealerHandsMap.getOrDefault(tableId, new CardHand(21));

            // Compare values and determine winner
            int playerValue = playerHand.getTotalValue();
            int dealerValue = dealerHand.getTotalValue();

            boolean playerWins = false;
            if (playerValue <= 21) {
                // If dealer has less than 17, dealer must hit
                while (dealerValue < 17) {
                    Card newCard = dealer.dealCardToSelf();
                    dealerHand.addCard(newCard);
                    dealerValue = dealerHand.getTotalValue();
                }

                // Check if dealer busts
                if (dealerValue > 21) {
                    playerWins = true;
                } else {
                    // Compare hands if both under 21
                    playerWins = playerValue > dealerValue;
                }
            }

            // Send results to both dealer and player
            if (playerWins) {
                int winnings = currentPlayer.getBet() * 2;
                currentPlayer.getWallet().addFunds(winnings);
                currentPlayer.incrementWins();
            } else {
                currentPlayer.incrementLosses();
            }

            // Flip dealer's second card face up
            if (dealerHand.getNumCards() >= 2) {
                dealerHand.getCard(1).setFaceUp(true);
            }

            // Send stand response with full hand info
            sendNetworkMessage(new Message.Stand.Response(playerHand, playerWins));

            // Notify player of the result
            notifyPlayerOfStandResult(playerName, playerHand, dealerHand, playerWins);
        });

        // Handle LOBBY DATA Request
        addMessageHook(Message.LobbyData.Request.class, (req) -> {
            System.out.println("Lobby data request");

            // Fetch all table threads from the server
            TableThread[] tableThreads = serverRef.getTables();
            Table[] tables;

            if (tableThreads != null && tableThreads.length != 0) {
                tables = new Table[tableThreads.length];

                for (int i = 0; i < tableThreads.length; i++) {
                    tables[i] = tableThreads[i].getTable();
                }
            } else {
                tables = new Table[0];
            }

            int activePlayers = serverRef.getClientsInLobbySize();
            int dealerId = req.getDealerId();

            sendNetworkMessage(new Message.LobbyData.Response(tables, activePlayers, dealerId));
        });

        // Handle GAME DATA Request
        addMessageHook(Message.GameData.Request.class, (req) -> {
            System.out.println("GameData request");

            CardHand dealerHand = dealer.getHand();
            CardHand playerHand = currentPlayer != null ? currentPlayer.getHand() : new CardHand(21);

            sendNetworkMessage(new Message.GameData.Response(playerHand, dealerHand));
        });

        // Handle CLOCK SYNC Request
        addMessageHook(Message.ClockSync.Request.class, (req) -> {
            System.out.println("ClockSync request");

            float serverTime = System.nanoTime() / 1_000_000_000.0f;
            sendNetworkMessage(new Message.ClockSync.Response(serverTime));
        });

        // Handle TABLE DATA Request
        addMessageHook(Message.TableData.Request.class, (req) -> {
            System.out.println("TableData request");

            int tableId = 1;
            int[] playerIds = new int[]{101, 102};
            int dealerId = dealer.getDealerId();

            sendNetworkMessage(new Message.TableData.Response(dealerId, playerIds, playerIds.length));
        });

        // Handle PLAYER READY Request
        addMessageHook(Message.PlayerReady.Request.class, (req) -> {
            System.out.println("PlayerReady Request from player: " + req.getPlayerName());

            if (activeTable != null) {
                TableThread tableThread = activeTable;

                Player[] tablePlayers = tableThread.getTable().getPlayers();
                for (int i = 0; i < tableThread.getTable().getPlayerCount(); i++) {
                    if (tablePlayers[i] != null &&
                            tablePlayers[i].getUsername().equals(req.getPlayerName())) {

                        tablePlayers[i].setReady(true);
                        sendNetworkMessage(new Message.PlayerReady.Response(true, req.getPlayerName()));
                        break;
                    }
                }
            }
        });

        // Handle SHUFFLE Request
        // In DealerClientThread.java, update the Shuffle.Request handler:

        addMessageHook(Message.Shuffle.Request.class, (req) -> {
            System.out.println("Shuffle Request received for table " + req.getTableId());

            // Perform the shuffle
            dealer.shuffle();
            sendNetworkMessage(new Message.Shuffle.Response(true));

            // Find the table to broadcast shuffle completion
            boolean foundTable = false;
            TableThread[] tables = serverRef.getTables();

            for (TableThread tableThread : tables) {
                if (tableThread != null && tableThread.getTable().getTableId() == req.getTableId()) {
                    foundTable = true;
                    System.out.println("Found table " + req.getTableId() + " to broadcast shuffle completion");

                    // Create and broadcast the shuffle complete notification
                    Message.ShuffleComplete.Response notification =
                            new Message.ShuffleComplete.Response(req.getTableId());

                    // This sends the notification to all players at the table
                    tableThread.broadcastToPlayers(notification);
                    break;
                }
            }

            if (!foundTable) {
                System.out.println("ERROR: Could not find table " + req.getTableId() + " to broadcast shuffle completion");
            }
        });

        // In PlayerClientThread.java, update or add the ShuffleComplete handler:

        addMessageHook(Message.ShuffleComplete.Response.class, (response) -> {
            System.out.println("ShuffleComplete notification received for table " + response.getTableId());

            if (activeTable != null && activeTable.getTable().getTableId() == response.getTableId()) {
                // Notify the client that they can now place their bet
                sendNetworkMessage(new Message.ShuffleComplete.Response(response.getTableId()));

                // Enable bet placement UI on client side
                sendNetworkMessage(new Message.GameState.Update(true));
            }
        });

        // Handle DEAL INITIAL CARDS Request with synchronized values
        addMessageHook(Message.DealInitialCards.Request.class, (req) -> {
            System.out.println("DealInitialCards Request for player: " + req.getPlayerName() +
                    " on table " + req.getTableId() + " with bet amount: " + req.getBetAmount());

            if (activeTable != null) {
                Table table = activeTable.getTable();
                int tableId = req.getTableId();
                String playerName = req.getPlayerName();

                // Find the player who placed the bet
                Player targetPlayer = null;
                for (Player player : table.getPlayers()) {
                    if (player != null && player.getUsername().equals(playerName)) {
                        targetPlayer = player;
                        break;
                    }
                }

                if (targetPlayer != null) {
                    System.out.println("Found target player: " + targetPlayer.getUsername());

                    // Set the bet amount
                    targetPlayer.setBet(req.getBetAmount());

                    // Make sure we have a shuffled deck
                    if (dealer.getShoe().getTotalCards() < 10) {
                        dealer.shuffle();
                    }

                    // Deal two cards to the player - both face up by default
                    Card playerCard1 = dealer.dealCard(targetPlayer);
                    Card playerCard2 = dealer.dealCard(targetPlayer);
                    Card[] playerCards = {playerCard1, playerCard2};

                    // Deal two cards to the dealer - first one face up, second one face down
                    CardHand dealerHand = new CardHand(21);
                    Card dealerCard1 = dealer.dealCardToSelf();
                    dealerCard1.setFaceUp(true);
                    dealerHand.addCard(dealerCard1);

                    Card dealerCard2 = dealer.dealCardToSelf();
                    dealerCard2.setFaceUp(false);  // Second card face down
                    dealerHand.addCard(dealerCard2);

                    Card[] dealerCards = {dealerCard1, dealerCard2};

                    System.out.println("Cards dealt. Player cards: " + playerCard1.getValue() + playerCard1.getSuit() +
                            ", " + playerCard2.getValue() + playerCard2.getSuit());
                    System.out.println("Dealer cards: " + dealerCard1.getValue() + dealerCard1.getSuit() +
                            ", " + dealerCard2.getValue() + dealerCard2.getSuit());

                    // Store hands in our tracking maps for later comparison
                    if (!playerHandsMap.containsKey(tableId)) {
                        playerHandsMap.put(tableId, new HashMap<>());
                    }
                    playerHandsMap.get(tableId).put(playerName, targetPlayer.getHand());
                    dealerHandsMap.put(tableId, dealerHand);

                    // Send response to dealer client with card info
                    Message.DealInitialCards.Response dealerResponse = new Message.DealInitialCards.Response(
                            true, dealerCards, playerCards, playerName);
                    sendNetworkMessage(dealerResponse);
                    System.out.println("Sent cards info to dealer client");

                    // Notify the player about their cards
                    notifyPlayerAboutInitialCards(targetPlayer, dealerCards, playerCards);

                    // Enable appropriate UI actions for the dealer
                    sendNetworkMessage(new Message.GameState.Update(true));

                    // Check for blackjack
                    if (targetPlayer.blackjackCheck()) {
                        handleBlackjack(tableId, targetPlayer, dealerHand);
                    }
                } else {
                    System.out.println("ERROR: Could not find target player: " + req.getPlayerName());
                }
            } else {
                System.out.println("ERROR: No active table found for dealer");
            }
        });

        // Add this message hook to the class that's producing the error

        addMessageHook(Message.GameState.Update.class, (update) -> {
            System.out.println("Game state update received: dealer turn=" + update.isDealerTurn() +
                    ", player turn=" + update.isPlayerTurn());

            // Handle the game state update based on the context
            // For example, enable/disable UI elements
            boolean dealerTurn = update.isDealerTurn();
            boolean playerTurn = update.isPlayerTurn();
            boolean roundComplete = update.isRoundComplete();

            // Update UI state or game logic based on the received update
            // This will depend on whether this is being added to PlayerClientThread or DealerClientThread
        });

        // Handle HIT Request with proper synchronization
        addMessageHook(Message.Hit.Request.class, (req) -> {
            System.out.println("Hit Request from player");

            String playerName = req.getPlayerName();
            Player targetPlayer = null;

            if (activeTable != null && playerName != null) {
                for (Player player : activeTable.getTable().getPlayers()) {
                    if (player != null && player.getUsername().equals(playerName)) {
                        targetPlayer = player;
                        break;
                    }
                }
            } else {
                targetPlayer = currentPlayer;
            }

            if (targetPlayer != null) {
                // Deal a card to the player
                Card card = dealer.dealCard(targetPlayer);
                System.out.println("Dealt card to player: " + card.getValue() + " of " + card.getSuit());

                // Update our tracking maps
                int tableId = activeTable.getTable().getTableId();
                if (playerHandsMap.containsKey(tableId)) {
                    playerHandsMap.get(tableId).put(playerName, targetPlayer.getHand());
                }

                // Check if the player busted
                boolean busted = targetPlayer.bustCheck();
                CardHand playerHand = targetPlayer.getHand();

                // Send response back to player
                sendNetworkMessage(new Message.Hit.Response(
                        card, playerHand, !busted));

                // If player busted, handle game over
                if (busted) {
                    handlePlayerBust(tableId, targetPlayer);
                }

                // Broadcast this action to other players if needed
                if (activeTable != null) {
                    Message.GameState.Update notification = new Message.GameState.Update(false);
                    activeTable.broadcastToPlayers(notification);
                }
            } else {
                System.out.println("ERROR: Could not find target player");
            }
        });
    }

    /**
     * Helper method to handle player blackjack
     */
    private void handleBlackjack(int tableId, Player player, CardHand dealerHand) {
        // Check if dealer also has blackjack
        boolean dealerBlackjack = dealerHand.getNumCards() == 2 &&
                dealerHand.getTotalValue() == 21;

        if (dealerBlackjack) {
            // It's a push (tie)
            player.getWallet().addFunds(player.getBet()); // Return the bet

            // Notify player and dealer
            notifyPlayerOfResult(player, "Push! Both you and dealer have Blackjack.", 0);
        } else {
            // Player wins with blackjack (pays 3:2)
            int winnings = (int)(player.getBet() * 2.5); // Original bet + 1.5x
            player.getWallet().addFunds(winnings);
            player.incrementWins();

            // Notify player and dealer
            notifyPlayerOfResult(player, "Blackjack! You win!", winnings);
        }

        // Reveal dealer's cards
        dealerHand.getCard(1).setFaceUp(true);

        // Send updated dealer hand to player for reveal
        notifyPlayerOfDealerReveal(player.getUsername(), dealerHand);
    }

    /**
     * Helper method to handle player bust
     */
    private void handlePlayerBust(int tableId, Player player) {
        // Player loses
        player.incrementLosses();

        // No need to take the bet as it was already deducted

        // Notify the player
        notifyPlayerOfResult(player, "Bust! You lose.", 0);

        // Reveal dealer's cards
        CardHand dealerHand = dealerHandsMap.get(tableId);
        if (dealerHand != null && dealerHand.getNumCards() >= 2) {
            dealerHand.getCard(1).setFaceUp(true);

            // Send updated dealer hand to player for reveal
            notifyPlayerOfDealerReveal(player.getUsername(), dealerHand);
        }
    }

    /**
     * Helper method to notify player of dealer card reveal
     */
    private void notifyPlayerOfDealerReveal(String playerName, CardHand dealerHand) {
        if (activeTable != null) {
            for (int i = 0; i < activeTable.joinedUsersCount; i++) {
                if (activeTable.joinedUsers[i] instanceof PlayerClientThread) {
                    PlayerClientThread playerThread = (PlayerClientThread) activeTable.joinedUsers[i];
                    if (playerThread.getPlayer().getUsername().equals(playerName)) {
                        // Create dealer cards array
                        Card[] dealerCards = new Card[dealerHand.getNumCards()];
                        for (int j = 0; j < dealerHand.getNumCards(); j++) {
                            dealerCards[j] = dealerHand.getCard(j);
                        }

                        // Send dealer reveal message
                        playerThread.sendNetworkMessage(new Message.GameData.Response(
                                playerThread.getPlayer().getHand(), dealerHand));
                        break;
                    }
                }
            }
        }
    }

    /**
     * Helper method to notify player of game result
     */
    private void notifyPlayerOfResult(Player player, String resultMessage, int winnings) {
        // This would be a custom message type in a real implementation
        // For now, we'll use GameState.Update with custom fields
        if (activeTable != null) {
            for (int i = 0; i < activeTable.joinedUsersCount; i++) {
                if (activeTable.joinedUsers[i] instanceof PlayerClientThread) {
                    PlayerClientThread playerThread = (PlayerClientThread) activeTable.joinedUsers[i];
                    if (playerThread.getPlayer().getUsername().equals(player.getUsername())) {
                        // Send game result message
                        // In a real implementation, you'd create a specific message type for this
                        Message.GameState.Update resultUpdate = new Message.GameState.Update(false);
                        playerThread.sendNetworkMessage(resultUpdate);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Helper method to notify player of stand result
     */
    private void notifyPlayerOfStandResult(String playerName, CardHand playerHand,
                                           CardHand dealerHand, boolean playerWins) {
        if (activeTable != null) {
            for (int i = 0; i < activeTable.joinedUsersCount; i++) {
                if (activeTable.joinedUsers[i] instanceof PlayerClientThread) {
                    PlayerClientThread playerThread = (PlayerClientThread) activeTable.joinedUsers[i];
                    if (playerThread.getPlayer().getUsername().equals(playerName)) {
                        // Send stand result
                        playerThread.sendNetworkMessage(new Message.Stand.Response(
                                playerHand, playerWins));

                        // Also send updated dealer hand with face up cards
                        playerThread.sendNetworkMessage(new Message.GameData.Response(
                                playerHand, dealerHand));
                        break;
                    }
                }
            }
        }
    }

    /**
     * Helper method to notify player about initial cards
     */
    private synchronized void notifyPlayerAboutInitialCards(Player player, Card[] dealerCards, Card[] playerCards) {
        if (activeTable != null) {
            for (int i = 0; i < activeTable.joinedUsersCount; i++) {
                if (activeTable.joinedUsers[i] instanceof PlayerClientThread) {
                    PlayerClientThread playerThread = (PlayerClientThread) activeTable.joinedUsers[i];
                    if (playerThread.getPlayer().getUsername().equals(player.getUsername())) {
                        // Send the same card info to the player
                        playerThread.sendNetworkMessage(new Message.DealInitialCards.Response(
                                true, dealerCards, playerCards, player.getUsername()));
                        break;
                    }
                }
            }
        }
    }

    /**
     * Sets the currently active player for dealer reference.
     */
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }
}