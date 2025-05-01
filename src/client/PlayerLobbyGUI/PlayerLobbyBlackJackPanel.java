package client.PlayerLobbyGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import client.ClientMain;
import networking.Message;
import networking.AccountType;

public class PlayerLobbyBlackJackPanel extends JPanel {
    private ArrayList<PlayerLobbyBlackJack.Table> tables;
    private JPanel tablesPanel;
    private int playerId;
    private String playerName;
    private int currentTableId;

    /// Background texture colors
    private Color feltGreen = new Color(0, 102, 0); // Brighter green like in the image
    private Color feltPatternColor = new Color(0, 85, 0); // Slightly darker green for pattern

    public PlayerLobbyBlackJackPanel(ArrayList<PlayerLobbyBlackJack.Table> tables) {
        this.tables = tables;
        this.playerId = playerId;
        this.playerName = playerName;
        setLayout(new BorderLayout());
        setBackground(feltGreen);

        ///  Setup UI for player side
        playerSetupUI();

        /// Request lobby data when panel is created
        requestLobbyData();
    }
    /// Method to request lobby data from server
    private void requestLobbyData() {
        Message.LobbyData.Request request = new Message.LobbyData.Request(playerId, AccountType.PLAYER);
        ClientMain.client.sendNetworkMessage(request);
    }

    ///  Method to update UI with lobby data received from server
    public void updateLobbyData(Message.LobbyData.Response response) {
        /// Update tables
//       SwingUtilities.invokeLater(() -> {
//            // Update dealer and player counts
//            int totalDealers = response.getDealerCount();
//            int totalPlayers = response.getPlayerCount();
//
//            totalDealersLabel.setText("Total Dealers: " + totalDealers);
//            totalPlayersLabel.setText("Total Players: " + totalPlayers);
//
//            // Update tables if necessary
//            if (response.getTables() != null) {
//                // Clear current tables and add new ones from response
//                tables.clear();
//                for (Message.Table table : response.getTables()) {
//                    // Convert network table to GUI table
//                    tables.add(new PlayerLobbyBlackJack.Table(
//                        table.getId(),
//                        table.getOccupancy(),
//                        table.getMaxPlayers(),
//                        table.getDealerName()
//                    ));
//                }

        /// Refresh UI
        refreshTablesList();

    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Enable anti-aliasing for smoother rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Paint the green felt background with pattern
        paintFeltBackground(g2d);

        g2d.dispose();
    }

    private void paintFeltBackground(Graphics2D g2d) {
        // Fill the background with base felt green
        g2d.setColor(feltGreen);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw felt pattern (small diamonds)
        g2d.setColor(feltPatternColor);
        int squareSize = 16;

        for (int x = 0; x < getWidth(); x += squareSize * 2) {
            for (int y = 0; y < getHeight(); y += squareSize * 2) {
                /// Draw checkerboard pattern
                g2d.fillRect(x, y, squareSize, squareSize);
                g2d.fillRect(x + squareSize, y + squareSize, squareSize, squareSize);
            }
        }
    }

