public class Player {
    private static int idCount = 0;
    private int playerId;
    private boolean isReady;
    private CardHand hand;
    private CardHand splitHand; // null unless split
    private CardHand activeHandRef;
    private int wager;
    private Wallet wallet;

    public Player() {
        this.playerId = ++idCount;
        this.isReady = false;
        this.hand = new CardHand(21);
        this.splitHand = null;
        this.activeHandRef = hand;
        this.wager = 0;
        this.wallet = new Wallet();
    }

    public void setReady(boolean ready) {
        this.isReady = ready;
    }

    public void makeBet(float amount) {
        this.wager = (int) amount;
        wallet.removeFunds(-amount);
    }

    public void doubleDown() {
        wallet.removeFunds(-wager);
        wager *= 2;
        // Assume draw one more card
        // needs Game/Dealer context
    }

    public void stand() {
        // If the player has split, and they are currently playing their first hand
        if (splitHand != null && activeHandRef == hand) {
            activeHandRef = splitHand;  // move to next hand
        } else {
            // No more hands to play or already on split hand — turn is over
            // might have to notify the Game/Table class here
            isReady = false;  // or a 'hasPlayed' flag if needed
        }
    }


    public void hit() {
        // activeHandRef.addCard(...); // needs Game/Dealer context
    }

    public void insurance() {
        // insurance logic can vary — just a placeholder for now
    }

    public void surrender() {
        // logic to surrender — typically return half wager
        int refund = wager / 2;
        wallet.addFunds(refund);
        wager = 0;
    }

    public boolean blackjackCheck() {
        return activeHandRef.getNumCards() == 2 && activeHandRef.getTotalValue() == 21;
    }

    public Wallet getWallet() {
        return wallet;
    }

}
