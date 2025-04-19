package networking;

import java.io.Serializable;

public class Message implements Serializable {
    public static class Login {
        public static class Request extends Message {
            private String username;
            private String password;

            public Request(String username, String password) {
                this.username = username;
                this.password = password;
            }

            public String getUsername() {
                return username;
            }

            public String getPassword() {
                return password;
            }
        }
        public static class Response extends Message {
            private boolean status;
            private AccountType type;       // this doesn't exist yet

            public Response (boolean status, AccountType type) {
                this.status = status;
                this.type = type;
            }

            public boolean getStatus() {
                return status;
            }

            public AccountType getType() {
                return type;
            }
        }
    }
    public static class Hit {
        public static class Request extends Message {}
        public static class Response extends Message {}
    }
    public static class Stand {
        public static class Request extends Message {}
        public static class Response extends Message {}
    }
    public static class Leave {
        public static class Request extends Message {}
        public static class Response extends Message {}
    }
    public static class LobbyData {
        public static class Request extends Message {}
        public static class Response extends Message {}
    }
    public static class TableData {
        public static class Request extends Message {}
        public static class Response extends Message {}
    }
    public static class ClockSync {
        public static class Request extends Message {}
        public static class Response extends Message {}
    }
    public static class GameData {
        public static class Request extends Message {}
        public static class Response extends Message {}
    }
    public static class PlayerReady {
        public static class Request extends Message {}
        public static class Response extends Message {}
    }
    public static class PlayerLeave {
        public static class Request extends Message {}
        public static class Response extends Message {}
    }
    public static class DealerLeave {
        public static class Request extends Message {}
        public static class Response extends Message {}
    }
}
