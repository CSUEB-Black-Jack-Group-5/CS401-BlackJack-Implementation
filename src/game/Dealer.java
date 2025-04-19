package game;
import java.util.List;

public class Dealer {
    private static int idCount = 0;
    private String username;
    private String password;
    private int dealerId;
    private CardHand hand;
    private Shoe shoe;
    private int minValueRequired;

    /**
     * Constructs a new Dealer with login credentials and a minimum hand value to stop hitting.
     * Initializes a fresh hand and a shoe with 6 decks.
     */
    public Dealer(String username, String password, int minValueRequired) {
        this.username = username;
        this.password = password;
        this.dealerId = ++idCount;
        this.hand = new CardHand(21);
        this.shoe = new Shoe(6); // defaulting to 6 decks
        this.minValueRequired = minValueRequired;
    }

    /**
     * Shuffles the cards in the dealer's shoe.
     * This prepares the deck for a new round.
     */
    public void shuffle() {
        shoe.shuffle();
    }

    /**
     * Returns the dealer's current hand of cards.
     * Used for logic or display of dealer state.
     */
    public CardHand getHand() {
        return hand;
    }

    /**
     * Gets a card from the dealer's hand at a specific index.
     * Helpful to show one card face-up (e.g., in UI).
     */
    public Card getCardByIndex(int index) {
        return hand.getCard(index);
    }

    /**
     * Deals one card from the shoe to a player.
     * This is modular and reusable across game phases.
     *
     * This function was added to separate card-dealing logic and
     * allow reuse in deal(), hit() logic, or testing scenarios.
     */
    public void dealCard(Player player) {
        Card card = shoe.dealCard();
        player.getHand().addCard(card);
    }

    /**
     * Deals one card from the shoe to the dealer's own hand.
     *
     * This was added to simulate the dealer’s own turn where they hit until they meet a value requirement.
     */
    public void hitSelf() {
        Card card = shoe.dealCard();
        hand.addCard(card);
    }

    /**
     * Resets the dealer's hand for a new round.
     *
     * This was added to cleanly wipe the dealer's hand at the start of a new game phase.
     */
    public void resetHand() {
        this.hand = new CardHand(21);
    }

    /**
     * Deals two cards to each player and two to the dealer.
     *
     * This was added to handle the initial card distribution phase in one method.
     * Uses dealCard() and hitSelf() for modularity.
     */
    public void deal(List<Player> players) {
        for (Player player : players) {
            dealCard(player);
            dealCard(player);
        }
        hitSelf();
        hitSelf();
    }

    // Getters

    public int getDealerId() {
        return dealerId;
    }

    public int getMinValueRequired() {
        return minValueRequired;
    }

    public Shoe getShoe() {
        return shoe;
    }
}
