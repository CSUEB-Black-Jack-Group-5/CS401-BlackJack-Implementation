package server;

import game.Dealer;
import game.Player;
import game.Table;
import networking.AccountType;
import networking.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import dbHelper.CSVDatabaseHelper;

public class Server {
    // Client threads
    private final ClientThreadWithHooks[] connectedClients;
    private int connectClientsSize;
    private final ClientThreadWithHooks[] clientsInLobby;
    private int clientsInLobbySize;

    private HashMap<Class<Message>, List<ClientThreadWithHooks>> events;

    private Thread connectionThread;

    private final Hashtable<Integer, TableThread> tables;
    private int tablesSize;

    private CSVDatabaseHelper db;

    ServerSocket serverSocket;
    int port;
    boolean running;

    /**
        @description: Register a new event listener for event T (T must extend Message)
     */
    public <T extends Message> void registerClientWithEvent(ClientThreadWithHooks client, T event) {
        events.get(event.getClass()).add(client);
    }
    /**
     @description: Looks at the registered clients for event T, and sends the message 'event' to it
     */
    public <T extends Message> void fireEvent(T event) {
        for (ClientThreadWithHooks client : events.get(event.getClass())) {
            client.sendNetworkMessage(event);
        }
    }

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
                            case AccountType.PLAYER -> new PlayerClientThread(socket, serverRef, writer, reader, username);
                            case AccountType.DEALER -> new DealerClientThread(socket, serverRef, writer, reader, username);
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

        tables = new Hashtable<>();
        tablesSize = 0;
        events = new HashMap<>();
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

    public Table[] getTables() {
        Table[] arr = new Table[tables.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = tables.get(i).getTable();
        }
        return arr;
    }
    public ClientThreadWithHooks[] getDealersInLobby() {
        return  Arrays.stream(clientsInLobby)
                .filter(clientThreadWithHooks -> clientThreadWithHooks instanceof DealerClientThread)
                .toArray(DealerClientThread[]::new);
    }
    public PlayerClientThread[] getPlayersInLobby() {
        return  Arrays.stream(clientsInLobby)
                .filter(clientThreadWithHooks -> clientThreadWithHooks instanceof PlayerClientThread)
                .toArray(PlayerClientThread[]::new);
    }
    public Table getTableById(int tableId) {
        return tables.get(tableId).getTable();
    }
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
    public boolean movePlayerClientToTable(PlayerClientThread clientThread, int tableId, Player player) {
        // if the table doesn't exist, the operation fails
        if (tableId < 0 || tableId >= tablesSize) {
            System.err.println("tableId = " + tableId + " doesn't exist");
            return false;
        // otherwise ->
        } else {
            TableThread tableThread = tables.get(tableId);
            // moves the client to the tableThread
            tableThread.addClientToTable(clientThread);
            Table table = tables.get(tableId).getTable();
            // adds the player to the table
            table.addPlayer(player);

            // updates the table data for everyone in the table
            for (int i = 0; i < table.getPlayerCount(); i++) {
                if (tableThread.getJoinedUsers()[i] == null) continue;
                tableThread.getJoinedUsers()[i].sendNetworkMessage(new Message.TableData.Response(table.getPlayers(), table.getDealer(), table.getPlayerCount()));
            }
        }
        return true;
    }
    public TableThread spawnTable(DealerClientThread clientThread, Dealer dealer) {
        // makes a new table thread
        TableThread tableThread = new TableThread(dealer);
        // adds the dealer client to an array of clients in the table thread
        tableThread.addClientToTable(clientThread);
        // stores the table thread in the hashtable in server
        tables.put(tableThread.getTable().getTableId(), tableThread);
        // starts the table thread
        new Thread(tableThread).start();
        tablesSize++;

        // updates the lobby for every client
        for (int i = 0; i < clientsInLobbySize; i++) {
            if (clientsInLobby[i] == null) continue;
            clientsInLobby[i].sendNetworkMessage(new Message.LobbyData.Response(getTables(), getPlayersInLobby().length, getDealersInLobby().length));
        }

        return tableThread;
    }
    public void broadcastNetworkMessageToTable(Message message) {
        for (ClientThreadWithHooks client : connectedClients) {
            client.sendNetworkMessage(message);
        }
    }
}
