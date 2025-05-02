package client.test_gui;

import client.ClientWithHooks;
import networking.AccountType;
import networking.Message;

public class TestGuiMain {
    static ClientWithHooks client;
    public static void main(String[] args) {
        client = new ClientWithHooks("localhost", 3333);
        System.out.println("Created client");
        TestGuiFrame testGui = new TestGuiFrame(client);
        System.out.println("Created frame");
        client.addMessageHook(Message.CreateAccount.Response.class, (response) -> {});
        client.addMessageHook(Message.Login.Response.class, (response) -> {
            switch (response.getType()) {
                case AccountType.DEALER: {
                    client.addMessageHook(Message.CreateTable.Response.class, (res2) -> {
                        System.out.println("CreateTable Response");
                    });
                    // TODO: Make a deal message class
                    // client.addMessageHook(Message.Deal.Response.class, (res2) -> {
                    //     System.out.println("Deal Response");
                    // });
                    client.addMessageHook(Message.Stand.Response.class, (res2) -> {
                        System.out.println("Stand Response");
                    });
                    client.addMessageHook(Message.LobbyData.Response.class, (res2) -> {
                        System.out.println("Lobby data Response");
                    });
                    client.addMessageHook(Message.GameData.Response.class, (res2) -> {
                        System.out.println("GameData Response");
                    });
                    client.addMessageHook(Message.ClockSync.Response.class, (res2) -> {
                        System.out.println("ClockSync Response");
                    });
                    client.addMessageHook(Message.TableData.Response.class, (res2) -> {
                        System.out.println("TableData Response");
                    });
                    testGui.showDealer();
                }
                break;
                case AccountType.PLAYER: {
                    client.addMessageHook(Message.JoinTable.Response.class, (req) -> {
                        System.out.println("JoinTable Response");
                    });
                    client.addMessageHook(Message.Hit.Response.class, (req) -> {
                        System.out.println("Hit Response");
                    });
                    client.addMessageHook(Message.Stand.Response.class, (req) -> {
                        System.out.println("Stand Response");
                    });
                    client.addMessageHook(Message.LobbyData.Response.class, (res) -> {
                        System.out.println("Lobby data response");
                        // testGui.createLobbyGui(res.getTables());
                    });
                    client.addMessageHook(Message.GameData.Response.class, (req) -> {
                        System.out.println("GameData response");
                    });
                    client.addMessageHook(Message.ClockSync.Response.class, (req) -> {
                        System.out.println("ClockSync response");
                    });
                    client.addMessageHook(Message.TableData.Response.class, (req) -> {
                        System.out.println("TableData response");
                    });
                    client.addMessageHook(Message.PlayerReady.Response.class, (req) -> {
                        System.out.println("PlayerReady response");
                    });
                    client.addMessageHook(Message.PlayerLeave.Response.class, (req) -> {
                        System.out.println("PlayerLeave response");
                    });
                    testGui.showPlayer();
                    break;
                }
            }
        });
        testGui.setVisible(true);
    }
}
