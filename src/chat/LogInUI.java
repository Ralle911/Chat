package chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
/**
 *
 * @author Emil Sandgren
 */
public class LogInUI extends Thread{
	private JLabel lblIp = new JLabel("IP:");
	private JLabel lblPort = new JLabel("Port:"); 
	private JLabel lblWelcomeText = new JLabel("Log in to bIRC or create a server");
	private JLabel lblUserName = new JLabel("Username:");
	
	private JTextField txtIp = new JTextField("localhost");
	private JTextField txtPort = new JTextField("3450");
	private JTextField txtUserName = new JTextField();
	
	private JButton btnLogIn = new JButton("Login");
	private JButton btnCancel = new JButton("Cancel");
	private JButton btnCreateServer = new JButton("Create Server");
	
	private Font fontWelcome = new Font("Sans-Serif",Font.BOLD,20);
	private Font fontIpPort = new Font("Sans-Serif",Font.PLAIN,17);
	private Font fontButtons = new Font("Sans-Serif", Font.BOLD, 15);
	private Font fontInfo = new Font("Sans-Serif",Font.BOLD|Font.ITALIC,17);
	
	private BorderLayout borderLayout = new BorderLayout();
	private JPanel pnlOuterBorderLayout = new JPanel(borderLayout);
	private JPanel pnlCenterGrid = new JPanel(new GridLayout(3,2,5,5));
	private JPanel pnlCenterFlow = new JPanel(new FlowLayout());
	private JPanel pnlNorthGrid = new JPanel(new GridLayout(2,1,5,5));
	private JPanel pnlNorthGridGrid = new JPanel(new GridLayout(1,2,5,5));
	private ServerPanel serverPanel;
	private JFrame frame;
	
	public LogInUI() {
		serverPanel = new ServerPanel();
		serverPanel.start();
		initPanels();
		lookAndFeel();
		initGraphics();
		initButtons();
		initListeners();
	}
	
	public void initListeners() {
		LogInMenuListener log = new LogInMenuListener();
		btnCreateServer.addActionListener(log);
		btnLogIn.addActionListener(log);
		txtUserName.addActionListener(new EnterListener());
		serverPanel.txtServerPort.addActionListener(new EnterListenerServer());
		serverPanel.btnServerCreateServer.addActionListener(new CreateServerListener());
	}
	
