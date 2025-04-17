public class ClientMain {
    static LoginGUI loginGUI;
    static ClientWithHooks client;
    static ClientGUI clientGUI;

    public static void main(String[] args) {
        /*
            LoginGUI will send the server a message indicating a login request
            The server will send a message back indicating what type of user logged in
               - This case is handled below
         */
        loginGUI = new LoginGui(client);
        client.addMessageHook(Message.Login.Response.class, (res) -> {
            loginGUI.close();                          // close the login gui (we're done with it)
            switch (res.getType()) {
                case DEALER: {
                    client.addMessageHook(Message.Leave.Response.class, (res) -> {});
                    client.addMessageHook(Message.TableData.Response.class, (res) -> {});
                    client.addMessageHook(Message.ClockSync.Response.class, (res) -> {});
                    client.addMessageHook(Message.GameData.Response.class, (res) -> {});
                    client.addMessageHook(Message.LobbyData.Response.class, (res) -> {});
                    clientGUI = new DealerClientGUI(client);
                }
                case PLAYER: {
                    client.addMessageHook(Message.Hit.Response.class, (res) -> {});
                    client.addMessageHook(Message.Stand.Response.class, (res) -> {});
                    client.addMessageHook(Message.Leave.Response.class, (res) -> {});
                    client.addMessageHook(Message.LobbyData.Response.class, (res) -> {});
                    client.addMessageHook(Message.TableData.Response.class, (res) -> {});
                    client.addMessageHook(Message.ClockSync.Response.class, (res) -> {});
                    client.addMessageHook(Message.GameData.Response.class, (res) -> {});
                    client.addMessageHook(Message.PlayerReady.Response.class, (res) -> {});
                    client.addMessageHook(Message.PlayerLeave.Response.class, (res) -> {});
                    clientGUI = new PlayerClientGUI(client); // open the player client gui
                }
            };
        });
    }
}
