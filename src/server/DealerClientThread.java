package server;

import networking.Message;
import game.CardHand;
import game.Dealer;
import game.Player;
import game.Table;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * DealerClientThread handles all server-side message interactions for a dealer client.
 */
public class DealerClientThread extends ClientThreadWithHooks {
    private final Server serverRef;
    private final Dealer dealer;
    private Player currentPlayer;

    /**
     * Constructor initializes the thread, registers message hooks, and prepares the dealer object.
     */

    public DealerClientThread(Socket socket, Server serverRef, ObjectOutputStream writer, ObjectInputStream reader) {
        super(socket, writer, reader);
        this.serverRef = serverRef;
        this.dealer = new Dealer("dealer1", "letmein", 17);  // Temporary dealer login

        System.out.println("Spawned dealer thread");

        /**
         * This is where the server game logic for the dealer happens.
         *
         * The original commented-out logic is preserved below for reference.
         * I also tried implementing both hooks below these comments.
         */

        // addMessageHook(Message.CreateTable.Request.class, (req) -> {
        //     System.out.println("CreateTable Request");
        //     serverRef.spawnTable();
        //     sendNetworkMessage(new Message.CreateTable.Response()); //TODO: Be more thoughtful about errors
        //     serverRef.broadcastNetworkMessage(new Message.LobbyData.Response());
        // });

        // addMessageHook(Message.Deal.Request.class, (req) -> {
        //     System.out.println("Deal Request");
        //     sendNetworkMessage(new Message.Deal.Response());
        // });

        /**
         * Handles CreateTable Request — spawns a new table and sends a basic response.
         * Right now, this just assumes the table is always created successfully.
         */
        addMessageHook(Message.CreateTable.Request.class, (req) -> {
            System.out.println("CreateTable Request");

            // Create a new table on the server
            serverRef.spawnTable();

            // Send back a dummy tableId for now (e.g., always 1)
            // Replace with actual table ID logic if needed later
            sendNetworkMessage(new Message.CreateTable.Response(true, 1));
        });



        /**
         * Handles Deal Request — deals two cards to the current player.
         * This is a basic placeholder version until full table support is added.
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
            //---------DUMMY ID -----------------
            boolean dummyStatus = true;
            sendNetworkMessage(new Message.Deal.Response(dummyStatus));
        });



        // Handle STAND Request — sends player hand only (no bust check)
        addMessageHook(Message.Stand.Request.class, (req) -> {
            System.out.println("Stand Request");

            CardHand playerHand = currentPlayer != null ? currentPlayer.getHand() : new CardHand(21);
            sendNetworkMessage(new Message.Stand.Response(playerHand, false));
        });

        // Handle LOBBY DATA Request — returns info about all tables and current activity
        addMessageHook(Message.LobbyData.Request.class, (req) -> {
            System.out.println("Lobby data request");

            // Fetch all table threads from the server
            TableThread[] tableThreads = serverRef.getTables();
            Table[] tables = new Table[tableThreads.length];

            // Convert each TableThread to it's underlying Table object
            for (int i = 0; i < tableThreads.length; i++) {
                tables[i] = tableThreads[i] != null ? tableThreads[i].getTable() : null;
            }

            // Count active players using a helper method; use dealer ID for tracking
            int activePlayers = serverRef.getPlayersInLobby().length;
            int dealerId = dealer.getDealerId();

            // Send lobby data response including table list and player/dealer count
            sendNetworkMessage(new Message.LobbyData.Response(tables, activePlayers, dealerId));
        });

        // Handle GAME DATA Request — sends current state of dealer + player hand
        addMessageHook(Message.GameData.Request.class, (req) -> {
            System.out.println("GameData request");

            // Get the dealer's amd current player's hands, or a default if player not set
            CardHand dealerHand = dealer.getHand();
            CardHand playerHand = currentPlayer != null ? currentPlayer.getHand() : new CardHand(21);

            // Send back both hands to update the client UI
            sendNetworkMessage(new Message.GameData.Response(playerHand, dealerHand));
        });

        // Handle CLOCK SYNC Request — used to sync client clocks with server
        addMessageHook(Message.ClockSync.Request.class, (req) -> {
            System.out.println("ClockSync request");

            // Calculates server time in seconds
            float serverTime = System.nanoTime() / 1_000_000_000.0f;

            // Respond with server time
            sendNetworkMessage(new Message.ClockSync.Response(serverTime));
        });


        // Handle TABLE DATA Request — sends static info about a specific table
        addMessageHook(Message.TableData.Request.class, (req) -> {
            System.out.println("TableData request");


            // These are placeholders; we can replace with real table/game logic later
            int tableId = 1;
            int[] playerIds = new int[]{101, 102}; // Example hardcoded player IDs
            int dealerId = dealer.getDealerId();

            // Respond with table metadata
            sendNetworkMessage(new Message.TableData.Response(dealerId, playerIds, playerIds.length));
        });
    }

    /**
     * Sets the currently active player for dealer reference.
     */
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }
}
