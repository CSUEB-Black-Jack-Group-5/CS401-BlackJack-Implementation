package client.DealerLobbyGUI;

import client.BlackjackGame;
import client.ClientMain;
import game.Table;
import networking.AccountType;
import networking.Message;

import javax.swing.*;
import java.util.ArrayList;

public class DealerLobbyBlackJack extends JFrame {
    private ArrayList<GuiTable> tables;
    private DealerLobbyBlackJackPanel dealerLobbyBlackJackPanel;

    public static class GuiTable {
        int id;
        public int occupancy;
        int maxPlayers;
        String dealerName;
        private int dealerId;
//
        public GuiTable(int id, int occupancy, int maxPlayers, String dealerName, int dealerId) {
            this.id = id;
            this.occupancy = occupancy;
            this.maxPlayers = maxPlayers;
            this.dealerName = dealerName;
            this.dealerId = dealerId;
        }
    }

    public DealerLobbyBlackJack() {
        /// Initialize tables data
        tables = new ArrayList<>();
        tables.add(new GuiTable(1, 0, 6, "DealerName",1));
        tables.add(new GuiTable(2, 4, 6, "DealerName",2));
        tables.add(new GuiTable(3, 3, 6, "DealerName",3));
        tables.add(new GuiTable(4, 6, 6, "DealerName",4));

        /// Set up the frame
        setTitle("Dealer Lobby");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        /// Create the lobby panel and set it as the content pane
        dealerLobbyBlackJackPanel = new DealerLobbyBlackJackPanel(tables);
        setContentPane(dealerLobbyBlackJackPanel);

        /// Set up message hooks to handle server responses
        // setupMessageHooks();

        /// Request initial lobby data
        requestLobbyData();

    }

    public void handleLobbyDataResponse(Message.LobbyData.Response response) {
        dealerLobbyBlackJackPanel.updateLobbyData(response);
    }

    public void handleCreateTableResponse(Message.CreateTable.Response response) {
        dealerLobbyBlackJackPanel.handleCreateTableResponse(response);
    }

    private void setupMessageHooks() {
        /// Hook for lobby data response
        // BlackjackGame.client.addMessageHook(Message.LobbyData.Response.class, response -> {
        //     Message.LobbyData.Response lobbyDataResponse = (Message.LobbyData.Response) response;
        //     dealerLobbyBlackJackPanel.updateLobbyData(lobbyDataResponse);
        // });

        /// Hook for create table response
        // BlackjackGame.client.addMessageHook(Message.CreateTable.Response.class, response -> {
        //     Message.CreateTable.Response createTableResponse = (Message.CreateTable.Response) response;
        //     dealerLobbyBlackJackPanel.handleCreateTableResponse(createTableResponse);
        // });
    }

    private void requestLobbyData() {
        Message.LobbyData.Request request = new Message.LobbyData.Request(0, AccountType.DEALER);
        BlackjackGame.client.sendNetworkMessage(request);
    }
}