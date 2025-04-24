package client.test_gui;

import client.ClientWithHooks;
import networking.Message;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class TestGuiFrame extends JFrame {
    ClientWithHooks client;
    JPanel panel;
    JPanel loginPanel;
    JPanel playerPanel;
    JPanel dealerPanel;
    public TestGuiFrame(ClientWithHooks client) {
        this.client = client;

        setSize(new Dimension(600, 600));
        panel = new JPanel();
        loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.add(new TestMessagePanel.Builder().setText("CreateAccount").setButtonListener((ctx) -> {
            System.out.println(ctx.inputField.getText());
            String[] args = ctx.inputField.getText().split(" ");
            client.sendNetworkMessage(new Message.CreateAccount.Request(args[0], args[1]));
        }).build());
        loginPanel.add(new TestMessagePanel.Builder().setText("Login").setButtonListener((ctx) -> {
            String[] args = ctx.inputField.getText().split(" ");
            client.sendNetworkMessage(new Message.Login.Request(args[0], args[1]));
        }).build());

        playerPanel = new JPanel();
        playerPanel.setVisible(false);
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
        playerPanel.add(new TestMessagePanel.Builder().setText("Join Table").setButtonListener((ctx) -> {
            System.out.println("Join Table");
            client.sendNetworkMessage(new Message.JoinTable.Request());
        }).build());
        playerPanel.add(new TestMessagePanel.Builder().setText("Hit").setButtonListener((ctx) -> {
            client.sendNetworkMessage(new Message.Hit.Request());
        }).build());
        playerPanel.add(new TestMessagePanel.Builder().setText("Stand").setButtonListener((ctx) -> {
            client.sendNetworkMessage(new Message.Stand.Request());
        }).build());
        playerPanel.add(new TestMessagePanel.Builder().setText("Ready").setButtonListener((ctx) -> {
            client.sendNetworkMessage(new Message.PlayerReady.Request());
        }).build());
        playerPanel.add(new TestMessagePanel.Builder().setText("PlayerLeave").setButtonListener((ctx) -> {
            client.sendNetworkMessage(new Message.PlayerLeave.Request());
        }).build());

        dealerPanel = new JPanel();
        dealerPanel.setVisible(false);
        dealerPanel.setLayout(new BoxLayout(dealerPanel, BoxLayout.Y_AXIS));
        dealerPanel.add(new TestMessagePanel.Builder().setText("Create Table").setButtonListener((ctx) -> {
            client.sendNetworkMessage(new Message.CreateTable.Request());
        }).build());
        dealerPanel.add(new TestMessagePanel.Builder().setText("Deal").setButtonListener((ctx) -> {
            client.sendNetworkMessage(new Message.Deal.Request());
        }).build());
        dealerPanel.add(new TestMessagePanel.Builder().setText("DealerLeave").setButtonListener((ctx) -> {
            client.sendNetworkMessage(new Message.DealerLeave.Request());
        }).build());

        panel.add(new TestMessagePanel.Builder().setText("Disconnect").setButtonListener((ctx) -> {
            // client.sendNetworkMessage(new Message.Disconnect.Request());
        }).build());

        panel.add(loginPanel);
        panel.add(playerPanel);
        panel.add(dealerPanel);
        add(panel);
    }
    public void showPlayer() {
        loginPanel.setVisible(false);
        playerPanel.setVisible(true);
        dealerPanel.setVisible(false);
    }
    public void showDealer() {
        loginPanel.setVisible(false);
        dealerPanel.setVisible(true);
        playerPanel.setVisible(false);
    }
    // public void createLobbyGui(Table[] tables) {
    //     for (int i = 0; i < tables.length; i++) {
    //         Table table = tables[i];
    //         JPanel tablePanel = new JPanel();
    //         tablePanel.add(new JLabel("Table #" + i));
    //         tablePanel.add(new JLabel(table.getUserCount() + "/6"));
    //         JButton joinButton = new JButton("Join");
    //         joinButton.addActionListener((action) -> {
    //             client.sendNetworkMessage(new Message.JoinTable.Request(i));
    //         });
    //     }
    // }

    private static class TestMessagePanel extends JPanel {
        JLabel responseLabel;
        JTextField inputField;
        JButton sendButton;

        public TestMessagePanel() {
            responseLabel = new JLabel();
            inputField = new JTextField();
            sendButton = new JButton();
        }
        public static class Builder {
            TestMessagePanel panel;
            public Builder() {
                this.panel = new TestMessagePanel();
            }
            public Builder setText(String text) {
                panel.responseLabel.setText(text);
                return this;
            }
            public Builder setButtonListener(Consumer<TestMessagePanel> consumer) {
                panel.sendButton.addActionListener((action) -> {
                    consumer.accept(panel);
                });
                panel.sendButton.setText("Send");
                return this;
            }
            public TestMessagePanel build() {
                panel.add(panel.responseLabel);
                panel.inputField.setPreferredSize(new Dimension(100, 40));
                panel.add(panel.inputField);
                panel.add(panel.sendButton);
                return panel;
            }
        }
    }
}
