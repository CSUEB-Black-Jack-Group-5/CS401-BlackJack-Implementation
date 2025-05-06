package game;

import java.io.Serializable;

public class Card implements Serializable {
    private Suit suit;
    private Value value;
    private boolean faceUp;  // Added field to track face up/down state

    public Card(Suit suit, Value value) {
        this.suit = suit;
        this.value = value;
        this.faceUp = true;  // Default to face up
    }

    public Value getValue() {
        return this.value;
    }

    public Suit getSuit() {
        return this.suit;
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public void setFaceUp(boolean faceUp) {
        this.faceUp = faceUp;
    }

    public void flip() {
        this.faceUp = !this.faceUp;
    }
}