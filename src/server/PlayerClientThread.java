package server;

import game.CardHand;
import game.Card;
import game.Value;
import game.Suit;
import game.Table;

import networking.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PlayerClientThread extends ClientThreadWithHooks {
    public PlayerClientThread(Socket socket, Server server, ObjectOutputStream writer, ObjectInputStream reader) {
        super(socket, writer, reader);
        System.out.println("Spawned player thread: " + socket.getInetAddress());

        /**
         * This is where the server game logic for the dealer happens
         */
        addMessageHook(Message.JoinTable.Request.class, (req) -> {
            System.out.println("JoinTable Request");
            boolean status = server.movePlayerClientToTable(this, req.getTableId());
            sendNetworkMessage(new Message.JoinTable.Response(status));
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

            // Fetch all table threads from the server
            // TableThread[] tableThreads = serverRef.getTables();
            Table[] tables = server.getTables();

            // Table[] tables = new Table[tableThreads.length];

            // Convert each TableThread to it's underlying Table object
            // for (int i = 0; i < tableThreads.length; i++) {
            //     tables[i] = tableThreads[i] != null ? tableThreads[i].getTable() : null;
            // }

            // Count active players using a helper method; use dealer ID for tracking
            int activePlayers = server.getPlayersInLobby().length;

            // Send lobby data response including table list and player/dealer count
            sendNetworkMessage(new Message.LobbyData.Response(tables, activePlayers, 0));
            System.out.println("Lobby data request");
            // final Table[] tables = Arrays.stream(server.getTables()).map(tableThread -> tableThread.table).toArray();
            // final ClientThreadWithHooks[] dealers = server.getDealersInLobby();
            // final ClientThreadWithHooks[] players = server.getPlayersInLobby();
            // Message.LobbyData.Response response = new Message.LobbyData.Response(tables, dealers.length, players.length);


            //-------------NOTE------------------
            // dummy vals to compile
            // Table[] tables = new Table[1];
            // int dummyPlayerCount = 6;
            // int dummyDealerCount = 1;
            // sendNetworkMessage(new Message.LobbyData.Response(tables,dummyPlayerCount,dummyDealerCount));
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
        showMessageHooks();
    }
}
