package Chat;

import sun.util.resources.cldr.chr.CalendarData_chr_US;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Model class for the client.
 *
 * @author Emil Sandgren, Kalle Bornemark, Erik Sandgren, Jimmy Maksymiw, Lorenz Puskas & Rasmus Andersson
 */

public class Client {
    private Socket socket;
    private ClientController controller;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private ArrayList<User> userList;
    private User user;

    public Client(String ip, int port, User user) {
        this.user = user;
        try {
            socket = new Socket(ip, port);
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            new Listener().start();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void setClientController(ClientController clientController) {
        this.controller = clientController;
    }

    public void sendMessage(Message message) {
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {}
    }

    private class Listener extends Thread {
        public void run() {
            Object object;
            try {
                // Skicka User
                oos.writeObject(user);
                // Få tillbaka User
                object = ois.readObject();
                if (object instanceof User) {
                    user = (User)object;
                }
                while (true) {
                    object = ois.readObject();
                    controller.newMessage(object);
                }
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }
}
