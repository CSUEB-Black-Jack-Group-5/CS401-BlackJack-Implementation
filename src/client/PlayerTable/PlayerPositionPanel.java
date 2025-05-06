package client.PlayerTable;

import javax.swing.*;
import java.awt.*;

/// Represents a player's position on the Blackjack table
public class PlayerPositionPanel extends JPanel {
    private boolean occupied;
    private String positionLabel;
    private String playerName;
    private boolean ready = false;
    private static final Color POSITION_BACKGROUND = new Color(0, 0, 0, 150);
    private static final Color OCCUPIED_BORDER = new Color(0, 255, 0, 200); // Green for occupied
    private static final Color VACANT_BORDER = new Color(200, 200, 200, 150); // Gray for vacant

    /// Create a player position with an occupied status and label
    ///
    /// @param occupied whether the position is occupied
    /// @param positionLabel the label for the position
    public PlayerPositionPanel(boolean occupied, String positionLabel) {
        this.occupied = occupied;
        this.playerName = playerName;
        this.positionLabel = positionLabel;
        setPreferredSize(new Dimension(80, 80));
        setOpaque(false);
    }

    /// Check if the position is occupied
    public boolean isOccupied() {
        return occupied;
    }

    /// Set whether the position is occupied
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
        repaint();
    }

    /// Get the position label
    public String getPositionLabel() {
        return positionLabel;
    }

    /// Set the position label
    public void setPositionLabel(String label) {
        this.positionLabel = label;
        repaint();
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw circular background
        g2d.setColor(POSITION_BACKGROUND);
        g2d.fillOval(0, 0, getWidth(), getHeight());

        // Draw border with appropriate color
        g2d.setColor(occupied ? OCCUPIED_BORDER : VACANT_BORDER);
        g2d.setStroke(new BasicStroke(3.0f));
        g2d.drawOval(0, 0, getWidth() - 1, getHeight() - 1);

        // Draw position label
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics metrics = g2d.getFontMetrics();
        int textWidth = metrics.stringWidth(positionLabel);
        int textHeight = metrics.getHeight();
        g2d.drawString(positionLabel, (getWidth() - textWidth) / 2,
                (getHeight() + textHeight / 2) / 2);

        g2d.dispose();
        if (ready) {
            g2d = (Graphics2D) g;
            g2d.setColor(new Color(0, 255, 0, 150)); // Green halo for ready status
            g2d.setStroke(new BasicStroke(3f));
            g2d.drawOval(2, 2, getWidth() - 4, getHeight() - 4);

            // Draw "READY" text below
            g2d.setColor(Color.GREEN);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            String readyText = "READY";
            FontMetrics fm = g2d.getFontMetrics();
            textWidth = fm.stringWidth(readyText);
            g2d.drawString(readyText, (getWidth() - textWidth) / 2, getHeight() + 15);
        }
    }
}