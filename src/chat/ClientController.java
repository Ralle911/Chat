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
    public void newMessage(Object object) {
        if (object instanceof Message) {
            Message message = (Message)object;
            ui.appendText(message.getTimestamp() + " " + message.getFrom().getId() + ": " + (String)message.getContent());
        } else {
            ui.appendText(object.toString());
        }
    }
    
    public void sendMessage(Conversation to, User from, Object message) {
        Message msg = new Message(to, from, message);
        client.sendMessage(msg);
    }

    public void sendMessage(String msg) {
        Conversation con = new Conversation(null);
        con.setToAll(true);
        Message message = new Message(con, client.getUser(), msg);
        client.sendMessage(message);
    }

    public void setConnectedUsers(ArrayList<User> userList) {
        ui.setConnectedUsers(userList);
    }

    public void appendText(String txt) {
        ui.appendText(txt);
    }

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