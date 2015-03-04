package Chat;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Controller class to handle system logic.
 *
 * @author Emil Sandgren, Kalle Bornemark, Erik Sandgren, Jimmy Maksymiw, Lorenz Puskas & Rasmus Andersson
 */

public class ClientController {
	private ClientUI ui = new ClientUI(this);
    private Client client;
    private String name;

	public ClientController(Client client, String name) {
        this.client = client;
        this.client.setClientController(this);
        this.name = name;
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
    
    public void sendMessage(String message) {
    	Message msg = new Message(name, message);
        client.sendMessage(msg);
    }
}
