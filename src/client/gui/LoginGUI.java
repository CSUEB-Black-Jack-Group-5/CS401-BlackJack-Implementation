package client.gui;
///  Login GUI

import client.BlackjackGame;
import networking.Message;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginGUI extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private boolean succeeded;

    public LoginGUI(Frame parent) {
        super(parent, "Blackjack Login", true);

        /// Panel for UI
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 30));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        /// Title Layout
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(30, 30, 30));
        titlePanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        ///  Title Text
        JLabel titleLabel = new JLabel("ACCOUNT LOGIN", JLabel.CENTER);
        titleLabel.setForeground(BlackjackIntroGUI.GOLD);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        /// Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(30, 30, 30));
        GridBagConstraints cs = new GridBagConstraints();
        cs.fill = GridBagConstraints.HORIZONTAL;

        /// User name Label
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        ///  User name layout
        usernameField = new JTextField(20);
        usernameField.setBackground(new Color(50, 50, 50));
        usernameField.setForeground(Color.WHITE);
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        usernameField.setCaretColor(Color.WHITE);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BlackjackIntroGUI.GOLD, 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        /// Password label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        /// Password layout
        passwordField = new JPasswordField(20);
        passwordField.setBackground(new Color(50, 50, 50));
        passwordField.setForeground(Color.WHITE);
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BlackjackIntroGUI.GOLD, 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        /// Component by using grid for form of panel
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        cs.insets = new Insets(10, 0, 5, 10);
        formPanel.add(userLabel, cs);

        /// Component by using grid for form of panel
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        cs.insets = new Insets(10, 0, 5, 0);
        formPanel.add(usernameField, cs);

        /// Component by using grid for form of panel
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        cs.insets = new Insets(5, 0, 10, 10);
        formPanel.add(passwordLabel, cs);

        /// Component by using grid for form of panel
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        cs.insets = new Insets(5, 0, 10, 0);
        formPanel.add(passwordField, cs);

        /// Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(30, 30, 30));

        /// Login button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        /// Login button Layout
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(BlackjackIntroGUI.BUTTON_COLOR);
        loginButton.setBorder(BorderFactory.createLineBorder(BlackjackIntroGUI.GOLD, 2));
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(100, 40));

        /// Hover
        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(BlackjackIntroGUI.HOVER_COLOR);
            }
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(BlackjackIntroGUI.BUTTON_COLOR);
            }
        });

        /// check login
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        /// Cancel Button
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        /// Cancel Button Layout
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBackground(BlackjackIntroGUI.BUTTON_COLOR);
        cancelButton.setBorder(BorderFactory.createLineBorder(BlackjackIntroGUI.GOLD, 2));
        cancelButton.setFocusPainted(false);
        cancelButton.setPreferredSize(new Dimension(100, 40));

        /// Hover for cancel button
        cancelButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                cancelButton.setBackground(BlackjackIntroGUI.HOVER_COLOR);
            }
            public void mouseExited(MouseEvent e) {
                cancelButton.setBackground(BlackjackIntroGUI.BUTTON_COLOR);
            }
        });

        ///  Callback for cancel button to return to introGUI and clean everything by dispose()
        cancelButton.addActionListener(e -> {
            succeeded = false;
            dispose();
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                /// Dark background
                g2d.setColor(new Color(30, 30, 30));
                g2d.fillRect(0, 0, getWidth(), getHeight());

                /// Vertical stripes
                g2d.setColor(new Color(50, 50, 50));
                for (int i = 0; i < getWidth(); i += 20) {
                    g2d.drawLine(i, 0, i, getHeight());
                }

                /// game.Card suits as decoration
                SuitRenderer.drawCardSuit(g2d, 50, 40, "♠", new Color(200, 200, 200));
                SuitRenderer.drawCardSuit(g2d, getWidth() - 50, 40, "♥", new Color(200, 0, 0));
                SuitRenderer.drawCardSuit(g2d, 50, getHeight() - 40, "♣", new Color(200, 200, 200));
                SuitRenderer.drawCardSuit(g2d, getWidth() - 50, getHeight() - 40, "♦", new Color(200, 0, 0));

                g2d.dispose();
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        /// Add all panels
        backgroundPanel.add(titlePanel, BorderLayout.NORTH);
        backgroundPanel.add(formPanel, BorderLayout.CENTER);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

        /// Set content pane
        setContentPane(backgroundPanel);

        /// Set dialog properties
        setSize(400, 300);
        setResizable(false);
        setLocationRelativeTo(parent);

        /// Set default button and focus
        getRootPane().setDefaultButton(loginButton);
        usernameField.requestFocusInWindow();
    }

    private boolean authenticate(String username, String password) {
        /// Send login request to the server
        if (BlackjackGame.client != null) {
            Message.Login.Request loginRequest = new Message.Login.Request(username, password);
            BlackjackGame.client.sendNetworkMessage(loginRequest);
            return true;
        }
        return false;
    }

    private void handleLogin() {
        String username = getUsername();
        String password = getPassword();

        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(LoginGUI.this,
                    "Please enter username and password",
                    "Account Login",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (authenticate(username, password)) {
            /// We'll get the real result from the server response
            succeeded = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(LoginGUI.this,
                    "Failed to connect to server",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            // Reset password field
            passwordField.setText("");
            succeeded = false;
        }
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public boolean isSucceeded() {
        return succeeded;
    }
}