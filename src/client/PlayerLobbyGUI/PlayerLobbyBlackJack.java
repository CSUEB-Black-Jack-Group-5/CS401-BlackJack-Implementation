package client.PlayerLobbyGUI;

import client.ClientMain;
import game.Table;
import networking.AccountType;
import networking.Message;

import javax.swing.*;
import java.util.ArrayList;

public class PlayerLobbyBlackJack extends JFrame {
    private ArrayList<Table> tables;
    private PlayerLobbyBlackJackPanel playerLobbyBlackJackPanel;

//    public static class Table {
//        int id;
//        int occupancy;
//        int maxPlayers;
//
//        public Table(int id, int occupancy, int maxPlayers) {
//            this.id = id;
//            this.occupancy = occupancy;
//            this.maxPlayers = maxPlayers;
//        }
//    }

    public PlayerLobbyBlackJack() {
        /// Initialize tables data
//        tables = new ArrayList<>();
//        tables.add(new Table(1, 0, 6));
//        tables.add(new Table(2, 4, 6));
//        tables.add(new Table(3, 3, 6));
//        tables.add(new Table(4, 6, 6));

        /// Set up the frame
        setTitle("Player Lobby");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        /// Create the lobby panel and set it as the content pane
        playerLobbyBlackJackPanel = new PlayerLobbyBlackJackPanel(tables);
        setContentPane(playerLobbyBlackJackPanel);

        /// Set up message hooks to handle server responses
        setupMessageHooks();

        /// Request initial lobby data
        requestLobbyData();
    }
    private void setupMessageHooks() {
        // Hook for lobby data response
        ClientMain.client.addMessageHook(Message.LobbyData.Response.class, response -> {
            Message.LobbyData.Response lobbyDataResponse = (Message.LobbyData.Response) response;
            playerLobbyBlackJackPanel.updateLobbyData(lobbyDataResponse);
        });

        // Hook for join table response
        ClientMain.client.addMessageHook(Message.JoinTable.Response.class, response -> {
            Message.JoinTable.Response joinTableResponse = (Message.JoinTable.Response) response;
            playerLobbyBlackJackPanel.handleJoinTableResponse(joinTableResponse,tableId);
        });
    }

    private void requestLobbyData() {
        Message.LobbyData.Request request = new Message.LobbyData.Request(playerId, AccountType.PLAYER);
        ClientMain.client.sendNetworkMessage(request);
    }
}