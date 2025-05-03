package tests;

import game.Table;
import game.Player;
import game.Dealer;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

// please update i swear to god
public class TableTests {
    static String username = "fwaffycafecat";
    static String password = "garbage_value";
    static Table table;
    static Dealer dealer = new Dealer(username, password,17);
    Dealer dealer1 = new Dealer("murderless_Crow", "testing5",17);
    Dealer dealer2 = new Dealer("City_Escape", "supersonicspeed",12);
    Player user1 = new Player("void_stranger", "0");
    Player user2 = new Player("layer_cake", "1");
    Player user3 = new Player("gloam_valley", "2");
    Player user4 = new Player("blink_gone", "3");
    Player user5 = new Player("rude_buster", "4");
    Player user6 = new Player("tarrey_town", "5");

    @BeforeClass
    public static void setUp() {
        // Initialize a Table object before each test
        table = new Table(dealer);
    }

    /* CHECKS ALL PROPERTIES OF THE TABLE ADDED */
    @Test
    public void checkTableId() {
        // check id
        assertEquals(0, table.getTableId());
    }

    @Test
    public void checkDealer() {
        // check dealer
        assertEquals(dealer, table.getDealer());
    }

    @Test
    public void checkPlayers() {
        table.clearTableData();
        // add players to a table
        table.addPlayer(user1);
        table.addPlayer(user2);
        System.out.println(table.toString());
        // check player list
        assertEquals(user1, table.getPlayers()[0]);
        assertEquals(user2, table.getPlayers()[1]);
    }

    @Test
    public void checkPlayerCount() {
        table.clearTableData();
        // add players to a table
        table.addPlayer(user1);
        // check player count
        assertEquals(1, table.getPlayerCount());
    }

    @Test
    public void checkPlayerLimit() {
        // check player limit
        assertEquals(6, table.getPlayerLimit());
    }

    @Test
    public void checkIsActive() {
        // check is active
        assertEquals(false, table.isActive());
    }

    @Test
    public void checkIsFull() {
        table.clearTableData();
        System.out.println(table.toString());
        // check whether the table is full or not
        assertEquals(false, table.isFull());
        // add players to a table
        table.addPlayer(user1);
        table.addPlayer(user2);
        table.addPlayer(user3);
        table.addPlayer(user4);
        table.addPlayer(user5);
        table.addPlayer(user6);
        System.out.println(table.toString());
        // check whether the table is full or not
        assertEquals(true, table.isFull());
    }

    @Test
    public void checkTableReady() {
        table.clearTableData();
        // add players to a table
        table.addPlayer(user1);
        table.addPlayer(user2);
        table.addPlayer(user3);
        table.addPlayer(user4);
        table.addPlayer(user5);
        table.addPlayer(user6);
        System.out.println(table.toString());
        // check whether the table is ready or not
        assertEquals(false, table.tableReady());
        // set all players to ready
        user1.setReady(true);
        user2.setReady(true);
        user3.setReady(true);
        user4.setReady(true);
        user5.setReady(true);
        user6.setReady(true);
        System.out.println(table.toString());
        // check whether the table is ready or not
        assertEquals(true, table.tableReady());
    }

    @Test
    public void checkAddPlayer() {
        table.clearTableData();
        // add players to a table
        table.addPlayer(user1);
        System.out.println(table.toString());
        // check whether the table added a player or not
        assertEquals(user1, table.getPlayers()[0]);
    }

    @Test
    public void checkRemovePlayer() {
        table.clearTableData();
        // add players to a table
        table.addPlayer(user1);
        table.addPlayer(user2);
        table.addPlayer(user3);
        System.out.println(table.toString());
        // remove a player
        table.removePlayer(user2);
        System.out.println(table.toString());
        // check whether the table removed the player or not
        assertEquals(user1, table.getPlayers()[0]);
    }

}
