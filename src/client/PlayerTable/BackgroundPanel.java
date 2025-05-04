package client.PlayerTable;

import javax.swing.*;
import java.awt.*;

/// Panel with a patterned background for the Blackjack table
public class BackgroundPanel extends JPanel {

    public BackgroundPanel() {
        super(new BorderLayout());
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Create subtle pattern on the table
        g2d.setColor(BlackjackConstants.TABLE_BACKGROUND);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw subtle pattern
        g2d.setColor(BlackjackConstants.TABLE_PATTERN);
        for (int i = 0; i < getWidth(); i += 30) {
            for (int j = 0; j < getHeight(); j += 30) {
                if ((i / 30 + j / 30) % 2 == 0) {
                    g2d.fillRect(i, j, 15, 15);
                }
            }
        }
    }
}