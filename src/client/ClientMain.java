package client;

import client.gui.BlackjackIntroGUI;
import game.Table;
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

            // we have to do it this way because we set the tables to a fixed size of 10
            System.out.println("Displaying all the Tables ");
            Table[] tables = res.getTables();
            for (Table table : tables) {
                if (table != null) {
                    System.out.println("Table # " + table.getTableId());
                }
            }


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

                    new Thread(() -> showDealerMenu(client, scanner, username,res.getType())).start();
                } else if (res.getType() == AccountType.PLAYER) {
                    new Thread(()-> showPlayerMenu(client, scanner,username,res.getType())).start();
                }
            } else {
                System.out.println("Login failed");
                loginComplete.set(true);
            }
        });
        client.addMessageHook(Message.JoinTable.Response.class, (res)->{
            if(res.getStatus()) {
                System.out.println("SUCCESS JOINING A TABLE");
            }else {
                System.out.println("Failed Joining a Table");
            }
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

    public static void showDealerMenu(ClientWithHooks client, Scanner scanner, String userName, AccountType accountType) {
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
//                    showMenu = false;
                    break;
                case "2":
                    System.out.println("Goodbye!");
                    showMenu = false;
                    return;
                case "3":
                    System.out.println("Displaying Lobby Data");
                    client.sendNetworkMessage(new Message.LobbyData.Request(0,accountType));
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public static void showPlayerMenu(ClientWithHooks client, Scanner scanner, String userName, AccountType accountType) {
        System.out.println("Welcome, Player!");
        boolean showMenu = true;
        while (showMenu) {
            System.out.println("\nPlayer Menu:");
            System.out.println("1. Join a table");
            System.out.println("2. Exit");

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    // show tables first
                    client.sendNetworkMessage(new Message.LobbyData.Request(0,accountType));
                    System.out.println("Select a Table by its ID: ");
                    String tableSelect = scanner.nextLine();

                    try{
                        client.sendNetworkMessage(new Message.JoinTable.Request(0,Integer.parseInt(tableSelect)));

                    }catch (NullPointerException e){
                        System.out.println("Error Jointing table");
                    }
                    break;
                case "2":
                    System.out.println("Goodbye!");
                    showMenu = false;
                    return;
                case "3":
                    System.out.println("Please enter your bet amount:");
                    int bet = Integer.parseInt(scanner.nextLine());
                    client.sendNetworkMessage(new Message.Bet.Response(bet));
                    break;
                default:
                    System.out.println("Invalid choice.");


            }
        }
    }
//    public static void handleInGameMenu(ClientWithHooks client, Scanner scanner,String userName) {
//        boolean inGame = true;
//
//        while (inGame) {
//            System.out.println("\nGame Menu:");
//            System.out.println("1. Place a Bet");
//            System.out.println("2. Hit");
//            System.out.println("3. Stand");
//            System.out.println("4. Exit Table");
//
//            String input = scanner.nextLine();
//
//            switch (input) {
//                case "1":
//                    System.out.println("Enter your bet amount:");
//                    try {
//                        int bet = Integer.parseInt(scanner.nextLine());
//                        client.sendNetworkMessage(new Message.Bet.Response(bet));
//                    } catch (NumberFormatException e) {
//                        System.out.println("Invalid number.");
//                    }
//                    break;
//
//                case "2":
//                    client.sendNetworkMessage(new Message.PlayerAction.Request(userName,"hit"));
//                    break;
//
//                case "3":
//                    client.sendNetworkMessage(new Message.PlayerAction.Request(userName,"stand"));
//                    break;
//
//                case "4":
//                    System.out.println("Leaving the table...");
//                    client.sendNetworkMessage(new Message.LeaveTable.Request());
//                    inGame = false;
//                    break;
//
//                default:
//                    System.out.println("Invalid input.");
//            }
//        }
//    }
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
