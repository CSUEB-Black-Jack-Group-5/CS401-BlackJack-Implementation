package game;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

public class DealerTest {

    private Dealer dealer;
    private Player player;

    @BeforeEach
    public void setUp() {
        dealer = new Dealer("dealer1", "password", 17);
        player = new Player("testuser", "u001");
    }

    @Test
    public void testDealCard() {
        int initialCards = player.getHand().getNumCards();
        dealer.dealCard(player);
        assertEquals(initialCards + 1, player.getHand().getNumCards());
    }

    @Test
    public void testHitSelf() {
        int initialCards = dealer.getHand().getNumCards();
        dealer.hitSelf();
        assertEquals(initialCards + 1, dealer.getHand().getNumCards());
    }

    @Test
    public void testShuffle() {
        assertDoesNotThrow(() -> dealer.shuffle());
    }

    @Test
    public void testResetHand() {
        dealer.hitSelf();
        dealer.resetHand();
        assertEquals(0, dealer.getHand().getNumCards());
    }

    @Test
    public void testDealMultiplePlayers() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("p1", "u002"));
        players.add(new Player("p2", "u003"));
        dealer.deal(players);
        for (Player p : players) {
            assertEquals(2, p.getHand().getNumCards());
        }
        assertEquals(2, dealer.getHand().getNumCards());
    }

    @Test
    public void testHasBlackjackTrue() {
        dealer.getHand().addCard(new Card(Suit.HEARTS, Value.ACE));
        dealer.getHand().addCard(new Card(Suit.SPADES, Value.KING));
        assertTrue(dealer.hasBlackjack());
    }

    @Test
    public void testPayPlayer() {
        float initialFunds = player.getWallet().getFunds();
        dealer.payPlayer(player, 100);
        assertEquals(initialFunds + 100, player.getWallet().getFunds());
    }
}
