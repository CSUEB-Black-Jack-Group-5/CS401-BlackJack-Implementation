package tests;

import game.Shoe;
import org.junit.jupiter.api.Test;

public class ShoeTests {
    @Test
    public void shoe_deal_card() {
        Shoe shoe = new Shoe(1);
        shoe.dealCard();
    }
}
