package server;

import java.net.Socket;

public class PlayerClientThread extends ClientThreadWithHooks {
    public PlayerClientThread(Socket socket, Server serverRef) {
        super(socket);
    }

    @Override
    public void run() {
        // player client logic
    }
}
