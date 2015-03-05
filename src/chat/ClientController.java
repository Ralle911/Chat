package chat;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Controller class to handle system logic.
 *
 * @author Emil Sandgren, Kalle Bornemark, Erik Sandgren, Jimmy Maksymiw, Lorenz Puskas & Rasmus Andersson
 */

public class ClientController {
	private ClientUI ui = new ClientUI(this);
    private Client client;

    /**
     * Creates a new Controller (with given Client).
     * Also creates a new UI, and displays it in a JFrame.
     *
     * @param client
     */
	public ClientController(Client client) {
        this.client = client;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.add(ui);
				frame.pack();
				frame.setVisible(true);
                ui.focusTextField();
			}
		});
	}

    public void setName() {
        client.setUser(JOptionPane.showInputDialog("What's your name, buddy?"));
    }

    /**
     * Receives an object that's either a Message object or a String
     * and sends it to the UI.
     *
     * @param object A Message object or a String
     */
    public void newMessage(Object object) {
        if (object instanceof Message) {
            Message message = (Message)object;
            ui.appendText(message.getFrom().getId() + ": " + (String)message.getContent());
        } else {
            ui.appendText(object.toString());
        }
    }

    /**
     * Recieves a Conversation object including information of message destination,
     * a User object including information of sender,
     * and a String including the comment to be sent.
     *
     * @param to Conversation object including destination.
     * @param from The User that sent it.
     * @param comment The comment to be sent.
     */
    public void sendMessage(Conversation to, User from, String comment) {
        Message msg = new Message(to, from, comment);
        client.sendMessage(msg);
    }

    /**
     * Sends a Message object to client with given String.
     *
     * @param msg The comment to be sent.
     */
    public void sendMessage(String msg) {
        Conversation con = new Conversation(null);
        con.setToAll(true);
        Message message = new Message(con, client.getUser(), msg);
        client.sendMessage(message);
    }

    /**
     * Sends the ArrayList with connected users to the UI.
     *
     * @param userList The ArrayList with connected users.
     */
    public void setConnectedUsers(ArrayList<User> userList) {
        ui.setConnectedUsers(userList);
    }

    /**
     * Appends new text to the main UI window.
     *
     * @param txt The text to append.
     */
    public void appendText(String txt) {
        ui.appendText(txt);
    }

    /**
     * Closes the clients socket.
     */
    public void disconnectClient() {
        client.disconnectClient();
    }

//    public Conversation newConversation(String str) {
//        String[] split = str.split(",");
//        ArrayList<User> temp = new ArrayList<>();
//        for (int i = 0; i < split.length; i++) {
//            temp.add(split[i]);
//        }
//        return new Conversation()
//    }
}