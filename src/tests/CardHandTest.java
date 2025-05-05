import game.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests based on the testing document.
 */
public class CardHandTest {

    private CardHand hand;

    @BeforeEach
    public void setUp() {
        hand = new CardHand(21);
    }

    @Test
    public void testGetTotalValue() {
        // getTotalValue() should return sum of card values
        hand.addCard(new Card(Suit.HEARTS, Value.FIVE));
        hand.addCard(new Card(Suit.SPADES, Value.THREE));
        assertEquals(8, hand.getTotalValue());
    }

    @Test
    public void testAddCardModifiesHand() {
        // addCard() should modify the internal array
        Card card = new Card(Suit.CLUBS, Value.SEVEN);
        boolean added = hand.addCard(card);
        assertTrue(added);
        assertEquals(card, hand.getCard(0));
    }

    @Test
    public void testGetNumCards() {
        // getNumCards() should return number of cards in hand
        hand.addCard(new Card(Suit.HEARTS, Value.TWO));
        hand.addCard(new Card(Suit.HEARTS, Value.FOUR));
        assertEquals(2, hand.getNumCards());
    }

    @Test
    public void testGetCardAtIndex() {
        // getCard(index) should return card at correct position
        Card first = new Card(Suit.HEARTS, Value.NINE);
        Card second = new Card(Suit.DIAMONDS, Value.SIX);
        hand.addCard(first);
        hand.addCard(second);
        assertEquals(first, hand.getCard(0));
        assertEquals(second, hand.getCard(1));
    }

    @Test
    public void testBustCheck() {
        // bustCheck() should return true if total value > 21
        hand.addCard(new Card(Suit.HEARTS, Value.KING));
        hand.addCard(new Card(Suit.SPADES, Value.QUEEN));
        hand.addCard(new Card(Suit.DIAMONDS, Value.TWO));
        assertTrue(hand.bustCheck());
    }
}
