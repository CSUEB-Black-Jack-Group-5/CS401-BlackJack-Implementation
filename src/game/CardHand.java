package game;

public class CardHand {
    private int numCards;
    private Card[] hand;
    private int valueLimit;

    public CardHand(int valueLimit){
        this.valueLimit = valueLimit;
        this.numCards = 0;
        this.hand = new Card[52];
    }

    public int getTotalValue(){
        int total = 0;
        for(int i = 0; i < numCards; i++){
            // need to change return type of card class
            total += hand[i].getValue();
        }
        return total;
    }
    public boolean addCard(Card card){
        // need to change return type of card class
        int potentialTotal = getTotalValue() + card.getValue();

        if (potentialTotal > valueLimit) {
            return false;
        }

        hand[numCards] = card;
        numCards++;
        return true;

    }
    public int getNumCards(){
        return this.numCards;
    }
    public Card getCard(int index){
        // bounds checking
        if(index < 0 || index>=numCards){
            return null;
        }
        return hand[index];
    }
    public boolean bustCheck() {
        return getTotalValue() > valueLimit;
    }
}
