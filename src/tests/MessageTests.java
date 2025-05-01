import game.Shoe;
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
        @Test public void request_constructor() {
            Message.CreateAccount.Request request = new Message.CreateAccount.Request("rf", "passwd");
            Assertions.assertEquals("rf", request.getUsername());
            Assertions.assertEquals("passwd", request.getPassword());
        }
        @Test public void response_constructor() {
            Message.CreateAccount.Response response = new Message.CreateAccount.Response(true);
            Assertions.assertTrue(response.getStatus());
            response = new Message.CreateAccount.Response(false);
            Assertions.assertFalse(response.getStatus());
        }
    }

    @Nested
    @DisplayName("JoinTable")
    public class JoinTable {
        @Test public void request_constructor() {
            Message.JoinTable.Request request = new Message.JoinTable.Request(0, 3413);
            Assertions.assertEquals(0, request.getUserId());
            Assertions.assertEquals(3413, request.getTableId());
        }
        @Test public void response_constructor() {
            Message.JoinTable.Response response = new Message.JoinTable.Response(true);
            Assertions.assertTrue(response.getStatus());

            response = new Message.JoinTable.Response(false);
            Assertions.assertFalse(response.getStatus());
        }
    }

    @Nested
    @DisplayName("CreateTable")
    public class CreateTable {
        @Test public void request_constructor() {
            Message.CreateTable.Request request = new Message.CreateTable.Request("Dealer");
            Assertions.assertEquals("Dealer", request.getDealerId());
        }
        @Test public void response_constructor() {
            Message.CreateTable.Response response = new Message.CreateTable.Response(true, 0);
            Assertions.assertTrue(response.getStatus());
            Assertions.assertEquals(0, response.getTableId());
        }
    }

    @Nested
    @DisplayName("Hit")
    public class Hit {
        @Test public void request_constructor() {
            Shoe mockShoe = new Shoe(2);
            Message.Hit.Request request = new Message.Hit.Request(0, mockShoe);
            Assertions.assertEquals(0, request.getPlayerId());
            // NOTE: I'm not 100% confident with this test. Will adjust to be more accurate when merged with staging
            Assertions.assertEquals(mockShoe, request.getDeck());
        }
        @Test public void response_constructor() {
            Card drawCard = new Card(Suit.CLUBS, Value.FIVE);      // what card was drawn from the shoe
            CardHand cardHand = new CardHand(21);        // what the player's current hand is
            cardHand.addCard(new Card(Suit.DIAMONDS, Value.FOUR)); // --
            cardHand.addCard(new Card(Suit.CLUBS, Value.SIX));     // --
            Message.Hit.Response response = new Message.Hit.Response(drawCard, cardHand, cardHand.bustCheck());

            Assertions.assertEquals(cardHand.bustCheck(), response.getStatus());
            Assertions.assertEquals(drawCard, response.getDraw());
            Assertions.assertFalse(response.getStatus());
        }
    }

    @Nested
    @DisplayName("Stand")
    public class Stand {
        @Test public void request_constructor() {
            Message.Stand.Request request = new Message.Stand.Request(3);
            Assertions.assertEquals(3, request.getPlayerId());

            Message.Stand.Request request1 = new Message.Stand.Request(94);
            Assertions.assertEquals(94, request1.getPlayerId());
        }
        @Test public void response_constructor() {
            CardHand cardHand = new CardHand(21);
            cardHand.addCard(new Card(Suit.CLUBS, Value.FIVE));
            cardHand.addCard(new Card(Suit.DIAMONDS, Value.FIVE));
            Message.Stand.Response response = new Message.Stand.Response(cardHand, cardHand.bustCheck());

            Assertions.assertEquals(cardHand, response.getHand());
            Assertions.assertFalse(response.getStatus());
        }
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