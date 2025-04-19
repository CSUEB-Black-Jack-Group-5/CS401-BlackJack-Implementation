package game;

public enum Value {
    TWO(2, false, 0),
    THREE(3, false, 0),
    FOUR(4, false, 0),
    FIVE(5, false, 0),
    SIX(6, false, 0),
    SEVEN(7, false, 0),
    EIGHT(8, false, 0),
    NINE(9, false, 0),
    TEN(10, false, 0),
    JACK(10, false, 0),
    QUEEN(10, false, 0),
    KING(10, false, 0),
    ACE(1, true, 11);

    private final int Value;
    private final boolean hasAlternate;
    private final int alternateValue;

    // based on UML class diagrams. Ace is the only card that has alternate
    Value(int value, boolean hasAlternate, int alternateValue) {
        this.Value = value;
        this.hasAlternate = hasAlternate;
        this.alternateValue = alternateValue;
    }

    public int getValue() {
        return Value;
    }

    public boolean hasAlternate() {
        return hasAlternate;
    }

    public int getAlternate() {
        return alternateValue;
    }
}
