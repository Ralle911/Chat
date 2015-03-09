package chat;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Controller class to handle system logic.
 *
 * @author Emil Sandgren, Kalle Bornemark, Erik Sandgren,
 * Jimmy Maksymiw, Lorenz Puskas & Rasmus Andersson
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
				JFrame frame = new JFrame("bIRC");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.add(ui);
				frame.pack();
                frame.setLocationRelativeTo(null);
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
            ui.appendContent(message);
        } else {
            ui.appendServerMessage((String)object);
        }
    }

    public String getUserID () {
        return client.getUser().getId();
    }

    public void sendMessage(int conID, Object content) {
        Message message = new Message(conID, client.getUser().getId(), content);
        client.sendObject(message);
    }

    public void sendImage(int conID, String url) {
        System.out.println(url);
        ImageIcon icon = new ImageIcon(url);
        Image img = icon.getImage();
        BufferedImage scaledImage = ImageScaleHandler.createScaledImage(img, 100);
        icon = new ImageIcon(scaledImage);
        sendMessage(conID, icon);
    }


    public void sendParticipants(String[] participants) {
    	HashSet<String> setParticpants = new HashSet<>();
    	for(String participant: participants) {
    		setParticpants.add(participant);
    	}
        client.sendObject(setParticpants);
    }

    /**
     * Sends the ArrayList with connected users to the UI.
     *
     * @param userList The ArrayList with connected users.
     */
    public void setConnectedUsers(ArrayList<String> userList) {
        ui.setConnectedUsers(userList);
    }

    /**
     * Appends new text to the main UI window.
     *
     * @param txt The text to append.
     */
//    public void appendText(String txt) {
//        ui.appendContent(txt);
//    }

    /**
     * Closes the clients socket.
     */
    public void disconnectClient() {
        client.disconnectClient();
    }

    public void newConversation(Conversation con) {
        HashSet<String> users = con.getInvolvedUsers();
        String[] usersHashToStringArray = users.toArray(new String[users.size()]);
        int conID = con.getId();
        ui.createConversation(usersHashToStringArray, conID);
//        while (con.getConversationLog().iterator().hasNext()) {
//            ui.appendContent(con.getConversationLog().iterator().next());
//        }
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