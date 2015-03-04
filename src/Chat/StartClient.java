package Chat;

import javax.swing.*;

/**
 * Run class to start a client.
 *
 * @author Emil Sandgren, Kalle Bornemark, Erik Sandgren, Jimmy Maksymiw, Lorenz Puskas & Rasmus Andersson
 */
public class StartClient {
    public static void main(String[] args) {
        Client client = new Client("localhost", 3450, new User(JOptionPane.showInputDialog("What's your name, buddy?")));
//        Client client = new Client("10.2.10.38", 3450, this);
        new ClientController(client);
    }
}