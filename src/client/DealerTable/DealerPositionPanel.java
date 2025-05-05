package client.DealerTable;

/// Dealer Position Panel
import javax.swing.*;
import java.awt.*;

///  Dealer Position
public class DealerPositionPanel extends JPanel {
    private String dealerName;
    private static final Color GOLD_ACCENT = new Color(212, 175, 55);    // Gold accent

    public DealerPositionPanel(String dealerName) {
        this.dealerName = dealerName;
        setOpaque(false);
        setPreferredSize(new Dimension(100, 100));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw dealer position with stylish appearance
        int width = getWidth();
        int height = getHeight();

        // Draw outer circle with gradient
        GradientPaint gp = new GradientPaint(
                0, 0, new Color(0, 0, 0, 100),
                width, height, new Color(0, 0, 0, 170));
        g2d.setPaint(gp);
        g2d.fillOval(0, 0, width - 1, height - 1);

        // Draw border
        g2d.setColor(GOLD_ACCENT);
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawOval(0, 0, width - 1, height - 1);

        // Draw dealer icon
        int iconSize = width / 2;
        int iconX = (width - iconSize) / 2;
        int iconY = (height - iconSize) / 2 - 10;

        // Draw dealer avatar (simple silhouette)
        g2d.setColor(Color.WHITE);

        // Draw head
        int headSize = iconSize / 2;
        g2d.fillOval(iconX + iconSize / 4, iconY, headSize, headSize);

        // Draw body
        int bodyWidth = iconSize / 2;
        int bodyHeight = iconSize / 2;
        g2d.fillRect(iconX + iconSize / 4, iconY + headSize, bodyWidth, bodyHeight);

        // Draw bow tie
        g2d.setColor(GOLD_ACCENT);
        int tieY = iconY + headSize + 5;
        int[] tieXPoints = {iconX + iconSize / 2 - 10, iconX + iconSize / 2, iconX + iconSize / 2 + 10};
        int[] tieYPoints = {tieY, tieY - 5, tieY};
        g2d.fillPolygon(tieXPoints, tieYPoints, 3);

        int[] tieXPoints2 = {iconX + iconSize / 2 - 10, iconX + iconSize / 2, iconX + iconSize / 2 + 10};
        int[] tieYPoints2 = {tieY, tieY + 5, tieY};
        g2d.fillPolygon(tieXPoints2, tieYPoints2, 3);
    }

    /// Dealer Name
//    public String getDealerName() {
//        return dealerName;
//    }

    ///  Set Dealer Name
//    public void setDealerName(String dealerName) {
//        this.dealerName = dealerName;
//        repaint();
//    }
}
