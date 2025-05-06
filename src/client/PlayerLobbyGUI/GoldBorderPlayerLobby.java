package client.PlayerLobbyGUI;

import javax.swing.border.AbstractBorder;
import java.awt.*;

// Custom gold border class
class GoldBorderPlayerLobby extends AbstractBorder {
    private int thickness;
    private int radius;
    private Color goldColor = new Color(217, 187, 132);

    public GoldBorderPlayerLobby(int thickness, int radius) {
        this.thickness = thickness;
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(goldColor);
        g2.setStroke(new BasicStroke(thickness));
        g2.drawRoundRect(x + thickness/2, y + thickness/2,
                width - thickness, height - thickness,
                radius, radius);
        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness, thickness, thickness, thickness);
    }
}