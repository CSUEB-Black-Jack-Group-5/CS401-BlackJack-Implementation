package server;

import game.Player;
import game.Table;
import networking.Message;

public class TableThread implements Runnable {
    private static final int MAX_JOINED_USERS = 6;

    // Table table;
    ClientThreadWithHooks[] joinedUsers;
    int joinedUsersCount;
    Table table;

    public TableThread() {
        this.joinedUsers = new ClientThreadWithHooks[MAX_JOINED_USERS];
        this.joinedUsersCount = 0;
        this.table = new Table();
    }

    @Override
    public void run() {}

    public Table getTable() {
        return table;
    }

    public void addDealerToTable(DealerClientThread dealerThread) {
        this.joinedUsers[this.joinedUsersCount++] = dealerThread;
        dealerThread.setActiveTable(this);
    }
    public void addClientToTable(ClientThreadWithHooks clientThreadWithHooks) {
        this.joinedUsers[this.joinedUsersCount++] = clientThreadWithHooks;
        clientThreadWithHooks.setActiveTable(this);
    }

    // In TableThread.java
    // In TableThread.java, ensure broadcastToPlayers is properly implemented:

    public void broadcastToPlayers(Message message) {
        System.out.println("Broadcasting message " + message.getClass().getSimpleName() +
                " to " + joinedUsersCount + " users");

        int playerCount = 0;

        // Send to all joined users that are PlayerClientThreads
        for (int i = 0; i < joinedUsersCount; i++) {
            if (joinedUsers[i] != null && joinedUsers[i] instanceof PlayerClientThread) {
                PlayerClientThread playerThread = (PlayerClientThread)joinedUsers[i];
                System.out.println("Sending to player: " + playerThread.getPlayer().getUsername());
                joinedUsers[i].sendNetworkMessage(message);
                playerCount++;
            }
        }

        System.out.println("Message broadcast to " + playerCount + " players");
    }


}
