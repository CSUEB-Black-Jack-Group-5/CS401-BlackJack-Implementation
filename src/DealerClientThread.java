import java.net.Socket;

public class DealerClientThread extends ClientThreadWithHooks {
    public DealerClientThread(Socket socket, Server serverRef) {
        super(socket);
    }

    @Override
    public void run() {
        // dealer client logic
    }
}
