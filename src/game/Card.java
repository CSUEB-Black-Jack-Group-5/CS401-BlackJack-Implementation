package game;

import java.io.Serializable;

public class Card implements Serializable {
    private Suit suit;
    private Value value;

    public Card(Suit suit,Value value){
        if (suit == null || value == null) {
            throw new NullPointerException("Suit and value cannot be null");
        }
        this.suit = suit;
        this.value = value;
    }

    public Value getValue() {
        return this.value;
    }

    public Suit getSuit() {
        return this.suit;
    }
    @Override
    public String toString() {
        return value + " of " + suit;
    }
}
