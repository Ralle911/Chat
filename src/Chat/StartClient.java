package Chat;

import javax.swing.*;

/**
 * @author Kalle Bornemark
 */
public class StartClient {
    public static void main(String[] args) {
        Client client = new Client("localhost", 3450);
//        Client client = new Client("10.2.10.38", 3450, this);
        new ClientController(client, JOptionPane.showInputDialog("What's your name, buddy?"));
    }
}