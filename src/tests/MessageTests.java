import networking.AccountType;
import networking.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class MessageTests {
    @Nested
    @DisplayName("Login")
    public class Login {
        @Test
        public void request_constructor() {
            Message.Login.Request request = new Message.Login.Request("rf", "passwd");
            Assertions.assertEquals("rf", request.getUsername());
            Assertions.assertEquals("passwd", request.getPassword());

            request = new Message.Login.Request("chris", "idkwhathispasswordis");
            Assertions.assertEquals("chris", request.getUsername());
            Assertions.assertEquals("idkwhathispasswordis", request.getPassword());
        }

        @Test
        public void response_constructor() {
            Message.Login.Response response = new Message.Login.Response(true, AccountType.DEALER);
            Assertions.assertTrue(response.getStatus());
            Assertions.assertEquals(AccountType.DEALER, response.getType());

            response = new Message.Login.Response(false, AccountType.PLAYER);
            Assertions.assertFalse(response.getStatus());
            Assertions.assertEquals(AccountType.PLAYER, response.getType());
        }
    }

    @Nested
    @DisplayName("CreateAccount")
    public class CreateAccount {
        @Test public void request_constructor() {}
        @Test public void response_constructor() {}
    }

    @Nested
    @DisplayName("JoinTable")
    public class JoinTable {
        @Test public void request_constructor() {}
        @Test public void response_constructor() {}
    }

    @Nested
    @DisplayName("CreateTable")
    public class CreateTable {
        @Test public void request_constructor() {}
        @Test public void response_constructor() {}
    }

    @Nested
    @DisplayName("Hit")
    public class Hit {
        @Test public void request_constructor() {}
        @Test public void response_constructor() {}
    }

    @Nested
    @DisplayName("Stand")
    public class Stand {
        @Test public void request_constructor() {}
        @Test public void response_constructor() {}
    }

    @Nested
    @DisplayName("Split")
    public class Split {
        @Test public void request_constructor() {}
        @Test public void response_constructor() {}
    }

    @Nested
    @DisplayName("DoubleDown")
    public class DoubleDown {
        @Test public void request_constructor() {}
        @Test public void response_constructor() {}
    }

    @Nested
    @DisplayName("LobbyData")
    public class LobbyData {
        @Test public void request_constructor() {}
        @Test public void response_constructor() {}
    }

    @Nested
    @DisplayName("TableData")
    public class TableData {
        @Test public void request_constructor() {}
        @Test public void response_constructor() {}
    }

    @Nested
    @DisplayName("GameData")
    public class GameData {
        @Test public void request_constructor() {}
        @Test public void response_constructor() {}
    }

    @Nested
    @DisplayName("ClockSync")
    public class ClockSync {
        @Test public void request_constructor() {}
        @Test public void response_constructor() {}
    }

    @Nested
    @DisplayName("PlayerReady")
    public class PlayerReady {
        @Test public void request_constructor() {}
        @Test public void response_constructor() {}
    }

    @Nested
    @DisplayName("PlayerLeave")
    public class PlayerLeave {
        @Test public void request_constructor() {}
        @Test public void response_constructor() {}
    }

    @Nested
    @DisplayName("DealerLeave")
    public class DealerLeave {
        @Test public void request_constructor() {}
        @Test public void response_constructor() {}
    }
}
