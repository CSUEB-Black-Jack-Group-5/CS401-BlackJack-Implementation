package networking;

public enum AccountType {
    DEALER,
    PLAYER;

    public int getValue(){
        return switch (this) {
            case DEALER -> 0;
            case PLAYER -> 1;
        };
    }
}
