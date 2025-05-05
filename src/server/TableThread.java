package server;

import game.Dealer;
import game.Table;
import networking.Message;

public class TableThread implements Runnable {
    private static final int MAX_JOINED_USERS = 6;

    // Table table;
    ClientThreadWithHooks[] joinedUsers;
    int joinedUsersCount;
    Table table;

    public TableThread(Dealer dealer) {
        this.joinedUsers = new ClientThreadWithHooks[MAX_JOINED_USERS];
        this.joinedUsersCount = 0;
        this.table = new Table(dealer);
    }

    @Override
    public void run() {}

    public Table getTable() {
        return table;
    }

    public void addClientToTable(ClientThreadWithHooks clientThreadWithHooks) {
        if (joinedUsersCount < MAX_JOINED_USERS) {
            joinedUsers[joinedUsersCount++] = clientThreadWithHooks;
            clientThreadWithHooks.setActiveTable(this);
            // let client know they joined the table
            clientThreadWithHooks.sendNetworkMessage(new Message.JoinTable.Response(true));
        } else {
            // let client know that the table is full
            clientThreadWithHooks.sendNetworkMessage(new Message.JoinTable.Response(false));
        }
    }
}
