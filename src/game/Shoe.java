package game;

public class Shoe {
    private Card[] extendedDeck;
    private int numCards;
    private int numDecks;


    public  Shoe(int numDecks){
        this.numDecks = numDecks;
        // standard deck is 52 cards
        this.numCards = numDecks * 52;
        this.extendedDeck = new Card[this.numCards];
        //createExtendedDecks();
    }
    public void shuffle(){
        // TODO: Implement shuffle logic
        // should also think of the 'cut' once we reach the 'cut' card
        // we implement a reshuffle

    }
    public Card dealCard(){
        // TODO: Implement dealing logic
        // shoe should never be empty so no need to throw an exception
        return null;
    }
    /*
        In UML diagram we have drawCard no sure if that is needed since dealCard
        essentially draws a card from the deck.
    * */

    // helper func
    private void createExtendedDecks(){
        //TODO: logic to create decks with value and suits

    }
}
