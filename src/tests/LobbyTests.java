package tests;

import game.Lobby;
import game.Player;
import game.Dealer;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

// please update i swear to god
public class LobbyTests {
    static String username = "fwaffycafecat";
    static String password = "garbage_value";
    static Lobby lobby = new Lobby();
    static int limit = 17;
    static Dealer dealer = new Dealer(username, password, limit);
    Dealer dealer1 = new Dealer("murderless_Crow", "testing5", limit);
    Dealer dealer2 = new Dealer("City_Escape", "supersonicspeed", limit);
    Player user1 = new Player("void_stranger", "0");
    Player user2 = new Player("layer_cake", "1");

    @BeforeClass
    public static void setUp() {
        // Initialize a Table object before each test
        lobby.addTable(dealer);
    }

    /* CHECKS ALL PROPERTIES OF THE TABLE ADDED */
    @Test
    // might have to run this one individually because the id number gets effed up by the random order
    public void checkTableId() {
        // check id
        assertEquals(0, lobby.getTables()[0].getTableId());
    }

    @Test
    public void checkDealer() {
        // check dealer
        assertEquals(dealer, lobby.getTables()[0].getDealer());
    }

    @Test
    public void checkPlayers() {
        lobby.getTables()[0].clearTableData();
        // add players to a table
        lobby.getTables()[0].addPlayer(user1);
        lobby.getTables()[0].addPlayer(user2);
        System.out.println(lobby.toString());
        // check player list
        assertEquals(user1, lobby.getTables()[0].getPlayers()[0]);
        assertEquals(user2, lobby.getTables()[0].getPlayers()[1]);
    }

    @Test
    public void checkPlayerCount() {
        lobby.getTables()[0].clearTableData();
        // add players to a table
        lobby.getTables()[0].addPlayer(user1);
        // check player count
        assertEquals(1, lobby.getTables()[0].getPlayerCount());
    }

    @Test
    public void checkPlayerLimit() {
        // check player limit
        assertEquals(6, lobby.getTables()[0].getPlayerLimit());
    }

    @Test
    public void checkIsActive() {
        // check is active
        assertEquals(false, lobby.getTables()[0].isActive());
    }

    @Test
    public void addTable() {
        lobby.addTable(dealer1);
        lobby.addTable(dealer2);
        System.out.println(lobby.toString());
    }

    @Test
    public void removeTable() {
        lobby.addTable(dealer1);
        lobby.addTable(dealer2);
        lobby.removeTable(dealer1);
        System.out.println(lobby.toString());
    }

}
