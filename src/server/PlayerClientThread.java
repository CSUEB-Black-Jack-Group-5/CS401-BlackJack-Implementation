package server;

import game.CardHand;
import game.Card;
import game.Value;
import game.Suit;
import game.Table;
import game.Player;
import game.Wallet;
import networking.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PlayerClientThread extends ClientThreadWithHooks {
    private final Server server;
    private Player player; // Represents the player tied to this client thread
    private Table currentTable; // The table this player has joined

    public PlayerClientThread(Socket socket, Server server, ObjectOutputStream writer, ObjectInputStream reader) {
        super(socket, writer, reader);
        this.server = server;
        System.out.println("Spawned player thread: " + socket.getInetAddress());

        /**
         * This is where the server game logic for the dealer happens
         */
        addMessageHook(Message.JoinTable.Request.class, (Message.JoinTable.Request req) -> {
            System.out.println("JoinTable Request");
            // boolean status = server.movePlayerClientToTable(this, req.getTableId());
            // sendNetworkMessage(new Message.JoinTable.Response(status));

            // ----- PROPOSED IMPLEMENTATION BELOW -----
            boolean status = server.movePlayerClientToTable(this, req.getTableId());
            if (status) {
                TableThread[] allTables = server.getTables();
                if (req.getTableId() >= 0 && req.getTableId() < allTables.length) {
                    currentTable = allTables[req.getTableId()].getTable();
                    String username = "guest" + req.getTableId();  // I have no clue how to fix this
                    player = new Player(username, username); // Create a temporary guest player
                    currentTable.addPlayer(player); // Add player to the table
                } else {
                    status = false;
                }
            }
            sendNetworkMessage(new Message.JoinTable.Response(status)); // Send join status back to client
        });

        addMessageHook(Message.Hit.Request.class, (req) -> {
            System.out.println("Hit Request");
            // server.hitPlayer(this, req.getTableId());
            // Table table = server.getTableById(req.getTableId());

            // sendNetworkMessage(new Message.Hit.Response(table));
            // server.broadcastNetworkMessage(new Message.Hit.Response());

            // ----- PROPOSED IMPLEMENTATION BELOW -----
            if (player == null || currentTable == null) return; // Ensure game state is initialized
            currentTable.getDealer().dealCard(player); // Dealer deals a card
            Card drawnCard = player.getHand().getCard(player.getHand().getNumCards() - 1); // Get last card
            boolean success = !player.bustCheck(); // Check if player is still in the game
            sendNetworkMessage(new Message.Hit.Response(drawnCard, player.getHand(), success)); // Send result
        });

        addMessageHook(Message.Stand.Request.class, (req) -> {
            System.out.println("Stand Request");

            // ----- PROPOSED IMPLEMENTATION BELOW -----
            if (player == null || currentTable == null) return;
            player.stand(); // Mark player as standing
            sendNetworkMessage(new Message.Stand.Response(player.getHand(), true));
        });

        addMessageHook(Message.PlayerReady.Request.class, (req) -> {
            System.out.println("PlayerReady request");

            // ----- PROPOSED IMPLEMENTATION BELOW -----
            if (player == null || currentTable == null) return;
            player.setReady(true); // Set player ready status
            sendNetworkMessage(new Message.PlayerReady.Response(true)); // Confirm readiness
        });

        addMessageHook(Message.PlayerLeave.Request.class, (req) -> {
            System.out.println("PlayerLeave request");
            // int tableId = req.getTableId();

            // ----- PROPOSED IMPLEMENTATION BELOW -----
            if (player != null && currentTable != null) {
                currentTable.removePlayer(player); // Remove player from table
            }
            sendNetworkMessage(new Message.PlayerLeave.Response(true)); // Confirm leave
        });

        addMessageHook(Message.LobbyData.Request.class, (req) -> {
            System.out.println("Lobby data request");
            // final Table[] tables = Arrays.stream(server.getTables()).map(tableThread -> tableThread.table).toArray();
            // final ClientThreadWithHooks[] dealers = server.getDealersInLobby();
            // final ClientThreadWithHooks[] players = server.getPlayersInLobby();
            // Message.LobbyData.Response response = new Message.LobbyData.Response(tables, dealers.length, players.length);

            // ----- PROPOSED IMPLEMENTATION BELOW -----
            TableThread[] tables = server.getTables(); // Get all table threads
            int playerCount = server.getPlayersInLobby().length;
            int dealerCount = server.getDealersInLobby().length;
            Table[] tableObjs = new Table[tables.length]; // Extract Table objects
            for (int i = 0; i < tables.length; i++) {
                tableObjs[i] = tables[i].getTable();
            }
            sendNetworkMessage(new Message.LobbyData.Response(tableObjs, playerCount, dealerCount)); // Send lobby info
        });

        addMessageHook(Message.GameData.Request.class, (req) -> {
            System.out.println("GameData request");

            // ----- PROPOSED IMPLEMENTATION BELOW -----
            if (player == null || currentTable == null) return;
            CardHand playerHand = player.getHand(); // Player’s current hand
            CardHand dealerHand = currentTable.getDealer().getHand(); // Dealer’s hand
            sendNetworkMessage(new Message.GameData.Response(playerHand, dealerHand)); // Send both
        });

        addMessageHook(Message.TableData.Request.class, (req) -> {
            System.out.println("TableData request");
            // Table table = server.getTableById(req.getTableId());

            // ----- PROPOSED IMPLEMENTATION BELOW -----
            if (currentTable == null) return;

            // Get the dealer's username for this table
            String dealerUsername = currentTable.getDealer().getUsername();

            // Get all current players at the table
            Player[] players = currentTable.getPlayers();

            // Extract usernames from each Player (playerId no longer used)
            String[] playerUsernames = new String[currentTable.getPlayerCount()];
            for (int i = 0; i < currentTable.getPlayerCount(); i++) {
                playerUsernames[i] = players[i].getUsername();
            }

            int joinedCount = currentTable.getPlayerCount();

            // Send response with dealer name, list of player usernames, and player count
            sendNetworkMessage(new Message.TableData.Response(dealerUsername, playerUsernames, joinedCount)); // Send info
        });

        addMessageHook(Message.Split.Request.class, (req)->{
            System.out.println("Split request");

            // ----- PROPOSED IMPLEMENTATION BELOW -----
            if (player == null) return;
            boolean success = player.split(); // Attempt to split the hand
            CardHand main = player.getHand(); // Get updated main hand
            CardHand split = success ? player.getSplitHand() : null;
            sendNetworkMessage(new Message.Split.Response(main, split, success)); // Send result
        });

        addMessageHook(Message.DoubleDown.Request.class, (req)->{
            System.out.println("Double Down request");

            // ----- PROPOSED IMPLEMENTATION BELOW -----
            if (player == null || currentTable == null) return;
            currentTable.getDealer().dealCard(player); // Deal one more card
            float funds = player.getWallet().getFunds(); // Get current funds
            sendNetworkMessage(new Message.DoubleDown.Response((int) funds, true));
        });

        showMessageHooks();
    }
}
