package client.DealerTable;

/// Gradient Label
import javax.swing.*;
import java.awt.*;

/// Custom label with gradient text

public class GradientLabel extends JLabel {
    private Color startColor;
    private Color endColor;

    /// label with gradient text

    public GradientLabel(String text, Color startColor, Color endColor, int alignment) {
        super(text, alignment);
        this.startColor = startColor;
        this.endColor = endColor;
        setForeground(startColor); /// Default color for non-custom paint
    }

    /// Gets the gradient start color
    public Color getStartColor() {
        return startColor;
    }

    /// Sets the gradient start color
    public void setStartColor(Color color) {
        this.startColor = color;
        repaint();
    }

    /// Gets the gradient end color

    public Color getEndColor() {
        return endColor;
    }

    /// Sets the gradient end color
    public void setEndColor(Color color) {
        this.endColor = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        /// Create gradient paint for text
        GradientPaint gp = new GradientPaint(
                0, 0, startColor,
                getWidth(), 0, endColor);

        g2d.setPaint(gp);
        FontMetrics fm = g2d.getFontMetrics();

        /// Center text
        String text = getText();
        int x = 0;
        if (getHorizontalAlignment() == SwingConstants.CENTER) {
            x = (getWidth() - fm.stringWidth(text)) / 2;
        } else if (getHorizontalAlignment() == SwingConstants.RIGHT) {
            x = getWidth() - fm.stringWidth(text);
        }

        int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();

        g2d.drawString(text, x, y);
        g2d.dispose();
    }
}
