package client.PlayerTable;

import javax.swing.*;
import java.awt.*;

/// A panel that represents the playing surface for the Blackjack game
public class PlayingSurfacePanel extends JPanel {

    /// Create a new playing surface panel
    public PlayingSurfacePanel() {
        super(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(900, 400));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw table border
        g2d.setColor(new Color(255, 215, 0, 80)); // Gold with transparency
        g2d.setStroke(new BasicStroke(3.0f));

        // Draw an arc to represent the table edge
        int arcWidth = getWidth() - 40;
        int arcHeight = 300;
        g2d.drawArc(20, -150, arcWidth, arcHeight, 0, 180);

        g2d.dispose();
    }
}