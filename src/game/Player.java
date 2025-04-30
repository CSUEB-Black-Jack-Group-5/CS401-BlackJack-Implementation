package game;

import java.io.*;
import java.nio.file.*;

public class Player {
    private static int idCount = 0;
    private int playerId;
    private boolean isReady;
    private CardHand hand;
    private CardHand splitHand;
    private CardHand activeHandRef;
    private int wager;
    private Wallet wallet;
    private String username; // Username format: "basims21"
    private String userId;   // UserId format: "u001"

    public Player(String username, String userId) {
        this.playerId = ++idCount;
        this.username = username;
        this.userId = userId;
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

    public boolean isReady() {
        return isReady;
    }

    public void makeBet(float amount) {
        this.wager = (int) amount;
        wallet.removeFunds(-amount);
    }

    public void doubleDown(Card card) {
        wallet.removeFunds(-wager);
        wager *= 2;
        hit(card); // Player automatically draws one card
    }

    /**
     * Allows player to turn a pair of cards into 2 different hands.
     * They can play both hands, and potentially double their winnings or losses.
     */
    public boolean split() {
        if (hand.getNumCards() == 2 &&
                hand.getCard(0).getValue().getValue() == hand.getCard(1).getValue().getValue()) {

            // Store original two cards first
            Card firstCard = hand.getCard(0);
            Card secondCard = hand.getCard(1);

            // Create two new hands
            hand = new CardHand(21);
            hand.addCard(firstCard);

            splitHand = new CardHand(21);
            splitHand.addCard(secondCard);

            activeHandRef = hand;
            return true;
        }
        return false;
    }

    /**
     * Locks in player's current hand value
     */
    public void stand() {
        if (splitHand != null && activeHandRef == hand) {
            activeHandRef = splitHand;  // move to next hand
        } else {
            isReady = false;
        }
    }

    /**
     * Player tries to hit by adding a card to their current hand.
     * @param card The new card to add
     * @return true if successfully added without busting, false if bust occurred
     */
    public boolean hit(Card card) {
        return activeHandRef.addCard(card);
    }

    /**
     * Handles placing and resolving an insurance bet.
     * @param dealerHasBlackjack true if dealer has Blackjack, false otherwise
     */
    public void insurance(boolean dealerHasBlackjack) {
        if (wager > 0) {
            int insuranceAmount = wager / 2;
            wallet.removeFunds(-insuranceAmount);

            if (dealerHasBlackjack) {
                wallet.addFunds(insuranceAmount * 3); // original insurance + 2x winnings
            }
        }
    }

    /**
     * Resets the player's hands and wager for a new round.
     */
    public void resetHand() {
        this.hand = new CardHand(21);
        this.splitHand = null;
        this.activeHandRef = hand;
        this.isReady = false;
        this.wager = 0;
    }

    /**
     * Lets player quit early so that they only lose half of their bet.
     */
    public void surrender() {
        int refund = wager / 2;
        wallet.addFunds(refund);
        wager = 0;
    }

    /**
     * Detects if the player got a 2-card Blackjack.
     * It helps determine a quick automatic win.
     */
    public boolean blackjackCheck() {
        return activeHandRef.getNumCards() == 2 && activeHandRef.getTotalValue() == 21;
    }

    /**
     * Checks if the player busted (hand value exceeded 21)
     */
    public boolean bustCheck() {
        return activeHandRef.bustCheck();
    }

    public CardHand getHand() {
        return hand;
    }

    public Wallet getWallet() {
        return wallet;
    }

    // Database Methods

    /**
     * Saves the player's updated funds inside db/Players/<userId>/<userId>.csv
     */
    public void saveFunds() {
        String filePath = "db/Players/" + userId + "/" + userId + ".csv";
        try {
            Path path = Paths.get(filePath);
            StringBuilder updatedContent = new StringBuilder();
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                String header = reader.readLine();
                updatedContent.append(header).append("\n");
                String line = reader.readLine();
                if (line != null) {
                    String[] fields = line.split("\t");
                    fields[3] = String.valueOf(wallet.getFunds());
                    updatedContent.append(String.join("\t", fields));
                }
            }
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write(updatedContent.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Appends a new history entry to db/Players/<userId>/<userId>_history.csv
     */
    public void saveHistory(int wins, int losses, String gameId, String dealerId) {
        String filePath = "db/Players/" + userId + "/" + userId + "_history.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            writer.println(userId + "\t" + wins + "\t" + losses + "\t" + gameId + "\t" + dealerId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
