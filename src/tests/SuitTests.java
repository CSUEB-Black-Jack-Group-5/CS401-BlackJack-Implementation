import game.Suit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SuitTests {
    @Test
    public void suit_clubs_value() {
        Suit suit = Suit.CLUBS;
        Assertions.assertEquals(0, suit.getValue());
    }

    @Test
    public void suit_hearts_value() {
        Suit suit = Suit.HEARTS;
        Assertions.assertEquals(1, suit.getValue());
    }

    @Test
    public void suit_spades_value() {
        Suit suit = Suit.SPADES;
        Assertions.assertEquals(2, suit.getValue());
    }

    @Test
    public void suit_diamonds_value() {
        Suit suit = Suit.DIAMONDS;
        Assertions.assertEquals(3, suit.getValue());
    }
}
