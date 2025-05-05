package client.PlayerTable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/// Main class for the Blackjack Player Table GUI
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
    /// Maybe this chip can be change in your funds
    private int chipCount = 1000; /// Default starting chips
    private int currentBet = 0;
    private JLabel chipsLabel;
    private JLabel betLabel;

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
        // updateTimerDisplay(15); // Default time count maybe server will do it

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
        readyButton.setEnabled(false);

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
    }

    /// Show confirmation dialog when leaving table
    private void showLeaveConfirmation() {
        // Create a custom styled dialog for leave confirmation
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
            dispose(); // Close the table window
        });

        confirmDialog.setVisible(true);
    }
}