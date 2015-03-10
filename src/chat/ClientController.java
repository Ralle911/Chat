package chat;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Controller class to handle system logic between client and GUI.
 *
 * @author Emil Sandgren, Kalle Bornemark, Erik Sandgren,
 * Jimmy Maksymiw, Lorenz Puskas & Rasmus Andersson
 */
public class ClientController {
	private StartClient ui = new StartClient(this);
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

    /**
     * Returns the current user's ID.
     *
     * @return A string containing the current user's ID.
     */
    public String getUserID () {
        return client.getUser().getId();
    }

    /**
     * Creates a new message containing given ID and content, then sends it to the client.
     *
     * @param conID Conversation-ID of the message.
     * @param content The message's content.
     */
    public void sendMessage(int conID, Object content) {
        Message message = new Message(conID, client.getUser().getId(), content);
        client.sendObject(message);
    }

    /**
     * Takes a conversation ID and String with URL to image, scales the image and sends it to the client.
     *
     * @param conID Conversation-ID of the image.
     * @param url A string containing the URl to the image to be sent.
     */
    public void sendImage(int conID, String url) {
        System.out.println(url);
        ImageIcon icon = new ImageIcon(url);
        Image img = icon.getImage();
        BufferedImage scaledImage = ImageScaleHandler.createScaledImage(img, 250);
        icon = new ImageIcon(scaledImage);
        sendMessage(conID, icon);
    }


    /**
     * Creates a HashSet of given String array with participants, and sends it to the client.
     *
     * @param conversationParticipants A string array with conversaion participants.
     */
    public void sendParticipants(String[] conversationParticipants) {
    	HashSet<String> setParticpants = new HashSet<>();
    	for(String participant: conversationParticipants) {
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
     * Presents a Conversation in the UI.
     *
     * @param con The Conversation object to be presented in the UI.
     */
    public void newConversation(Conversation con) {
        HashSet<String> users = con.getInvolvedUsers();
        String[] usersHashToStringArray = users.toArray(new String[users.size()]);
        int conID = con.getId();
        ui.createConversation(usersHashToStringArray, conID);
        for (Message message : con.getConversationLog()) {
            ui.appendContent(message);
        }
    }
}