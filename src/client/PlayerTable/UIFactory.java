package client.PlayerTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/// Factory class for creating UI components with consistent styling
public class UIFactory {

    /// Create a styled button with consistent look and feel
    public static JButton createStyledButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(textColor);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        // Round the corners with a custom border
        button.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(new Color(0, 0, 0, 50), 1, 10),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(lightenColor(bgColor, 0.2f));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    /// Create a styled label with consistent look and feel
    public static JLabel createStyledLabel(String text, Color foreColor, int alignment) {
        JLabel label = new JLabel(text, alignment);
        label.setFont(BlackjackConstants.LABEL_FONT);
        label.setForeground(foreColor);

        return label;
    }

    /// Lighten a color by a factor
    private static Color lightenColor(Color color, float factor) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        int r2 = Math.min(255, (int)(r + (255 - r) * factor));
        int g2 = Math.min(255, (int)(g + (255 - g) * factor));
        int b2 = Math.min(255, (int)(b + (255 - b) * factor));

        return new Color(r2, g2, b2);
    }
}