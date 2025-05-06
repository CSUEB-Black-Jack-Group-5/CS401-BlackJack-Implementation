package client.DealerTable;
/// Player Position Panel
import javax.swing.*;
import java.awt.*;

/**
 * Custom player position panel with enhanced visuals
 */
public class PlayerPositionPanel extends JPanel {
    private boolean occupied;
    private String positionLabel;

    public PlayerPositionPanel(boolean occupied, String positionLabel) {
        this.occupied = occupied;
        this.positionLabel = positionLabel;
        setOpaque(false);
        setPreferredSize(new Dimension(80, 80));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Draw position indicator circle
        g2d.setColor(new Color(255, 255, 255, 50));
        g2d.fillOval(0, 0, width - 1, height - 1);

        // Draw outer border
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawOval(0, 0, width - 1, height - 1);

        if (occupied) {
            // Draw player representation
            g2d.setColor(new Color(255, 255, 255, 160));
            g2d.fillOval(5, 5, width - 10, height - 10);

            // Draw player silhouette
            g2d.setColor(new Color(30, 50, 100, 200));

            // Head
            int headSize = width / 3;
            g2d.fillOval(width / 2 - headSize / 2, height / 4, headSize, headSize);

            // Body
            int bodyWidth = width / 2;
            int bodyTopY = height / 4 + headSize;
            int bodyHeight = height - bodyTopY - 5;

            g2d.fillRect(width / 2 - bodyWidth / 2, bodyTopY, bodyWidth, bodyHeight);

            // Draw position number
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            FontMetrics fm = g2d.getFontMetrics();
            String text = positionLabel;
            int textX = (width - fm.stringWidth(text)) / 2;
            int textY = height - 10;
            g2d.drawString(text, textX, textY);
        } else {
            // Draw empty position indicator
            g2d.setColor(new Color(255, 255, 255, 80));
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            FontMetrics fm = g2d.getFontMetrics();
            String text = "?";
            int textX = (width - fm.stringWidth(text)) / 2;
            int textY = height / 2 + fm.getAscent() / 2;
            g2d.drawString(text, textX, textY);

            // Draw position number
            g2d.setColor(new Color(255, 255, 255, 120));
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            fm = g2d.getFontMetrics();
            text = positionLabel;
            textX = (width - fm.stringWidth(text)) / 2;
            textY = height - 10;
            g2d.drawString(text, textX, textY);
        }
    }

    ///  Occupy
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
        repaint();
    }

   ///  Status
    public boolean isOccupied() {
        return occupied;
    }

    /// position label
    public String getPositionLabel() {
        return positionLabel;
    }
}