import java.awt.*;

public class SuitRenderer {
    public static void drawCardSuit(Graphics2D g2, int x, int y, String suit, Color color) {
        Font suitFont = new Font("Arial", Font.BOLD, 40);
        g2.setFont(suitFont);
        g2.setColor(color);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(suit, x - fm.stringWidth(suit) / 2, y);
    }
}