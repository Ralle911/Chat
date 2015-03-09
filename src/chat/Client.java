package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

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
     * Sends an object object to the server.
     *
     * @param object The object that should be sent to the server.
     */
    public void sendObject(Object object) {
        try {
            oos.writeObject(object);
            oos.flush();
        } catch (IOException e) {}
    }

    /**
     * Sets the client user by creating a new User object with given name.
     *
     * @param name The name of the user to be created.
     */
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
                    controller.newMessage(log.iterator().next().getContent());
                }
            }
        }
    }

    /**
     * Sends the users conversations to the controller to be displayed in the UI.
     */
    public void initConversations() {
        for (Conversation con : user.getConversations()) {
            controller.newConversation(con);
        }
    }

    /**
     * Asks for a username, creates a User object with given name and sends it to the server.
     * The server then either accepts or denies the User object.
     * If successful, sets the received User object as current user and announces login in chat.
     * If not, notifies in chat and requests a new name.
     */
    public void setUser() {
        Object object = null;
        while (!(object instanceof User)) {
            try {
                controller.setName();
                sendObject(user);
                object = ois.readObject();
                if (object instanceof User) {
                    user = (User)object;
                    controller.newMessage("You logged in as " + user.getId());
                    initConversations();
                } else {
                    controller.newMessage(object);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e2) {
                e2.printStackTrace();
            }

        }
    }

    /**
     * Listens to incoming Messages, user lists, Conversations or server messages.
     */
    public void startCommunication() {
        Object object;
        try {
            while (!Thread.interrupted()) {
                object = ois.readObject();
                if (object instanceof Message) {
                    controller.newMessage(object);
                } else if (object instanceof ArrayList) {
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e2) {
            e2.printStackTrace();
        }
    }

    /**
     * Class to handle communication between client and server.
     */
    private class Listener extends Thread {
		public void run() {
            setUser();
            startCommunication();
        }
    }
}
