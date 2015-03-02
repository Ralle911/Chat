package Chat;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


/**
 * @author Jimmy Maksymiw
 */

public class ClientController {
	private ClientUI ui = new ClientUI(this);
	private Message message;

	public ClientController() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.add(ui);
				frame.pack();
				frame.setVisible(true);
			}
		});
	}
    public void newMessage(Message message) {
        ui.appendText(message.toString());
    }
    
    public void writeMessage(String message) {
    	this.message = new Message(message);
    }
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ClientController controller = new ClientController();
			}
		});
	}
}
