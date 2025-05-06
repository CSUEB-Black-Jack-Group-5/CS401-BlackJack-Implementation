package server;

import game.Dealer;
import game.Player;
import game.Table;
import networking.Message;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TableThread implements Runnable {
    private static final int MAX_JOINED_USERS = 6;

    private final Map<String, Integer> playerBets = new ConcurrentHashMap<>();
    private List<Player> playersInTable;
    private boolean stop;
    // Table table;
    ClientThreadWithHooks[] joinedUsers;
    int joinedUsersCount;
    Table table;

    public TableThread(Dealer dealer) {
        this.joinedUsers = new ClientThreadWithHooks[MAX_JOINED_USERS];
        this.joinedUsersCount = 0;
        this.table = new Table(dealer);
        this.stop = true;
    }

    public ClientThreadWithHooks[] getJoinedUsers() {
        return Arrays.stream(joinedUsers).filter(Objects::nonNull).toArray(ClientThreadWithHooks[]::new);
    }

    @Override
    public void run() {
        while (stop) {
            // initialize in here?
            this.playersInTable = List.of(table.getPlayers());

            while (!playersInTable.isEmpty()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Game starting with " + playersInTable.size() + " players!");

            // ask each player for a bet
            for (ClientThreadWithHooks playerMsg : getJoinedUsers()) {
                playerMsg.sendNetworkMessage(new Message.Bet.Request());
            }


            waitForAllPlayerBets();

            // deal  initial hands
            for(Player p : playersInTable){
                table.dealInitialCards(p.getUsername());
            }
            // deal to dealer
            table.dealInitialCardsToDealer();


            // ask each player to hit or stand in turn
            for (int i = 0; i < getJoinedUsers().length; i++) {
                ClientThreadWithHooks playerMsg = getJoinedUsers()[i];
                Player player = playersInTable.get(i);
                boolean turnActive = true;
                while (turnActive) {
                    // this can be empty because we will set it in the playerClientThread;
                    playerMsg.sendNetworkMessage(new Message.PlayerAction.Request(player.getUsername(),""));

                    waitForPlayerAction(playerMsg, player);

                    if (player.getLastAction().equals("stand") || player.getHand().getTotalValue() >= 21) {
                        turnActive = false;
                    }
                }
            }

            // everyone already went so now deal dealer hand
            table.playDealerHand();
            // now evaluate dealer hand
            table.evaluateAllHands();

            // check gameData now? idk
            stop = false;

        }
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
    private String waitForPlayerAction(ClientThreadWithHooks playerMsg, Player player) {
        System.out.println("Waiting for action from player: " + player.getUsername());

        while (true) {
            String action = player.getLastAction();
            if (action != null) {
                return action;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.out.println("Interrupted while waiting for player action");
                return "TIMEOUT";
            }
        }
    }
    private void waitForAllPlayerBets() {
        System.out.println("Waiting for all players to place bets...");

        while (true) {
            boolean allBetsPlaced = true;

            for (Player player : playersInTable) {
                if (player.getCurrentBet() <= 0) {
                    allBetsPlaced = false;
                    break;
                }
            }

            if (allBetsPlaced) {
                System.out.println("All bets received.");
                break;
            }

            try {
                Thread.sleep(500); // Don't tight loop
            } catch (InterruptedException e) {
                System.out.println("Interrupted while waiting for bets");
                break;
            }
        }
    }

}
