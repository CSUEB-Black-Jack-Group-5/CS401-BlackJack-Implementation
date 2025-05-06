package server;

import game.Dealer;
import game.Table;

public class TableThread implements Runnable {
    private static final int MAX_JOINED_USERS = 7;

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
        this.joinedUsers[this.joinedUsersCount++] = clientThreadWithHooks;
        clientThreadWithHooks.setActiveTable(this);
    }

    public ClientThreadWithHooks[] getJoinedUsers() {
        return joinedUsers;
    }
}
