package client;

import client.gui.BlackjackIntroGUI;
import client.DealerLobbyGUI.DealerLobbyBlackJack;
import client.PlayerLobbyGUI.PlayerLobbyBlackJack;
import networking.AccountType;
import networking.Message;

import javax.swing.*;

/// Main class
public class BlackjackGame {
    public static ClientWithHooks client;
    static BlackjackIntroGUI introGUI;
    static PlayerLobbyBlackJack playerLobby;
    static DealerLobbyBlackJack dealerLobby;

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            // Initialize the client
            client = new ClientWithHooks("localhost", 3333);

            // Set up hooks for login responses and other messages
            setupMessageHooks();

            // Create and show the intro GUI
            introGUI = new BlackjackIntroGUI(client);
            introGUI.setVisible(true);
        });
    }

    private static void setupMessageHooks() {
        // Login Response Hook
        client.addMessageHook(Message.Login.Response.class, (response) -> {
            Message.Login.Response loginResponse = (Message.Login.Response) response;

            if (loginResponse.getStatus()) {
                System.out.println("Login successful! Account type: " + loginResponse.getType());

                // Based on account type, set up appropriate hooks and GUI
                switch (loginResponse.getType()) {
                    case DEALER: {
                        showDealerGUI();
                        setupDealerHooks();

                        // After showing dealer lobby, request lobby data from server
                        client.sendNetworkMessage(new Message.LobbyData.Request(0, AccountType.DEALER));
                        break;
                    }
                    case PLAYER: {
                        showPlayerGUI();
                        setupPlayerHooks();

                        // After showing player lobby, request lobby data from server
                        client.sendNetworkMessage(new Message.LobbyData.Request(0, AccountType.PLAYER));
                        break;
                    }
                }
            } else {
                System.out.println("Login failed!");
                // Display error message in the UI
                javax.swing.SwingUtilities.invokeLater(() -> {
                    javax.swing.JOptionPane.showMessageDialog(null,
                            "Login failed. Please check your credentials.",
                            "Login Error",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                });
            }
        });

        /// Create Account Response Hook
        client.addMessageHook(Message.CreateAccount.Response.class, (response) -> {
            Message.CreateAccount.Response createAccountResponse = (Message.CreateAccount.Response) response;

            if (createAccountResponse.getStatus()) {
                System.out.println("Account creation successful!");
                // Display success message in the UI
                javax.swing.SwingUtilities.invokeLater(() -> {
                    javax.swing.JOptionPane.showMessageDialog(null,
                            "Account created successfully! You can now log in.",
                            "Account Created",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);
                });
            } else {
                System.out.println("Account creation failed!");
                // Display error message in the UI
                javax.swing.SwingUtilities.invokeLater(() -> {
                    javax.swing.JOptionPane.showMessageDialog(null,
                            "Account creation failed. Username may already exist.",
                            "Registration Error",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                });
            }
        });
    }

    private static void setupDealerHooks() {
        client.addMessageHook(Message.CreateTable.Response.class, (res) -> {
            System.out.println("CreateTable Response");
            dealerLobby.handleCreateTableResponse(res);
        });

        client.addMessageHook(Message.Stand.Response.class, (res) -> {
            System.out.println("Stand Response");
        });

        // NOTE: This is setup in DealerLobbyBlackJackPanel
        client.addMessageHook(Message.LobbyData.Response.class, (res) -> {
             System.out.println("Lobby data Response");
             System.out.println("   Tables: " + res.getTables().length);
             dealerLobby.handleLobbyDataResponse(res);
             // Handle lobby data response - update lobby UI
        });

        client.addMessageHook(Message.GameData.Response.class, (res) -> {
            System.out.println("GameData Response");
            // Handle game data response
        });

        client.addMessageHook(Message.ClockSync.Response.class, (res) -> {
            System.out.println("ClockSync Response");
            // Handle clock sync response
        });

        client.addMessageHook(Message.TableData.Response.class, (res) -> {
            System.out.println("TableData Response");
            // Handle table data response
        });
    }

    private static void setupPlayerHooks() {
        client.addMessageHook(Message.JoinTable.Response.class, (res) -> {
            System.out.println("JoinTable Response");
            System.out.println(res.getStatus());
            // Handle join table response
        });

        client.addMessageHook(Message.Hit.Response.class, (res) -> {
            System.out.println("Hit Response");
            // Handle hit response
        });

        client.addMessageHook(Message.Stand.Response.class, (res) -> {
            System.out.println("Stand Response");
            // Handle stand response
        });

        client.addMessageHook(Message.LobbyData.Response.class, (res) -> {
            playerLobby.handleLobbyDataResponse(res);
        });

        // Handled by DealerLobbyBlackJackPanel
        // client.addMessageHook(Message.LobbyData.Response.class, (res) -> {
        //     System.out.println("Lobby data Response");
        //     // Handle lobby data response - update lobby UI
        // });

        client.addMessageHook(Message.GameData.Response.class, (res) -> {
            System.out.println("GameData Response");
            // Handle game data response
        });

        client.addMessageHook(Message.ClockSync.Response.class, (res) -> {
            System.out.println("ClockSync Response");
            // Handle clock sync response
        });

        client.addMessageHook(Message.TableData.Response.class, (res) -> {
            System.out.println("TableData Response");
            // Handle table data response
        });

        client.addMessageHook(Message.PlayerReady.Response.class, (res) -> {
            System.out.println("PlayerReady Response");
            // Handle player ready response
        });

        client.addMessageHook(Message.PlayerLeave.Response.class, (res) -> {
            System.out.println("PlayerLeave Response");
            // Handle player leave response
        });
    }

    private static void showDealerGUI() {
        //javax.swing.SwingUtilities.invokeLater(() -> {
            // Hide intro GUI if it's still visible
            if (introGUI != null && introGUI.isVisible()) {
                introGUI.setVisible(false);
            }

            // Show dealer lobby
            dealerLobby = new DealerLobbyBlackJack();
            dealerLobby.setVisible(true);
        // });
    }

    private static void showPlayerGUI() {
        // javax.swing.SwingUtilities.invokeLater(() -> {
            // Hide intro GUI if it's still visible
            if (introGUI != null && introGUI.isVisible()) {
                introGUI.setVisible(false);
            }

            // Show player lobby
            playerLobby = new PlayerLobbyBlackJack();
            playerLobby.setVisible(true);
        // });
      
//        javax.swing.SwingUtilities.invokeLater(() -> {
//            BlackjackIntroGUI game = new BlackjackIntroGUI();
//            game.show();
//        });

    }
}