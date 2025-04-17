package client;

public class PlayerClientMain {
    static ClientWithHooks client;
    static LoginGUI loginGui;           // Todo: LoginGUI
    static PlayerGUI playerGUI;         // Todo: PlayerGUI

    // Todo: Message.Login.Response
    // Todo: Message.Hit.Response
    // Todo: Message.Stand.Response
    // Todo: Message.Leave.Response
    // Todo: Message.LobbyData.Response
    // Todo: Message.ClockSync.Response
    // Todo: Message.GameData.Response
    // Todo: Message.PlayerReady.Response
    // Todo: Message.PlayerLeave.Response
    public static void main(String[] args) {
        loginGui = new LoginGUI(client);
        playerGUI = new PlayerGUI(client);

        client = new ClientWithHooks(PLAYER, "localhost", 3333);
        client.addMessageHook(Login.Response.class, (res) -> {
            // Note: By the time this code runs the login has already happened
            loginGui.close();
            playerGUI.show();
        });
        client.addMessageHook(Hit.Response.class, (res) -> {});
        client.addMessageHook(Stand.Response.class, (res) -> {});
        client.addMessageHook(Leave.Response.class, (res) -> {});
        client.addMessageHook(LobbyData.Response.class, (res) -> {});
        client.addMessageHook(TableData.Response.class, (res) -> {});
        client.addMessageHook(ClockSync.Response.class, (res) -> {});
        client.addMessageHook(GameData.Response.class, (res) -> {});
        client.addMessageHook(PlayerReady.Response.class, (res) -> {});
        client.addMessageHook(PlayerLeave.Response.class, (res) -> {});
    }
}
