package server;

import game.*;

import networking.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PlayerClientThread extends ClientThreadWithHooks {
    private Player player;

    public PlayerClientThread(Socket socket, Server server, ObjectOutputStream writer, ObjectInputStream reader, String username) {
        super(socket, writer, reader);

        this.player = new Player(username,"01");

        System.out.println("Spawned player thread: " + socket.getInetAddress());

        /**
         * This is where the server game logic for the dealer happens
         */
        addMessageHook(Message.JoinTable.Request.class, (req) -> {
            System.out.println("JoinTable Request");
            boolean status = server.movePlayerClientToTable(this, req.getTableId(), player);
            Table table = server.getTableById(req.getTableId());
            // System.out.println(server.getTableById(req.getTableId()).toString());
            // System.out.println("Player Count: " + server.getTableById(req.getTableId()).getPlayerCount());
            sendNetworkMessage(new Message.JoinTable.Response(status, player.getUsername(), table, table.getPlayerCount()));
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
            System.out.println("Lobby data request");
            // final Table[] tables = Arrays.stream(server.getTables()).map(tableThread -> tableThread.table).toArray();
            // final ClientThreadWithHooks[] dealers = server.getDealersInLobby();
            // final ClientThreadWithHooks[] players = server.getPlayersInLobby();
            // Message.LobbyData.Response response = new Message.LobbyData.Response(tables, dealers.length, players.length);

            // Fetch all table threads from the server
            Table[] tables = server.getTables();
            // Count active players using a helper method
            int activePlayers = server.getPlayersInLobby().length;
            int activeDealers = server.getDealersInLobby().length;

//            //-------------NOTE------------------
//            // dummy vals to compile
//            Table[] tables = new Table[1];
//            int dummyPlayerCount = 6;
//            int dummyDealerCount = 1;
            sendNetworkMessage(new Message.LobbyData.Response(tables,activePlayers,activeDealers));
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
            // These are placeholders; we can replace with real table/game logic later
            int tableId = req.getTableId();

            Table table = server.getTableById(tableId);
            Dealer dealer = table.getDealer();
            Player[] playerList = table.getPlayers();
            int playersJoined = table.getPlayerCount();

            Player[] players = new Player[table.getPlayerCount()];
            for (int i = 0; i < players.length; i++) {
                players[i] = playerList[i];
            }

            // Respond with table metadata
            sendNetworkMessage(new Message.TableData.Response(players, dealer , playersJoined));
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
        showMessageHooks();
    }
}
