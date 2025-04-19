package server;

import networking.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    // Client threads
    private final ClientThreadWithHooks[] connectedClients;
    private int connectClientsSize;
    // private final ClientThread[] clientsInLobby;
    // private int clientsInLobbySize;

    private Thread connectionThread;

    // private TableThread[] tables;
    // private int tablesSize;

    // private Database db;

    ServerSocket serverSocket;
    int port;
    boolean running;

    /**
     * @apiNote For internal use only for the main connection thread
     * <p>
     * This thread deals with getting clients fully logged in.
     *
     * @implNote The connection expects that the client sends the server what type of client
     *           it is via the Login.Request object
     */
    private class ClientLoginHandler implements Runnable {
        Socket socket;
        ObjectInputStream reader;
        ObjectOutputStream writer;
        public ClientLoginHandler(Socket socket) {
            this.socket = socket;
            try {
                reader = new ObjectInputStream(socket.getInputStream());
                writer = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                System.err.println("Failed to get socket streams");
                throw new RuntimeException(e);
            }
        }

        @Override
        public void run() {
            try {
                Message message;
                while ((message = (Message) reader.readObject()) != null) {
                    if (message instanceof Message.CreateAccount.Request createAccountRequest) {
                        String username = createAccountRequest.getUsername();
                        String password = createAccountRequest.getPassword();

                        if (db.checkUserExists(username)) {
                            System.err.println("User exists with username: " + username);
                            writer.writeObject(new Message.CreateAccount.Response(/* fail status */));
                        }
                        else {
                            db.registerNewUser(username, password);
                            writer.writeObject(new Message.CreateAccount.Response(/* success status */));
                        }
                    }
                    // Login
                    // ========================================================
                    if (message instanceof Message.Login.Request loginRequest) {
                        String username = loginRequest.getUsername();
                        String password = loginRequest.getPassword();

                        if (!checkCredentials(username, password)) writer.writeObject(Message.Login.Response(/* fail status */));
                        ClientThreadWithHooks clientThread = switch (loginRequest.getUserType()) {
                            case UserType.PLAYER -> new PlayerClientThread(clientSocket, this);
                            case UserType.DEALER -> new DealerClientThread(clientSocket, this);
                        };
                        writer.writeObject(new Message.Login.Response(/* status */));

                        // Set up the client thread message hooks
                        clientThread.addMessageHook(Message.Hit.Request.class, (response) -> {
                        });
                        clientThread.addMessageHook(Message.Stand.Request.class, (response) -> {
                        });
                        clientThread.addMessageHook(Message.Leave.Request.class, (response) -> {
                        });
                        connectedClients[connectClientsSize] = clientThread;
                        new Thread(clientThread);
                        break; // Stop thread here
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error: " + e.getLocalizedMessage());
            }
        }
    }

    /**
     * @apiNote For internal use only for the main connection thread
     * <p>
     * This function is one of the multithreaded parts of the server
     * It is responsible for accepting new connections
     * It distinguishes between player and dealer connections as well
     *
     * @implNote The connection expects that the client sends the server what type of client
     *           it is via the Login.Request object
     */
    private void connectionHandler() {
        try {
            serverSocket = new ServerSocket(this.port);
            serverSocket.setReuseAddress(true);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ObjectInputStream reader = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream writer = new ObjectOutputStream(clientSocket.getOutputStream());
                synchronized (connectedClients) {
                    Message message;

                    // This won't quite work for a fully multithreaded server as it will block the main thread waiting for a single
                    //      client to complete the login process. We need to spawn a new temporary thread for handling the client login process
                    while ((message = (Message) reader.readObject()) != null) {
                        // Handle the account create request from the newly connected client
                        //      Client has just opened in this scenario and prompts for login/create account
                        //      Client has not yet moved onto the lobby window
                        //      There are two possible messages we can get here CreateAccount, and Login
                        //          - Dealers will only trigger Login
                        //          - Clients might trigger CreateAccount and will always trigger Login

                        // Create Account
                        // ========================================================
                        if (message instanceof Message.CreateAccount.Request createAccountRequest) {
                            String username = createAccountRequest.getUsername();
                            String password = createAccountRequest.getPassword();

                            if (db.checkUserExists(username)) {
                                System.err.println("User exists with username: " + username);
                                writer.writeObject(new Message.CreateAccount.Response(/* fail status */));
                            }
                            else {
                                db.registerNewUser(username, password);
                                writer.writeObject(new Message.CreateAccount.Response(/* success status */));
                            }
                        }
                        // Login
                        // ========================================================
                        if (message instanceof Message.Login.Request loginRequest) {
                            String username = loginRequest.getUsername();
                            String password = loginRequest.getPassword();
                            // server.Server determines whether player or dealer

                            if (!checkCredentials(username, password)) writer.writeObject(Message.Login.Response(/* fail status */));

                            UserType t = getUserType(username);

                            writer.writeObject(new Message.Login.Response(/* status */));
                            ClientThread clientThread = new ClientThread(t, clientSocket, this);

                            // Set up the client thread message hooks
                            clientThread.addMessageHook(Message.Hit.Request.class, (response) -> {
                            });
                            clientThread.addMessageHook(Message.Stand.Request.class, (response) -> {
                            });
                            clientThread.addMessageHook(Message.Leave.Request.class, (response) -> {
                            });
                            connectedClients[connectClientsSize] = clientThread;
                            new Thread(clientThread);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read socket");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to read socket object class type");
        }
    }

    public Server(int port) {
        this.port = port;
        this.running = true;
        connectedClients = new ClientThreadWithHooks[10]; // default to 10 elements
        connectClientsSize = 0;
    }
    public void startServer() {
        connectionThread = new Thread(this::connectionHandler);
        connectionThread.start();
    }
    public void stopServer() {
        if (!running) return;
        try {
            // 1. Send disconnect message to all clients

            // 2. Join the connection thread
            connectionThread.join();
        } catch (InterruptedException e) {
            System.err.println("Failed to join thread: " + e.getLocalizedMessage());
        }
    }
    public void disconnectServer() {}
}
