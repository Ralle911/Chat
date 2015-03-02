package Chat;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


/**
 * @author Jimmy Maksymiw
 */

public class ClientController {
	private ClientUI ui = new ClientUI(this);
    private Client client;
    private String name;

	public ClientController(String name) {
        this.name = name;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.add(ui);
				frame.pack();
				frame.setVisible(true);
			}
		});
        client = new Client("localhost", 3450, this);
	}
    public void newMessage(Message message) {
        ui.appendText(message.toString());
    }
    
    public void sendMessage(String message) {
    	Message msg = new Message(name, message);
        client.sendMessage(msg);
    }
}
