package client.DealerTable;

/// RoundedPanel
import javax.swing.*;
import java.awt.*;

///Custom panel with rounded corners

public class RoundedPanel extends JPanel {
    private int cornerRadius;
    private Color backgroundColor;
    private float opacity;

    /// Rounded Panel
    public RoundedPanel(int radius, Color bgColor, float opacity) {
        super();
        this.cornerRadius = radius;
        this.backgroundColor = bgColor;
        this.opacity = opacity;
        setOpaque(false);
    }

    /// Creates a new rounded panel with the specified radius and background color
    public RoundedPanel(int radius, Color bgColor) {
        this(radius, bgColor, 1.0f);
    }

    ///  Corner
    public int getCornerRadius() {
        return cornerRadius;
    }

    /// Sets the corner radius
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    /// Gets the background color
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /// Sets the background color
    public void setBackgroundColor(Color bgColor) {
        this.backgroundColor = bgColor;
        repaint();
    }

    /// Gets the opacity level
    public float getOpacity() {
        return opacity;
    }

    /// Sets the opacity level
    public void setOpacity(float opacity) {
        this.opacity = opacity;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension arcs = new Dimension(cornerRadius, cornerRadius);
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        /// Create a translucent color if opacity < 1
        Color drawColor = new Color(
                backgroundColor.getRed(),
                backgroundColor.getGreen(),
                backgroundColor.getBlue(),
                (int) (backgroundColor.getAlpha() * opacity)
        );

        /// Draw the rounded panel
        graphics.setColor(drawColor);
        graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
    }
}