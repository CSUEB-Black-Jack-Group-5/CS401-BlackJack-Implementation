package server;

public class TableThread implements Runnable {
    private static final int MAX_JOINED_USERS = 6;

    // Table table;
    ClientThreadWithHooks[] joinedUsers;
    int joinedUsersCount;

    public TableThread() {
        this.joinedUsers = new ClientThreadWithHooks[MAX_JOINED_USERS];
        this.joinedUsersCount = 0;
        // this.table = new Table();
    }

    @Override
    public void run() {}

    // public Table getTable() {
    //     return table;
    // }

    public void addClientToTable(ClientThreadWithHooks clientThreadWithHooks) {
        this.joinedUsers[this.joinedUsersCount++] = clientThreadWithHooks;
        clientThreadWithHooks.setActiveTable(this);
    }
}
