package Chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author Kalle Bornemark
 */
public class Client {
    private Socket socket;
    private ClientController controller;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public Client(String ip, int port, ClientController controller) {
        this.controller = controller;
        try {
            socket = new Socket(ip, port);
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            new Listener().start();
        } catch (IOException e) {
            System.err.println(e);
        }
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
            Message message;
            while(true) {
                try {
                    object = ois.readObject();
                    message = (Message)object;
                    controller.newMessage(message);
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        }
    }
}
