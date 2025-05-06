package tests;

import game.Value;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

public class ValueTests {
    @Test
    public void value_two() {
        Value v = Value.TWO;
        assertEquals(2, v.getValue());
        assertFalse(v.hasAlternate());
    }
    @Test
    public void value_three() {
        Value v = Value.THREE;
        assertEquals(3, v.getValue());
        assertFalse(v.hasAlternate());
    }
    @Test
    public void value_four() {
        Value v = Value.FOUR;
        assertEquals(4, v.getValue());
        assertFalse(v.hasAlternate());
    }
    @Test
    public void value_five() {
        Value v = Value.FIVE;
        assertEquals(5, v.getValue());
        assertFalse(v.hasAlternate());
    }
    @Test
    public void value_six() {
        Value v = Value.SIX;
        assertEquals(6, v.getValue());
        assertFalse(v.hasAlternate());
    }
    @Test
    public void value_seven() {
        Value v = Value.SEVEN;
        assertEquals(7, v.getValue());
        assertFalse(v.hasAlternate());
    }
    @Test
    public void value_eight() {
        Value v = Value.EIGHT;
        assertEquals(8, v.getValue());
        assertFalse(v.hasAlternate());
    }
    @Test
    public void value_nine() {
        Value v = Value.NINE;
        assertEquals(9, v.getValue());
        assertFalse(v.hasAlternate());
    }
    @Test
    public void value_ten() {
        Value v = Value.TEN;
        assertEquals(10, v.getValue());
        assertFalse(v.hasAlternate());
    }
    @Test
    public void value_jack() {
        Value v = Value.JACK;
        assertEquals(10, v.getValue());
        assertFalse(v.hasAlternate());
    }
    @Test
    public void value_queen() {
        Value v = Value.QUEEN;
        assertEquals(10, v.getValue());
        assertFalse(v.hasAlternate());
    }
    @Test
    public void value_king() {
        Value v = Value.KING;
        assertEquals(10, v.getValue());
        assertFalse(v.hasAlternate());
    }
    @Test
    public void value_ace() {
        Value v = Value.ACE;
        assertEquals(1, v.getValue());
        assertTrue(v.hasAlternate());
        assertEquals(11, v.getAlternate());
    }
}
