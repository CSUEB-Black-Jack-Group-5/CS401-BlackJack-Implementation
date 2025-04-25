package server;

import networking.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

public class PlayerClientThread extends ClientThreadWithHooks {
    public PlayerClientThread(Socket socket, Server server, ObjectOutputStream writer, ObjectInputStream reader) {
        super(socket, writer, reader);
        System.out.println("Spawned player thread: " + socket.getInetAddress());

        /**
         * This is where the server game logic for the dealer happens
         */
        addMessageHook(Message.JoinTable.Request.class, (req) -> {
            System.out.println("JoinTable Request");
            // boolean status = server.movePlayerClientToTable(this, req.getTableId());
            // sendNetworkMessage(new Message.JoinTable.Response(status));
        });
        addMessageHook(Message.Hit.Request.class, (req) -> {
            System.out.println("Hit Request");
            // server.hitPlayer(this, req.getTableId());
            // Table table = server.getTableById(req.getTableId());

            // sendNetworkMessage(new Message.Hit.Response(table));
            // server.broadcastNetworkMessage(new Message.Hit.Response());
            sendNetworkMessage(new Message.Hit.Response());
        });
        addMessageHook(Message.Stand.Request.class, (req) -> {
            System.out.println("Stand Request");
            sendNetworkMessage(new Message.Stand.Response());
        });
        addMessageHook(Message.PlayerReady.Request.class, (req) -> {
            System.out.println("PlayerReady request");
            sendNetworkMessage(new Message.PlayerReady.Response());
        });
        addMessageHook(Message.PlayerLeave.Request.class, (req) -> {
            System.out.println("PlayerLeave request");
            // int tableId = req.getTableId();
            sendNetworkMessage(new Message.PlayerLeave.Response());
        });
        addMessageHook(Message.LobbyData.Request.class, (req) -> {
            System.out.println("Lobby data request");
            // final Table[] tables = Arrays.stream(server.getTables()).map(tableThread -> tableThread.table).toArray();
            // final ClientThreadWithHooks[] dealers = server.getDealersInLobby();
            // final ClientThreadWithHooks[] players = server.getPlayersInLobby();
            // Message.LobbyData.Response response = new Message.LobbyData.Response(tables, dealers.length, players.length);
            sendNetworkMessage(new Message.LobbyData.Response());
        });
        addMessageHook(Message.GameData.Request.class, (req) -> {
            System.out.println("GameData request");
            sendNetworkMessage(new Message.GameData.Response());
        });
        addMessageHook(Message.ClockSync.Request.class, (req) -> {
            System.out.println("ClockSync request");
            // float timer = server.getTableById(req.getTableId()).getTimer();
            // sendNetworkMessage(new Message.ClockSync.Response(timer));
            sendNetworkMessage(new Message.ClockSync.Response());
        });
        addMessageHook(Message.TableData.Request.class, (req) -> {
            System.out.println("TableData request");
            // Table table = server.getTableById(req.getTableId());
            sendNetworkMessage(new Message.TableData.Response());
        });
        showMessageHooks();
    }
}
