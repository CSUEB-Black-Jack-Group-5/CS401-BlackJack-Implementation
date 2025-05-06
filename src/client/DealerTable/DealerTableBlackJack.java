package client.DealerTable;

import client.BlackjackGame;
import game.Card;
import game.CardHand;
import game.Suit;
import game.Value;
import networking.Message;
import server.TableThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Dealer Table GUI for Blackjack Game
 * Implements proper server connection and synchronization with player views
 */
public class DealerTableBlackJack extends JFrame {
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
    private JButton drawCardButton;
    private JButton payPlayerButton;
    private JButton shuffleButton;
    private JButton leaveButton;

    /// Player positions
    private ArrayList<PlayerPositionPanel> playerPositions;
    private DealerPositionPanel dealerPanel;

    /// Timer components
    private JPanel timerPanel;
    private JLabel[] timerLabels;
    private Timer countdownTimer;
    private int timeRemaining;

    /// Card components
    private JPanel dealerCardsPanel;
    private ArrayList<JPanel> playerCardsPanels;

    /// Table data
    private int tableId;
    private String dealerName;
    private int occupancy;
    private int maxPlayers;

    /// Game state
    private boolean gameInProgress;
    private boolean hasDisplayedReadyNotification = false;

    // Track player information
    private Map<String, Integer> playerPositionMap = new HashMap<>();
    private Map<String, Boolean> playerReadyStatus = new HashMap<>();
    private String currentActivePlayer = null;

    // Track dealer hand value
    private int dealerHandValue = 0;
    private boolean dealerMustStand = false;

    /// Custom colors
    private static final Color TABLE_BACKGROUND = new Color(0, 80, 20);  // Darker green
    private static final Color TABLE_PATTERN = new Color(0, 70, 20);     // Pattern color
    private static final Color TABLE_COLOR = new Color(30, 90, 110);     // Deep teal
    private static final Color GOLD_ACCENT = new Color(212, 175, 55);    // Gold accent

