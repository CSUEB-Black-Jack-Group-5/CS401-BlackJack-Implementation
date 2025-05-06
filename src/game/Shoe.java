package game;

import java.io.Serializable;
import java.util.Random;

public class Shoe implements Serializable {
    private Card[] extendedDeck;
    private int numCards;
    private int numDecks;


    public  Shoe(int numDecks){
        this.numDecks = numDecks;
        // standard deck is 52 cards
        this.numCards = numDecks * 52;
        this.extendedDeck = new Card[this.numCards];
        createExtendedDecks();
    }
    // implementing Fisher-Yates shuffle
    public void shuffle(){
        Random rand = new Random();
        for(int i = this.numCards - 1; i > 0; i--){
            int index = rand.nextInt(i+1);
            // swap
            Card tmp =  extendedDeck[index];
            extendedDeck[index] = extendedDeck[i];
            extendedDeck[i] = tmp;
        }
    }
    public Card dealCard(){
        // TODO: Implement dealing logic
        // shoe should never be empty so no need to throw an exception
        Card topCard = extendedDeck[numCards-1];
        numCards--;
        // if topCard is the cut card implement a reshuffle
        return topCard;
    }

    // helper func
    private void createExtendedDecks(){
        int cardIndex = 0;
        // TODO:
        // should add the 'cut' card at the beginning
        // need to create another constructor to accept a cut card as param.
        for(int deck = 0; deck < this.numDecks; deck++){
            // creating each card combo for each deck
            for(Suit suit: Suit.values()){
                for(Value value: Value.values()){
                    extendedDeck[cardIndex] = new Card(suit,value);
                    cardIndex++;
                }
            }
        }
    }
    // naive way of testing for now
    // TODO: implement JUnit tests
    public void printDeck() {
        System.out.println("Shoe contains " + numCards + " cards:");
        for (int i = 0; i < numCards; i++) {
            Card card = extendedDeck[i];
            System.out.println(i + ": " + card.getSuit() + " " + card.getValue());
        }
    }

    public int getNumCards() {
        return this.numCards;
    }

    public int getTotalCards() {
        return this.numCards;
    }
}
