package client.DealerLobbyGUI;

import javax.swing.*;
import java.awt.*;

/// Custom rounded panel class
class RoundedPanelDealerLobby extends JPanel {
    private int cornerRadius;

    public RoundedPanelDealerLobby(int radius) {
        super();
        this.cornerRadius = radius;
        setOpaque(false);
    }

    public RoundedPanelDealerLobby(int radius, Color bgColor) {
        super();
        this.cornerRadius = radius;
        setOpaque(false);
        setBackground(bgColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Paint background with rounded corners
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.dispose();
    }
}