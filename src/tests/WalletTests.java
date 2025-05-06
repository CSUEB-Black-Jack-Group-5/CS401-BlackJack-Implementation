package tests;

import game.Wallet;
import org.junit.Test;

import static org.junit.Assert.*;

public class WalletTests {
    @Test
    public void wallet_constructor_and_getFunds() {
        Wallet wallet = new Wallet();
        assertEquals(0.0f, wallet.getFunds(), 0.001);
    }

    @Test
    public void wallet_addFunds() {
        Wallet wallet = new Wallet();
        wallet.addFunds(1.0f);
        assertEquals(1.0f, wallet.getFunds(), 0.001);

        wallet.addFunds(45.42f);
        assertEquals(46.42f, wallet.getFunds(), 0.01);
    }

    @Test
    public void wallet_removeFunds() {
        Wallet wallet = new Wallet();
        wallet.addFunds(45.0f);
        assertTrue(wallet.removeFunds(-32.0f));
        assertEquals(13.0f, wallet.getFunds(), 0.001);
        assertFalse(wallet.removeFunds(-45.0f));
        assertEquals(13.0f, wallet.getFunds(), 0.001);
    }
}
