package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ShoeTests.class,
        CardHandTest.class,
        CardTests.class,
        DealerTest.class,
        PlayerTests.class,
        ShoeTests.class,
        SuitTests.class,
        TableTests.class,
        TimerTests.class,
        ValueTests.class,
        WalletTests.class,
        LobbyTests.class,

}

)
public class AllTests {
}
