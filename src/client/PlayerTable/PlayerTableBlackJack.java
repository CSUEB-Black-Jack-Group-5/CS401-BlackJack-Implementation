package client.PlayerTable;

import client.BlackjackGame;
import game.Card;
import game.Suit;
import game.Value;
import networking.Message;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Main class for the Blackjack Player Table GUI
 * Implements proper server communication and synchronization with dealer view
 */
public class PlayerTableBlackJack extends JFrame {
    /// Main panels
    private JPanel mainPanel;
    private JPanel tablePanel;
    private JPanel controlPanel;
    private JPanel statusPanel;

    /// Table info components
    private JLabel tableInfoLabel;
    private JLabel occupancyLabel;
    private Color tableColor;

    /// Control buttons
    private JButton hitButton;
    private JButton standButton;
    private JButton doubleDownButton;
    private JButton leaveButton;
    private JButton readyButton;

    /// Player positions
    private ArrayList<PlayerPositionPanel> playerPositions;
    private DealerPositionPanel dealerPanel;
    private PlayerPositionPanel currentPlayerPosition;

    /// Timer components
    private JPanel timerPanel;
    private JLabel[] timerLabels;

    /// Card components
    private JPanel dealerCardsPanel;
    private ArrayList<JPanel> playerCardsPanels;
    private JPanel myCardsPanel;

    /// Table data
    private int tableId;
    private String dealerName;
    private int occupancy;
    private int maxPlayers;
    private int playerPosition; // Current player's position at the table
    private String playerName;

    /// Chip count and bet amount for the player
    private int chipCount = 1000; // Default starting chips
    private int currentBet = 0;
    private JLabel chipsLabel;
    private JLabel betLabel;

    /// Game state tracking
    private boolean isPlayerTurn = false;
    private boolean hasBlackjack = false;
    private boolean hasBusted = false;
    private boolean roundComplete = false;

    /// Constructor for the PlayerTableBlackJack class
    public PlayerTableBlackJack(int tableId, String dealerName, int playerPosition, int occupancy, int maxPlayers, String playerName) {
        this.tableId = tableId;
        this.dealerName = dealerName;
        this.playerPosition = playerPosition;
        this.occupancy = occupancy;
        this.maxPlayers = maxPlayers;
        this.playerName = playerName;

        // Set table color
        tableColor = BlackjackConstants.TABLE_COLOR;

        initComponents();
        setupLayout();
        setupListeners();
        setupMessageHooks();

        // Show the table
        setVisible(true);
    }

