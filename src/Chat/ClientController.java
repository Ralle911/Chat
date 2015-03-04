package Chat;

import javax.swing.*;

/**
 * Controller class to handle system logic.
 *
 * @author Emil Sandgren, Kalle Bornemark, Erik Sandgren, Jimmy Maksymiw, Lorenz Puskas & Rasmus Andersson
 */

public class ClientController {
	private ClientUI ui = new ClientUI(this);
    private Client client;

	public ClientController(Client client, String name) {
        this.client = client;
        this.client.setClientController(this);
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
            ui.appendText(message.toString());
        } else {
            ui.appendText(object.toString());
        }
    }
    
    public void sendMessage(Conversation to, User from, Object message) {
        Message msg = new Message(to, from, message);
        client.sendMessage(msg);
    }
}