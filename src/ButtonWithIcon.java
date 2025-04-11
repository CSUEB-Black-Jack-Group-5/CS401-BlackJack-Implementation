package src;

import javax.swing.*;
import java.awt.*;

public class ButtonWithIcon extends JFrame {

    public ButtonWithIcon() {
        ImageIcon logo = new ImageIcon("src/my_logo.jpg");

        // Set up the frame
        this.setTitle("Black Jack Game");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(logo.getImage());

        // Create a panel with the green background color
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(49, 128, 6));

        // Using BorderLayout for the main panel
        mainPanel.setLayout(new BorderLayout());

        // Create a center panel for buttons with GridBagLayout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(49, 128, 6)); // Same green background
        buttonPanel.setLayout(new GridBagLayout());

        // Create Start button
        JButton startBtn = new JButton();
        startBtn.setText("Start");
        startBtn.setPreferredSize(new Dimension(200, 100));
        startBtn.setFont(new Font("Consolas", Font.BOLD, 20));
        startBtn.setBackground(Color.RED);
        startBtn.setOpaque(true);
        startBtn.setBorderPainted(true);
        startBtn.setBorder(BorderFactory.createEtchedBorder());

        // Create Exit button
        JButton exitBtn = new JButton();
        exitBtn.setText("Exit");
        exitBtn.setPreferredSize(new Dimension(200, 100));
        exitBtn.setFont(new Font("Consolas", Font.BOLD, 20));
        exitBtn.setBackground(Color.RED);
        exitBtn.setOpaque(true);
        exitBtn.setBorderPainted(true);
        exitBtn.setBorder(BorderFactory.createEtchedBorder());

        // Set up GridBagConstraints for button positioning
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 50, 0); // Bottom padding
        buttonPanel.add(startBtn, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0); // No padding
        buttonPanel.add(exitBtn, gbc);

        // Add button panel to the center of the main panel
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Add panel to frame
        this.add(mainPanel);

        this.setVisible(true);
    }
}