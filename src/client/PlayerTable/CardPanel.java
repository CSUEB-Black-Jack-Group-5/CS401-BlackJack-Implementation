package client.PlayerTable;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/// A panel that represents a playing card in the Blackjack game
public class CardPanel extends JPanel {
    private String value;  /// Card value (A, 2-10, J, Q, K)
    private String suit;   /// Card suit (♠, ♥, ♦, ♣)
    private boolean faceUp;

    /// Card appearance constants
    private static final int CORNER_RADIUS = 8;
    private static final Color CARD_BORDER = Color.BLACK;
    private static final Color CARD_BACK_COLOR = new Color(30, 100, 200);

    public CardPanel() {
        this("", "", false);
    }

    public CardPanel(String value, String suit, boolean faceUp) {
        this.value = value;
        this.suit = suit;
        this.faceUp = faceUp;

        setPreferredSize(new Dimension(50, 70));
        setOpaque(false);
    }

    public CardPanel(String value, String suit) {
        this(value, suit, true);
    }

    public String getValue() {
        return value;
    }

    public String getSuit() {
        return suit;
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public void setFaceUp(boolean faceUp) {
        this.faceUp = faceUp;
        repaint();
    }

    public void setFaceDown(boolean faceDown) {
        this.faceUp = !faceDown;
        repaint();
    }
    public void flip() {
        this.faceUp = !this.faceUp;
        repaint();
    }

    private Color getSuitColor() {
        /// Red for hearts and diamonds, black for clubs and spades
        return (suit.equals("♥") || suit.equals("♦")) ? Color.RED : Color.BLACK;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        if (faceUp) {
            drawFaceUpCard(g2d, width, height);
        } else {
            drawFaceDownCard(g2d, width, height);
        }

        g2d.dispose();
    }

    /// Draw a face-up card
    private void drawFaceUpCard(Graphics2D g2d, int width, int height) {
        /// Draw card background (white)
        g2d.setColor(Color.WHITE);
        RoundRectangle2D.Float cardShape = new RoundRectangle2D.Float(
                0, 0, width - 1, height - 1, CORNER_RADIUS, CORNER_RADIUS);
        g2d.fill(cardShape);

        /// Draw card border
        g2d.setColor(CARD_BORDER);
        g2d.draw(cardShape);

        /// Set color based on suit
        g2d.setColor(getSuitColor());

        /// Draw value in top-left corner
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString(value, 3, 12);

        /// Draw suit in top-left corner
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString(suit, 3, 25);

        /// Draw value and suit in bottom-right corner (upside down)
        g2d.rotate(Math.PI, width - 3, height - 12);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString(value, 0, 0);

        g2d.rotate(Math.PI, width - 3, height - 25);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString(suit, 0, 0);
        g2d.rotate(Math.PI, width - 3, height - 25);

        /// Draw suit in center
        g2d.setFont(new Font("Arial", Font.BOLD, 22));
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(suit, (width - fm.stringWidth(suit)) / 2, height / 2 + fm.getAscent() / 2);
    }

    /// Draw a face-down card
    private void drawFaceDownCard(Graphics2D g2d, int width, int height) {
        /// Draw card background (white)
        g2d.setColor(Color.WHITE);
        RoundRectangle2D.Float cardShape = new RoundRectangle2D.Float(
                0, 0, width - 1, height - 1, CORNER_RADIUS, CORNER_RADIUS);
        g2d.fill(cardShape);

        /// Draw card border
        g2d.setColor(CARD_BORDER);
        g2d.draw(cardShape);

        /// Draw card back pattern
        g2d.setColor(CARD_BACK_COLOR);
        RoundRectangle2D.Float backShape = new RoundRectangle2D.Float(
                2, 2, width - 5, height - 5, CORNER_RADIUS - 2, CORNER_RADIUS - 2);
        g2d.fill(backShape);

        /// Draw pattern on card back
        g2d.setColor(new Color(20, 60, 150));
        for (int i = 0; i < width; i += 4) {
            g2d.drawLine(i, 0, i, height);
        }

        for (int i = 0; i < height; i += 4) {
            g2d.drawLine(0, i, width, i);
        }
    }
}