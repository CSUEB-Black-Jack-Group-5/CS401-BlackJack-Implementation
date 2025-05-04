package client.DealerTable;

// File 4: CardPanel.java
import javax.swing.*;
import java.awt.*;

/// Card panel to display playing cards

public class CardPanel extends JPanel {
    private String value;

    ///  maybe doing in gamelogic
    private String suit;
    private boolean isFaceUp = true;

    ///  Playing card panel
    public CardPanel(String value, String suit) {
        this.value = value;
        this.suit = suit;
        setOpaque(false);
        setPreferredSize(new Dimension(50, 70));
    }

    /// Flips the card over
    public void flip() {
        isFaceUp = !isFaceUp;
        repaint();
    }

    /// / Card value in GUI

    public String getValue() {
        return value;
    }

    /// Card suit maybe doing in server
    public String getSuit() {
        return suit;
    }

    /// Check face up card
//    public boolean isFaceUp() {
//        return isFaceUp;
//    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Card dimensions
        int width = getWidth();
        int height = getHeight();
        int arcSize = 8;

        if (isFaceUp) {
            // Draw card face
            g2d.setColor(Color.WHITE);
            g2d.fillRoundRect(0, 0, width - 1, height - 1, arcSize, arcSize);
            g2d.setColor(Color.BLACK);
            g2d.drawRoundRect(0, 0, width - 1, height - 1, arcSize, arcSize);

            // Determine card color
            Color cardColor = suit.equals("♥") || suit.equals("♦") ? Color.RED : Color.BLACK;
            g2d.setColor(cardColor);

            // Draw value and suit in top-left corner
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString(value, 5, 15);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString(suit, 5, 30);

            // Draw value and suit in bottom-right corner (upside down)
            g2d.rotate(Math.PI, width - 5, height - 15);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString(value, 0, 0);
            g2d.rotate(-Math.PI, width - 5, height - 15);

            g2d.rotate(Math.PI, width - 5, height - 30);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString(suit, 0, 0);
            g2d.rotate(-Math.PI, width - 5, height - 30);

            // Draw center suit for picture cards or middle value for number cards
            g2d.setFont(new Font("Arial", Font.BOLD, 22));
            FontMetrics fm = g2d.getFontMetrics();
            String centerText = suit;
            if (value.equals("J") || value.equals("Q") || value.equals("K")) {
                // For face cards, draw a symbol
                String symbol = value.equals("J") ? "J" : (value.equals("Q") ? "Q" : "K");
                centerText = symbol + suit;
            }

            int centerX = (width - fm.stringWidth(centerText)) / 2;
            int centerY = height / 2 + fm.getAscent() / 2;
            g2d.drawString(centerText, centerX, centerY);

        } else {
            // Draw card back
            g2d.setColor(Color.WHITE);
            g2d.fillRoundRect(0, 0, width - 1, height - 1, arcSize, arcSize);
            g2d.setColor(Color.BLACK);
            g2d.drawRoundRect(0, 0, width - 1, height - 1, arcSize, arcSize);

            // Draw pattern on card back
            g2d.setColor(new Color(30, 100, 200));
            g2d.fillRoundRect(4, 4, width - 9, height - 9, arcSize - 2, arcSize - 2);

            // Draw decorative pattern
            g2d.setColor(new Color(20, 60, 150));
            for (int i = 0; i < width; i += 6) {
                g2d.drawLine(i, 0, i, height);
            }

            for (int i = 0; i < height; i += 6) {
                g2d.drawLine(0, i, width, i);
            }
        }
    }
}