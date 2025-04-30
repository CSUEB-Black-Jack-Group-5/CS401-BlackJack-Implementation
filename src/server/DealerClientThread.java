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
    private final Server serverRef;         // Used for accessing tables and lobby players from the main server
    private final Dealer dealer;            // Dealer instance that handles dealing and game state
    private Player currentPlayer;           // The currently active player at the dealer's table (set externally)

    /**
     * Constructor initializes the thread, registers message hooks, and prepares the dealer object.
     */
    public DealerClientThread(Socket socket, Server serverRef, ObjectOutputStream writer, ObjectInputStream reader) {
        super(socket, writer, reader);
        this.serverRef = serverRef;

        // Create the dealer instance with default username/password and minimum stand value of 17
        this.dealer = new Dealer("dealer1", "letmein", 17);

        System.out.println("Spawned dealer thread");

        // Handle STAND Request — player finishes their turn
        addMessageHook(Message.Stand.Request.class, (req) -> {
            System.out.println("Stand Request");

            // If a player is set, use their actual hand and bust status, otherwise use a placeholder
            CardHand playerHand = currentPlayer != null ? currentPlayer.getHand() : new CardHand(21);
            boolean isBust = currentPlayer != null && currentPlayer.bustCheck();

            // Send a response with the player's final hand and whether they busted
            sendNetworkMessage(new Message.Stand.Response(playerHand, isBust));
        });

        // Handle LOBBY DATA Request — returns info about all tables and current activity
        addMessageHook(Message.LobbyData.Request.class, (req) -> {
            System.out.println("Lobby data request");

            // Fetch all table threads from the server
            TableThread[] tableThreads = serverRef.getTables();
            Table[] tables = new Table[tableThreads.length];

            // Convert each TableThread to its underlying Table object
            for (int i = 0; i < tableThreads.length; i++) {
                tables[i] = tableThreads[i] != null ? tableThreads[i].getTable() : null;
            }

            // Count active players using a helper method; use dealer ID for tracking
            int activePlayers = serverRef.getPlayersInLobby().length;
            int dealerId = dealer.getDealerId();

            // Send lobby data response including table list and player/dealer count
            sendNetworkMessage(new Message.LobbyData.Response(tables, activePlayers, dealerId));
        });

        // Handle GAME DATA Request — sends current state of the dealer's and player's hands
        addMessageHook(Message.GameData.Request.class, (req) -> {
            System.out.println("GameData request");

            // Get the dealer’s and current player’s hands, or a default if player not set
            CardHand dealerHand = dealer.getHand();
            CardHand playerHand = currentPlayer != null ? currentPlayer.getHand() : new CardHand(21);

            // Send back both hands to update the client UI
            sendNetworkMessage(new Message.GameData.Response(playerHand, dealerHand));
        });

        // Handle CLOCK SYNC Request — used to sync client clocks with server
        addMessageHook(Message.ClockSync.Request.class, (req) -> {
            System.out.println("ClockSync request");

            // Calculate server time in seconds
            float serverTime = System.nanoTime() / 1_000_000_000.0f;

            // Respond with server time
            sendNetworkMessage(new Message.ClockSync.Response(serverTime));
        });

        // Handle TABLE DATA Request — sends static info about a specific table
        addMessageHook(Message.TableData.Request.class, (req) -> {
            System.out.println("TableData request");

            // These are placeholders; we can replace with real table/game logic later
            int tableId = 1;
            int[] playerIds = new int[]{101, 102};  // Example hardcoded player IDs
            int dealerId = dealer.getDealerId();

            // Respond with table metadata
            sendNetworkMessage(new Message.TableData.Response(dealerId, playerIds, playerIds.length));
        });
    }

    /**
     * Sets the current player for the dealer thread to use during turn-based actions.
     */
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }
}
