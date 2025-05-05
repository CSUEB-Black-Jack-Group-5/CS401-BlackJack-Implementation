package client;

import client.gui.BlackjackIntroGUI;
import networking.AccountType;
import networking.Message;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientMain {
    static BlackjackIntroGUI GUI;
    static ClientWithHooks client;
    // static ClientGUI clientGUI;

    public static void main(String[] args) {
        /*
            client.gui.LoginGUI will send the server a message indicating a login request
            The server will send a message back indicating what type of user logged in
               - This case is handled below
         */
        client = new ClientWithHooks("localhost", 3333);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter userName: ");
        String username = scanner.nextLine();
        System.out.println("Enter Password: ");
        String password = scanner.nextLine();

        Message.Login.Request loginRequest = new Message.Login.Request(username, password);
        client.sendNetworkMessage(loginRequest);
        // this makes it thread safe
        AtomicBoolean loginComplete = new AtomicBoolean(false);

        client.addMessageHook(Message.LobbyData.Response.class, (res)->{
            System.out.println("# of tables "+ res.getTables()[0].getTableId());
            System.out.println("# of d "+ res.getDealerCount());
            System.out.println("# of p "+ res.getPlayerCount());

        });
        client.addMessageHook(Message.CreateTable.Response.class, (res)->{
            if(res.getStatus()){
                System.out.println("Table created successfully! Table ID: " + res.getTableId());
            }else {
                System.out.println("Failed to create table :(");
            }
        });
        client.addMessageHook(Message.Login.Response.class, (res) -> {
            if (res.getStatus()) {
                System.out.println("Login successful. Account Type: " + res.getType());
                loginComplete.set(true);

                if (res.getType() == AccountType.DEALER) {

                    new Thread(() -> showDealerMenu(client, scanner, username)).start();
                }
            } else {
                System.out.println("Login failed");
                loginComplete.set(true);
            }
        });
        client.addMessageHook(Message.JoinTable.Response.class, (res)->{

        });

        // Wait for login to finish
        while (!loginComplete.get()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.err.println("Interrupted while waiting for login.");
                return;
            }
        }

    }
    public static void showDealerMenu(ClientWithHooks client, Scanner scanner, String userName) {
        System.out.println("Welcome, Dealer!");
        boolean showMenu = true;
        while (showMenu) {
            System.out.println("\nDealer Menu:");
            System.out.println("1. Create a new table");
            System.out.println("2. Exit");

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    client.sendNetworkMessage(new Message.CreateTable.Request(userName));
                    showMenu = false;
                    break;
                case "2":
                    System.out.println("Goodbye!");
                    showMenu = false;
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
//SwingUtilities.invokeLater(() -> {
//            client = new ClientWithHooks("localhost", 3333);
//            GUI = new BlackjackIntroGUI(client);
//            GUI.show();
//        });

//             switch (res.getType()) {
//                 case DEALER: {
//                     client.addMessageHook(Message.Leave.Response.class, (res) -> {});
//                     client.addMessageHook(Message.TableData.Response.class, (res) -> {});
//                     client.addMessageHook(Message.ClockSync.Response.class, (res) -> {});
//                     client.addMessageHook(Message.GameData.Response.class, (res) -> {});
//                     client.addMessageHook(Message.LobbyData.Response.class, (res) -> {});
////                     clientGUI = new DealerClientGUI(client);
//                 }
//                 case PLAYER: {
//                     client.addMessageHook(Message.Hit.Response.class, (res) -> {});
//                     client.addMessageHook(Message.Stand.Response.class, (res) -> {});
//                     client.addMessageHook(Message.Leave.Response.class, (res) -> {});
//                     client.addMessageHook(Message.LobbyData.Response.class, (res) -> {});
//                     client.addMessageHook(Message.TableData.Response.class, (res) -> {});
//                     client.addMessageHook(Message.ClockSync.Response.class, (res) -> {});
//                     client.addMessageHook(Message.GameData.Response.class, (res) -> {});
//                     client.addMessageHook(Message.PlayerReady.Response.class, (res) -> {});
//                     client.addMessageHook(Message.PlayerLeave.Response.class, (res) -> {});
//                     clientGUI = new PlayerClientGUI(client); // open the player client gui
//                 }
//             };