	public void initPanels(){
		pnlOuterBorderLayout.setPreferredSize(new Dimension(430,190));
		pnlCenterGrid.setBounds(100, 200, 200, 50);
		pnlOuterBorderLayout.add(btnCreateServer,BorderLayout.SOUTH);
		pnlOuterBorderLayout.add(pnlCenterFlow,BorderLayout.CENTER);
		pnlCenterFlow.add(pnlCenterGrid);
		
		pnlOuterBorderLayout.add(pnlNorthGrid,BorderLayout.NORTH);
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
	
	public void initButtons() {
		btnCreateServer.setForeground(new Color(1,48,69));
		btnCreateServer.setFont(fontButtons);
		btnCancel.setFont(fontButtons);
		btnLogIn.setFont(fontButtons);
		
		pnlCenterGrid.add(lblIp);
		pnlCenterGrid.add(txtIp);
		pnlCenterGrid.add(lblPort);
		pnlCenterGrid.add(txtPort);
		pnlCenterGrid.add(btnLogIn);
		pnlCenterGrid.add(btnCancel);
	}
	
	public void initGraphics() {
		pnlCenterGrid.setOpaque(false);
		pnlCenterFlow.setOpaque(false);
		pnlNorthGridGrid.setOpaque(false);
		pnlNorthGrid.setOpaque(false);
		pnlOuterBorderLayout.setBackground(Color.WHITE);
		lblUserName.setBackground(Color.WHITE);
		lblUserName.setOpaque(false);
	}
	
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
	
	public void run() {
		this.frame = new JFrame("bIRC Login");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(pnlOuterBorderLayout);
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
	}
	
	private class ServerPanel extends Thread {
		private JPanel pnlServerOuterBorderLayout = new JPanel(new BorderLayout());
		private JPanel pnlServerCenterFlow = new JPanel(new FlowLayout());
		private JPanel pnlServerCenterGrid = new JPanel(new GridLayout(2,2,5,5));
		private JFrame frame = new JFrame("bIRC Create Server");
		
		private JTextField txtServerPort = new JTextField("3450");
		private JLabel lblServerWelcomeMessage = new JLabel("Create Server");
		private JLabel lblServerPort = new JLabel("Port:");
		private JLabel lblServerShowServerIp = new JLabel();
		
		private JButton btnServerCreateServer = new JButton("Create");
		private JButton btnServerCancel = new JButton("Cancel");
	
		public ServerPanel() {
			initPanels();
			initLabels();
			setlblServerShowServerIp();
		}
		
		public void initPanels() {
			pnlServerOuterBorderLayout.setPreferredSize(new Dimension(350,110));
			pnlServerOuterBorderLayout.add(pnlServerCenterFlow,BorderLayout.CENTER);
			pnlServerCenterFlow.add(pnlServerCenterGrid);
			pnlServerOuterBorderLayout.add(lblServerWelcomeMessage,BorderLayout.NORTH);
			pnlServerOuterBorderLayout.add(lblServerShowServerIp,BorderLayout.SOUTH);	
			
			pnlServerCenterFlow.setOpaque(true);
			pnlServerCenterFlow.setBackground(Color.WHITE);
			pnlServerCenterGrid.setOpaque(true);
			pnlServerCenterGrid.setBackground(Color.WHITE);
			
			pnlServerCenterGrid.add(lblServerPort);
			pnlServerCenterGrid.add(txtServerPort);
			pnlServerCenterGrid.add(btnServerCreateServer);
			pnlServerCenterGrid.add(btnServerCancel);
		}
		
		public void initLabels() {
			lblServerWelcomeMessage.setFont(fontWelcome);
			lblServerShowServerIp.setFont(fontInfo);
			lblServerWelcomeMessage.setHorizontalAlignment(JLabel.CENTER);
			lblServerShowServerIp.setHorizontalAlignment(JLabel.CENTER);
			lblServerPort.setFont(fontIpPort);
			lblWelcomeText.setOpaque(true);
			lblServerPort.setOpaque(true);
			lblServerPort.setBackground(Color.WHITE);
		}
		
		public void setlblServerShowServerIp() {
			try {
				String message = ""+InetAddress.getLocalHost();
				String realmessage[] = message.split("/");
				lblServerShowServerIp.setText("Server ip is: " + realmessage[1]);
			} catch (UnknownHostException e) {
				JOptionPane.showMessageDialog(null, "An error occurred.");
			}
		}
		
		public void run() {
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.add(pnlServerOuterBorderLayout);
			frame.pack();
			frame.setVisible(false);
			frame.setLocationRelativeTo(null);
			frame.setResizable(false);
		}
		
		public int getPort() {
			return Integer.parseInt(this.txtServerPort.getText());
		}
	}
	
	private class LogInMenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (btnCreateServer==e.getSource()) {
				serverPanel.frame.setVisible(true);
			}
			if (btnLogIn==e.getSource()) {
					if(txtUserName.getText().length() <= 10) {
						new Client(txtIp.getText(), Integer.parseInt(txtPort.getText()),txtUserName.getText());
						frame.setVisible(false);
					} else {
					JOptionPane.showMessageDialog(null, "Namnet får max vara 10 karaktärer!");
					txtUserName.setText("");
				}
			}
		}
	}
	
	private class EnterListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
				if(txtUserName.getText().length() <= 10) {
					new Client(txtIp.getText(), Integer.parseInt(txtPort.getText()),txtUserName.getText());
					frame.setVisible(false);
				} else {
				JOptionPane.showMessageDialog(null, "Namnet får max vara 10 karaktärer!");
				txtUserName.setText("");
				}
			}
	}
	
	private class EnterListenerServer implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			new Server(serverPanel.getPort());
			serverPanel.frame.dispose();
		}
	}
	
	private class CreateServerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (serverPanel.btnServerCreateServer==e.getSource()) {
				new Server(serverPanel.getPort());
				serverPanel.frame.dispose();
			}
		}
	}
	
	public static void main(String[] args) { 
		new LogInUI().start();
	}
}
