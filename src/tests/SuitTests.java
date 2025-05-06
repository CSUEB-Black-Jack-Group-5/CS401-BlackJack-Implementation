package tests;

import game.Suit;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertEquals;

public class SuitTests {
    @Test
    public void suit_clubs_value() {
        Suit suit = Suit.CLUBS;
        assertEquals(0, suit.getValue());
    }

    @Test
    public void suit_hearts_value() {
        Suit suit = Suit.HEARTS;
        assertEquals(1, suit.getValue());
    }

    @Test
    public void suit_spades_value() {
        Suit suit = Suit.SPADES;
        assertEquals(2, suit.getValue());
    }

    @Test
    public void suit_diamonds_value() {
        Suit suit = Suit.DIAMONDS;
        assertEquals(3, suit.getValue());
    }
}
