package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.*;

/**
 * Model class for the server.
 *
 * @author Emil Sandgren, Kalle Bornemark, Erik Sandgren,
 * Jimmy Maksymiw, Lorenz Puskas & Rasmus Andersson
 */
public class Server implements Runnable {
    private ServerSocket serverSocket;
    private ArrayList<ConnectedClient> connectedClients;
    private ArrayList<User> registeredUsers;
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    public Server(int port) {
        initLogger();
        registeredUsers = new ArrayList<>();
        connectedClients = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(port);
            new Thread(this).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initiates the Logger
     */
    private void initLogger() {
        Handler fh;
        try {
            fh = new FileHandler("./src/log/Server.log");
            LOGGER.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            LOGGER.setLevel(Level.FINE);
        } catch (IOException e) {}
    }

    /**
     * Returns the User which ID matches the given ID.
     * Returns null if it doesn't exist.
     *
     * @param id The ID of the User that is to be found.
     * @return The matching User object, or null.
     */
    public User getUser(String id) {
        for (User user : registeredUsers) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Sends an object to all currently connected clients.
     *
     * @param object The object to be sent.
     */
    public synchronized void sendObjectToAll(Object object) {
        for (ConnectedClient client : connectedClients) {
            client.sendObject(object);
        }
    }

    /**
     * Checks who the message shall be sent to, then sends it.
     *
     * @param message The message to be sent.
     */
    public synchronized void sendMessage(Message message) {
        Conversation conversation = null;
        String to = "";

        // Lobby message
        if (message.getConversationID() == -1) {
            sendObjectToAll(message);
            to += "lobby";
        } else {
            User senderUser = null;

            // Finds the sender user
            for (ConnectedClient cClient : connectedClients) {
                if (cClient.getUser().getId().equals(message.getFromUserID())) {
                    senderUser = cClient.getUser();

                    // Finds the conversation the message shall be sent to
                    for (Conversation con : senderUser.getConversations()) {
                        if (con.getId() == message.getConversationID()) {
                            conversation = con;
                            to += conversation.getInvolvedUsers().toString();

                            // Finds the message's recipient users, then sends the message
                            for (String s : con.getInvolvedUsers()) {
                                for (ConnectedClient conClient : connectedClients) {
                                    if (conClient.getUser().getId().equals(s)) {
                                        conClient.sendObject(message);
                                    }
                                }
                            }
                            conversation.addMessage(message);
                        }
                    }
                }
            }
        }
        LOGGER.info("-- NEW MESSAGE SENT --\n" +
                "From: " + message.getFromUserID() + "\n" +
                "To: " + to + "\n" +
                "Message: " + message.getContent().toString());
    }

    /**
     * Sends a Conversation object to its involved users
     *
     * @param conversation The Conversation object to be sent.
     */
    public synchronized void sendConversation(Conversation conversation) {
        HashSet<String> users = conversation.getInvolvedUsers();
        for (String s : users) {
            for (ConnectedClient c : connectedClients) {
                if (c.getUser().getId().equals(s)) {
                    c.sendObject(conversation);
                }
            }
        }
    }

    /**
     * Sends an ArrayList with all connected user's IDs.
     */
    public synchronized void sendConnectedClients() {
        ArrayList<String> connectedUsers = new ArrayList<>();
        for (ConnectedClient client : connectedClients) {
            connectedUsers.add(client.getUser().getId());
        }
        sendObjectToAll(connectedUsers);
    }

    /**
     * Waits for client to connect.
     * Creates a new instance of ConnectedClient upon client connection.
     * Adds client to list of connected clients.
     */
    public void run() {
        LOGGER.info("Server started.");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                ConnectedClient client = new ConnectedClient(socket, this);
                connectedClients.add(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Class to handle the communication between server and connected clients.
     */
    private class ConnectedClient implements Runnable {
        private Thread client = new Thread(this);
        private ObjectOutputStream oos;
        private ObjectInputStream ois;
        private Server server;
        private User user;
        private Socket socket;

        public ConnectedClient(Socket socket, Server server) {
            LOGGER.info("Client connected: " + socket.getInetAddress());
            this.socket = socket;
            this.server = server;
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            client.start();
        }

        /**
         * Returns the connected clients current User.
         *
         * @return The connected clients current User
         */
        public User getUser() {
            return user;
        }

        /**
         * Sends an object to the client.
         *
         * @param object The object to be sent.
         */
        public synchronized void sendObject(Object object) {
            try {
                oos.writeObject(object);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Removes the user from the list of connected clients.
         */
        public void removeConnectedClient() {
            for (int i = 0; i < connectedClients.size(); i++) {
                if (connectedClients.get(i).getUser().getId().equals(this.getUser().getId())) {
                    connectedClients.remove(i);
                    System.out.println("Client removed from connectedClients");
                }
            }
        }

        /**
         * Removes the connected client,
         * sends an updated list of connected clients to other connected clients,
         * sends a server message with information of who disconnected
         * and closes the client's socket.
         */
        public void disconnectClient() {
            removeConnectedClient();
            sendConnectedClients();
            sendObjectToAll("Client disconnected: " + user.getId());
            LOGGER.info("Client disconnected: " + user.getId());
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * Checks if given user exists among already registered users.
         *
         * @return Whether given user already exists or not.
         */
        public boolean isUserInDatabase(User user) {
            for (User u : registeredUsers) {
                if (u.getId().equals(user.getId())) {
                    return true;
                }
            }
            return false;
        }

        public User getUser(String ID) {
            for (User user : registeredUsers) {
                if (user.getId().equals(ID)) {
                    return user;
                }
            }
            return null;
        }

        /**
         * Compare given user ID with connected client's IDs and check if the user is online.
         *
         * @param id User ID to check online status.
         * @return Whether given user is online or not.
         */
        public boolean isUserOnline(String id) {
            for (ConnectedClient client : connectedClients) {

                if (client.getUser().getId().equals(id) && client != this) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Checks if given set of User IDs already has an open conversation.
         * If it does, it sends the conversation to its participants.
         * If it doesn't, it creates a new conversation, adds it to the current users
         * conversation list, and sends the conversation to its participants.
         *
         * @param participants A HashSet of user-IDs.
         */
        public void updateConversation(HashSet<String> participants) {
            boolean exists = false;
            Conversation conversation = null;
            for (Conversation con : user.getConversations()) {
                if (con.getInvolvedUsers().equals(participants)) {
                    conversation = con;
                    exists = true;
                }

            }
            if (!exists) {
                conversation = new Conversation(participants);
                addConversation(conversation);
            }
            sendConversation(conversation);
        }

        /**
         * Adds given conversation to all its participants' User objects.
         *
         * @param con The conversation to be added.
         */
        public void addConversation(Conversation con) {
            for (User user : registeredUsers) {
                for (String ID : con.getInvolvedUsers()) {
                    if (ID.equals(user.getId())) {
                        user.addConversation(con);
                    }
                }
            }
        }

        /**
         * Check if given message is part of an already existing conversation.
         *
         * @param message The message to be checked.
         * @return Whether given message is part of a conversation or not.
         */
        public Conversation isPartOfConversation(Message message) {
            for (Conversation con : user.getConversations()) {
                if (con.getId() == message.getConversationID()) {
                    return con;
                }
            }
            return null;
        }

        /**
         * Forces connecting users to pick a user that's not already logged in,
         * and updates user database if needed.
         * Announces connected to other connected users.
         */
        public void validateIncomingUser() {
            Object object;
            try {
                object = ois.readObject();
                user = (User) object;
                LOGGER.info("Checking online status for user: " + user.getId());
                while (isUserOnline(user.getId())) {
                    LOGGER.info("User " + user.getId() + " already connected. Asking for new name.");
                    sendObject("Client named " + user.getId()+ " already connected, try again!");
                    // Wait for new user
                    object = ois.readObject();
                    user = (User) object;
                    LOGGER.info("Checking online status for user: " + user.getId());
                }
                if (!isUserInDatabase(user)) {
                    registeredUsers.add(user);
                } else {
                    user = getUser(user.getId());
                }
                oos.writeObject(user);
                server.sendObjectToAll("Client connected: " + user.getId());
                LOGGER.info("Client connected: " + user.getId());
                sendConnectedClients();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * Listens to incoming Messages, Conversations, HashSets of User IDs or server messages.
         */
        public void startCommunication() {
            Object object;
            Message message;
            try {
                while (!Thread.interrupted()) {
                    object = ois.readObject();
                    if (object instanceof Message) {
                        message = (Message) object;
                        server.sendMessage(message);
                    } else if (object instanceof Conversation) {
                        Conversation con = (Conversation) object;
                        oos.writeObject(con);
                    } else if (object instanceof HashSet) {
                        @SuppressWarnings("unchecked")
                        HashSet<String> participants = (HashSet<String>) object;
                        updateConversation(participants);
                    } else {
                        server.sendObjectToAll(object);
                    }
                }
            } catch (IOException e) {
                disconnectClient();
                e.printStackTrace();
            } catch (ClassNotFoundException e2) {
                e2.printStackTrace();
            }
        }

        public void run() {
            validateIncomingUser();
            startCommunication();
        }
    }
}