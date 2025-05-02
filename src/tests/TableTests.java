package tests;

import game.Player;
import game.Table;
import game.Dealer;
import game.Table;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TableTests {
    Player user1 = new Player("void_stranger", "0");
    Player user2 = new Player("layer_cake", "1");
    Player user3 = new Player("gloam_valley", "2");
    Player user4 = new Player("blink_gone", "3");
    Player user5 = new Player("rude_buster", "4");
    Player user6 = new Player("tarrey_town", "5");
    Player user7 = new Player("", "6");
    static String username = "fwaffycafecat";
    static String password = "garbage_value";
    static Dealer dealer = new Dealer(username, password);
    Dealer dealer1 = new Dealer("moon_in_one's_cup", "genshin");
    Dealer dealer2 = new Dealer("city_escape", "sonic");
    static Table table;

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
    public void checkPlayerLimit() {
        // check player limit
        assertEquals(6, table.getPlayerLimit());
    }

    @Test
    public void checkPlayerCount() {
        // check player count
        assertEquals(0, table.getPlayerCount());
    }

    @Test
    public void checkIsActive() {
        // check is active
        assertEquals(true, table.isActive());
    }

}
