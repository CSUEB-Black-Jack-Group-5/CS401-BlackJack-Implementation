package client.DealerTable;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

///  Dealer Table
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

    /// Custom colors
    private static final Color TABLE_BACKGROUND = new Color(0, 80, 20);  // Darker green
    private static final Color TABLE_PATTERN = new Color(0, 70, 20);     // Pattern color
    private static final Color TABLE_COLOR = new Color(30, 90, 110);   // Deep teal
    private static final Color GOLD_ACCENT = new Color(212, 175, 55);    // Gold accent

    // Custom fonts
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 28);
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font INFO_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font COUNT_FONT = new Font("Digital-7", Font.BOLD, 24);

    /**
     * Constructor for the Table.DealerTableBlackJack class
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

        // Initialize the countdown timer
        // Will do in server
        // countdownTimer = new Timer(1000, e -> updateTimer());
    }

    /// Initialize all UI components
    private void initComponents() {
        // Set up the frame with improved dimensions for widescreen displays

        // table Id will getting response from server
        // setTitle("Group5 Blackjack - Table #" + tableId);
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

        // Timer will do in server
        // updateTimerDisplay(timeRemaining);

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

                // Draw suits at various positions
//                for (int i = 0; i < 8; i++) {
//                    String suit = suits[i % 4];
//                    int x = 100 + (int) (Math.random() * (getWidth() - 200));
//                    int y = 100 + (int) (Math.random() * (getHeight() - 200));
//                    g2d.drawString(suit, x, y);
//                }
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
        int centerX = 450; // Center X coordinate
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

        ///  Think about how deal with server
//        // Draw card button action
//        drawCardButton.addActionListener(e -> {
//            drawCardForRandomPlayer();
//        });
//
//        // Pay player button action
//        payPlayerButton.addActionListener(e -> {
//            showPayPlayerDialog();
//        });

        // Shuffle button action
        shuffleButton.addActionListener(e -> {
            animateShuffleDeck();
        });
    }

    // Draw a card for a random player (demo functionality)
    private void drawCardForRandomPlayer() {
        // Select a random player with probability based on occupancy
//        int playerIndex = (int) (Math.random() * occupancy);
//        if (playerIndex >= playerPositions.size()) {
//            playerIndex = 0; // Safety check
//        }

        /// Add a card to that player's area
//        JPanel playerCardPanel = playerCardsPanels.get(playerIndex);
//        CardPanel newCard = new CardPanel(getRandomCardValue(), getRandomCardSuit());
//        playerCardPanel.add(newCard);
//        playerCardPanel.revalidate();
//        playerCardPanel.repaint();

        // Potentially add a card to dealer as well (50% chance)
//        if (Math.random() > 0.5) {
//            CardPanel dealerCard = new CardPanel(getRandomCardValue(), getRandomCardSuit());
//            dealerCardsPanel.add(dealerCard);
//            dealerCardsPanel.revalidate();
//            dealerCardsPanel.repaint();
//        }

        // Start or reset timer will do in server
        // startTimer();
    }

    /// Generate a random card value

    private String getRandomCardValue() {
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

        ///  Do it in random but now server will do
        return values[(int) (Math.random() * values.length)];
    }

    /**
     * Generate a random card suit
     */
    private String getRandomCardSuit() {
        String[] suits = {"♥", "♦", "♣", "♠"};
        return suits[(int) (Math.random() * suits.length)];
    }

    /**
     * Show dialog for paying a player
     */
    private void showPayPlayerDialog() {
        // Create array of player names based on occupancy
        String[] playerOptions = new String[occupancy];
        for (int i = 0; i < occupancy; i++) {
            playerOptions[i] = "Player " + (i + 1);
        }

        // Create custom panel with combo box and amount field
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<String> playerCombo = new JComboBox<>(playerOptions);
        JTextField amountField = new JTextField("50.00");

        panel.add(new JLabel("Select Player:"));
        panel.add(playerCombo);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Pay Player", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String player = (String) playerCombo.getSelectedItem();
            String amount = amountField.getText();

            JOptionPane.showMessageDialog(this,
                    "Paid " + player + " $" + amount,
                    "Payment Complete", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Animate shuffling the deck
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

            // Re-enable buttons
            shuffleButton.setEnabled(true);
            drawCardButton.setEnabled(true);
            payPlayerButton.setEnabled(true);

            // Refresh display
            revalidate();
            repaint();

            JOptionPane.showMessageDialog(DealerTableBlackJack.this,
                    "Deck has been shuffled", "Shuffle Complete", JOptionPane.INFORMATION_MESSAGE);
        });
        closeTimer.setRepeats(false);
        closeTimer.start();
    }

    // Show confirmation dialog when leaving table
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
            dispose(); // Close the table window
        });

        confirmDialog.setVisible(true);
    }

    // Helper method to create styled buttons
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
}