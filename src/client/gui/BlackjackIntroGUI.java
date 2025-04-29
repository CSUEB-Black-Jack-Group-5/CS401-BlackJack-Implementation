package client.gui;

import client.DealerLobbyGUI.DealerLobbyBlackJack;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlackjackIntroGUI extends Component {
    /// game.Card animation components
    private Timer cardAnimationTimer;
    private List<CardAnimate> animatedCards = new ArrayList<>();
    private Random random = new Random();
    private JFrame frame;
    private BlackjackPanel panel;

    /// Constants
    public static final Color DARK_GREEN = new Color(0, 100, 0);
    public static final Color GOLD = new Color(218, 165, 32);
    public static final Color BUTTON_COLOR = new Color(139, 0, 0);
    public static final Color HOVER_COLOR = new Color(178, 34, 34);

    public BlackjackIntroGUI() {
        ///  title
        frame = new JFrame("Blackjack - Group5 Casino");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        /// Set up the content pane with a custom panel
        panel = new BlackjackPanel(this);
        frame.setContentPane(panel);

        /// Start the card animation
        startCardAnimation();
    }

    public void show() {
        frame.setVisible(true);
    }

    private void startCardAnimation() {
        /// Create 10 random cards for animation
        for (int i = 0; i < 10; i++) {
            animatedCards.add(new CardAnimate(
                    random.nextInt(800),
                    random.nextInt(600),
                    random.nextInt(5) + 1,  /// Speed
                    random.nextInt(360)     /// Rotation
            ));
        }

        /// Animation timer
        cardAnimationTimer = new Timer(30, e -> {
            for (CardAnimate card : animatedCards) {
                card.update();
            }
            panel.repaint();
        });
        cardAnimationTimer.start();
    }

    public List<CardAnimate> getAnimatedCards() {
        return animatedCards;
    }

    ///  Method for showing login GUI
    public void showLoginGUI() {
        LoginGUI loginGUI = new LoginGUI(frame);
        loginGUI.setVisible(true);

        /// Login check popup window successful result
        if (loginGUI.isSucceeded()) {
            frame.dispose();  // or frame.setVisible(false);

            // Open the dealer lobby
            DealerLobbyBlackJack dealerLobby = new DealerLobbyBlackJack();
            dealerLobby.setVisible(true);
        }
    }
}