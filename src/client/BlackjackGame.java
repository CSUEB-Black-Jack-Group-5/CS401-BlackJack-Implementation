package client;

import client.PlayerTable.PlayerTableBlackJack;
import client.gui.BlackjackIntroGUI;
import client.DealerLobbyGUI.DealerLobbyBlackJack;
import client.PlayerLobbyGUI.PlayerLobbyBlackJack;
import client.DealerTable.DealerTableBlackJack;
import client.PlayerTable.PlayerTableBlackJack;
import networking.AccountType;
import networking.Message;

import javax.swing.*;

/// Main class
public class BlackjackGame {
    public static ClientWithHooks client;
    static BlackjackIntroGUI introGUI;
    static PlayerLobbyBlackJack playerLobby;
    static DealerLobbyBlackJack dealerLobby;
    static PlayerTableBlackJack playerTable;
    static DealerTableBlackJack dealerTable;

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
                        setupDealerHooks();
                        showDealerGUI();

                        // After showing dealer lobby, request lobby data from server
                        client.sendNetworkMessage(new Message.LobbyData.Request(0, AccountType.DEALER));
                        break;
                    }
                    case PLAYER: {
                        setupPlayerHooks();
                        showPlayerGUI();

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

            // Handle create table response
            Message.CreateTable.Response createTableResponse = (Message.CreateTable.Response) res;
            dealerLobby.getDealerLobbyBlackJackPanel().handleCreateTableResponse(createTableResponse);

            // Join the table just created
            int tableId = res.getTableId();
            String dealerName = res.getDealerUsername();
            int occupancy = res.getPlayerCount();
            int maxPlayers = res.getPlayerLimit();
            dealerTable = new DealerTableBlackJack(tableId, dealerName, occupancy, maxPlayers);

            // Hide the Dealer Lobby GUI if it's still visible
            if (dealerLobby != null && dealerLobby.isVisible()) {
                dealerLobby.setVisible(false);
            }

            // Show dealer table
            dealerTable.setVisible(true);
        });

        client.addMessageHook(Message.Stand.Response.class, (res) -> {
            System.out.println("Stand Response");
        });

        client.addMessageHook(Message.LobbyData.Response.class, (res) -> {
            System.out.println("Lobby data Response");
            Message.LobbyData.Response lobbyDataResponse = (Message.LobbyData.Response) res;
            // Handle lobby data response - update lobby UI
            dealerLobby.getDealerLobbyBlackJackPanel().updateLobbyData(lobbyDataResponse);
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

        // lobby pane instantiated here to avoid null arguments when calling for lobby data
        dealerLobby = new DealerLobbyBlackJack();
    }

    private static void setupPlayerHooks() {
        client.addMessageHook(Message.JoinTable.Response.class, (res) -> {
            System.out.println("JoinTable Response");
            // Handle join table response
            Message.JoinTable.Response joinTableResponse = (Message.JoinTable.Response) res;
            playerLobby.getPlayerLobbyBlackJackPanel().handleJoinTableResponse(joinTableResponse);

            // Join the table requested
            int tableId = res.getTableId();
            String dealerName = res.getDealerUsername();
            int playerPosition = res.getPlayerCount();
            int occupancy = res.getPlayerCount();
            int maxPlayers = res.getPlayerLimit();
            String playerName = "dummy_value";
            playerTable = new PlayerTableBlackJack(tableId, dealerName, playerPosition, occupancy, maxPlayers, playerName);

            // Hide the Player Lobby GUI if it's still visible
            if (playerLobby != null && playerLobby.isVisible()) {
                playerLobby.setVisible(false);
            }

            // Show player table
            playerTable.setVisible(true);
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
            System.out.println("Lobby data Response");
            Message.LobbyData.Response lobbyDataResponse = (Message.LobbyData.Response) res;
            // Handle lobby data response - update lobby UI
            playerLobby.getPlayerLobbyBlackJackPanel().updateLobbyData(lobbyDataResponse);
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

        client.addMessageHook(Message.PlayerReady.Response.class, (res) -> {
            System.out.println("PlayerReady Response");
            // Handle player ready response
        });

        client.addMessageHook(Message.PlayerLeave.Response.class, (res) -> {
            System.out.println("PlayerLeave Response");
            // Handle player leave response
        });

        // lobby pane instantiated here to avoid null arguments when calling for lobby data
        playerLobby = new PlayerLobbyBlackJack();
    }

    private static void showDealerGUI() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            // Hide intro GUI if it's still visible
            if (introGUI != null && introGUI.isVisible()) {
                introGUI.setVisible(false);
            }

            // Show dealer lobby
            dealerLobby.setVisible(true);
        });
    }

    private static void showPlayerGUI() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            // Hide intro GUI if it's still visible
            if (introGUI != null && introGUI.isVisible()) {
                introGUI.setVisible(false);
            }

            // Show player lobby
            playerLobby.setVisible(true);
        });
      
//        javax.swing.SwingUtilities.invokeLater(() -> {
//            BlackjackIntroGUI game = new BlackjackIntroGUI();
//            game.show();
//        });

    }
}