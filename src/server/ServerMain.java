package server;

public class ServerMain {
    public static void main(String[] args) {
        Server server = new Server(3333);
        Runtime.getRuntime().addShutdownHook(new Thread(server::stopServer));
        server.startServer();
    }
}
