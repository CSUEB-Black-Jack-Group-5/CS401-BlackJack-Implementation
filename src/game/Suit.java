package game;

import java.io.Serializable;

public enum Suit implements Serializable {
    CLUBS,
    HEARTS,
    SPADES,
    DIAMONDS;
//edit
    public int getValue(){
        return switch (this) {
            case CLUBS -> 0;
            case HEARTS -> 1;
            case SPADES -> 2;
            case DIAMONDS -> 3;
        };
    }
}
