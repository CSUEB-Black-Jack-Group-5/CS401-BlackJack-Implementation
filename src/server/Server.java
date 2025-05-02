package server;

import networking.AccountType;
import networking.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import dbHelper.CSVDatabaseHelper;

public class Server {
    // Client threads
    private final ClientThreadWithHooks[] connectedClients;
    private int connectClientsSize;
    private final ClientThreadWithHooks[] clientsInLobby;
    private int clientsInLobbySize;

    private Thread connectionThread;

    private final TableThread[] tables;
    private int tablesSize;

    private CSVDatabaseHelper db;

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
        Server serverRef;
        AccountType accountType;
        public ClientLoginHandler(Socket socket, Server serverRef) {
            this.socket = socket;
            this.serverRef = serverRef;
            try {
                writer = new ObjectOutputStream(socket.getOutputStream());
                reader = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                System.err.println("Failed to get socket streams");
                throw new RuntimeException(e);
            }
        }

        @Override
        public void run() {
            // system.out
            System.out.println("Server listening on port 3333");
            try {
                Message message;
                while ((message = (Message) reader.readObject()) != null) {
                    System.out.println(message.getClass());
                    if (message instanceof Message.CreateAccount.Request createAccountRequest) {
                        String username = createAccountRequest.getUsername();
                        String password = createAccountRequest.getPassword();
                        System.out.println("Create Account: " + username + ", " + password);

                        // TODO: implement later
                        // if (db.checkUserExists(username)) {
                        //     System.err.println("User exists with username: " + username);
                        //     writer.writeObject(new Message.CreateAccount.Response(/* fail status */));
                        // }
                        // else {
                        //     db.registerNewUser(username, password);
                        //     db.save();
                        //     writer.writeObject(new Message.CreateAccount.Response(/* success status */));
                        // }
                    }
                    // Login
                    // ========================================================
                    if (message instanceof Message.Login.Request loginRequest) {
                        String username = loginRequest.getUsername();
                        String password = loginRequest.getPassword();

                        // Still have not done the checking still trying to figure out how to send the
                        // request via client side.
                        if(CSVDatabaseHelper.dealerExists(username,password)){
                            accountType = AccountType.DEALER;
                        }
                        else if(CSVDatabaseHelper.playerExists(username,password)){
                            accountType = AccountType.PLAYER;
                        }
                        if (accountType == null) {
                            writer.writeObject(new Message.Login.Response(false, null));
                            return;
                        }
                        // TODO: if (!checkCredentials(username, password))
                        //          writer.writeObject(Message.Login.Response(/* fail status */));
                        // TODO: AccountType accountType = serverRef.db.getUserTypeFor(username);
//                        AccountType accountType = AccountType.PLAYER;
                        ClientThreadWithHooks clientThread = switch (accountType) {
                            case AccountType.PLAYER -> new PlayerClientThread(socket, serverRef, writer, reader);
                            case AccountType.DEALER -> new DealerClientThread(socket, serverRef, writer, reader);
                        };
                        writer.writeObject(new Message.Login.Response(true, accountType));

                        connectedClients[connectClientsSize++] = clientThread;
                        clientsInLobby[clientsInLobbySize++] = clientThread;
                        new Thread(clientThread).start();
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
                new Thread(new ClientLoginHandler(clientSocket, this)).start();
            }
        } catch (IOException e) {
            System.err.println("Failed to read socket");
        }
    }

    public Server(int port) {
        this.port = port;
        this.running = true;
        connectedClients = new ClientThreadWithHooks[10]; // default to 10 elements
        connectClientsSize = 0;
        clientsInLobby = new ClientThreadWithHooks[10];
        clientsInLobbySize = 0;

        tables = new TableThread[10];
        tablesSize = 0;
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

    public TableThread[] getTables() {
        return tables;
    }
    public ClientThreadWithHooks[] getDealersInLobby() {
        return (ClientThreadWithHooks[]) Arrays.stream(clientsInLobby)
                .filter(clientThreadWithHooks -> clientThreadWithHooks instanceof DealerClientThread)
                .toArray();
    }
    public ClientThreadWithHooks[] getPlayersInLobby() {
        return (ClientThreadWithHooks[]) Arrays.stream(clientsInLobby)
                .filter(clientThreadWithHooks -> clientThreadWithHooks instanceof PlayerClientThread)
                .toArray();
    }
    // public Table getTableById(int tableId) {
    //     List<Table> filteredTables = Arrays.stream(tables).filter(tableThread -> tableThread.table.getTableId() == tableId).toList();
    //     if (filteredTables.size() == 0) return null;
    //     return filteredTables.get(0);
    // }
    // public boolean hitPlayer(PlayerClientThread clientThread, int tableId) {
    //     if (tableId < 0 || tableId >= tablesSize) {
    //         System.err.println("tableId = " + tableId + " doesn't exist");
    //         return false;
    //     }
    //     tables[tableId].hitPlayer(clientThread);

    //     return true;
    // }
    public boolean movePlayerClientToTable(PlayerClientThread clientThread, int tableId) {
        if (tableId < 0 || tableId >= tablesSize) {
            System.err.println("tableId = " + tableId + " doesn't exist");
            return false;
        }
        tables[tableId].addClientToTable(clientThread);
        return true;
    }
    public void spawnTable() {
        TableThread tableThread = new TableThread();
        tables[tablesSize++] = tableThread;
        new Thread(tableThread).start();
    }
    public void broadcastNetworkMessageToTable(Message message) {
        for (ClientThreadWithHooks client : connectedClients) {
            client.sendNetworkMessage(message);
        }
    }
}
