package game;
import java.io.Serializable;
import java.util.List;

public class Dealer implements Serializable {
    private static int idCount = 0;
    private String username;
    private String password;
    private int dealerId;
    private CardHand hand;
    private Shoe shoe;
    private int minValueRequired;

    public Dealer(String username, String password, int minValueRequired) {
        this.username = username;
        this.password = password;
        this.dealerId = ++idCount;
        this.hand = new CardHand(21);
        this.shoe = new Shoe(6); // defaulting to 6 decks
        this.minValueRequired = minValueRequired;
    }

    public void shuffle() {
        shoe.shuffle();
    }

    public CardHand getHand() {
        return hand;
    }

    public Card getCardByIndex(int index) {
        return hand.getCard(index);
    }

    public Card dealCard(Player player) {
        Card card = shoe.dealCard();
        player.getHand().addCard(card);
        return card;
    }

    public void hitSelf() {
        Card card = shoe.dealCard();
        hand.addCard(card);
    }

    public Card dealCardToSelf() {
        Card card = this.shoe.dealCard();
        this.hand.addCard(card);
        return card;
    }

    public int getTotalCards() {
        return this.shoe.getNumCards();
    }

    public void resetHand() {
        this.hand = new CardHand(21);
    }

    public void deal(List<Player> players) {
        for (Player player : players) {
            dealCard(player);
            dealCard(player);
        }
        hitSelf();
        hitSelf();
    }

    /**
     * Checks if the dealer has a natural Blackjack (two cards totaling 21).
     * Used to determine insurance resolution.
     */
    public boolean hasBlackjack() {
        return hand.getNumCards() == 2 && hand.getTotalValue() == 21;
    }

    /**
     * Pays a winning amount to the player by adding it to their wallet.
     * @param player The player to pay.
     * @param amount The amount to pay.
     */
    public void payPlayer(Player player, float amount) {
        player.getWallet().addFunds(amount);
    }

    // Getters
    public int getDealerId() {
        return dealerId;
    }

    public String getUsername() {
        return username;
    }

    public int getMinValueRequired() {
        return minValueRequired;
    }

    public Shoe getShoe() {
        return shoe;
    }
}
