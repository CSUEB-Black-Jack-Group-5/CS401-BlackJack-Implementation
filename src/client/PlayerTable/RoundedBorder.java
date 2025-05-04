package client.PlayerTable;

import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/// A border implementation with rounded corners
public class RoundedBorder implements Border {
    private int radius;
    private Color color;
    private int thickness;

    /// Create a rounded border with specified color, thickness, and corner radius
    public RoundedBorder(Color color, int thickness, int radius) {
        this.color = color;
        this.thickness = thickness;
        this.radius = radius;
    }

    /// Get the border color
    public Color getColor() {
        return color;
    }

    /// Set the border color
    public void setColor(Color color) {
        this.color = color;
    }

    /// Get the border thickness
    public int getThickness() {
        return thickness;
    }

    /// Set the border thickness
    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    /// Get the corner radius
    public int getRadius() {
        return radius;
    }

    /// Set the corner radius
    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        RoundRectangle2D.Float roundedRect = new RoundRectangle2D.Float(
                x, y, width - 1, height - 1, radius, radius);

        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(thickness));
        g2d.draw(roundedRect);

        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness + 2, thickness + 2, thickness + 2, thickness + 2);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}