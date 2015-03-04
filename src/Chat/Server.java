package Chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Model class for the server.
 *
 * @author Emil Sandgren, Kalle Bornemark, Erik Sandgren, Jimmy Maksymiw, Lorenz Puskas & Rasmus Andersson
 */
public class Server implements Runnable {
    private ServerSocket serverSocket;
    private ArrayList<ConnectedClient> connectedClients;

    public Server(int port) {
        System.out.println("Server started");
        connectedClients = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(port);
            new Thread(this).start();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    public void writeToAll(Object object) {
        for (ConnectedClient client : connectedClients) {
            client.write(object);
        }
    }

    public void run() {
        while(true) {
            try {
                Socket socket = serverSocket.accept();
                ConnectedClient client = new ConnectedClient(socket, this);
                connectedClients.add(client);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    private class ConnectedClient implements Runnable {
        private Thread client = new Thread(this);
        private ObjectOutputStream oos;
        private ObjectInputStream ois;
        private Server server;

        public ConnectedClient(Socket socket, Server server) {
            this.server = server;
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {}
            client.start();
            server.writeToAll("Client connected: " + socket.getInetAddress());
        }

        public void write(Object object) {
            try {
                oos.writeObject(object);
            } catch (IOException e) {}
        }

        public void run() {
            Object object;
            Message message;
            try {
                while(true) {
                    object = ois.readObject();
                    message = (Message)object;
                    server.writeToAll(message);
                }
            } catch (IOException e) {
                System.err.println(e);
            } catch (ClassNotFoundException e2) {
                System.err.println(e2);
            }
        }
    }

}