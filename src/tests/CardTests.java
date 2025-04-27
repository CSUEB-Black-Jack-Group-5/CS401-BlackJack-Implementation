import game.Card;
import game.Suit;
import game.Value;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CardTests {
    @Test
    public void card_constructor() {
        // Test all possible combinations a card can be in
        for (Suit s : Suit.values()) {
            for (Value v : Value.values()) {
                Card card = new Card(s, v);
                Assertions.assertEquals(s, card.getSuit());
                Assertions.assertEquals(v, card.getValue());
            }
        }
    }
}
