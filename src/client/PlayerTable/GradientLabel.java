package client.PlayerTable;

import javax.swing.*;
import java.awt.*;

/// A label with gradient background
public class GradientLabel extends JLabel {
    private Color color1;
    private Color color2;

    /// Create a label with gradient background
    public GradientLabel(String text, Color color1, Color color2, int alignment) {
        super(text, alignment);
        this.color1 = color1;
        this.color2 = color2;
        setOpaque(false);
    }

    /// Get the start color of the gradient
    public Color getStartColor() {
        return color1;
    }

    /// Set the start color of the gradient
    public void setStartColor(Color color) {
        this.color1 = color;
        repaint();
    }

    /// Get the end color of the gradient
    public Color getEndColor() {
        return color2;
    }

    /// Set the end color of the gradient
    public void setEndColor(Color color) {
        this.color2 = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create gradient paint
        GradientPaint gp = new GradientPaint(
                0, 0, color1,
                0, getHeight(), color2);
        g2d.setPaint(gp);

        // Fill background
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.dispose();

        // Call the super implementation to paint the text
        super.paintComponent(g);
    }
}