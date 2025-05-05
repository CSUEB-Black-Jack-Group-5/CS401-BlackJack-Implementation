package server;

import game.Dealer;
import game.Player;
import game.Table;
import networking.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TableThread implements Runnable {
    private static final int MAX_JOINED_USERS = 6;

    private final Map<String, Integer> playerBets = new ConcurrentHashMap<>();


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
    public void run() {

    }

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
    public void placeBet(String username, int bet) {
        playerBets.put(username, bet);
    }
    public boolean allPlayersBet() {
        return playerBets.size() == joinedUsersCount;
    }
    public void resetBets() {
        playerBets.clear();
    }

}
