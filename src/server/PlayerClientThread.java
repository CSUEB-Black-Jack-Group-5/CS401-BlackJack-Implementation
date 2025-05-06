package server;

import game.*;

import networking.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class PlayerClientThread extends ClientThreadWithHooks {
    private final Player player;


    public PlayerClientThread(Player player, Socket socket, Server server, ObjectOutputStream writer, ObjectInputStream reader) {
        super(socket, writer, reader);
        this.player = player;
        System.out.println("Spawned player thread: " + socket.getInetAddress());

        /**
         * This is where the server game logic for the player happens
         */
        addMessageHook(Message.JoinTable.Request.class, (req) -> {
            int tableId = req.getTableId();

            TableThread[] tableThreads = server.getTables();
            boolean tableFound = false;

            for (TableThread tableThread : tableThreads) {
                if (tableThread == null) continue;

                if (tableThread.getTable().getTableId() == tableId) {

                    tableFound = server.movePlayerClientToTable(this,tableId);
                    // need to add a player to
                    sendNetworkMessage(new Message.JoinTable.Response(true));
                    break;
                }
            }

            if (!tableFound) {
                sendNetworkMessage(new Message.JoinTable.Response(false));
            }
        });
        addMessageHook(Message.Hit.Request.class, (req) -> {
            System.out.println("Hit Request");
            // server.hitPlayer(this, req.getTableId());
            // Table table = server.getTableById(req.getTableId());

            // sendNetworkMessage(new Message.Hit.Response(table));
            // server.broadcastNetworkMessage(new Message.Hit.Response());

            //-------------NOTE------------------
            // dummy vals to compile
            boolean dummyStatus = true;
            Card dummyCard1 = new Card(Suit.DIAMONDS, Value.EIGHT);
            Card dummyCard2 = new Card(Suit.CLUBS, Value.ACE);
            CardHand dummyCardHand = new CardHand(21);

            // just testing addCard in cardHand
            dummyCardHand.addCard(dummyCard1);
            dummyCardHand.addCard(dummyCard2);

            sendNetworkMessage(new Message.Hit.Response(dummyCard1,dummyCardHand,dummyStatus));
        });
        addMessageHook(Message.Stand.Request.class, (req) -> {
            System.out.println("Stand Request");

            //-------------NOTE------------------
            // dummy vals to compile
            boolean dummyStatus = true;
            CardHand dummyCardHand = new CardHand(21);
            sendNetworkMessage(new Message.Stand.Response(dummyCardHand,dummyStatus));
        });
        addMessageHook(Message.PlayerReady.Request.class, (req) -> {
            System.out.println("PlayerReady request");

            //-------------NOTE------------------
            // dummy vals to compile
            boolean dummyStatus = true;
            sendNetworkMessage(new Message.PlayerReady.Response(dummyStatus));
        });
        addMessageHook(Message.PlayerLeave.Request.class, (req) -> {
            System.out.println("PlayerLeave request");
            // int tableId = req.getTableId();

            //-------------NOTE------------------
            // dummy vals to compile
            boolean dummyStatus = true;
            sendNetworkMessage(new Message.PlayerLeave.Response(dummyStatus));
        });
        addMessageHook(Message.LobbyData.Request.class, (req) -> {
            System.out.println("Lobby data request From PlayerClientThread");

            // Fetch all table threads from the server
            TableThread[] tableThreads = server.getTables();
            Table[] tables = new Table[tableThreads.length];

            // Convert each TableThread to it's underlying Table object
            for (int i = 0; i < tableThreads.length; i++) {
                tables[i] = tableThreads[i] != null ? tableThreads[i].getTable() : null;
            }

            // Count active players using a helper method; use dealer ID for tracking
            int activePlayers = server.getPlayersInLobby().length;

            // doesnt matter since we are not using id's
            int dealerId = 0;

            // Send lobby data response including table list and player/dealer count
            sendNetworkMessage(new Message.LobbyData.Response(tables, activePlayers, dealerId));
        });
        addMessageHook(Message.GameData.Request.class, (req) -> {
            System.out.println("GameData request");

            //-------------NOTE------------------
            // dummy vals to compile
            CardHand dummyCardHand = new CardHand(21);
            CardHand dummyDealerHand = new CardHand(21);
            sendNetworkMessage(new Message.GameData.Response(dummyCardHand,dummyDealerHand));
        });
        addMessageHook(Message.ClockSync.Request.class, (req) -> {
            System.out.println("ClockSync request");
            // float timer = server.getTableById(req.getTableId()).getTimer();
            // sendNetworkMessage(new Message.ClockSync.Response(timer));

            //-------------NOTE------------------
            // dummy vals to compile
            float dummyClockTime = 60;
            sendNetworkMessage(new Message.ClockSync.Response(dummyClockTime));
        });
        addMessageHook(Message.TableData.Request.class, (req) -> {
            System.out.println("TableData request");
            // Table table = server.getTableById(req.getTableId());

            //-------------NOTE------------------
            // dummy vals to compile
            int dummyDealerID = 1;
            int[] dummyPlayerIDs = {1,2,3,4};
            int dummyPlayersJoined = 4;
            sendNetworkMessage(new Message.TableData.Response(dummyDealerID,dummyPlayerIDs,dummyPlayersJoined));
        });
        addMessageHook(Message.Split.Request.class, (req)->{
            System.out.println("Split request");

            //-------------NOTE------------------
            // dummy vals to compile
            CardHand dummyCardHand = new CardHand(21);
            CardHand dummyCardHand2 = new CardHand(21);
            boolean dummyStatus = true;
            sendNetworkMessage(new Message.Split.Response(dummyCardHand,dummyCardHand2,dummyStatus));
        });
        addMessageHook(Message.DoubleDown.Request.class, (req)->{
            System.out.println("Double Down request");

            //-------------NOTE------------------
            // dummy vals to compile
            int dummyWager = 100;
            boolean dummyStatus = true;
            sendNetworkMessage(new Message.DoubleDown.Response(dummyWager,dummyStatus));
        });

        addMessageHook(Message.Bet.Request.class, (res) -> {
            String username = getPlayerUserName();
//            int bet = res.getAmount();

            TableThread tableThread = server.getTableByUsername(username);
            tableThread.placeBet(username, bet);

            System.out.println("Received bet from " + username + ": $" + bet);

            // Check if all players have bet
            if (tableThread.allPlayersBet()) {
                System.out.println("All players have bet. Starting game...");

            }
        });
        addMessageHook(Message.PlayerAction.Request.class, (Message msg) -> {
            Message.PlayerAction.Request request = (Message.PlayerAction.Request) msg;
            Scanner scanner = new Scanner(System.in);
            System.out.println("It's your turn, " + request.getUserName() + "! Enter 'hit' or 'stand':");

            player.setLastAction(scanner.nextLine().trim().toLowerCase());
            if (!player.getLastAction().equals("hit") && !player.getLastAction().equals("stand")) {
                System.out.println("Invalid input. Defaulting to 'stand'.");
                player.setLastAction("stand");
            }


            sendNetworkMessage(new Message.PlayerAction.Request(request.getUserName(), player.getLastAction()));
        });
        showMessageHooks();
    }
    public String getPlayerUserName(){
        return player.getUsername();
    }
    public Player getPlayer(){
        return player;
    }

}
