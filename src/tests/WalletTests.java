import game.Wallet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WalletTests {
    @Test
    public void wallet_constructor_and_getFunds() {
        Wallet wallet = new Wallet();
        Assertions.assertEquals(0.0f, wallet.getFunds());
    }

    @Test
    public void wallet_addFunds() {
        Wallet wallet = new Wallet();
        wallet.addFunds(1.0f);
        Assertions.assertEquals(1.0f, wallet.getFunds());

        wallet.addFunds(45.42f);
        Assertions.assertEquals(46.42f, wallet.getFunds());
    }

    @Test
    public void wallet_removeFunds() {
        Wallet wallet = new Wallet();
        wallet.addFunds(45.0f);
        Assertions.assertTrue(wallet.removeFunds(32.0f));
        Assertions.assertEquals(13.0f, wallet.getFunds());
        Assertions.assertFalse(wallet.removeFunds(45.0f));
        Assertions.assertEquals(13.0f, wallet.getFunds());
    }
}
