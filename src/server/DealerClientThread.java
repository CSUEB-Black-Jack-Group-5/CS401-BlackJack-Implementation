package server;

import networking.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class DealerClientThread extends ClientThreadWithHooks {
    public DealerClientThread(Socket socket, Server serverRef, ObjectOutputStream writer, ObjectInputStream reader) {
        super(socket, writer, reader);
        System.out.println("Spawned dealer thread");

        /**
         * This is where the server game logic for the dealer happens
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
        addMessageHook(Message.Stand.Request.class, (req) -> {
            System.out.println("Stand Request");
            sendNetworkMessage(new Message.Stand.Response());
        });
        addMessageHook(Message.LobbyData.Request.class, (req) -> {
            System.out.println("Lobby data request");
            sendNetworkMessage(new Message.LobbyData.Response());
        });
        addMessageHook(Message.GameData.Request.class, (req) -> {
            System.out.println("GameData request");
            sendNetworkMessage(new Message.GameData.Response());
        });
        addMessageHook(Message.ClockSync.Request.class, (req) -> {
            System.out.println("ClockSync request");
            sendNetworkMessage(new Message.ClockSync.Response());
        });
        addMessageHook(Message.TableData.Request.class, (req) -> {
            System.out.println("TableData request");
            sendNetworkMessage(new Message.TableData.Response());
        });
    }
}
