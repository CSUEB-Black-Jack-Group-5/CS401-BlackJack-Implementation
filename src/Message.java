import java.io.Serializable;

public class Message implements Serializable {
    public static class Login {
        public static class Request extends Message {}
        public static class Response extends Message {}
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
