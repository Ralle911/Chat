package Chat;

import javax.swing.*;

/**
 * @author Kalle Bornemark
 */
public class StartClient {
    public static void main(String[] args) {
        new ClientController(JOptionPane.showInputDialog("What's your name, buddy?"));
    }
}