    // Custom fonts
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 28);
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font INFO_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font COUNT_FONT = new Font("Digital-7", Font.BOLD, 24);

    /**
     * Constructor for the DealerTableBlackJack class
     */
    public DealerTableBlackJack(int tableId, String dealerName, int occupancy, int maxPlayers) {
        this.tableId = tableId;
        this.dealerName = dealerName;
        this.occupancy = occupancy;
        this.maxPlayers = maxPlayers;
        this.gameInProgress = false;
        this.timeRemaining = 15; // Default timer value in seconds

        // Set table color
        tableColor = TABLE_COLOR;

        initComponents();
        setupLayout();
        setupListeners();
        setupMessageHooks();

        // Make the window visible
        setVisible(true);
    }

    /// Initialize all UI components
    private void initComponents() {
        // Set up the frame with improved dimensions for widescreen displays
        setTitle("Group5 Blackjack - Table #" + tableId + " - Dealer: " + dealerName);
        setSize(1100, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Use a subtle gradient background for the main panel
        mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Create subtle pattern on the table
                g2d.setColor(TABLE_BACKGROUND);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Draw subtle pattern
                g2d.setColor(TABLE_PATTERN);
                for (int i = 0; i < getWidth(); i += 30) {
                    for (int j = 0; j < getHeight(); j += 30) {
                        if ((i / 30 + j / 30) % 2 == 0) {
                            g2d.fillRect(i, j, 15, 15);
                        }
                    }
                }
            }
        };

        /// Create stylish table panel with curved borders
        tablePanel = new RoundedPanel(25, tableColor, 0.9f);
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        /// Table title with stylish gold gradient text
        tableInfoLabel = new GradientLabel("GROUP 5 - BLACKJACK - TABLE " + tableId,
                new Color(255, 215, 0), new Color(184, 134, 11),
                SwingConstants.CENTER);
        tableInfoLabel.setFont(TITLE_FONT);
        tableInfoLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Occupancy indicator with improved visual design
        occupancyLabel = new JLabel("<html><b>PLAYERS:</b> " + occupancy + "/" + maxPlayers + "</html>",
                SwingConstants.CENTER);
        occupancyLabel.setFont(LABEL_FONT);
        occupancyLabel.setForeground(Color.WHITE);
        occupancyLabel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(Color.WHITE, 2, 10),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        /// Create dealer position with visual design
        dealerPanel = new DealerPositionPanel(dealerName);

        /// Initialize dealer cards area
        dealerCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        dealerCardsPanel.setOpaque(false);

        /// Create player positions with improved visuals
        playerPositions = new ArrayList<>();
        playerCardsPanels = new ArrayList<>();

        /// Create timer display with digital appearance
        timerPanel = new JPanel(new GridLayout(1, 4, 3, 0));
        timerPanel.setOpaque(false);
        timerLabels = new JLabel[4];

        for (int i = 0; i < 4; i++) {
            JPanel timerBox = new RoundedPanel(8, new Color(0, 0, 0, 150), 1.0f);
            timerBox.setLayout(new BorderLayout());

            timerLabels[i] = new JLabel("0", SwingConstants.CENTER);
            timerLabels[i].setFont(new Font("Monospaced", Font.BOLD, 22));
            timerLabels[i].setForeground(new Color(255, 60, 60)); // Red LED color

            timerBox.add(timerLabels[i], BorderLayout.CENTER);
            timerPanel.add(timerBox);
        }

        /// Create stylish control buttons
        drawCardButton = createStyledButton("Draw Card", new Color(40, 167, 69), Color.WHITE);
        payPlayerButton = createStyledButton("Pay Player", new Color(255, 193, 7), Color.BLACK);
        shuffleButton = createStyledButton("Shuffle Deck", new Color(23, 162, 184), Color.WHITE);
        leaveButton = createStyledButton("Leave Table", new Color(220, 53, 69), Color.WHITE);

        /// Set initial button states
        drawCardButton.setEnabled(false);
        payPlayerButton.setEnabled(false);
        shuffleButton.setEnabled(true);

        /// Control panel with sleek transparent background
        controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        controlPanel.setOpaque(false);

        /// Status panel
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

        // Create dealer name badge
        JPanel dealerInfoPanel = new JPanel();
        dealerInfoPanel.setOpaque(false);
        dealerInfoPanel.setLayout(new BoxLayout(dealerInfoPanel, BoxLayout.Y_AXIS));

        JLabel dealerTitle = new JLabel("DEALER", SwingConstants.CENTER);
        dealerTitle.setForeground(Color.WHITE);
        dealerTitle.setFont(new Font("Arial", Font.BOLD, 12));
        dealerTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel dealerNameLabel = new JLabel(dealerName, SwingConstants.CENTER);
        dealerNameLabel.setForeground(GOLD_ACCENT);
        dealerNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dealerNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        dealerInfoPanel.add(dealerTitle);
        dealerInfoPanel.add(Box.createVerticalStrut(3));
        dealerInfoPanel.add(dealerNameLabel);

        /// Set up the card table layout with playing surface
        JPanel playingSurfacePanel = createPlayingSurface();

        /// Set up player positioning around the table
        setupPlayerPositions(playingSurfacePanel);

        /// Add dealer and playing area
        JPanel dealerAreaPanel = new JPanel(new BorderLayout(0, 5));
        dealerAreaPanel.setOpaque(false);
        dealerAreaPanel.add(dealerInfoPanel, BorderLayout.NORTH);
        dealerAreaPanel.add(dealerPanel, BorderLayout.CENTER);
        dealerAreaPanel.add(dealerCardsPanel, BorderLayout.SOUTH);

        playingSurfacePanel.add(dealerAreaPanel, BorderLayout.NORTH);

        /// Add playing surface to the table panel
        tablePanel.add(headerPanel, BorderLayout.NORTH);
        tablePanel.add(playingSurfacePanel, BorderLayout.CENTER);

        // Set up bottom controls section
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);

        // Create timer display with title
        JPanel timerContainer = new JPanel(new BorderLayout(0, 5));
        timerContainer.setOpaque(false);
        JLabel timerTitle = new JLabel("TURN TIMER", SwingConstants.CENTER);
        timerTitle.setForeground(Color.WHITE);
        timerTitle.setFont(new Font("Arial", Font.BOLD, 14));
        timerContainer.add(timerTitle, BorderLayout.NORTH);
        timerContainer.add(timerPanel, BorderLayout.CENTER);

        // Add control buttons
        controlPanel.add(drawCardButton);
        controlPanel.add(payPlayerButton);
        controlPanel.add(shuffleButton);

        // Create separate panel for leave button (right-aligned)
        JPanel leaveButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        leaveButtonPanel.setOpaque(false);
        leaveButtonPanel.add(leaveButton);

        // Add panels to bottom section with proper spacing
        bottomPanel.add(Box.createVerticalStrut(10));
        bottomPanel.add(timerContainer);
        bottomPanel.add(Box.createVerticalStrut(20));
        bottomPanel.add(controlPanel);
        bottomPanel.add(Box.createVerticalStrut(15));
        bottomPanel.add(leaveButtonPanel);

        tablePanel.add(bottomPanel, BorderLayout.SOUTH);

        /// Add the finalized table panel to the main panel
        mainPanel.add(tablePanel, BorderLayout.CENTER);
    }

    /// Creates the playing surface with appropriate design
    private JPanel createPlayingSurface() {
        JPanel playingSurfacePanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                /// Enable anti-aliasing
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw an elliptical playing area with gradient
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(0, 100, 0, 100),
                        getWidth(), getHeight(), new Color(0, 70, 0, 100));
                g2d.setPaint(gradient);

                g2d.fillOval(50, 50, getWidth() - 100, getHeight() - 100);

                // Draw the edge of the playing area
                g2d.setColor(new Color(255, 255, 255, 50));
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawOval(50, 50, getWidth() - 100, getHeight() - 100);

                // Draw decorative card suit symbols
                drawCardSuitSymbols(g2d);
            }

            private void drawCardSuitSymbols(Graphics2D g2d) {
                // Draw semi-transparent card suit symbols
                g2d.setColor(new Color(255, 255, 255, 15));
                Font suitFont = new Font("Serif", Font.BOLD, 40);
                g2d.setFont(suitFont);

                // Hearts, Diamonds, Clubs, Spades symbols
                String[] suits = {"♥", "♦", "♣", "♠"};
            }
        };
        playingSurfacePanel.setOpaque(false);
        playingSurfacePanel.setPreferredSize(new Dimension(900, 400));

        return playingSurfacePanel;
    }

    /// Set up player positions in a semi-circle
    private void setupPlayerPositions(JPanel surface) {
        // Create and position player spots in a semi-circle
        JPanel playerPositionPanel = new JPanel();
        playerPositionPanel.setLayout(null); // Use absolute positioning
        playerPositionPanel.setOpaque(false);

        /// Create player positions
        for (int i = 0; i < maxPlayers; i++) {
            PlayerPositionPanel position = new PlayerPositionPanel(i < occupancy, "P" + (i + 1));
            playerPositions.add(position);
            playerPositionPanel.add(position);

            // Create card area for this player
            JPanel cardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            cardPanel.setOpaque(false);
            playerCardsPanels.add(cardPanel);
            playerPositionPanel.add(cardPanel);
        }

        /// Position players in a semi-circle and their respective card panels
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

    /**
     * Set up event listeners for UI components
     */
    private void setupListeners() {
        // Leave button action
        leaveButton.addActionListener(e -> showLeaveConfirmation());

        // Shuffle button action
        shuffleButton.addActionListener(e -> {
            requestShuffle();
            animateShuffleDeck();
        });

        // Draw card button (for dealer's turn)
        drawCardButton.addActionListener(e -> {
            drawCardForDealer();
        });

        // Pay player button action
        payPlayerButton.addActionListener(e -> {
            showPayPlayerDialog();
        });
    }

    /**
     * Set up message hooks for server communication
     */
    private void setupMessageHooks() {
        // Player Ready notification
        BlackjackGame.client.addMessageHook(Message.PlayerReady.Response.class, (response) -> {
            if (response.getStatus()) {
                SwingUtilities.invokeLater(() -> {
                    // Highlight the shuffle button to prompt the dealer
                    shuffleButton.setBackground(new Color(50, 200, 255));

                    // Store player ready status
                    String playerName = response.getPlayerName();
                    playerReadyStatus.put(playerName, true);

                    // Find and mark the player position as ready
                    int playerPos = getPlayerPositionIndex(playerName);
                    if (playerPos >= 0 && playerPos < playerPositions.size()) {
                        playerPositions.get(playerPos).setReady(true);
                        playerPositions.get(playerPos).repaint();
                    }

                    // Show notification dialog
                    if (!hasDisplayedReadyNotification) {
                        JOptionPane.showMessageDialog(this,
                                "Player " + response.getPlayerName() + " is ready. Please shuffle the deck when all players are ready.",
                                "Player Ready", JOptionPane.INFORMATION_MESSAGE);
                        hasDisplayedReadyNotification = true;
                    }
                });
            }
        });

        // Shuffle response
        BlackjackGame.client.addMessageHook(Message.Shuffle.Response.class, (response) -> {
            if (response.getStatus()) {
                System.out.println("Shuffle successful");
                // Animation is handled by the shuffle button click event
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to shuffle deck. Please try again.",
                        "Shuffle Error", JOptionPane.ERROR_MESSAGE);
                shuffleButton.setEnabled(true);
            }
        });

        // Add this to the setupMessageHooks() method in DealerTableBlackJack.java

// Handle GameState.Update messages
        // In DealerTableBlackJack.java - modify the GameState.Update handler
        BlackjackGame.client.addMessageHook(Message.GameState.Update.class, (update) -> {
            System.out.println("GameState update received - dealer turn: " + update.isDealerTurn() +
                    ", player turn: " + update.isPlayerTurn() +
                    ", round complete: " + update.isRoundComplete());

            safeUIUpdate(() -> {
                // Update dealer controls based on whose turn it is
                if (update.isDealerTurn()) {
                    // It's dealer's turn - enable draw card button
                    drawCardButton.setEnabled(true && !dealerMustStand);

                    // Set currentActivePlayer if provided in the message
                    if (update.getResultMessage() != null &&
                            update.getResultMessage().contains("Player") &&
                            update.getResultMessage().contains("busted")) {
                        // Extract player name from message if it contains "Player X busted"
                        String msg = update.getResultMessage();
                        int playerIdx = msg.indexOf("Player ") + 7;
                        int endIdx = msg.indexOf(" ", playerIdx);
                        if (endIdx > playerIdx) {
                            currentActivePlayer = msg.substring(playerIdx, endIdx);
                        }
                    }
                } else {
                    // It's not dealer's turn - disable draw card button
                    drawCardButton.setEnabled(false);
                }

                // Always keep pay player button enabled
                payPlayerButton.setEnabled(true);

                // If a specific message is provided, display it
                if (update.getResultMessage() != null) {
                    JOptionPane.showMessageDialog(DealerTableBlackJack.this,
                            update.getResultMessage(),
                            "Game Update",
                            JOptionPane.INFORMATION_MESSAGE);
                }

                // Handle round completion
                if (update.isRoundComplete()) {
                    // Reset UI for next round
                    shuffleButton.setEnabled(true);
                }
            });
        });
        // Deal Initial Cards response
        BlackjackGame.client.addMessageHook(Message.DealInitialCards.Response.class, (response) -> {
            if (response.getStatus()) {
                safeUIUpdate(() -> {
                    // Display dealer cards
                    dealerCardsPanel.removeAll();
                    Card[] dealerCards = response.getDealerCards();

                    // Display dealer's cards - both visible to dealer
                    for (Card card : dealerCards) {
                        CardPanel cardPanel = new CardPanel(card.getValue().toString(),
                                getSuitSymbol(card.getSuit()));
                        // Dealer can see both cards
                        cardPanel.setFaceDown(false);
                        dealerCardsPanel.add(cardPanel);
                    }

                    // Find the player position
                    String playerName = response.getPlayerName();
                    int playerPosition = getPlayerPositionIndex(playerName);

                    if (playerPosition >= 0) {
                        // Get the player's card panel
                        JPanel playerCardPanel = playerCardsPanels.get(playerPosition);
                        playerCardPanel.removeAll();

                        // Show player's cards in dealer view
                        Card[] playerCards = response.getPlayerCards();
                        for (Card card : playerCards) {
                            CardPanel playerCardUI = new CardPanel(card.getValue().toString(),
                                    getSuitSymbol(card.getSuit()));
                            playerCardUI.setFaceUp(true); // Dealer can see all cards
                            playerCardPanel.add(playerCardUI);
                        }
                    }

                    // Calculate current hand values
                    calculateDealerHandValue();

                    // Refresh display
                    dealerCardsPanel.revalidate();
                    dealerCardsPanel.repaint();

                    for (JPanel panel : playerCardsPanels) {
                        panel.revalidate();
                        panel.repaint();
                    }

                    // Enable appropriate game controls
                    drawCardButton.setEnabled(false); // Wait for player actions
                    payPlayerButton.setEnabled(true);
                    shuffleButton.setEnabled(false);

                    // Check for dealer blackjack
                    if (dealerHandValue == 21) {
                        handleDealerBlackjack();
                    }
                });
            }
        });

        // Hit response for player's hit action
        BlackjackGame.client.addMessageHook(Message.Hit.Request.class, (request) -> {
            String playerName = request.getPlayerName();

            // Find player position
            int playerPos = getPlayerPositionIndex(playerName);
            if (playerPos >= 0) {
                // Generate a card for the player
                Card newCard = generateRandomCard();

                // Show the card in the dealer's view of that player
                JPanel playerCardPanel = playerCardsPanels.get(playerPos);

                safeUIUpdate(() -> {
                    CardPanel cardPanel = new CardPanel(
                            newCard.getValue().toString(),
                            getSuitSymbol(newCard.getSuit()));
                    cardPanel.setFaceUp(true); // Dealer can see all cards
                    playerCardPanel.add(cardPanel);
                    playerCardPanel.revalidate();
                    playerCardPanel.repaint();
                });

                // Create response back to player
                CardHand dummyHand = new CardHand(21);
                dummyHand.addCard(newCard);

                Message.Hit.Response response = new Message.Hit.Response(
                        newCard, dummyHand, true);

                // Send the response
                BlackjackGame.client.sendNetworkMessage(response);

                // Check for bust
                int playerHandValue = calculateHandValue(playerCardPanel);
                if (playerHandValue > 21) {
                    handlePlayerBust(playerName);
                }
            }
        });

        // Stand request from player
        BlackjackGame.client.addMessageHook(Message.Stand.Request.class, (request) -> {
            String playerName = request.getPlayerName();
            System.out.println("Player " + playerName + " stands");

            // Now it's dealer's turn
            drawCardButton.setEnabled(true);

            // Calculate dealer's current hand value
            calculateDealerHandValue();

            // If dealer has 17 or more, auto-stand
            if (dealerHandValue >= 17) {
                dealerMustStand = true;
                handleDealerFinish(playerName);
            } else {
                // Prompt dealer to draw cards
                JOptionPane.showMessageDialog(this,
                        "Player has stood. Now deal cards to yourself until you reach 17 or higher.",
                        "Dealer's Turn", JOptionPane.INFORMATION_MESSAGE);

                currentActivePlayer = playerName;
            }
        });
    }

    /**
     * Handles dealer's blackjack
     */
    private void handleDealerBlackjack() {
        JOptionPane.showMessageDialog(this,
                "You have Blackjack!",
                "Blackjack", JOptionPane.INFORMATION_MESSAGE);

        // Reveal dealer's face-down card
        for (Component component : dealerCardsPanel.getComponents()) {
            if (component instanceof CardPanel) {
                CardPanel cardPanel = (CardPanel) component;
                cardPanel.setFaceDown(false);
            }
        }

        // Dealer wins against all players who don't have blackjack
        for (String playerName : playerPositionMap.keySet()) {
            int playerPos = playerPositionMap.get(playerName);
            JPanel playerCardPanel = playerCardsPanels.get(playerPos);

            // Calculate player hand value
            int playerHandValue = calculateHandValue(playerCardPanel);

            // Create a response indicating dealer blackjack
            boolean playerWins = false;
            if (playerHandValue == 21 && playerCardPanel.getComponentCount() == 2) {
                // Push - player also has blackjack
                playerWins = false; // Push is not a win
                payPlayer(playerName, 1.0f); // Return bet

                // Notify player of push
                Message.GameResult.Response result = new Message.GameResult.Response(
                        false, true, "Push - both you and dealer have Blackjack",
                        0, playerHandValue, dealerHandValue, playerName, tableId);
                BlackjackGame.client.sendNetworkMessage(result);
            } else {
                // Dealer wins
                Message.GameResult.Response result = new Message.GameResult.Response(
                        false, false, "Dealer has Blackjack. You lose.",
                        0, playerHandValue, dealerHandValue, playerName, tableId);
                BlackjackGame.client.sendNetworkMessage(result);
            }
        }

        // Reset for next round
        resetGameAfterDelay();
    }

    /**
     * Handles player bust
     */
    private void handlePlayerBust(String playerName) {
        System.out.println("Player " + playerName + " busted");

        // Create game result message
        int playerPos = getPlayerPositionIndex(playerName);
        if (playerPos >= 0) {
            JPanel playerCardPanel = playerCardsPanels.get(playerPos);
            int playerHandValue = calculateHandValue(playerCardPanel);

            Message.GameResult.Response result = new Message.GameResult.Response(
                    false, false, "Bust! Your hand value: " + playerHandValue,
                    0, playerHandValue, dealerHandValue, playerName, tableId);
            BlackjackGame.client.sendNetworkMessage(result);
        }
    }

    /**
     * Handles dealer's finished turn
     */
    private void handleDealerFinish(String playerName) {
        // Reveal dealer's face-down card
        for (Component component : dealerCardsPanel.getComponents()) {
            if (component instanceof CardPanel) {
                CardPanel cardPanel = (CardPanel) component;
                cardPanel.setFaceDown(false);
            }
        }

        // Compare hands
        int playerPos = getPlayerPositionIndex(playerName);
        if (playerPos >= 0) {
            JPanel playerCardPanel = playerCardsPanels.get(playerPos);
            int playerHandValue = calculateHandValue(playerCardPanel);

            boolean playerWins = false;
            boolean push = false;
            String resultMessage;
            int winnings = 0;

            // Determine winner
            if (dealerHandValue > 21) {
                // Dealer busts
                playerWins = true;
                resultMessage = "Dealer busts with " + dealerHandValue + ". You win!";
                winnings = 2; // Original bet + 1x
                payPlayer(playerName, 2.0f);
            } else if (playerHandValue > dealerHandValue) {
                // Player has higher value
                playerWins = true;
                resultMessage = "You win with " + playerHandValue + " against dealer's " + dealerHandValue;
                winnings = 2; // Original bet + 1x
                payPlayer(playerName, 2.0f);
            } else if (dealerHandValue > playerHandValue) {
                // Dealer has higher value
                playerWins = false;
                resultMessage = "Dealer wins with " + dealerHandValue + " against your " + playerHandValue;
                winnings = 0;
            } else {
                // Push (tie)
                push = true;
                resultMessage = "Push. Your hand: " + playerHandValue + ", Dealer hand: " + dealerHandValue;
                winnings = 1; // Return original bet
                payPlayer(playerName, 1.0f);
            }

            // Send result to player
            Message.GameResult.Response result = new Message.GameResult.Response(
                    playerWins, push, resultMessage, winnings, playerHandValue,
                    dealerHandValue, playerName, tableId);
            BlackjackGame.client.sendNetworkMessage(result);
        }

        // Disable dealer controls
        drawCardButton.setEnabled(false);

        // Reset for next round
        resetGameAfterDelay();
    }

    /**
     * Pay a player a specified amount
     */
    private void payPlayer(String playerName, float multiplier) {
        // In a real implementation, this would update the server-side player wallet
        System.out.println("Paying player " + playerName + " " + multiplier + "x their bet");
    }

    /**
     * Reset the game state after a round is complete
     */
    private void resetGameAfterDelay() {
        Timer resetTimer = new Timer(3000, e -> {
            // Clear all cards
            dealerCardsPanel.removeAll();
            for (JPanel panel : playerCardsPanels) {
                panel.removeAll();
            }

            // Reset game state variables
            dealerHandValue = 0;
            dealerMustStand = false;
            currentActivePlayer = null;
            hasDisplayedReadyNotification = false;

            // Reset player ready status
            for (String player : playerReadyStatus.keySet()) {
                playerReadyStatus.put(player, false);
            }

            // Update UI
            for (PlayerPositionPanel position : playerPositions) {
                position.setReady(false);
            }

            // Enable shuffle for next round
            shuffleButton.setEnabled(true);
            drawCardButton.setEnabled(false);
            payPlayerButton.setEnabled(false);

            // Refresh display
            revalidate();
            repaint();
        });
        resetTimer.setRepeats(false);
        resetTimer.start();
    }

    /**
     * Draw a card for the dealer
     */
    // In DealerTableBlackJack.java

