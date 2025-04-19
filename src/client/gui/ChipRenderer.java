package client.gui;
///  Poker Chips Design

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class ChipRenderer {
    public static void drawPokerChips(Graphics2D g2) {
        /// poker chips decoration
        int[] chipColors = {
                Color.RED.getRGB(),
                Color.BLUE.getRGB(),
                Color.BLACK.getRGB(),
                BlackjackIntroGUI.GOLD.getRGB(),
                Color.GREEN.getRGB()
        };

        /// Left side of intro GUI
        for (int i = 0; i < 8; i++) {
            drawChip(g2, 100, 500 - i * 4, chipColors[i % chipColors.length]);
        }

        /// Right side of intro GUI
        for (int i = 0; i < 8; i++) {
            drawChip(g2, 700, 500 - i * 4, chipColors[(i + 2) % chipColors.length]);
        }

        /// Random chips
        drawChip(g2, 150, 480, chipColors[0]);
        drawChip(g2, 200, 520, chipColors[1]);
        drawChip(g2, 650, 490, chipColors[2]);
        drawChip(g2, 600, 530, chipColors[3]);
    }

    private static void drawChip(Graphics2D g2, int x, int y, int color) {
        ///  Set the outter circle for chips
        g2.setColor(new Color(color));
        g2.fill(new Ellipse2D.Double(x - 25, y - 25, 50, 50));

        /// Set the inner circle for chips
        g2.setColor(new Color(color).brighter());
        g2.fill(new Ellipse2D.Double(x - 20, y - 20, 40, 40));

        ///  Center for chips
        g2.setColor(new Color(color));
        g2.fill(new Ellipse2D.Double(x - 10, y - 10, 20, 20));

        /// Edge pattern
        g2.setColor(Color.WHITE);
        for (int i = 0; i < 8; i++) {
            double angle = Math.PI * i / 4;
            int x1 = x + (int)(23 * Math.cos(angle));
            int y1 = y + (int)(23 * Math.sin(angle));
            int x2 = x + (int)(20 * Math.cos(angle));
            int y2 = y + (int)(20 * Math.sin(angle));
            g2.drawLine(x1, y1, x2, y2);
        }
    }
}