    /// Initialize all UI components
    private void initComponents() {
        // Set up the frame
        setTitle("Blackjack - Table #" + tableId + " - Player: " + playerName);
        setSize(1100, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create main panel with pattern background
        mainPanel = new BackgroundPanel();

        // Create table panel with curved borders
        tablePanel = new RoundedPanel(25, tableColor, 0.9f);
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create table title
        tableInfoLabel = new GradientLabel("BLACKJACK - TABLE " + tableId,
                new Color(255, 215, 0), new Color(184, 134, 11),
                SwingConstants.CENTER);
        tableInfoLabel.setFont(BlackjackConstants.TITLE_FONT);
        tableInfoLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Create occupancy label
        occupancyLabel = new JLabel("<html><b>PLAYERS:</b> " + occupancy + "/" + maxPlayers + "</html>",
                SwingConstants.CENTER);
        occupancyLabel.setFont(BlackjackConstants.LABEL_FONT);
        occupancyLabel.setForeground(Color.WHITE);
        occupancyLabel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(Color.WHITE, 2, 10),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Create dealer position panel
        dealerPanel = new DealerPositionPanel(dealerName);

        // Create dealer cards panel
        dealerCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        dealerCardsPanel.setOpaque(false);

        // Create player positions
        playerPositions = new ArrayList<>();
        playerCardsPanels = new ArrayList<>();

        // Create timer panel
        timerPanel = new JPanel(new GridLayout(1, 4, 3, 0));
        timerPanel.setOpaque(false);
        timerLabels = new JLabel[4];

        for (int i = 0; i < 4; i++) {
            // Timer Box
            JPanel timerBox = new RoundedPanel(8, new Color(0, 0, 0, 150), 1.0f);
            timerBox.setLayout(new BorderLayout());

            timerLabels[i] = new JLabel("0", SwingConstants.CENTER);
            timerLabels[i].setFont(new Font("Monospaced", Font.BOLD, 22));
            timerLabels[i].setForeground(new Color(255, 60, 60)); // Red LED color

            timerBox.add(timerLabels[i], BorderLayout.CENTER);
            timerPanel.add(timerBox);
        }

        // Create player's cards panel
        myCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        myCardsPanel.setOpaque(false);

        // Create control buttons
        hitButton = UIFactory.createStyledButton("Hit", new Color(40, 167, 69), Color.WHITE);
        standButton = UIFactory.createStyledButton("Stand", new Color(255, 193, 7), Color.BLACK);
        doubleDownButton = UIFactory.createStyledButton("Double Down", new Color(23, 162, 184), Color.WHITE);
        readyButton = UIFactory.createStyledButton("Ready", new Color(220, 53, 69), Color.WHITE);
        leaveButton = UIFactory.createStyledButton("Leave Table", new Color(220, 53, 69), Color.WHITE);

        // Set initial button states
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        doubleDownButton.setEnabled(false);
        readyButton.setEnabled(true); // Ready button enabled initially

        // Create chip and bet labels
        chipsLabel = new JLabel("Your Funds: $" + chipCount, SwingConstants.CENTER);
        chipsLabel.setFont(BlackjackConstants.LABEL_FONT);
        chipsLabel.setForeground(Color.WHITE);
        chipsLabel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(BlackjackConstants.GOLD_ACCENT, 2, 10),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        betLabel = new JLabel("Bet: $" + currentBet, SwingConstants.CENTER);
        betLabel.setFont(BlackjackConstants.LABEL_FONT);
        betLabel.setForeground(Color.WHITE);
        betLabel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(BlackjackConstants.GOLD_ACCENT, 2, 10),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Create control panel
        controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        controlPanel.setOpaque(false);

        // Create status panel
        statusPanel = new JPanel(new BorderLayout());
        statusPanel.setOpaque(false);
    }

    /// Set up the layout of components
    private void setupLayout() {
        setContentPane(mainPanel);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout(20, 0));
        headerPanel.setOpaque(false);
        headerPanel.add(tableInfoLabel, BorderLayout.CENTER);
        headerPanel.add(occupancyLabel, BorderLayout.EAST);

        // Create dealer info panel
        JPanel dealerInfoPanel = new JPanel();
        dealerInfoPanel.setOpaque(false);
        dealerInfoPanel.setLayout(new BoxLayout(dealerInfoPanel, BoxLayout.Y_AXIS));

        JLabel dealerTitle = new JLabel("DEALER", SwingConstants.CENTER);
        dealerTitle.setForeground(Color.WHITE);
        dealerTitle.setFont(new Font("Arial", Font.BOLD, 12));
        dealerTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel dealerNameLabel = new JLabel(dealerName, SwingConstants.CENTER);
        dealerNameLabel.setForeground(BlackjackConstants.GOLD_ACCENT);
        dealerNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dealerNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        dealerInfoPanel.add(dealerTitle);
        dealerInfoPanel.add(Box.createVerticalStrut(3));
        dealerInfoPanel.add(dealerNameLabel);

        // Create playing surface
        JPanel playingSurfacePanel = new PlayingSurfacePanel();

        // Setup player positions
        setupPlayerPositions(playingSurfacePanel);

        // Create dealer area
        JPanel dealerAreaPanel = new JPanel(new BorderLayout(0, 5));
        dealerAreaPanel.setOpaque(false);
        dealerAreaPanel.add(dealerInfoPanel, BorderLayout.NORTH);
        dealerAreaPanel.add(dealerPanel, BorderLayout.CENTER);
        dealerAreaPanel.add(dealerCardsPanel, BorderLayout.SOUTH);

        playingSurfacePanel.add(dealerAreaPanel, BorderLayout.NORTH);

        // Create player area
        JPanel playerAreaPanel = new JPanel(new BorderLayout());
        playerAreaPanel.setOpaque(false);

        // Create player info panel
        JPanel playerInfoPanel = new JPanel();
        playerInfoPanel.setOpaque(false);
        playerInfoPanel.setLayout(new BoxLayout(playerInfoPanel, BoxLayout.Y_AXIS));

        JLabel playerTitle = new JLabel("YOU", SwingConstants.CENTER);
        playerTitle.setForeground(Color.WHITE);
        playerTitle.setFont(new Font("Arial", Font.BOLD, 12));
        playerTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel playerNameLabel = new JLabel(playerName, SwingConstants.CENTER);
        playerNameLabel.setForeground(BlackjackConstants.GOLD_ACCENT);
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        playerNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        playerInfoPanel.add(playerTitle);
        playerInfoPanel.add(Box.createVerticalStrut(3));
        playerInfoPanel.add(playerNameLabel);

        // Add components to player area
        playerAreaPanel.add(playerInfoPanel, BorderLayout.NORTH);
        playerAreaPanel.add(myCardsPanel, BorderLayout.CENTER);

        // Add player area to playing surface
        playingSurfacePanel.add(playerAreaPanel, BorderLayout.SOUTH);

        // Add playing surface to table panel
        tablePanel.add(headerPanel, BorderLayout.NORTH);
        tablePanel.add(playingSurfacePanel, BorderLayout.CENTER);

        // Create bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);

        // Create timer container
        JPanel timerContainer = new JPanel(new BorderLayout(0, 5));
        timerContainer.setOpaque(false);
        JLabel timerTitle = new JLabel("TURN TIMER", SwingConstants.CENTER);
        timerTitle.setForeground(Color.WHITE);
        timerTitle.setFont(new Font("Arial", Font.BOLD, 14));
        timerContainer.add(timerTitle, BorderLayout.NORTH);
        timerContainer.add(timerPanel, BorderLayout.CENTER);

        // Create chips/bet panel
        JPanel chipsBetPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        chipsBetPanel.setOpaque(false);
        chipsBetPanel.add(chipsLabel);
        chipsBetPanel.add(betLabel);

        // Add game control buttons
        controlPanel.add(hitButton);
        controlPanel.add(standButton);
        controlPanel.add(doubleDownButton);
        controlPanel.add(readyButton);

        // Create leave button panel
        JPanel leaveButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        leaveButtonPanel.setOpaque(false);
        leaveButtonPanel.add(leaveButton);

        // Add panels to bottom section
        bottomPanel.add(Box.createVerticalStrut(10));
        bottomPanel.add(chipsBetPanel);
        bottomPanel.add(Box.createVerticalStrut(10));
        bottomPanel.add(timerContainer);
        bottomPanel.add(Box.createVerticalStrut(20));
        bottomPanel.add(controlPanel);
        bottomPanel.add(Box.createVerticalStrut(15));
        bottomPanel.add(leaveButtonPanel);

        tablePanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add table panel to main panel
        mainPanel.add(tablePanel, BorderLayout.CENTER);
    }