// Modify the drawCardForDealer method
    // In DealerTableBlackJack.java - modify the drawCardForDealer method
    private void drawCardForDealer() {
        // Generate a random card
        Card newCard = generateRandomCard();

        // Add the card to dealer's hand
        safeUIUpdate(() -> {
            CardPanel cardPanel = new CardPanel(
                    newCard.getValue().toString(),
                    getSuitSymbol(newCard.getSuit()));
            cardPanel.setFaceUp(true);
            dealerCardsPanel.add(cardPanel);
            dealerCardsPanel.revalidate();
            dealerCardsPanel.repaint();

            // Recalculate dealer hand value
            calculateDealerHandValue();

            // Check if dealer must stand
            if (dealerHandValue >= 17) {
                dealerMustStand = true;
                drawCardButton.setEnabled(false);

                // Show message
                JOptionPane.showMessageDialog(this,
                        "Dealer stands with hand value: " + dealerHandValue,
                        "Dealer Stands", JOptionPane.INFORMATION_MESSAGE);

                // Handle end of dealer's turn
                if (currentActivePlayer != null) {
                    handleDealerFinish(currentActivePlayer);
                }
                return; // Exit early if dealer must stand
            }

            // Check if dealer busted
            if (dealerHandValue > 21) {
                drawCardButton.setEnabled(false);

                JOptionPane.showMessageDialog(this,
                        "Dealer busted with hand value: " + dealerHandValue,
                        "Dealer Busted", JOptionPane.INFORMATION_MESSAGE);

                // Handle end of dealer's turn
                if (currentActivePlayer != null) {
                    handleDealerFinish(currentActivePlayer);
                }
                return; // Exit early if dealer busts
            }

            // Pass turn to player after dealer draws (if we didn't stand or bust)
            if (currentActivePlayer != null) {
                // Wait a short moment to allow the player to see the dealer's card
                Timer turnTimer = new Timer(1000, evt -> {
                    // Create a game state update for players - it's now player's turn
                    Message.GameState.Update playerTurnMsg =
                            new Message.GameState.Update(false, true, false,
                                    "Dealer drew " + newCard.getValue() + " of " + getSuitSymbol(newCard.getSuit()) +
                                            ". Your turn now.");

                    // Send directly to the client
                    BlackjackGame.client.sendNetworkMessage(playerTurnMsg);

                    // Update dealer UI - disable dealer controls until next turn
                    drawCardButton.setEnabled(false);
                });
                turnTimer.setRepeats(false);
                turnTimer.start();
            }
        });

        // Notify players about dealer's new card
        CardHand dummyHand = new CardHand(21);
        dummyHand.addCard(newCard);

        Message.Hit.Response dealerHitResponse = new Message.Hit.Response(
                newCard, dummyHand, true, true); // Flag as dealer card

        BlackjackGame.client.sendNetworkMessage(dealerHitResponse);
    }

    /**
     * Calculate dealer's current hand value
     */
    private void calculateDealerHandValue() {
        dealerHandValue = calculateHandValue(dealerCardsPanel);
    }

    /**
     * Calculate hand value from a panel of cards
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
     * Get the position index for a player by name
     */
    private int getPlayerPositionIndex(String playerName) {
        // Check if we already know this player's position
        if (playerPositionMap.containsKey(playerName)) {
            return playerPositionMap.get(playerName);
        }

        // Find first available position
        for (int i = 0; i < playerPositions.size(); i++) {
            if (!playerPositions.get(i).isOccupied()) {
                // Assign this position to the player
                playerPositions.get(i).setOccupied(true);
                playerPositionMap.put(playerName, i);
                return i;
            }
        }

        // If all positions are occupied, use the first one (not ideal but fallback)
        if (!playerPositionMap.isEmpty()) {
            return 0;
        }

        return -1; // No position available
    }

    private String getSuitSymbol(Suit suit) {
        return switch (suit) {
            case HEARTS -> "♥";
            case DIAMONDS -> "♦";
            case CLUBS -> "♣";
            case SPADES -> "♠";
        };
    }

    /**
     * Generate a random card
     */
    private Card generateRandomCard() {
        // Generate a random suit
        Suit[] suits = {Suit.HEARTS, Suit.DIAMONDS, Suit.CLUBS, Suit.SPADES};
        Suit randomSuit = suits[(int) (Math.random() * suits.length)];

        // Generate a random value
        Value[] values = {
                Value.ACE, Value.TWO, Value.THREE, Value.FOUR, Value.FIVE,
                Value.SIX, Value.SEVEN, Value.EIGHT, Value.NINE, Value.TEN,
                Value.JACK, Value.QUEEN, Value.KING
        };
        Value randomValue = values[(int) (Math.random() * values.length)];

        // Create and return the card
        return new Card(randomSuit, randomValue);
    }

    /**
     * Request shuffle from the server
     */
    private void requestShuffle() {
        System.out.println("Requesting shuffle for table " + tableId);
        Message.Shuffle.Request request = new Message.Shuffle.Request(tableId);
        BlackjackGame.client.sendNetworkMessage(request);
    }

    /**
     * Convert String to Suit
     */
    private Suit convertStringToSuit(String suitSymbol) {
        return switch (suitSymbol) {
            case "♥" -> Suit.HEARTS;
            case "♦" -> Suit.DIAMONDS;
            case "♣" -> Suit.CLUBS;
            case "♠" -> Suit.SPADES;
            default -> Suit.SPADES; // Default fallback
        };
    }

    /**
     * Convert String to Value
     */
    private Value convertStringToValue(String valueSymbol) {
        return switch (valueSymbol) {
            case "A" -> Value.ACE;
            case "2" -> Value.TWO;
            case "3" -> Value.THREE;
            case "4" -> Value.FOUR;
            case "5" -> Value.FIVE;
            case "6" -> Value.SIX;
            case "7" -> Value.SEVEN;
            case "8" -> Value.EIGHT;
            case "9" -> Value.NINE;
            case "10" -> Value.TEN;
            case "J" -> Value.JACK;
            case "Q" -> Value.QUEEN;
            case "K" -> Value.KING;
            default -> Value.ACE; // Default fallback
        };
    }

    /**
     * Animate shuffling the deck
     */
    private void animateShuffleDeck() {
        // Disable buttons during animation
        shuffleButton.setEnabled(false);
        drawCardButton.setEnabled(false);
        payPlayerButton.setEnabled(false);

        // Create a shuffling effect
        JDialog shuffleDialog = new JDialog(this, "Shuffling", false);
        shuffleDialog.setSize(300, 200);
        shuffleDialog.setLocationRelativeTo(this);

        JPanel animationPanel = new JPanel() {
            private int animationStep = 0;
            private Timer timer = new Timer(50, e -> {
                animationStep = (animationStep + 1) % 8;
                repaint();
            });

            {
                timer.start();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw shuffling animation
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;

                for (int i = 0; i < 8; i++) {
                    int angle = (i * 45 + animationStep * 15) % 360;
                    double radians = Math.toRadians(angle);

                    int x = (int) (centerX + 50 * Math.cos(radians));
                    int y = (int) (centerY + 50 * Math.sin(radians));

                    // Draw card back
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(x - 15, y - 20, 30, 40);
                    g2d.setColor(Color.RED);
                    g2d.drawRect(x - 15, y - 20, 30, 40);
                    g2d.drawLine(x - 15, y - 20, x + 15, y + 20);
                    g2d.drawLine(x + 15, y - 20, x - 15, y + 20);
                }
            }
        };

        JLabel shuffleLabel = new JLabel("Shuffling Deck...", SwingConstants.CENTER);
        shuffleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        shuffleDialog.setLayout(new BorderLayout());
        shuffleDialog.add(animationPanel, BorderLayout.CENTER);
        shuffleDialog.add(shuffleLabel, BorderLayout.SOUTH);

        // Show animation for 2 seconds
        shuffleDialog.setVisible(true);

        Timer closeTimer = new Timer(2000, e -> {
            shuffleDialog.dispose();

            // Clear all cards
            dealerCardsPanel.removeAll();
            for (JPanel panel : playerCardsPanels) {
                panel.removeAll();
            }

            // Reset game state
            dealerHandValue = 0;
            dealerMustStand = false;

            // Re-enable buttons
            shuffleButton.setEnabled(false); // Don't allow reshuffling until next round
            drawCardButton.setEnabled(false);
            payPlayerButton.setEnabled(false);

            // Refresh display
            revalidate();
            repaint();

            JOptionPane.showMessageDialog(DealerTableBlackJack.this,
                    "Deck has been shuffled. Waiting for players to place bets.",
                    "Shuffle Complete", JOptionPane.INFORMATION_MESSAGE);

            // Send ShuffleComplete to all players
            Message.ShuffleComplete.Response notification = new Message.ShuffleComplete.Response(tableId);
            BlackjackGame.client.sendNetworkMessage(notification);
        });
        closeTimer.setRepeats(false);
        closeTimer.start();
    }

    /**
     * Show pay player dialog
     */
    private void showPayPlayerDialog() {
        // Create array of player names based on occupancy
        String[] playerOptions = new String[playerPositionMap.size()];
        int index = 0;
        for (String playerName : playerPositionMap.keySet()) {
            playerOptions[index++] = playerName;
        }

        if (playerOptions.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "No players to pay.",
                    "No Players", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Create custom panel with combo box and amount field
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<String> playerCombo = new JComboBox<>(playerOptions);
        JTextField amountField = new JTextField("50");

        panel.add(new JLabel("Select Player:"));
        panel.add(playerCombo);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Pay Player", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String playerName = (String) playerCombo.getSelectedItem();
            int amount = Integer.parseInt(amountField.getText());

            JOptionPane.showMessageDialog(this,
                    "Paid " + playerName + " $" + amount,
                    "Payment Complete", JOptionPane.INFORMATION_MESSAGE);

            // In a real implementation, you'd update the player's wallet on the server
            payPlayer(playerName, amount / 50.0f);
        }
    }

    /**
     * Show confirmation dialog when leaving table
     */
    private void showLeaveConfirmation() {
        // Create a custom styled dialog for leave confirmation
        JDialog confirmDialog = new JDialog(this, "Confirm Leave", true);
        confirmDialog.setSize(400, 200);
        confirmDialog.setLocationRelativeTo(this);

        JPanel dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        dialogPanel.setBackground(new Color(245, 245, 245));

        JLabel confirmMessage = new JLabel("Are you sure you want to close this table?", SwingConstants.CENTER);
        confirmMessage.setFont(new Font("Arial", Font.BOLD, 16));
        confirmMessage.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Styled note panel
        JPanel notePanel = new RoundedPanel(10, new Color(255, 243, 205), 1.0f);
        notePanel.setLayout(new BorderLayout());
        notePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel noteLabel = new JLabel("Current players will experience no penalty", SwingConstants.CENTER);
        noteLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        noteLabel.setForeground(new Color(133, 100, 4));
        notePanel.add(noteLabel, BorderLayout.CENTER);

        // Styled button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        JButton noButton = createStyledButton("No", new Color(108, 117, 125), Color.BLACK);
        JButton yesButton = createStyledButton("Yes", new Color(220, 53, 69), Color.BLACK);

        buttonPanel.add(noButton);
        buttonPanel.add(yesButton);

        dialogPanel.add(confirmMessage, BorderLayout.NORTH);
        dialogPanel.add(notePanel, BorderLayout.CENTER);
        dialogPanel.add(buttonPanel, BorderLayout.SOUTH);

        confirmDialog.setContentPane(dialogPanel);

        noButton.addActionListener(e -> confirmDialog.dispose());
        yesButton.addActionListener(e -> {
            confirmDialog.dispose();

            // Send DealerLeave message to server
            Message.DealerLeave.Request request = new Message.DealerLeave.Request(
                    0, tableId); // Use actual dealer ID in real implementation
            BlackjackGame.client.sendNetworkMessage(request);

            dispose(); // Close the table window
        });

        confirmDialog.setVisible(true);
    }

    /**
     * Helper method to create styled buttons
     */
    private JButton createStyledButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
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