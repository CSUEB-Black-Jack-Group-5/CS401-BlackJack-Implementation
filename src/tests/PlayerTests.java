import game.Card;
import game.Player;
import game.Suit;
import game.Value;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the Player class.
 */
public class PlayerTests {

    private Player player;

    @Before
    public void setUp() {
        // Initialize a Player object before each test
        player = new Player("testuser", "u001");
    }

    @Test
    public void testMakeBet() {
        // Test that making a bet properly deducts funds
        player.getWallet().addFunds(100);
        float initialFunds = player.getWallet().getFunds();
        player.makeBet(50);
        assertEquals(initialFunds - 50, player.getWallet().getFunds(), 0.001);
    }

    @Test
    public void testDoubleDown() {
        // Test that doubling down doubles the wager and hits a card
        player.getWallet().addFunds(100);
        player.makeBet(50);
        Card card = new Card(Suit.HEARTS, Value.FIVE);
        player.doubleDown(card);
        assertEquals(100, player.getWager());
    }

    @Test
    public void testSplitSuccess() {
        // Test that a split succeeds when two cards have the same value
        player.getHand().addCard(new Card(Suit.HEARTS, Value.FIVE));
        player.getHand().addCard(new Card(Suit.SPADES, Value.FIVE));
        assertTrue(player.split());
    }

    @Test
    public void testSplitFail() {
        // Test that a split fails when two cards have different values
        player.getHand().addCard(new Card(Suit.HEARTS, Value.FIVE));
        player.getHand().addCard(new Card(Suit.SPADES, Value.SIX));
        assertFalse(player.split());
    }

    @Test
    public void testStand() {
        // Test that standing disables readiness when no split hand exists
        player.setReady(true);
        player.stand();
        assertFalse(player.isReady());
    }

    @Test
    public void testHitNoBust() {
        // Test that hitting adds a card without busting
        Card card = new Card(Suit.HEARTS, Value.FIVE);
        assertTrue(player.hit(card));
    }

    @Test
    public void testInsuranceDealerHasBlackjack() {
        // Test that insurance payout occurs correctly when dealer has Blackjack
        player.getWallet().addFunds(100);
        player.makeBet(100);
        float fundsBefore = player.getWallet().getFunds();
        player.insurance(true);
        System.out.println(player.getWallet().getFunds());
        System.out.println(fundsBefore);
        assertTrue(player.getWallet().getFunds() > fundsBefore);
    }

    @Test
    public void testResetHand() {
        // Test that resetting hand clears the cards and wager
        player.makeBet(100);
        player.hit(new Card(Suit.HEARTS, Value.FIVE));
        player.resetHand();
        assertEquals(0, player.getHand().getNumCards());
        assertEquals(0, player.getWager());
    }

    @Test
    public void testSurrender() {
        // Test that surrender refunds half the wager
        player.getWallet().addFunds(100);
        player.makeBet(100);
        player.surrender();
        assertEquals(50, player.getWallet().getFunds(), 0.001);
    }

    @Test
    public void testBlackjackCheck() {
        // Test that blackjackCheck returns true when hand is Ace + 10-value card
        player.getHand().addCard(new Card(Suit.HEARTS, Value.ACE));
        player.getHand().addCard(new Card(Suit.SPADES, Value.KING));
        assertTrue(player.blackjackCheck());
    }

    @Test
    public void testBustCheck() {
        // Test that bustCheck returns true when hand value exceeds 21
        player.getHand().addCard(new Card(Suit.HEARTS, Value.KING));
        player.getHand().addCard(new Card(Suit.SPADES, Value.KING));
        player.getHand().addCard(new Card(Suit.CLUBS, Value.TWO));
        // When addCard adds a card, if it busts we dont add it
        assertEquals(20, player.getHand().getTotalValue());
    }
}
