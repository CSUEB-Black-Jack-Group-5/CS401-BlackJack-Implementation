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
        client.addMessageHook(Message.Login.Response.class, (res) -> {
            // Note: By the time this code runs the login has already happened
            loginGui.close();
            playerGUI.show();
        });
        client.addMessageHook(Message.Hit.Response.class, (res) -> {});
        client.addMessageHook(Message.Stand.Response.class, (res) -> {});
        client.addMessageHook(Message.Leave.Response.class, (res) -> {});
        client.addMessageHook(Message.LobbyData.Response.class, (res) -> {});
        client.addMessageHook(Message.TableData.Response.class, (res) -> {});
        client.addMessageHook(Message.ClockSync.Response.class, (res) -> {});
        client.addMessageHook(Message.GameData.Response.class, (res) -> {});
        client.addMessageHook(Message.PlayerReady.Response.class, (res) -> {});
        client.addMessageHook(Message.PlayerLeave.Response.class, (res) -> {});
    }
}
