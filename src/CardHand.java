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
        // TODO: get card total
        return total;
    }
    public void addCard(Card card){
        // TODO: check if adding this card will exceed valueLimit

        hand[numCards] = card;
        numCards ++;
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
    private boolean bustCheck(Card[] hand){
        // TODO: loop through player hand if exceeds 21 then return true;
        return false;
    }
}
