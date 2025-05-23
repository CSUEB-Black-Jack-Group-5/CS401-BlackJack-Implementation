package client.DealerLobbyGUI;

import client.BlackjackGame;
import client.ClientMain;
import game.Table;
import networking.AccountType;
import networking.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DealerLobbyBlackJackPanel extends JPanel {
    private ArrayList<DealerLobbyBlackJack.GuiTable> GUItables;
    private JPanel tablesPanel;
    private JLabel dealerCountLabel;
    private JLabel playerCountLabel;
    private String dealerName;
    private int dealerId;

    /// Background texture colors
    private Color feltGreen = new Color(0, 102, 0); // Brighter green like in the image
    private Color feltPatternColor = new Color(0, 85, 0); // Slightly darker green for pattern

    public DealerLobbyBlackJackPanel(ArrayList<DealerLobbyBlackJack.GuiTable> tables) {
        this.GUItables = tables;
        this.dealerCountLabel = new JLabel();
        this.playerCountLabel = new JLabel();
        setLayout(new BorderLayout());
        setBackground(feltGreen);

        ///  Setup UI
        dealerSetupUI();

        ///  Request Lobby Data when panel is created
        requestLobbyData();
    }

    ///  Request Lobby Data
    private void requestLobbyData() {
        Message.LobbyData.Request request = new Message.LobbyData.Request(dealerId, AccountType.DEALER);
        BlackjackGame.client.sendNetworkMessage(request);
    }

    ///  Update UI with the lobby data when get the response back from server
    public void updateLobbyData(Message.LobbyData.Response response){
        int totalDealers = response.getDealerCount();
        int totalPlayers = response.getPlayerCount();

        dealerCountLabel.setText("Total of dealers: " + totalDealers);
        playerCountLabel.setText("Total of players: " + totalPlayers);

        ///  Because we don't have Table yet so i put it right here so can you guys depend on it to create table
        if (response.getTables() != null || response.getTables().length != 0) {
            /// Clear current tables and add new ones from response
            GUItables.clear();
            for (Table dealerTable : response.getTables()) {
                /// Convert network table to game Table
                DealerLobbyBlackJack.GuiTable table = new DealerLobbyBlackJack.GuiTable(
                    dealerTable.getTableId(),
                    dealerTable.getPlayerCount(),
                    dealerTable.getPlayerLimit(),
                    dealerTable.getDealer().getUsername(),
                    dealerTable.getDealer().getDealerId()
                );
                GUItables.add(table);
            }
        }

        // Refresh UI
        refreshTablesList();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        /// Enable anti-aliasing for smoother rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        /// Paint the green felt background with pattern
        paintFeltBackground(g2d);

        g2d.dispose();
    }

    private void paintFeltBackground(Graphics2D g2d) {
        /// Fill the background with base felt green
        g2d.setColor(feltGreen);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        /// Draw felt pattern (small diamonds)
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

    private void dealerSetupUI() {
        /// Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setOpaque(false);

        /// Create a rounded panel for the lobby content
        RoundedPanelDealerLobby contentPanel = new RoundedPanelDealerLobby(20, new Color(0, 148, 50)); // Slightly lighter green with transparency
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        /// Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(217, 187, 132, 150)), // Gold trim
                BorderFactory.createEmptyBorder(5, 5, 10, 5)
        ));

        /// Stats panel for dealers and players count
        JPanel statsPanel = new JPanel(new GridLayout(1, 2));
        statsPanel.setOpaque(false);

        JPanel dealerCountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel playerCountPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        dealerCountPanel.setOpaque(false);
        playerCountPanel.setOpaque(false);
        dealerCountPanel.add(dealerCountLabel);
        playerCountPanel.add(playerCountLabel);

        statsPanel.add(dealerCountPanel);
        statsPanel.add(playerCountPanel);
        headerPanel.add(statsPanel, BorderLayout.NORTH);

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

        /// Bottom panel with create table button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        RoundedButtonDealerLobby createTableButton = new RoundedButtonDealerLobby("Create Table", 15);
        createTableButton.setFont(new Font("Arial", Font.BOLD, 14));
        createTableButton.setBackground(new Color(204, 51, 63));
        createTableButton.setForeground(new Color(236, 236, 236));
        createTableButton.setFocusPainted(false);
        createTableButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        /// Add gold drop shadow effect
        createTableButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(2, 2, 4, 4),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        createTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewTable();
            }
        });

        bottomPanel.add(createTableButton);

        contentPanel.add(headerPanel, BorderLayout.CENTER);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        /// Add the main panel to this panel
        add(mainPanel, BorderLayout.CENTER);
    }

    private void refreshTablesList() {
        tablesPanel.removeAll();

        for (DealerLobbyBlackJack.GuiTable table : GUItables) {
            JPanel tablePanel = createTablePanel(table);
            tablesPanel.add(tablePanel);
        }

        tablesPanel.revalidate();
        tablesPanel.repaint();

        /// Update counts
        dealerCountLabel.setText("Total of dealers: " + GUItables.size());
        playerCountLabel.setText("Total of players: " + countTotalPlayers());
    }

    private JPanel createTablePanel(DealerLobbyBlackJack.GuiTable table) {
        /// Create a rounded panel for each table
        RoundedPanelDealerLobby panel = new RoundedPanelDealerLobby(15);

        Color tableColor;

        /// Do when finish table
        if (table.occupancy == table.maxPlayers) {
            tableColor = new Color(147, 29, 43, 220); // Full table - deep red
        } else if (table.occupancy > table.maxPlayers / 2) {
            tableColor = new Color(25, 97, 39, 220); // More than half full - deep green
        } else if (table.occupancy > 0) {
            tableColor = new Color(33, 84, 120, 220); // Some players - deep blue
        } else {
            tableColor = new Color(74, 35, 90, 220); // Empty table - deep purple
        }

        panel.setBackground(tableColor);
        panel.setLayout(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new GoldBorderDealerLobby(2, 8),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        /// Do when finish table
        JLabel titleLabel = new JLabel("Table " + table.id);
        titleLabel.setForeground(new Color(236, 236, 236)); // Off-white text
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        JLabel occupancyLabel = new JLabel(table.occupancy + "/" + table.maxPlayers);
        occupancyLabel.setForeground(new Color(217, 187, 132)); // Gold text
        occupancyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        occupancyLabel.setHorizontalAlignment(JLabel.CENTER);

        JLabel dealerLabel = new JLabel(table.dealerName);
        dealerLabel.setForeground(new Color(175, 175, 175)); // Light gray
        dealerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        dealerLabel.setHorizontalAlignment(JLabel.CENTER);

            ///  Do when finish table
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(occupancyLabel, BorderLayout.CENTER);
        panel.add(dealerLabel, BorderLayout.SOUTH);
        panel.setPreferredSize(new Dimension(100, 80));

        return panel;
    }

    private void createNewTable() {
        Message.CreateTable.Request request = new Message.CreateTable.Request();
        BlackjackGame.client.sendNetworkMessage(request);
    }

    /// Method to handle create table response
    public void handleCreateTableResponse(Message.CreateTable.Response response) {
        SwingUtilities.invokeLater(() -> {
            if (response.getStatus()) {
                Table newTable = response.getTable();
                int newTableId = newTable.getTableId();
                ///  add later
                GUItables.add(new DealerLobbyBlackJack.GuiTable(newTableId, 0, 6, dealerName, dealerId));
                refreshTablesList();

                /// After creating a table, request updated lobby data
                requestLobbyData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to create table. Please try again.",
                        "Table Creation Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    ///  Count Players
    private int countTotalPlayers() {
        int totalPlayers = 0;
//        for (DealerLobbyBlackJack.Table table : tables) {
//            totalPlayers += table.occupancy;
//        }
        return totalPlayers;
    }
}