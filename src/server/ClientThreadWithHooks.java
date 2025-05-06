package server;

import networking.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.function.Consumer;

public class ClientThreadWithHooks implements Runnable {
    private HashMap<Class<? extends Message>, Consumer<? super Message>> messageHooks;
    private volatile boolean running;
    private static int clientThreadIdCount = 0;

    private int clientThreadId;
    String currentlyJoinedRoom;
    String username;
    TableThread activeTable;

    Socket socket;
    ObjectOutputStream writer = null;
    ObjectInputStream reader = null;

    public ClientThreadWithHooks(Socket socket, ObjectOutputStream writer, ObjectInputStream reader) {
        this.socket = socket;
        this.running = true;
        this.messageHooks = new HashMap<>();
        this.activeTable = null;
        this.writer = writer;
        this.reader = reader;
        this.username = "unpopulated";
        this.clientThreadId = ClientThreadWithHooks.clientThreadIdCount++;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Message message;
                while ((message = (Message) reader.readObject()) != null) {
                    if (messageHooks.containsKey(message.getClass())) {
                        messageHooks.get(message.getClass()).accept(message);
                    }
                    else {
                        System.err.println("No hook for message: " + message.getClass());
                        Thread.dumpStack();
                    }
                }
            } catch (IOException e) {
                System.err.println("Failed to read socket");
                running = false;
            } catch (ClassNotFoundException e) {
                System.err.println("Failed to read object from socket");
                running = false;
            }
        }

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
            this.writer.writeObject(message);
        } catch (IOException e) {
            System.err.println("Failed to write message");
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }
    }

    public int getClientThreadId() {
        return clientThreadId;
    }

    public void shutdown() {
        this.running = false;
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Failed to close socket");
        }
    }

    public void setActiveTable(TableThread activeTable) {
        this.activeTable = activeTable;
    }
}
