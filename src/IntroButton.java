/// Custom button class

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class IntroButton extends JButton {
    private boolean isHovered = false;

    public IntroButton(String text) {
        super(text);
        setFont(new Font("Arial", Font.BOLD, 24));
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);

        /// Hover when click on button
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        /// Button background color
        g2.setColor(isHovered ? BlackjackIntroGUI.HOVER_COLOR : BlackjackIntroGUI.BUTTON_COLOR);
        RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(
                0, 0, getWidth(), getHeight(), 20, 20);
        g2.fill(roundedRectangle);

        ///  Button Border
        g2.setColor(BlackjackIntroGUI.GOLD);
        g2.setStroke(new BasicStroke(3));
        g2.draw(roundedRectangle);

        ///  Shine hover
        if (isHovered) {
            GradientPaint shine = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 100),
                    0, getHeight(), new Color(255, 255, 255, 0));
            g2.setPaint(shine);
            g2.fill(roundedRectangle);
        }

        ///  text
        FontMetrics fm = g2.getFontMetrics(getFont());
        Rectangle2D textBounds = fm.getStringBounds(getText(), g2);
        int textX = (int) (getWidth() - textBounds.getWidth()) / 2;
        int textY = (int) (getHeight() - textBounds.getHeight()) / 2 + fm.getAscent();

        /// Text shadow
        g2.setColor(new Color(0, 0, 0, 120));
        g2.drawString(getText(), textX + 2, textY + 2);

        ///  text color
        g2.setColor(Color.WHITE);
        g2.drawString(getText(), textX, textY);

        g2.dispose();
    }
}
