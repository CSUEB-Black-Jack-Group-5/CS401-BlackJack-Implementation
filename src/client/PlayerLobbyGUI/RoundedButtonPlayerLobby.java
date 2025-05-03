package client.PlayerLobbyGUI;

import javax.swing.*;
import java.awt.*;

/// Custom rounded button class
class RoundedButtonPlayerLobby extends JButton {
    private int cornerRadius;

    public RoundedButtonPlayerLobby(String text, int radius) {
        super(text);
        this.cornerRadius = radius;
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        /// Button shadow effect
        g2.setColor(new Color(0, 0, 0, 50));
        g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);

        // Button body
        if (getModel().isPressed()) {
            g2.setColor(getBackground().darker());
        } else if (getModel().isRollover()) {
            g2.setColor(getBackground().brighter());
        } else {
            g2.setColor(getBackground());
        }

        g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);

        /// Gold edge
        g2.setColor(new Color(217, 187, 132));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);

        /// Draw text
        FontMetrics fontMetrics = g2.getFontMetrics();
        Rectangle stringBounds = fontMetrics.getStringBounds(getText(), g2).getBounds();

        int textX = (getWidth() - stringBounds.width) / 2 - 1;
        int textY = (getHeight() - stringBounds.height) / 2 + fontMetrics.getAscent() - 1;

        g2.setColor(getForeground());
        g2.setFont(getFont());
        g2.drawString(getText(), textX, textY);
        g2.dispose();
    }
}