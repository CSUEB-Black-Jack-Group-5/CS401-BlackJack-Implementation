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
        int ace = 0;
        for(int i = 0; i < numCards; i++){
            // by default ace value holds 1
            total += hand[i].getValue().getValue();
            if(hand[i].getValue().hasAlternate()){
                ace++;
            }

        }
        // use alt. values for aces when possible
        for(int i = 0; i < ace; i++){
            // since we count ace as 1 first we set the aceVal to 10
            int aceVal = Value.ACE.getAlternate() - Value.ACE.getValue();
            if(total + aceVal <= valueLimit){
                total += aceVal;
            }
            else{
                // over 21 stop calculating aces
                break;
            }
        }
        return total;
    }
    public boolean addCard(Card card){
        hand[numCards] = card;
        numCards++;

        if (getTotalValue() > valueLimit) {
            // remove card if it would bust
            numCards--;
            return false;
        }

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