    /// Set up player positions in a semi-circle
    private void setupPlayerPositions(JPanel surface) {
        // Create player positions panel
        JPanel playerPositionPanel = new JPanel();
        playerPositionPanel.setLayout(null); // Use absolute positioning
        playerPositionPanel.setOpaque(false);

        // Create player positions
        for (int i = 0; i < maxPlayers; i++) {
            boolean isCurrentPlayer = (i == playerPosition - 1);
            boolean isOccupied = (i < occupancy);

            // Only show the current player as occupied if they are in the occupancy count
            if (isCurrentPlayer && i >= occupancy) {
                isOccupied = false;
            }

            String posLabel = isCurrentPlayer ? "YOU" : "P" + (i + 1);
            PlayerPositionPanel position = new PlayerPositionPanel(isOccupied, posLabel);

            // Store reference to current player's position
            if (isCurrentPlayer) {
                currentPlayerPosition = position;
            }

            playerPositions.add(position);
            playerPositionPanel.add(position);

            // Create card area for this player
            JPanel cardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            cardPanel.setOpaque(false);
            playerCardsPanels.add(cardPanel);
            playerPositionPanel.add(cardPanel);
        }

        // Position players in a semi-circle
        positionPlayersInSemicircle(playerPositionPanel);

        surface.add(playerPositionPanel, BorderLayout.CENTER);
    }

    /// Calculate and set positions for players in a semi-circle
    private void positionPlayersInSemicircle(JPanel panel) {
        int playerCount = playerPositions.size();
        int centerX = 500; // Center X coordinate
        int centerY = 300; // Center Y coordinate
        int radius = 250;  // Radius of the semi-circle

        for (int i = 0; i < playerCount; i++) {
            PlayerPositionPanel player = playerPositions.get(i);
            JPanel cardPanel = playerCardsPanels.get(i);

            // Calculate position along a semi-circle (bottom half)
            double angleStep = Math.PI / (playerCount + 1);
            double angle = Math.PI + angleStep * (i + 1);

            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));