    private void playerSetupUI() {
        /// Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setOpaque(false);

        /// Create a rounded panel for the lobby content
        /// Slightly lighter green with transparency
        RoundedPanelPlayerLobby contentPanel = new RoundedPanelPlayerLobby(20, new Color(0, 148, 50));
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        /// Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                /// Gold trim
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(217, 187, 132, 150)),
                BorderFactory.createEmptyBorder(5, 5, 10, 5)
        ));

        /// Tables grid
        tablesPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        tablesPanel.setOpaque(false);
        tablesPanel.setBorder(BorderFactory.createEmptyBorder(15, 5, 15, 5));

        /// Add tables to the grid
        refreshTablesList();

        JScrollPane scrollPane = new JScrollPane(tablesPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        headerPanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(headerPanel, BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        /// Add the main panel to this panel
        add(mainPanel, BorderLayout.CENTER);
    }

    private void refreshTablesList() {
        tablesPanel.removeAll();

        for (int i = 0; i < tables.size(); i++) {
            PlayerLobbyBlackJack.Table table = tables.get(i);
            JPanel tablePanel = createTablePanel(table, i);
            tablesPanel.add(tablePanel);
        }

        tablesPanel.revalidate();
        tablesPanel.repaint();
    }

    private JPanel createTablePanel(final PlayerLobbyBlackJack.Table table, final int index) {

        /// Create a rounded panel for each table
        final RoundedPanelPlayerLobby panel = new RoundedPanelPlayerLobby(15);

        Color tableColor;

        ///  Set table color for each table with different players in there
        if (table.occupancy == table.maxPlayers) {
            tableColor = new Color(147, 29, 43, 220);
        } else if (table.occupancy > table.maxPlayers / 2) {
            tableColor = new Color(25, 97, 39, 220);
        } else if (table.occupancy > 0) {
            tableColor = new Color(33, 84, 120, 220);
        } else {
            tableColor = new Color(74, 35, 90, 220);
        }

        panel.setBackground(tableColor);
        panel.setLayout(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new GoldBorderPlayerLobby(2, 8),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        int panelWidth = 200; // Fixed panel width
        int panelHeight = 150; // Fixed panel height
        panel.setPreferredSize(new Dimension(panelWidth, panelHeight));

        JLabel titleLabel = new JLabel("Table " + table.id);
        titleLabel.setForeground(new Color(236, 236, 236)); // Off-white text
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        JLabel occupancyLabel = new JLabel(table.occupancy + "/" + table.maxPlayers);
        occupancyLabel.setForeground(new Color(217, 187, 132));
        occupancyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        occupancyLabel.setHorizontalAlignment(JLabel.CENTER);

        /// Create join button for each table
        RoundedButtonPlayerLobby joinButton = new RoundedButtonPlayerLobby("Join Table", 10);
        joinButton.setFont(new Font("Arial", Font.BOLD, 12));
//        int buttonWidth = 50;
//        int buttonHeight = 26;
//        int buttonX = (panelWidth - buttonWidth) / 2;
//        int buttonY = panelHeight / 2 + 10;
//        ///  Set position of button
//        joinButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
        joinButton.setForeground(new Color(236, 236, 236));
        joinButton.setFocusPainted(false);
        joinButton.setPreferredSize(new Dimension(120, 45));

        /// Disable button if table is full
        if (table.occupancy >= table.maxPlayers) {
            joinButton.setEnabled(false);
            joinButton.setBackground(new Color(178, 190, 195));
        }
        else {
            joinButton.setBackground(new Color(204, 51, 63));
        }

        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                joinTable(table);
            }
        });

        /// Panel for the table information
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 2, 2));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        infoPanel.add(titleLabel);
        infoPanel.add(occupancyLabel);

        /// Create button panel for join button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(90, 0, 0, 0));
        buttonPanel.add(joinButton);


        panel.add(infoPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

    private void joinTable(PlayerLobbyBlackJack.Table table) {
        /// Check if table is full
        if (table.occupancy >= table.maxPlayers) {
            JOptionPane.showMessageDialog(this,
                    "This table is full. Please select another table.",
                    "Table Full",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        /// Join the table (increment occupancy)
        // Send join table request to server
        currentTableId = table.id; /// Store the table ID for the response
        Message.JoinTable.Request request = new Message.JoinTable.Request(playerId, table.id);
        ClientMain.client.sendNetworkMessage(request);
    }

    /// Method to handle join table response
    public void handleJoinTableResponse(Message.JoinTable.Response response, int tableId) {
        if (response.getStatus()) {
            /// Find the table and update its occupancy
            for (PlayerLobbyBlackJack.Table table : tables) {
                if (table.id == tableId) {
                    table.occupancy++;
                    break;
                }
            }

            /// Refresh the UI
            refreshTablesList();

            /// Show confirmation message
            JOptionPane.showMessageDialog(this,
                    "You've joined Table " + tableId,
                    "Table Joined",
                    JOptionPane.INFORMATION_MESSAGE);

            /// Request updated lobby data
            requestLobbyData();

        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to join table. It may be full or no longer available.",
                    "Join Failed",
                    JOptionPane.ERROR_MESSAGE);

            // Request updated lobby data to refresh the table list
            requestLobbyData();
        }
    }

}