package client.PlayerTable;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/// A panel with rounded corners and customizable background color and opacity
public class RoundedPanel extends JPanel {
    private int cornerRadius;
    private Color backgroundColor;
    private float opacity;

    /// Create a rounded panel with specified radius, background color, and opacity
    public RoundedPanel(int radius, Color bgColor, float opacity) {
        super();
        this.cornerRadius = radius;
        this.backgroundColor = bgColor;
        this.opacity = opacity;
        setOpaque(false);
    }

    /// Create a rounded panel with specified radius and background color (opacity 1.0)
    public RoundedPanel(int radius, Color bgColor) {
        this(radius, bgColor, 1.0f);
    }

    /// Get the corner radius
    public int getCornerRadius() {
        return cornerRadius;
    }

    /// Set the corner radius
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    /// Get the background color
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /// Set the background color
    public void setBackgroundColor(Color bgColor) {
        this.backgroundColor = bgColor;
        repaint();
    }

    /// Get the opacity value
    public float getOpacity() {
        return opacity;
    }

    /// Set the opacity value (0.0f - 1.0f)
    public void setOpacity(float opacity) {
        this.opacity = opacity;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set opacity/transparency
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
        g2d.setComposite(alphaComposite);

        // Create rounded rectangle for the background
        RoundRectangle2D.Float roundedRect = new RoundRectangle2D.Float(
                0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        // Draw the rounded background
        g2d.setColor(backgroundColor);
        g2d.fill(roundedRect);

        g2d.dispose();
    }
}