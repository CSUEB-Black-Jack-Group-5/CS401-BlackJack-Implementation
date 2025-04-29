package client.DealerLobbyGUI;

import javax.swing.*;
import java.util.ArrayList;

public class DealerLobbyBlackJack extends JFrame {
    private ArrayList<Table> tables;
    private DealerLobbyBlackJackPanel dealerLobbyBlackJackPanel;

    public static class Table {
        int id;
        int occupancy;
        int maxPlayers;
        String dealerName;

        public Table(int id, int occupancy, int maxPlayers, String dealerName) {
            this.id = id;
            this.occupancy = occupancy;
            this.maxPlayers = maxPlayers;
            this.dealerName = dealerName;
        }
    }

    public DealerLobbyBlackJack() {
        /// Initialize tables data
        tables = new ArrayList<>();
        tables.add(new Table(1, 0, 6, "DealerName"));
        tables.add(new Table(2, 4, 6, "DealerName"));
        tables.add(new Table(3, 3, 6, "DealerName"));
        tables.add(new Table(4, 6, 6, "DealerName"));

        /// Set up the frame
        setTitle("Dealer Lobby");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        /// Create the lobby panel and set it as the content pane
        dealerLobbyBlackJackPanel = new DealerLobbyBlackJackPanel(tables);
        setContentPane(dealerLobbyBlackJackPanel);
    }
}