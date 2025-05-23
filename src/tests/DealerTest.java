package tests;


import game.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DealerTest {

    private Dealer dealer;
    private Player player;

    @Before
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
}