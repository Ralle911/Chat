package chat;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Create an server-panel class.
 */
public class StartServer {
    private JPanel pnlServerCenterFlow = new JPanel(new FlowLayout());
    private JPanel pnlServerCenterGrid = new JPanel(new GridLayout(2,2,5,5));
    private JLabel lblWelcomeText = new JLabel("IRC Create Server");

    private JTextField txtServerPort = new JTextField("3450");
    private JLabel lblServerWelcomeMessage = new JLabel("Create Server");
    private JLabel lblServerPort = new JLabel("Port:");
    private JLabel lblServerShowServerIp = new JLabel();

    private JButton btnServerCreateServer = new JButton("Create");
    private JButton btnServerCancel = new JButton("Cancel");

    private Font fontWelcome = new Font("Sans-Serif",Font.BOLD,20);
    private Font fontIpPort = new Font("Sans-Serif",Font.PLAIN,17);
    private Font fontButtons = new Font("Sans-Serif", Font.BOLD, 15);
    private Font fontInfo = new Font("Sans-Serif",Font.BOLD|Font.ITALIC,17);

    public StartServer() {
        initPanels();
        initLabels();
        setlblServerShowServerIp();
    }

    /**
     * Initiate Server-Panels.
     */
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

    /**
     * Initiate Server-Labels.
     */
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
        JFrame frame = new JFrame("bIRC Create Server");
        JPanel pnlServerOuterBorderLayout = new JPanel(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(pnlServerOuterBorderLayout);
        frame.pack();
        frame.setVisible(false);
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
}