            // Set player position (centered on the calculated point)
            player.setBounds(x - 40, y - 40, 80, 80);

            // Position card panel above the player position
            int cardY = y + 45; // Position cards below the player
            cardPanel.setBounds(x - 80, cardY, 160, 70);
        }
    }

    /// Set up event listeners for UI components
    private void setupListeners() {
        // Leave button action
        leaveButton.addActionListener(e -> showLeaveConfirmation());

        // Ready button action
        readyButton.addActionListener(e -> {
            System.out.println("Ready button pressed");

            // Disable the Ready button once clicked
            readyButton.setEnabled(false);

            if (currentPlayerPosition != null) {
                currentPlayerPosition.setReady(true);
                currentPlayerPosition.repaint();
            }

            // Send PlayerReady request to server
            Message.PlayerReady.Request request = new Message.PlayerReady.Request(playerName, tableId);
            BlackjackGame.client.sendNetworkMessage(request);
        });

        // Hit button action
        hitButton.addActionListener(e -> {
            // Send hit request to dealer
            Message.Hit.Request request = new Message.Hit.Request(playerName, null);
            BlackjackGame.client.sendNetworkMessage(request);

            // Disable buttons until response received
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
            doubleDownButton.setEnabled(false);
        });

        // Stand button action
        standButton.addActionListener(e -> {
            // Send stand request to dealer
            Message.Stand.Request request = new Message.Stand.Request(playerName, tableId);
            BlackjackGame.client.sendNetworkMessage(request);

            // Disable all gameplay buttons - round is over for this player
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
            doubleDownButton.setEnabled(false);

            // Update game state
            isPlayerTurn = false;

            // Show waiting message
            JOptionPane.showMessageDialog(this,
                    "You chose to stand. Waiting for dealer's turn.",
                    "Stand", JOptionPane.INFORMATION_MESSAGE);
        });

        // Double Down button action
        doubleDownButton.addActionListener(e -> {
            // Check if player has enough funds
            if (chipCount < currentBet) {
                JOptionPane.showMessageDialog(this,
                        "You don't have enough funds to double down.",
                        "Insufficient Funds", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Deduct additional bet amount
            chipCount -= currentBet;
            currentBet *= 2;

            // Update UI
            chipsLabel.setText("Your Funds: $" + chipCount);
            betLabel.setText("Bet: $" + currentBet);

            // Send double down request to dealer
            Message.DoubleDown.Request request = new Message.DoubleDown.Request(
                    playerName, currentBet, tableId);
            BlackjackGame.client.sendNetworkMessage(request);

            // Disable all gameplay buttons - round is over for this player after double down
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
            doubleDownButton.setEnabled(false);

            // Update game state
            isPlayerTurn = false;
        });
    }

    /**
     * Set up message hooks for server communication
     */
    private void setupMessageHooks() {
        // PlayerReady response
        BlackjackGame.client.addMessageHook(Message.PlayerReady.Response.class, (response) -> {
            if (response.getStatus() && response.getPlayerName().equals(playerName)) {
                // Show confirmation message
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                            "You are ready. Waiting for dealer to shuffle.",
                            "Ready", JOptionPane.INFORMATION_MESSAGE);
                });
            } else if (!response.getStatus()) {
                // Error case - undo ready status and re-enable button
                if (currentPlayerPosition != null) {
                    currentPlayerPosition.setReady(false);
                    currentPlayerPosition.repaint();
                }

                // Re-enable Ready button
                SwingUtilities.invokeLater(() -> {
                    readyButton.setEnabled(true);
                    JOptionPane.showMessageDialog(this,
                            "Failed to set ready status. Please try again.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        });

        // ShuffleComplete notification
        BlackjackGame.client.addMessageHook(Message.ShuffleComplete.Response.class, (response) -> {
            if (response.getTableId() == tableId) {
                // Show bet dialog when dealer has shuffled
                SwingUtilities.invokeLater(() -> {
                    System.out.println("Showing bet dialog after shuffle complete");
                    showBetDialog();
                });
            }
        });

        // PlaceBet response
        BlackjackGame.client.addMessageHook(Message.PlaceBet.Response.class, (response) -> {
            if (response.getStatus()) {
                System.out.println("Bet placed successfully");
                // Waiting for cards to be dealt
            } else {
                System.out.println("Failed to place bet");
                // Return the bet amount to the player's funds
                chipCount += currentBet;
                currentBet = 0;

                // Update UI
                chipsLabel.setText("Your Funds: $" + chipCount);
                betLabel.setText("Bet: $" + currentBet);

                // Show error message
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                            "Failed to place bet. Please try again.",
                            "Bet Error", JOptionPane.ERROR_MESSAGE);

                    // Show bet dialog again
                    showBetDialog();
                });
            }
        });

        // Handle GameState.Update messages
        // In PlayerTableBlackJack.java - modify the GameState.Update handler
        BlackjackGame.client.addMessageHook(Message.GameState.Update.class, (update) -> {
            System.out.println("GameState update received - dealer turn: " + update.isDealerTurn() +
                    ", player turn: " + update.isPlayerTurn() +
                    ", round complete: " + update.isRoundComplete());

            safeUIUpdate(() -> {
                // Update player's turn status
                isPlayerTurn = update.isPlayerTurn();

                // Track whose turn it is and update UI accordingly
                if (update.isDealerTurn()) {
                    // It's dealer's turn - player cannot act
                    hitButton.setEnabled(false);
                    standButton.setEnabled(false);
                    doubleDownButton.setEnabled(false);
                } else if (update.isPlayerTurn()) {
                    // It's player's turn - enable appropriate buttons
                    // Only enable if not busted and not blackjack
                    hitButton.setEnabled(!hasBlackjack && !hasBusted);
                    standButton.setEnabled(!hasBlackjack && !hasBusted);
                    doubleDownButton.setEnabled(!hasBlackjack && !hasBusted &&
                            myCardsPanel.getComponentCount() == 2 &&
                            chipCount >= currentBet);
                }

                // Handle any message that came with the update
                if (update.getResultMessage() != null) {
                    JOptionPane.showMessageDialog(PlayerTableBlackJack.this,
                            update.getResultMessage(),
                            "Game Update", JOptionPane.INFORMATION_MESSAGE);
                }

                // Handle round completion separately from the ready button
                if (update.isRoundComplete()) {
                    roundComplete = true;
                    resetForNextRound();
                }

                // Important: Do NOT modify readyButton here
            });
        });
        // DealInitialCards response
        BlackjackGame.client.addMessageHook(Message.DealInitialCards.Response.class, (response) -> {
            System.out.println("Received DealInitialCards.Response with status: " + response.getStatus() +
                    " for player: " + response.getPlayerName());

            if (response.getStatus() && response.getPlayerName().equals(playerName)) {
                System.out.println("Processing cards for player: " + playerName);

                safeUIUpdate(() -> {
                    try {
                        // Display dealer cards
                        dealerCardsPanel.removeAll();
                        Card[] dealerCards = response.getDealerCards();
                        System.out.println("Dealer cards count: " + (dealerCards != null ? dealerCards.length : "null"));

                        if (dealerCards != null) {
                            // Process each dealer card - respecting their face up/down state
                            for (Card dealerCard : dealerCards) {
                                String cardValue = dealerCard.getValue().toString();
                                String suitSymbol = getSuitSymbol(dealerCard.getSuit());
                                System.out.println("Adding dealer card: " + cardValue + suitSymbol);

                                // Create card panel
                                CardPanel dealerCardPanel = new CardPanel(cardValue, suitSymbol);

                                // Set face up/down state according to the card's state
                                if (!dealerCard.isFaceUp()) {
                                    dealerCardPanel.setFaceDown(true);
                                }

                                dealerCardsPanel.add(dealerCardPanel);
                            }
                        }

                        // Display player cards - all face up for the player
                        myCardsPanel.removeAll();
                        Card[] playerCards = response.getPlayerCards();
                        System.out.println("Player cards count: " + (playerCards != null ? playerCards.length : "null"));

                        if (playerCards != null) {
                            // Process each player card
                            for (Card playerCard : playerCards) {
                                String cardValue = playerCard.getValue().toString();
                                String suitSymbol = getSuitSymbol(playerCard.getSuit());
                                System.out.println("Adding player card: " + cardValue + suitSymbol);

                                // Create card panel - player sees all their cards face up
                                CardPanel playerCardPanel = new CardPanel(cardValue, suitSymbol);
                                playerCardPanel.setFaceUp(true);

                                myCardsPanel.add(playerCardPanel);
                            }
                        }

                        // Refresh display
                        dealerCardsPanel.revalidate();
                        dealerCardsPanel.repaint();
                        myCardsPanel.revalidate();
                        myCardsPanel.repaint();
                        revalidate();
                        repaint();

                        // Calculate hand value
                        int handValue = calculateHandValue(myCardsPanel);

                        // Check for blackjack
                        if (handValue == 21 && myCardsPanel.getComponentCount() == 2) {
                            hasBlackjack = true;
                            JOptionPane.showMessageDialog(this,
                                    "Blackjack! Waiting for dealer...",
                                    "Blackjack", JOptionPane.INFORMATION_MESSAGE);

                            // Disable gameplay buttons
                            hitButton.setEnabled(false);
                            standButton.setEnabled(false);
                            doubleDownButton.setEnabled(false);
                        } else {
                            // Enable gameplay buttons
                            isPlayerTurn = true;
                            hitButton.setEnabled(true);
                            standButton.setEnabled(true);
                            doubleDownButton.setEnabled(chipCount >= currentBet);
                        }

                        // Disable ready button during gameplay
                        readyButton.setEnabled(false);
                    } catch (Exception e) {
                        System.err.println("Error updating UI: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            }
        });

        // Hit response
        BlackjackGame.client.addMessageHook(Message.Hit.Response.class, (response) -> {
            Card drawnCard = response.getDraw();
            boolean isDealerCard = response.isDealerCard();

            if (drawnCard != null) {
                safeUIUpdate(() -> {
                    // [existing code to display card]

                    // At the end of handling player's hit, if not busted, enable action buttons
                    if (!isDealerCard) {
                        // Calculate new hand value
                        int handValue = calculateHandValue(myCardsPanel);

                        // Check for bust
                        if (handValue > 21) {
                            hasBusted = true;
                            isPlayerTurn = false;

                            JOptionPane.showMessageDialog(this,
                                    "Bust! Your hand value: " + handValue,
                                    "Busted", JOptionPane.INFORMATION_MESSAGE);

                            // Disable gameplay buttons
                            hitButton.setEnabled(false);
                            standButton.setEnabled(false);
                            doubleDownButton.setEnabled(false);

                            // Player busted, turn goes to dealer automatically
                            Message.GameState.Update dealerTurnMsg =
                                    new Message.GameState.Update(true, false, false,
                                            "Player " + playerName + " busted. Dealer's turn.");
                            BlackjackGame.client.sendNetworkMessage(dealerTurnMsg);
                        } else {
                            // Player didn't bust, re-enable action buttons
                            isPlayerTurn = true;
                            hitButton.setEnabled(true);
                            standButton.setEnabled(true);
                            doubleDownButton.setEnabled(false); // Can't double down after hitting
                        }
                    }
                });
            }
        });

        // Stand response
        BlackjackGame.client.addMessageHook(Message.Stand.Response.class, (response) -> {
            boolean playerWins = response.playerWins();

            // Wait for GameResult message for final outcome
        });

        // DoubleDown response
        BlackjackGame.client.addMessageHook(Message.DoubleDown.Response.class, (response) -> {
            boolean status = response.getStatus();
            Card drawnCard = response.getDrawnCard();

            if (status && drawnCard != null) {
                // Add the drawn card to player's hand
                safeUIUpdate(() -> {
                    String cardValue = drawnCard.getValue().toString();
                    String suitSymbol = getSuitSymbol(drawnCard.getSuit());
                    CardPanel cardPanel = new CardPanel(cardValue, suitSymbol);
                    cardPanel.setFaceUp(true);
                    myCardsPanel.add(cardPanel);
                    myCardsPanel.revalidate();
                    myCardsPanel.repaint();

                    // Calculate hand value
                    int handValue = calculateHandValue(myCardsPanel);

                    // Check for bust
                    if (handValue > 21) {
                        hasBusted = true;
                        JOptionPane.showMessageDialog(this,
                                "Bust! Your hand value: " + handValue,
                                "Busted", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "You doubled down. Your hand value: " + handValue,
                                "Double Down", JOptionPane.INFORMATION_MESSAGE);
                    }
                });
            } else if (!status) {
                // Double down failed
                JOptionPane.showMessageDialog(this,
                        "Failed to double down.",
                        "Error", JOptionPane.ERROR_MESSAGE);

                // Return the additional bet
                chipCount += currentBet / 2;
                currentBet /= 2;

                // Update UI
                chipsLabel.setText("Your Funds: $" + chipCount);
                betLabel.setText("Bet: $" + currentBet);

                // Re-enable gameplay buttons
                hitButton.setEnabled(true);
                standButton.setEnabled(true);
                doubleDownButton.setEnabled(chipCount >= currentBet);
            }
        });

        // GameResult response
        BlackjackGame.client.addMessageHook(Message.GameResult.Response.class, (response) -> {
            if (response.getPlayerName().equals(playerName)) {
                boolean playerWins = response.playerWins();
                boolean isPush = response.isPush();
                String resultMessage = response.getResultMessage();
                int winnings = response.getWinnings();
                int playerHandValue = response.getPlayerHandValue();
                int dealerHandValue = response.getDealerHandValue();

                // Update player's chip count
                if (playerWins || isPush) {
                    chipCount += winnings;
                }

                // Reset bet amount
                currentBet = 0;

                // Update UI
                safeUIUpdate(() -> {
                    chipsLabel.setText("Your Funds: $" + chipCount);
                    betLabel.setText("Bet: $" + currentBet);

                    // Show result message
                    JOptionPane.showMessageDialog(this,
                            resultMessage,
                            playerWins ? "You Win!" : (isPush ? "Push" : "Dealer Wins"),
                            JOptionPane.INFORMATION_MESSAGE);

                    // Reveal dealer's face-down card if any
                    for (Component component : dealerCardsPanel.getComponents()) {
                        if (component instanceof CardPanel) {
                            CardPanel cardPanel = (CardPanel) component;
                            cardPanel.setFaceDown(false);
                        }
                    }
                    dealerCardsPanel.revalidate();
                    dealerCardsPanel.repaint();

                    // Reset game state for next round
                    resetForNextRound();
                });
            }
        });

        // DealerReveal response
        BlackjackGame.client.addMessageHook(Message.DealerReveal.Response.class, (response) -> {
            safeUIUpdate(() -> {
                // Reveal dealer's face-down card
                dealerCardsPanel.removeAll();

                Card[] dealerCards = response.getDealerCards();
                for (Card card : dealerCards) {
                    CardPanel cardPanel = new CardPanel(
                            card.getValue().toString(),
                            getSuitSymbol(card.getSuit()));
                    cardPanel.setFaceUp(true); // All cards face up
                    dealerCardsPanel.add(cardPanel);
                }

                dealerCardsPanel.revalidate();
                dealerCardsPanel.repaint();
            });
        });

        // Add this to the setupMessageHooks() method in PlayerTableBlackJack.java
    }

    /**
     * Reset game state for next round
     */
    private void resetForNextRound() {
        // Reset game state variables
        isPlayerTurn = false;
        hasBlackjack = false;
        hasBusted = false;
        roundComplete = false;

        // Clear cards
        myCardsPanel.removeAll();
        dealerCardsPanel.removeAll();
        myCardsPanel.revalidate();
        myCardsPanel.repaint();
        dealerCardsPanel.revalidate();
        dealerCardsPanel.repaint();

        // Reset ready status
        if (currentPlayerPosition != null) {
            currentPlayerPosition.setReady(false);
            currentPlayerPosition.repaint();
        }

        // Enable ready button for next round
        readyButton.setEnabled(true);

        // Disable gameplay buttons
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        doubleDownButton.setEnabled(false);
    }

    /**
     * Show confirmation dialog when leaving table
     */
    private void showLeaveConfirmation() {
        JDialog confirmDialog = new JDialog(this, "Confirm Leave", true);
        confirmDialog.setSize(400, 200);
        confirmDialog.setLocationRelativeTo(this);

        JPanel dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        dialogPanel.setBackground(new Color(245, 245, 245));

        JLabel confirmMessage = new JLabel("Are you sure you want to leave this table?", SwingConstants.CENTER);
        confirmMessage.setFont(new Font("Arial", Font.BOLD, 16));
        confirmMessage.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Styled note panel
        JPanel notePanel = new RoundedPanel(10, new Color(255, 243, 205), 1.0f);
        notePanel.setLayout(new BorderLayout());
        notePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel noteLabel = new JLabel("Your chips will be saved for your next game", SwingConstants.CENTER);
        noteLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        noteLabel.setForeground(new Color(133, 100, 4));
        notePanel.add(noteLabel, BorderLayout.CENTER);

        // Styled button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        JButton noButton = UIFactory.createStyledButton("No", new Color(108, 117, 125), Color.BLACK);
        JButton yesButton = UIFactory.createStyledButton("Yes", new Color(220, 53, 69), Color.BLACK);

        buttonPanel.add(noButton);
        buttonPanel.add(yesButton);

        dialogPanel.add(confirmMessage, BorderLayout.NORTH);
        dialogPanel.add(notePanel, BorderLayout.CENTER);
        dialogPanel.add(buttonPanel, BorderLayout.SOUTH);

        confirmDialog.setContentPane(dialogPanel);

        noButton.addActionListener(e -> confirmDialog.dispose());
        yesButton.addActionListener(e -> {
            confirmDialog.dispose();

            // Send PlayerLeave message to server
            Message.PlayerLeave.Request request = new Message.PlayerLeave.Request(
                    playerPosition, tableId);
            BlackjackGame.client.sendNetworkMessage(request);

            dispose(); // Close the table window
        });

        confirmDialog.setVisible(true);
    }

    /**
     * Show betting dialog
     */
    private void showBetDialog() {
        JDialog betDialog = new JDialog(this, "Place Your Bet", true);
        betDialog.setSize(300, 200);
        betDialog.setLocationRelativeTo(this);

        // Create panel for bet dialog
        JPanel betPanel = new JPanel(new BorderLayout(10, 10));
        betPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Available funds label
        JLabel fundsLabel = new JLabel("Available Funds: $" + chipCount);
        fundsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        fundsLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Bet amount spinner
        JPanel spinnerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel betAmountLabel = new JLabel("Bet Amount: $");
        JSpinner betSpinner = new JSpinner(new SpinnerNumberModel(10, 5, chipCount, 5));
        betSpinner.setPreferredSize(new Dimension(80, 25));
        spinnerPanel.add(betAmountLabel);
        spinnerPanel.add(betSpinner);

        // Place bet button
        JButton placeBetButton = UIFactory.createStyledButton("Place Bet",
                new Color(40, 167, 69), Color.WHITE);

        // Arrange components
        betPanel.add(fundsLabel, BorderLayout.NORTH);
        betPanel.add(spinnerPanel, BorderLayout.CENTER);
        betPanel.add(placeBetButton, BorderLayout.SOUTH);

        betDialog.add(betPanel);

        // Place bet action
        placeBetButton.addActionListener(e -> {
            currentBet = (Integer) betSpinner.getValue();
            chipCount -= currentBet;

            // Update UI
            betLabel.setText("Bet: $" + currentBet);
            chipsLabel.setText("Your Funds: $" + chipCount);

            // Send bet information to server
            Message.PlaceBet.Request betRequest = new Message.PlaceBet.Request(
                    playerName, tableId, currentBet);
            BlackjackGame.client.sendNetworkMessage(betRequest);

            betDialog.dispose();
        });

        betDialog.setVisible(true);
    }

    /**
     * Calculate hand value
     */
    private int calculateHandValue(JPanel cardsPanel) {
        int total = 0;
        int aceCount = 0;

        // Count all cards in the panel
        for (Component component : cardsPanel.getComponents()) {
            if (component instanceof CardPanel) {
                CardPanel cardPanel = (CardPanel) component;
                String value = cardPanel.getValue();

                // Handle face cards (J, Q, K)
                if (value.equals("J") || value.equals("Q") || value.equals("K")) {
                    total += 10;
                }
                // Handle Ace - count as 1 initially, adjust later
                else if (value.equals("A")) {
                    total += 1;
                    aceCount++;
                }
                // Handle number cards
                else {
                    try {
                        total += Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        // If parsing fails, default to 10
                        total += 10;
                    }
                }
            }
        }

        // Adjust for aces - can be 1 or 11
        for (int i = 0; i < aceCount; i++) {
            // If we can add 10 more without busting, do it (makes the Ace an 11)
            if (total + 10 <= 21) {
                total += 10;
            }
        }

        return total;
    }

    /**
     * Get suit symbol from Suit enum
     */
    private String getSuitSymbol(Suit suit) {
        return switch (suit) {
            case HEARTS -> "♥";
            case DIAMONDS -> "♦";
            case CLUBS -> "♣";
            case SPADES -> "♠";
        };
    }

    /**
     * Thread-safe UI updates
     */
    private void safeUIUpdate(Runnable updateTask) {
        if (SwingUtilities.isEventDispatchThread()) {
            updateTask.run();
        } else {
            SwingUtilities.invokeLater(updateTask);
        }
    }
}