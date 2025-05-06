package client.PlayerTable;

import javax.swing.*;
import java.awt.*;

/// Represents the dealer's position on the Blackjack table
public class DealerPositionPanel extends JPanel {
    private String dealerName;
    private static final Color DEALER_BACKGROUND = new Color(60, 0, 0, 180); // Dark red
    private static final Color DEALER_BORDER = new Color(212, 175, 55, 200); // Gold

    /// Create a dealer position with a name
    ///
    /// @param dealerName the dealer's name
    public DealerPositionPanel(String dealerName) {
        this.dealerName = dealerName;
        setPreferredSize(new Dimension(100, 100));
        setOpaque(false);
    }

    /// Get the dealer's name
    public String getDealerName() {
        return dealerName;
    }

    /// Set the dealer's name
    public void setDealerName(String name) {
        this.dealerName = name;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw dealer position background (circle)
        g2d.setColor(DEALER_BACKGROUND);
        g2d.fillOval(0, 0, getWidth(), getHeight());

        // Draw border
        g2d.setColor(DEALER_BORDER);
        g2d.setStroke(new BasicStroke(3.0f));
        g2d.drawOval(0, 0, getWidth() - 1, getHeight() - 1);

        // Draw dealer icon
        g2d.setColor(new Color(255, 255, 255, 200));
        int iconSize = 40;
        int iconX = (getWidth() - iconSize) / 2;
        int iconY = (getHeight() - iconSize) / 2;

        // Draw simple dealer icon (like a person silhouette)
        int headSize = 20;
        g2d.fillOval(iconX + (iconSize - headSize) / 2, iconY, headSize, headSize);
        g2d.fillRect(iconX + 10, iconY + headSize, 20, 20);

        g2d.dispose();
    }
}