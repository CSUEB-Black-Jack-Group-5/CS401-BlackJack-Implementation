package client.DealerTable;

/// RoundedBorder
import javax.swing.border.Border;
import java.awt.*;

public class RoundedBorder implements Border {
    private int radius;
    private Color color;
    private int thickness;

    /// Creates a new rounded border

    public RoundedBorder(Color color, int thickness, int radius) {
        this.color = color;
        this.thickness = thickness;
        this.radius = radius;
    }

    /// Gets the border color

    public Color getColor() {
        return color;
    }

    ///  Sets the border color

    public void setColor(Color color) {
        this.color = color;
    }

    ///  Gets the border thickness

    public int getThickness() {
        return thickness;
    }

    ///  Sets the border thickness

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    ///Gets the corner radius

    public int getRadius() {
        return radius;
    }

    /// Sets the corner radius
    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(radius + 1, radius + 1, radius + 2, radius);
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(thickness));
        g2d.drawRoundRect(x + thickness / 2, y + thickness / 2, width - thickness, height - thickness, radius, radius);
    }
}
