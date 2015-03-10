package chat;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Create an server-panel class.
 */
public class StartServer extends JPanel{
    private JPanel pnlServerCenterFlow = new JPanel(new FlowLayout());
    private JPanel pnlServerCenterGrid = new JPanel(new GridLayout(2,2,5,5));

    private JTextField txtServerPort = new JTextField("3450");
    private JLabel lblServerPort = new JLabel("Port:");
    private JLabel lblServerShowServerIp = new JLabel();

    private JButton btnServerCreateServer = new JButton("Create Server");

    private Font fontWelcome = new Font("Sans-Serif",Font.BOLD,20);
    private Font fontIpPort = new Font("Sans-Serif",Font.PLAIN,17);
    private Font fontButtons = new Font("Sans-Serif", Font.BOLD, 15);
    private Font fontInfo = new Font("Sans-Serif",Font.BOLD|Font.ITALIC,17);
    
    private Server server;
    
    public StartServer() {
        initPanels();
        initLabels();
        setlblServerShowServerIp();
        initListeners();
    }

    /**
     * Initiate Server-Panels.
     */
    public void initPanels() {
        setPreferredSize(new Dimension(350,110));
        add(pnlServerCenterFlow,BorderLayout.CENTER);
        pnlServerCenterFlow.add(pnlServerCenterGrid);
        add(lblServerShowServerIp,BorderLayout.SOUTH);

        pnlServerCenterFlow.setOpaque(true);
        pnlServerCenterFlow.setBackground(Color.WHITE);
        pnlServerCenterGrid.setOpaque(true);
        pnlServerCenterGrid.setBackground(Color.WHITE);

        pnlServerCenterGrid.add(lblServerPort);
        pnlServerCenterGrid.add(txtServerPort);
        pnlServerCenterGrid.add(btnServerCreateServer);
    }

    /**
     * Initiate Server-Labels.
     */
    public void initLabels() {
        lblServerShowServerIp.setFont(fontInfo);
        lblServerShowServerIp.setHorizontalAlignment(JLabel.CENTER);
        lblServerPort.setFont(fontIpPort);
        lblServerPort.setOpaque(true);
        lblServerPort.setBackground(Color.WHITE);
    }
    
    public void initListeners() {
    	CreateStopServerListener create = new CreateStopServerListener();
    	btnServerCreateServer.addActionListener(create);
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
    
    private class CreateStopServerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (btnServerCreateServer==e.getSource()) {
				server = new Server(getPort());
			}
		}
	}
}
