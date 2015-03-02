package Chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author Kalle Bornemark
 */
public class Server implements Runnable {
    private ServerSocket serverSocket;
    private Thread server = new Thread(this);
    private ArrayList<ConnectedClient> connectedClients;

    public Server(int port) {
        connectedClients = new ArrayList<ConnectedClient>();
        try {
            serverSocket = new ServerSocket(port);
            server.start();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    public void writeToAll(Message message) {
        for (ConnectedClient client : connectedClients) {
            client.write(message);
        }
    }

    public void run() {
        while(true) {
            try {
                Socket socket = serverSocket.accept();
                connectedClients.add(new ConnectedClient(socket));
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    private class ConnectedClient implements Runnable {
        private Socket socket;
        private Thread client = new Thread(this);
        private ObjectOutputStream oos;
        private ObjectInputStream ois;

        public ConnectedClient(Socket socket) {
            this.socket = socket;
            client.start();
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {}
        }

        public void write(Message msg) {
            try {
                oos.writeObject(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            Object object;
            Message message;
            try {
                while(true) {
                    object = ois.readObject();
                    message = (Message)object;
                    oos.writeTo
                }
            } catch (IOException e) {
                System.err.println(e);
            } catch (ClassNotFoundException e2) {
                System.err.println(e2);
            }
        }
    }


}