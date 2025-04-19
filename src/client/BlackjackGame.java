package client;

import client.gui.BlackjackIntroGUI;

/// Main class
public class BlackjackGame {
    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(() -> {
            BlackjackIntroGUI game = new BlackjackIntroGUI();
            game.show();
        });
    }
}
