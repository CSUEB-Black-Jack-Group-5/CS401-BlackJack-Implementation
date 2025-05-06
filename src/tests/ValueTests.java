import game.Value;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValueTests {
    @Test
    public void value_two() {
        Value v = Value.TWO;
        Assertions.assertEquals(2, v.getValue());
        Assertions.assertFalse(v.hasAlternate());
    }
    @Test
    public void value_three() {
        Value v = Value.THREE;
        Assertions.assertEquals(3, v.getValue());
        Assertions.assertFalse(v.hasAlternate());
    }
    @Test
    public void value_four() {
        Value v = Value.FOUR;
        Assertions.assertEquals(4, v.getValue());
        Assertions.assertFalse(v.hasAlternate());
    }
    @Test
    public void value_five() {
        Value v = Value.FIVE;
        Assertions.assertEquals(5, v.getValue());
        Assertions.assertFalse(v.hasAlternate());
    }
    @Test
    public void value_six() {
        Value v = Value.SIX;
        Assertions.assertEquals(6, v.getValue());
        Assertions.assertFalse(v.hasAlternate());
    }
    @Test
    public void value_seven() {
        Value v = Value.SEVEN;
        Assertions.assertEquals(7, v.getValue());
        Assertions.assertFalse(v.hasAlternate());
    }
    @Test
    public void value_eight() {
        Value v = Value.EIGHT;
        Assertions.assertEquals(8, v.getValue());
        Assertions.assertFalse(v.hasAlternate());
    }
    @Test
    public void value_nine() {
        Value v = Value.NINE;
        Assertions.assertEquals(9, v.getValue());
        Assertions.assertFalse(v.hasAlternate());
    }
    @Test
    public void value_ten() {
        Value v = Value.TEN;
        Assertions.assertEquals(10, v.getValue());
        Assertions.assertFalse(v.hasAlternate());
    }
    @Test
    public void value_jack() {
        Value v = Value.JACK;
        Assertions.assertEquals(10, v.getValue());
        Assertions.assertFalse(v.hasAlternate());
    }
    @Test
    public void value_queen() {
        Value v = Value.QUEEN;
        Assertions.assertEquals(10, v.getValue());
        Assertions.assertFalse(v.hasAlternate());
    }
    @Test
    public void value_king() {
        Value v = Value.KING;
        Assertions.assertEquals(10, v.getValue());
        Assertions.assertFalse(v.hasAlternate());
    }
    @Test
    public void value_ace() {
        Value v = Value.ACE;
        Assertions.assertEquals(1, v.getValue());
        Assertions.assertTrue(v.hasAlternate());
        Assertions.assertEquals(11, v.getAlternate());
    }
}
