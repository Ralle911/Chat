package chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Log in UI and start-class for the chat.
 *
 * @author Emil Sandgren, Kalle Bornemark, Erik Sandgren,
 * Jimmy Maksymiw, Lorenz Puskas & Rasmus Andersson.
 */
public class StartClient extends JPanel {
	private JLabel lblIp = new JLabel("IP:");
	private JLabel lblPort = new JLabel("Port:"); 
	private JLabel lblWelcomeText = new JLabel("Log in to bIRC");
	private JLabel lblUserName = new JLabel("Username:");
	
	private JTextField txtIp = new JTextField("localhost");
	private JTextField txtPort = new JTextField("3450");
	private JTextField txtUserName = new JTextField();
	
	private JButton btnLogIn = new JButton("Login");
	private JButton btnCancel = new JButton("Cancel");
	
	private Font fontWelcome = new Font("Sans-Serif",Font.BOLD,25);
	private Font fontIpPort = new Font("Sans-Serif",Font.PLAIN,17);
	private Font fontButtons = new Font("Sans-Serif",Font.BOLD,15);
	private Font fontUserName = new Font("Sans-Serif",Font.BOLD,17);
	
	private JPanel pnlCenterGrid = new JPanel(new GridLayout(3,2,5,5));
	private JPanel pnlCenterFlow = new JPanel(new FlowLayout());
	private JPanel pnlNorthGrid = new JPanel(new GridLayout(2,1,5,5));
	private JPanel pnlNorthGridGrid = new JPanel(new GridLayout(1,2,5,5));
	
	public StartClient() {
		setLayout(new BorderLayout());
		initPanels();
		lookAndFeel();
		initGraphics();
		initButtons();
		initListeners();
	}
	
	/**
	 * Initiates the listeners.
	 */
	public void initListeners() {
		LogInMenuListener log = new LogInMenuListener();
		btnLogIn.addActionListener(log);
		txtUserName.addActionListener(new EnterListener());
		btnCancel.addActionListener(log);
	}
	
	/**
	 * Initiates the panels.
	 */
	public void initPanels(){
		setPreferredSize(new Dimension(400, 180));
		pnlCenterGrid.setBounds(100, 200, 200, 50);
		add(pnlCenterFlow,BorderLayout.CENTER);
		pnlCenterFlow.add(pnlCenterGrid);
		
		add(pnlNorthGrid,BorderLayout.NORTH);
		pnlNorthGrid.add(lblWelcomeText);
		pnlNorthGrid.add(pnlNorthGridGrid);
		pnlNorthGridGrid.add(lblUserName);
		pnlNorthGridGrid.add(txtUserName);
		
		lblUserName.setHorizontalAlignment(JLabel.CENTER);
		lblUserName.setFont(fontIpPort);
		lblWelcomeText.setHorizontalAlignment(JLabel.CENTER);
		lblWelcomeText.setFont(fontWelcome);
		lblIp.setFont(fontIpPort);
		lblPort.setFont(fontIpPort);
	}
	
	/**
	 * Initiates the buttons.
	 */
	public void initButtons() {
		btnCancel.setFont(fontButtons);
		btnLogIn.setFont(fontButtons);
		
		pnlCenterGrid.add(lblIp);
		pnlCenterGrid.add(txtIp);
		pnlCenterGrid.add(lblPort);
		pnlCenterGrid.add(txtPort);
		pnlCenterGrid.add(btnLogIn);
		pnlCenterGrid.add(btnCancel);
	}
	
	/**
	 * Initiates the graphics and some design.
	 */
	public void initGraphics() {
		pnlCenterGrid.setOpaque(false);
		pnlCenterFlow.setOpaque(false);
		pnlNorthGridGrid.setOpaque(false);
		pnlNorthGrid.setOpaque(false);
		setBackground(Color.WHITE);
		lblUserName.setBackground(Color.WHITE);
		lblUserName.setOpaque(false);
		
		btnLogIn.setForeground(new Color(41,1,129));
		btnCancel.setForeground(new Color(41,1,129));
		
		txtIp.setFont(fontIpPort);
		txtPort.setFont(fontIpPort);
		txtUserName.setFont(fontUserName);
	}
	
	/**
	 * Sets the "Look and Feel" of the JComponents.
	 */
	public void lookAndFeel() {
   	 try {
   	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
   	    } catch (ClassNotFoundException e) {
   	        e.printStackTrace();
   	    } catch (InstantiationException e) {
   	        e.printStackTrace();
   	    } catch (IllegalAccessException e) {
   	        e.printStackTrace();
   	    } catch (UnsupportedLookAndFeelException e) {
   	        e.printStackTrace();
   	    }
   }

	/**
	 * Main method for the login-frame.
	 */
	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("bIRC Login");
                StartClient ui = new StartClient();
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.add(ui);
                frame.pack();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
                frame.setResizable(false);
            }
        });

	}
	
	/**
	 * Listener for login-button, create server-button and for the cancel-button.
	 * Also limits the username to a 10 char max.
	 */
	private class LogInMenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (btnLogIn==e.getSource()) {
					if (txtUserName.getText().length() <= 10) {
						new Client(txtIp.getText(), Integer.parseInt(txtPort.getText()),txtUserName.getText());
					} else {
					JOptionPane.showMessageDialog(null, "Namnet får max vara 10 karaktärer!");
					txtUserName.setText("");
				}
			}
			if (btnCancel==e.getSource()) {
				System.exit(0);
			}
		}
	}
	
	/**
	 * Listener for the textField. Enables you to press enter instead of login.
	 * Also limits the username to 10 chars.
	 */
	private class EnterListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(txtUserName.getText().length() <= 10) {
				new Client(txtIp.getText(), Integer.parseInt(txtPort.getText()),txtUserName.getText());
			} else {
				JOptionPane.showMessageDialog(null, "Namnet får max vara 10 karaktärer!");
				txtUserName.setText("");
			}
		}
	}
}
