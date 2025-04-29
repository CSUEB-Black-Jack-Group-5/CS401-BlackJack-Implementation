package client.PlayerLobbyGUI;

import javax.swing.*;
import java.util.ArrayList;

public class PlayerLobbyBlackJack extends JFrame {
    private ArrayList<Table> tables;
    private PlayerLobbyBlackJackPanel playerLobbyBlackJackPanel;

    public static class Table {
        int id;
        int occupancy;
        int maxPlayers;

        public Table(int id, int occupancy, int maxPlayers) {
            this.id = id;
            this.occupancy = occupancy;
            this.maxPlayers = maxPlayers;
        }
    }

    public PlayerLobbyBlackJack() {
        /// Initialize tables data
        tables = new ArrayList<>();
        tables.add(new Table(1, 0, 6));
        tables.add(new Table(2, 4, 6));
        tables.add(new Table(3, 3, 6));
        tables.add(new Table(4, 6, 6));

        /// Set up the frame
        setTitle("Dealer Lobby");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        /// Create the lobby panel and set it as the content pane
        playerLobbyBlackJackPanel = new PlayerLobbyBlackJackPanel(tables);
        setContentPane(playerLobbyBlackJackPanel);
    }
}