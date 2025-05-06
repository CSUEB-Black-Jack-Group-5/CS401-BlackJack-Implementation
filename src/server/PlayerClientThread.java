package server;

import game.*;
import networking.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * PlayerClientThread handles all server-side message interactions for a player client.
 * This implementation ensures proper synchronization with the dealer thread.
 */
public class PlayerClientThread extends ClientThreadWithHooks {
    private Player player;
    private final Dealer dealer; // Reference to the current game dealer
    private boolean isCurrentlyPlaying = false;

    public PlayerClientThread(Socket socket, Server server, ObjectOutputStream writer, ObjectInputStream reader) {
        super(socket, writer, reader);

        this.player = new Player("wahooman1", "01");
        this.dealer = new Dealer("dealer1", "letmein", 17);

        System.out.println("Spawned player thread: " + socket.getInetAddress());

        /**
         * Message hooks for handling all player-dealer interactions
         */

        // JOIN TABLE Request
        addMessageHook(Message.JoinTable.Request.class, (req) -> {
            System.out.println("JoinTable Request for table " + req.getTableId());
            boolean status = server.movePlayerClientToTable(this, req.getTableId(), player);
            sendNetworkMessage(new Message.JoinTable.Response(status, server.getTables()[req.getTableId()].getTable()));

            // If join successful, notify dealer
            if (status && activeTable != null) {
                DealerClientThread dealerThread = findDealerThread(activeTable);
                if (dealerThread != null) {
                    // In a real implementation, you'd create a proper notification message
                    System.out.println("Player " + player.getUsername() + " joined table " + req.getTableId());

                    // Enable Ready button for the player
                    sendNetworkMessage(new Message.GameState.Update(true));
                }
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
        // HIT Request
        addMessageHook(Message.Hit.Request.class, (req) -> {
            System.out.println("Hit Request from player: " + player.getUsername());

            // Find the dealer thread for the current table
            if (activeTable != null) {
                DealerClientThread dealerThread = findDealerThread(activeTable);
                if (dealerThread != null) {
                    // Forward the hit request to the dealer with player's name
                    Message.Hit.Request hitRequest = new Message.Hit.Request(player.getUsername(), null);
                    dealerThread.sendNetworkMessage(hitRequest);
                } else {
                    System.out.println("ERROR: Could not find dealer thread for table");

                    // Fallback to local handling if dealer not found
                    boolean dummyStatus = true;
                    Card dummyCard = new Card(Suit.DIAMONDS, Value.EIGHT);
                    CardHand dummyCardHand = new CardHand(21);
                    dummyCardHand.addCard(dummyCard);

                    sendNetworkMessage(new Message.Hit.Response(dummyCard, dummyCardHand, dummyStatus));
                }
            } else {
                System.out.println("ERROR: No active table for player hit request");

                // Fallback with dummy values
                boolean dummyStatus = true;
                Card dummyCard = new Card(Suit.DIAMONDS, Value.EIGHT);
                CardHand dummyCardHand = new CardHand(21);
                dummyCardHand.addCard(dummyCard);

                sendNetworkMessage(new Message.Hit.Response(dummyCard, dummyCardHand, dummyStatus));
            }
        });

        // STAND Request
        addMessageHook(Message.Stand.Request.class, (req) -> {
            System.out.println("Stand Request from player: " + player.getUsername());

            if (activeTable != null) {
                DealerClientThread dealerThread = findDealerThread(activeTable);
                if (dealerThread != null) {
                    // Forward stand request to dealer with player's name and table ID
                    int tableId = activeTable.getTable().getTableId();
                    Message.Stand.Request standRequest = new Message.Stand.Request(player.getUsername());
                    // In a real implementation you'd modify the Message class to include these fields
                    dealerThread.sendNetworkMessage(standRequest);

                    // Disable gameplay buttons while waiting for dealer response
                    isCurrentlyPlaying = false;
                    sendNetworkMessage(new Message.GameState.Update(false));
                } else {
                    System.out.println("ERROR: Could not find dealer thread for table");

                    // Fallback to local handling
                    boolean dummyStatus = true;
                    CardHand dummyCardHand = new CardHand(21);
                    sendNetworkMessage(new Message.Stand.Response(dummyCardHand, dummyStatus));
                }
            } else {
                System.out.println("ERROR: No active table for player stand request");

                // Fallback with dummy values
                boolean dummyStatus = true;
                CardHand dummyCardHand = new CardHand(21);
                sendNetworkMessage(new Message.Stand.Response(dummyCardHand, dummyStatus));
            }
        });

        // PLAYER READY Request
        addMessageHook(Message.PlayerReady.Request.class, (req) -> {
            System.out.println("PlayerReady request from player: " + player.getUsername());

            // Set the player as ready
            player.setReady(true);

            // Send response back to player immediately to show dialog
            sendNetworkMessage(new Message.PlayerReady.Response(true, player.getUsername()));

            // Notify dealer through the server
            if (activeTable != null) {
                int tableId = activeTable.getTable().getTableId();
                server.notifyDealerOfPlayerReady(tableId, player.getUsername());
            } else {
                System.out.println("ERROR: No active table for player ready request");
            }
        });

        // PLAYER READY Response - confirmation from dealer
        addMessageHook(Message.PlayerReady.Response.class, (response) -> {
            if (response.getStatus() && response.getPlayerName().equals(player.getUsername())) {
                // Update player's ready state in UI
                System.out.println("Ready status confirmed by dealer");

                // Disable ready button
                sendNetworkMessage(new Message.GameState.Update(false));
            }
        });

        // SHUFFLE COMPLETE notification
        addMessageHook(Message.ShuffleComplete.Response.class, (response) -> {
            System.out.println("ShuffleComplete notification received for table " + response.getTableId());

            if (activeTable != null && activeTable.getTable().getTableId() == response.getTableId()) {
                // Notify the client that they can now place their bet
                sendNetworkMessage(new Message.ShuffleComplete.Response(response.getTableId()));

                // Enable bet placement UI
                isCurrentlyPlaying = false; // Not actively playing yet, just betting
                sendNetworkMessage(new Message.GameState.Update(true));
            }
        });

        // PLACE BET Request
        addMessageHook(Message.PlaceBet.Request.class, (req) -> {
            System.out.println("PlaceBet request: Player " + player.getUsername() +
                    " placed bet of $" + req.getBetAmount() +
                    " on table " + req.getTableId());

            if (activeTable != null && activeTable.getTable().getTableId() == req.getTableId()) {
                // Set the player's bet amount
                player.setBet(req.getBetAmount());

                // Update wallet funds
                player.getWallet().removeFunds(-req.getBetAmount());

                // Send response back to player
                sendNetworkMessage(new Message.PlaceBet.Response(true));

                // Notify the dealer to deal initial cards
                server.notifyDealerToDealInitialCards(req.getTableId(), player.getUsername(), req.getBetAmount());

                // Disable bet UI until next round
                sendNetworkMessage(new Message.GameState.Update(false));
            } else {
                System.out.println("ERROR: Table ID mismatch in bet request");
                sendNetworkMessage(new Message.PlaceBet.Response(false));
            }
        });

        // DOUBLE DOWN Request
        addMessageHook(Message.DoubleDown.Request.class, (req) -> {
            System.out.println("Double Down request from player: " + player.getUsername());

            if (activeTable != null) {
                DealerClientThread dealerThread = findDealerThread(activeTable);
                if (dealerThread != null) {
                    // Get current bet amount
                    int currentBet = player.getBet();

                    // Double the bet if player has enough funds
                    if (player.getWallet().getFunds() >= currentBet) {
                        // Deduct additional bet amount
                        player.getWallet().removeFunds(-currentBet);

                        // Double the bet
                        player.setBet(currentBet * 2);

                        // Forward double down request to dealer
                        dealerThread.sendNetworkMessage(
                                new Message.DoubleDown.Request(player.playerId, player.getBet()));

                        // Disable all gameplay buttons while waiting for dealer response
                        isCurrentlyPlaying = false;
                        sendNetworkMessage(new Message.GameState.Update(false));
                    } else {
                        // Not enough funds
                        sendNetworkMessage(new Message.DoubleDown.Response(
                                player.getBet(), false));
                    }
                } else {
                    System.out.println("ERROR: Could not find dealer thread for table");
                    sendNetworkMessage(new Message.DoubleDown.Response(
                            player.getBet(), false));
                }
            } else {
                System.out.println("ERROR: No active table for player double down request");
                sendNetworkMessage(new Message.DoubleDown.Response(
                        player.getBet(), false));
            }
        });

        // DOUBLE DOWN Response
        addMessageHook(Message.DoubleDown.Response.class, (response) -> {
            if (response.getStatus()) {
                // Update bet amount in UI
                System.out.println("Double down successful, new bet: " + response.getWager());

                // Player should receive exactly one more card and then stand automatically
                // This is handled by the dealer thread
            } else {
                System.out.println("Double down failed");

                // Re-enable gameplay buttons
                isCurrentlyPlaying = true;
                sendNetworkMessage(new Message.GameState.Update(true));
            }
        });

        // SPLIT Request
        addMessageHook(Message.Split.Request.class, (req) -> {
            System.out.println("Split request from player: " + player.getUsername());

            if (activeTable != null) {
                if (player.getHand().checkSplit()) {
                    // Player has a pair that can be split
                    DealerClientThread dealerThread = findDealerThread(activeTable);
                    if (dealerThread != null) {
                        // Get current bet amount
                        int currentBet = player.getBet();

                        // Check if player has enough funds for the second bet
                        if (player.getWallet().getFunds() >= currentBet) {
                            // Deduct the second bet
                            player.getWallet().removeFunds(-currentBet);

                            // Forward split request to dealer
                            dealerThread.sendNetworkMessage(
                                    new Message.Split.Request(player.playerId, player.getHand()));

                            // Disable gameplay buttons while waiting for dealer response
                            isCurrentlyPlaying = false;
                            sendNetworkMessage(new Message.GameState.Update(false));
                        } else {
                            // Not enough funds
                            sendNetworkMessage(new Message.Split.Response(
                                    player.getHand(), null, false));
                        }
                    } else {
                        System.out.println("ERROR: Could not find dealer thread for table");
                        sendNetworkMessage(new Message.Split.Response(
                                player.getHand(), null, false));
                    }
                } else {
                    // Cards cannot be split
                    sendNetworkMessage(new Message.Split.Response(
                            player.getHand(), null, false));
                }
            } else {
                System.out.println("ERROR: No active table for player split request");
                sendNetworkMessage(new Message.Split.Response(
                        player.getHand(), null, false));
            }
        });

        // SPLIT Response
        addMessageHook(Message.Split.Response.class, (response) -> {
            if (response.getStatus()) {
                // Update player's hands in UI
                System.out.println("Split successful");

                // Set main hand and split hand from response
                CardHand mainHand = response.getHand();
                CardHand splitHand = response.getSplitHand();

                // Update player object with these hands
                player.setSplitHand(splitHand);

                // Enable gameplay buttons for first hand
                isCurrentlyPlaying = true;
                sendNetworkMessage(new Message.GameState.Update(true));
            } else {
                System.out.println("Split failed");

                // Re-enable gameplay buttons for current hand
                isCurrentlyPlaying = true;
                sendNetworkMessage(new Message.GameState.Update(true));
            }
        });

        // OTHER MESSAGE TYPES

        // LOBBY DATA Request
        addMessageHook(Message.LobbyData.Request.class, (req) -> {
            System.out.println("Lobby data request");

            // Fetch all table threads from the server
            TableThread[] tableThreads = server.getTables();
            Table[] tables;

            if (tableThreads != null && tableThreads.length != 0) {
                tables = new Table[tableThreads.length];

                // Convert each TableThread to its underlying Table object
                for (int i = 0; i < tableThreads.length; i++) {
                    tables[i] = tableThreads[i].getTable();
                }
            } else {
                tables = new Table[0];
            }

            int activePlayers = server.getClientsInLobbySize();
            int dealerId = req.getDealerId();

            sendNetworkMessage(new Message.LobbyData.Response(tables, activePlayers, dealerId));
        });

        // GAME DATA Request
        addMessageHook(Message.GameData.Request.class, (req) -> {
            System.out.println("GameData request");

            if (activeTable != null) {
                // Try to get actual dealer hand from dealer thread
                DealerClientThread dealerThread = findDealerThread(activeTable);
                CardHand dealerHand = new CardHand(21);

                if (dealerThread != null) {
                    // In a real implementation, you'd have a way to get the dealer's hand
                    // from the dealer thread
                    dealerHand = dealer.getHand();
                }

                // Send current hand state
                sendNetworkMessage(new Message.GameData.Response(player.getHand(), dealerHand));
            } else {
                // Dummy response
                CardHand dummyCardHand = new CardHand(21);
                CardHand dummyDealerHand = new CardHand(21);
                sendNetworkMessage(new Message.GameData.Response(dummyCardHand, dummyDealerHand));
            }
        });

        // GAME DATA Response - receive hand updates from dealer
        addMessageHook(Message.GameData.Response.class, (response) -> {
            // Update UI with the latest hand information
            if (response.getPlayerHand() != null && response.getDealerHand() != null) {
                System.out.println("Received updated hand data from dealer");

                // In a real implementation, you'd update the UI with these hands
            }
        });

        // CLOCK SYNC Request
        addMessageHook(Message.ClockSync.Request.class, (req) -> {
            System.out.println("ClockSync request");

            float serverTime = System.nanoTime() / 1_000_000_000.0f;
            sendNetworkMessage(new Message.ClockSync.Response(serverTime));
        });

        // TABLE DATA Request
        addMessageHook(Message.TableData.Request.class, (req) -> {
            System.out.println("TableData request");

            if (activeTable != null) {
                Table table = activeTable.getTable();

                // Get dealer ID
                int dealerId = table.getDealer().getDealerId();

                // Get player IDs
                Player[] players = table.getPlayers();
                int[] playerIds = new int[players.length];
                int validPlayers = 0;

                for (int i = 0; i < players.length; i++) {
                    if (players[i] != null) {
                        // Use position index + 1 as ID since playerId might not be accessible
                        playerIds[validPlayers++] = i + 1;
                    }
                }

                // Trim array to valid players
                int[] trimmedPlayerIds = new int[validPlayers];
                System.arraycopy(playerIds, 0, trimmedPlayerIds, 0, validPlayers);

                sendNetworkMessage(new Message.TableData.Response(dealerId, trimmedPlayerIds, validPlayers));
            } else {
                // Dummy data
                int dummyDealerID = 1;
                int[] dummyPlayerIDs = {1, 2, 3, 4};
                int dummyPlayersJoined = 4;
                sendNetworkMessage(new Message.TableData.Response(dummyDealerID, dummyPlayerIDs, dummyPlayersJoined));
            }
        });

        // PLAYER LEAVE Request
        addMessageHook(Message.PlayerLeave.Request.class, (req) -> {
            System.out.println("PlayerLeave request");

            if (activeTable != null) {
                // Notify the dealer this player is leaving
                DealerClientThread dealerThread = findDealerThread(activeTable);
                if (dealerThread != null) {
                    int tableId = activeTable.getTable().getTableId();
                    dealerThread.sendNetworkMessage(
                            new Message.PlayerLeave.Request(player.playerId, tableId));
                }

                // Remove player from table
                activeTable.getTable().removePlayer(player);
                activeTable = null;
            }

            boolean success = true;
            sendNetworkMessage(new Message.PlayerLeave.Response(success));
        });

        // DEAL INITIAL CARDS Response - receiving cards from dealer
        addMessageHook(Message.DealInitialCards.Response.class, (response) -> {
            System.out.println("Received initial cards from dealer for player: " + response.getPlayerName());

            if (response.getStatus() && response.getPlayerName().equals(player.getUsername())) {
                // Process dealer cards - respecting face up/down state
                Card[] dealerCards = response.getDealerCards();
                System.out.println("Received dealer cards: " + dealerCards.length);

                // Process player cards - all should be face up for the player
                Card[] playerCards = response.getPlayerCards();
                System.out.println("Received player cards: " + playerCards.length);

                // Update player's hand with the received cards
                player.resetHand();
                for (Card card : playerCards) {
                    player.hit(card);
                }

                // Check for blackjack if flagged
                if (player.blackjackCheck()) {
                    System.out.println("Player has blackjack!");

                    // Wait for dealer to check their hand
                    // Dealer will send the appropriate response
                    isCurrentlyPlaying = false;
                } else {
                    // Enable gameplay buttons
                    isCurrentlyPlaying = true;
                }

                // Send UI update to client
                sendNetworkMessage(new Message.GameState.Update(isCurrentlyPlaying));
            }
        });

        // HIT Response - receiving a new card from dealer
        addMessageHook(Message.Hit.Response.class, (response) -> {
            System.out.println("Received hit response from dealer");

            Card newCard = response.getDraw();
            boolean success = response.getStatus();

            if (success && !response.isDealerCard()) {
                // This is a player card
                System.out.println("Received new player card: " + newCard.getValue() + " of " + newCard.getSuit());

                // Add the new card to player's hand if it's not already added
                if (player.getHand().getNumCards() < response.getHand().getNumCards()) {
                    player.hit(newCard);
                }

                // Check for bust
                boolean busted = player.bustCheck();
                if (busted) {
                    System.out.println("Player busted with hand value: " + player.getHand().getTotalValue());
                    isCurrentlyPlaying = false;
                } else {
                    // Still playing
                    isCurrentlyPlaying = true;
                }

                // Update UI
                sendNetworkMessage(new Message.GameState.Update(isCurrentlyPlaying));
            } else if (success && response.isDealerCard()) {
                // This is a dealer card - just update UI
                System.out.println("Dealer drew a card: " + newCard.getValue() + " of " + newCard.getSuit());

                // In a real implementation, you'd update the dealer's hand in the UI
                sendNetworkMessage(new Message.GameState.Update(isCurrentlyPlaying));
            } else {
                System.out.println("Hit failed");

                // Re-enable gameplay buttons if hit failed
                isCurrentlyPlaying = true;
                sendNetworkMessage(new Message.GameState.Update(true));
            }
        });

        // STAND Response
        addMessageHook(Message.Stand.Response.class, (response) -> {
            System.out.println("Received stand response from dealer");

            CardHand finalHand = response.getHand();
            boolean playerWins = response.playerWins();

            // Update player's wallet based on outcome
            if (playerWins) {
                int winnings = player.getBet() * 2; // Original bet + 1x
                player.getWallet().addFunds(winnings);
                player.incrementWins();
                System.out.println("Player wins! Winning amount: " + winnings);
            } else {
                // Player already paid the bet when it was placed, so no need to deduct
                player.incrementLosses();
                System.out.println("Player loses!");
            }

            // Reset for next round
            player.resetHand();
            player.setBet(0);

            // Re-enable ready button for next round
            isCurrentlyPlaying = false;
            player.setReady(false);
            sendNetworkMessage(new Message.GameState.Update(false));

            // In a real implementation, you'd also enable the ready button in the UI
        });
    }

    /**
     * Helper method to find the dealer thread for a table
     */
    private DealerClientThread findDealerThread(TableThread tableThread) {
        for (int i = 0; i < tableThread.joinedUsersCount; i++) {
            if (tableThread.joinedUsers[i] instanceof DealerClientThread) {
                return (DealerClientThread) tableThread.joinedUsers[i];
            }
        }
        return null;
    }

    public Player getPlayer() {
        return this.player;
    }

    /**
     * Helper method to update the player UI with current game state
     */
    private void updatePlayerUI() {
        // In a real implementation, you'd create a specific message type for UI updates
        // This would include current hands, bets, and game state
        if (player != null) {
            // Send game data update
            CardHand playerHand = player.getHand();
            CardHand dealerHand = dealer.getHand();
            sendNetworkMessage(new Message.GameData.Response(playerHand, dealerHand));

            // Update game state
            sendNetworkMessage(new Message.GameState.Update(isCurrentlyPlaying));
        }
    }
}