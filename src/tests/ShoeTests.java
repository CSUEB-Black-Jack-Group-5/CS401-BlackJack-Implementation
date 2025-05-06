import game.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;


/**
 * Simple unit tests for the Shoe class.
 */
public class ShoeTests {

    private Shoe shoe;

    @BeforeEach
    public void setUp() {
        shoe = new Shoe(1); // one deck = 52 cards
    }

    @Test
    public void shoe_deal_card() {
        Shoe shoe = new Shoe(1);
        shoe.dealCard();
    }

    @Test
    public void testDealCardReturnsCard() {
        // Deal a card and check it's not null
        Card card = shoe.dealCard();
        assertNotNull(card);
    }

    @Test
    public void testDealCard52Times() {
        // Deal all cards and count
        int count = 0;
        while (count < 52) {
            assertNotNull(shoe.dealCard());
            count++;
        }
        assertEquals(52, count);
    }

    @Test
    public void testShuffleDoesNotCrash() {
        // Shuffling should not throw an exception
        assertDoesNotThrow(() -> shoe.shuffle());
    }

    @Test
    public void testIncludesAceOfSpades() {
        // Check if Ace of Spades is in the shoe
        boolean found = false;
        Shoe freshShoe = new Shoe(1);
        for (int i = 0; i < 52; i++) {
            Card c = freshShoe.dealCard();
            if (c.getSuit() == Suit.SPADES && c.getValue() == Value.ACE) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }
}
