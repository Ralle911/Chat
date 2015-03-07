package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Model class for the client.
 *
 * @author Emil Sandgren, Kalle Bornemark, Erik Sandgren,
 * Jimmy Maksymiw, Lorenz Puskas & Rasmus Andersson
 */

public class Client {
    private Socket socket;
    private ClientController controller;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private ArrayList<String> userList;
    private User user;

    /**
     * Constructor to create a new client.
     *
     * @param ip IP-address
     * @param port Port
     */
    public Client(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            controller = new ClientController(this);
            new Listener().start();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    /**
     * Sends a Message object to the server.
     *
     * @param message The Message object that should be sent to the server.
     */
    public void sendMessage(Message message) {
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {}
    }

    public void sendObject(Object object) {
        try {
            oos.writeObject(object);
            oos.flush();
        } catch (IOException e) {}
    }

    public void setUser(String name) {
        user = new User(name);
    }

    /**
     * Returns the clients User object.
     *
     * @return The clients User object.
     */
    public User getUser() {
        return user;
    }

    /**
     * Closes the clients socket.
     */
    public void disconnectClient() {
        try {
            socket.close();
        } catch (Exception e) {}
    }

    public void printLog() {
        ArrayList<Conversation> conversations = user.getConversations();
        for (Conversation con : conversations) {
            if (con.getId() == 1) {
                ChatLog log = con.getConversationLog();
                while (log.iterator().hasNext()) {
                    controller.appendText((String)log.iterator().next().getContent());
                }
            }
        }
    }

    public void initConversations() {
        for (Conversation con : user.getConversations()) {
            controller.newConversation(con);
        }
    }

    /**
     * Class to handle communication between client and server.
     */
    private class Listener extends Thread {
		public void run() {
            Object object = "";
            try {

                while (!(object instanceof User)) {
                    controller.setName();
                    oos.writeObject(user);                  /* Send User object to server */
                    object = ois.readObject();              /* Recieve the correct User object from server */
                    if (object instanceof User) {
                        user = (User)object;
                        controller.appendText("[Server: You logged in as " + user.getId() + "]");
                        initConversations();
                    } else {
                        controller.appendText((String) object);
                    }
                }

                while (!Thread.interrupted()) {         /* Client listens to new ArrayList<User> and Messages */
                    object = ois.readObject();
                    if (object instanceof Message) {
                        controller.newMessage(object);
                    } else if (object instanceof ArrayList) {  /* ArrayList with Users */
                        userList = (ArrayList<String>)object;
                        controller.setConnectedUsers(userList);
                    } else if (object instanceof Conversation) {
                        Conversation con = (Conversation)object;
                        user.addConversation(con);
                        controller.newConversation(con);
                    } else {
                        controller.newMessage(object);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
