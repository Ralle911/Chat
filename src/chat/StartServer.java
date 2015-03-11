package chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
 * Create an server-panel class.
 */
public class StartServer extends JPanel{
    private JPanel pnlServerCenterFlow = new JPanel(new FlowLayout());
    private JPanel pnlServerCenterGrid = new JPanel(new GridLayout(1,2,5,5));
    private JPanel pnlServerGrid = new JPanel(new GridLayout(2,1,5,5));

    private JTextField txtServerPort = new JTextField("3450");
    private JLabel lblServerPort = new JLabel("Port:");
    private JLabel lblServerShowServerIp = new JLabel();
    private JLabel lblWelcome = new JLabel("Create a bIRC server");
    private JButton btnServerCreateServer = new JButton("Create Server");

    private Font fontIpPort = new Font("Sans-Serif",Font.PLAIN,17);
    private Font fontInfo = new Font("Sans-Serif",Font.BOLD|Font.ITALIC,20);
    private Font fontWelcome = new Font("Sans-Serif", Font.BOLD,25);
    private Font fontButton = new Font("Sans-Serif", Font.BOLD,18);
    private Server server;
    
    public StartServer() {
    	lookAndFeel();
        initPanels();
        initLabels();
        setlblServerShowServerIp();
        initListeners();
    }

    /**
     * Initiate Server-Panels.
     */
    public void initPanels() {
        setPreferredSize(new Dimension(350,150));
        setOpaque(true);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        add(pnlServerGrid,BorderLayout.CENTER);
        pnlServerGrid.add(pnlServerCenterGrid);
        add(lblServerShowServerIp,BorderLayout.SOUTH);

        pnlServerCenterFlow.setOpaque(true);
        pnlServerCenterFlow.setBackground(Color.WHITE);
        pnlServerCenterGrid.setOpaque(true);
        pnlServerCenterGrid.setBackground(Color.WHITE);
        pnlServerGrid.setOpaque(true);
        pnlServerGrid.setBackground(Color.WHITE);

        pnlServerCenterGrid.add(lblServerPort);
        pnlServerCenterGrid.add(txtServerPort);
        btnServerCreateServer.setFont(fontButton);
        pnlServerGrid.add(btnServerCreateServer);
    }

    /**
     * Initiate Server-Labels.
     */
    public void initLabels() {
        lblServerPort.setHorizontalAlignment(JLabel.CENTER);
        lblWelcome.setHorizontalAlignment(JLabel.CENTER );
        lblServerShowServerIp.setFont(fontInfo);
        lblServerShowServerIp.setForeground(new Color(146,1,1));
        lblServerShowServerIp.setHorizontalAlignment(JLabel.CENTER);
        lblServerPort.setFont(fontIpPort);
        lblServerPort.setOpaque(true);
        lblServerPort.setBackground(Color.WHITE);
        lblWelcome.setFont(fontWelcome);
        add(lblWelcome,BorderLayout.NORTH);
        txtServerPort.setFont(fontIpPort);
    }
    
    public void initListeners() {
    	CreateStopServerListener create = new CreateStopServerListener();
    	EnterListener enter = new EnterListener();
    	btnServerCreateServer.addActionListener(create);
    	txtServerPort.addKeyListener(enter);
    }

    /**
     * Sets the ip-label to the local ip of your own computer.
     */
    public void setlblServerShowServerIp() {
        try {
            String message = ""+ InetAddress.getLocalHost();
            String realmessage[] = message.split("/");
            lblServerShowServerIp.setText("Server ip is: " + realmessage[1]);
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(null, "An error occurred.");
        }
    }
    
    /**
     * Main method for create a server-frame.
     * @param args
     */
    public static void main(String[] args) {
        StartServer server = new StartServer();
        JFrame frame = new JFrame("bIRC Create Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(server);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
    }

    /**
     * Returns the port from the textfield.
     *
     * @return Port for creating a server.
     */
    public int getPort() {
        return Integer.parseInt(this.txtServerPort.getText());
    }
    
    /**
     * Set the "Look and Feel".
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
     * Listener for create server. Starts a new server with the port of the textfield.
     */
    private class CreateStopServerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (btnServerCreateServer==e.getSource()) {
				server = new Server(getPort());
			}
		}
	}
    
    /**
     * Enter Listener for creating a server.
     */
    private class EnterListener implements KeyListener {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				server = new Server(getPort());
			}
		}

		public void keyReleased(KeyEvent arg0) {}

		public void keyTyped(KeyEvent arg0) {}
    }
}
