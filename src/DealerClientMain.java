public class DealerClientMain {
    static ClientWithHooks client;
    static LoginGUI loginGUI;           // TODO: LoginGUI
    static DealerGUI dealerGUI;         // TODO: DealerGUI

    // Todo: Message.Login.Response
    // Todo: Message.DealCard.Response
    // Todo: Message.Stand.Response
    // Todo: Message.Leave.Response
    // Todo: Message.LobbyData.Response
    // Todo: Message.ClockSync.Response
    // Todo: Message.GameData.Response
    public static void main(String[] args) {
        client = new ClientWithHooks(DEALER, "localhost", 3333);
        loginGUI = new LoginGUI(client);
        dealerGUI = new DealerGUI(client);

        client.addMessageHook(Message.Login.Response.class, (res) -> {
            loginGUI.close();
            dealerGUI.show();
        });
        client.addMessageHook(Message.Leave.Response.class, (res) -> {});
        client.addMessageHook(Message.TableData.Response.class, (res) -> {});
        client.addMessageHook(Message.ClockSync.Response.class, (res) -> {});
        client.addMessageHook(Message.GameData.Response.class, (res) -> {});
        client.addMessageHook(Message.LobbyData.Response.class, (res) -> {});
    }
}
