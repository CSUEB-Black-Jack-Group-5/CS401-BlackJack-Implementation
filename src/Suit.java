public enum Suit {
    CLUBS,
    HEARTS,
    SPADES,
    DIAMONDS;

    public int getValue(){
        return switch (this) {
            case CLUBS -> 0;
            case HEARTS -> 1;
            case SPADES -> 2;
            case DIAMONDS -> 3;
        };
    }
}
