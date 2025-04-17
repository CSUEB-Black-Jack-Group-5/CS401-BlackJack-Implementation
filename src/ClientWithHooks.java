import networking.Message;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.function.Consumer;

public class ClientWithHooks {
    // Client Data
    private String ip;
    private int port;

    private String username;
    private String activeRoom;
    private boolean switchingRooms;
    private boolean running;
    private boolean loggedIn;

    // Network data
    private Thread serverMessageListenerThread;
    private Socket socket;
    private ObjectOutputStream objectWriter = null;
    private ObjectInputStream objectReader = null;
    private volatile boolean responseProcessed;
    public boolean shouldTerminate;

    private final HashMap<Class<? extends Message>, Consumer<? super Message>> messageHooks;

    public ClientWithHooks(String ip, int port) {
        this.activeRoom = "";
        this.username = "";
        this.ip = ip;
        this.port = port;
        this.switchingRooms = true;
        this.running = true;
        this.responseProcessed = false;
        this.messageHooks = new HashMap<>();
        try {
            this.socket = new Socket(this.ip, this.port);
            this.objectReader = new ObjectInputStream(this.socket.getInputStream());
            this.objectWriter = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Failed to connect to " + this.ip + ":" + this.port);
            System.err.println("Is the server running?");
            System.exit(1);
        }
        this.serverMessageListenerThread = new Thread(() -> {
            while (running) {
                Message message;
                try {
                    if ((message = (Message) objectReader.readObject()) != null) {
                        this.responseProcessed = false;
                        System.out.println(message);
                        if (messageHooks.containsKey(message.getClass())) {
                            messageHooks.get(message.getClass()).accept(message);
                        }
                        else {
                            System.err.println("No hook for message: " + message.getClass());
                        }
                        if (shouldTerminate) {
                            socket.setSoTimeout(1);
                            running = false;
                            break;
                        }
                        this.responseProcessed = true;
                    }
                } catch (ClassNotFoundException e) {
                    System.out.println("Failed to read object : " + e.getLocalizedMessage());
                } catch (IllegalMonitorStateException e) {
                    System.out.println("Failed to unlock : " + e.getLocalizedMessage());
                } catch (InterruptedIOException e) {
                    running = false;
                    System.out.println("Interrupted in thread");
                    break;
                } catch (IOException e) {
                    running = false;
                    System.err.println("Server stopped. Client exiting...");
                    System.exit(0);
                }
            }
        });
        this.serverMessageListenerThread.start();
    }

    public <T extends Message> void addMessageHook(Class<T> messageClass, Consumer<? super T> consumer) {
        if (messageHooks.containsKey(messageClass)) {
            System.err.println("There is already a message hook for: " + messageClass);
            return;
        }
        messageHooks.put(messageClass, (Consumer<? super Message>) consumer);
    }

    public void showMessageHooks() {
        messageHooks.forEach((aClass, consumer) -> {
            System.out.println(aClass);
        });
    }
    public void sendNetworkMessage(Message message) {
        try {
            this.objectWriter.writeObject(message);
        } catch (IOException e) {
            System.err.println("Failed to write message");
        }
    }
}
