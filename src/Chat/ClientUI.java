package Chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Viewer class to handle the GUI.
 *
 * @author Emil Sandgren, Kalle Bornemark, Erik Sandgren, Jimmy Maksymiw, Lorenz Puskas & Rasmus Andersson
 */

public class ClientUI extends JPanel {
	private JPanel southPanel = new JPanel();
	private JPanel eastPanel = new JPanel();
	private JPanel eastPanelNorth = new JPanel(new GridLayout(2,1));
	
	private JButton btnSend = new JButton("Send");
	private JButton btnUsers = new JButton("Users");
	private JButton btnGroups = new JButton("Groups");
	private JButton btnLobby = new JButton("Lobby");
	
	private JTextPane tpUsers = new JTextPane();
	private JTextPane tpGroups = new JTextPane();
	private JTextPane tpChatWindow = new JTextPane();
	
	private JTextField tfMessageWindow = new JTextField();
    private JScrollPane scroll = new JScrollPane(tpChatWindow);
    
	private Font txtFont = new Font("Sans-Serif", Font.BOLD , 20);
	private SimpleAttributeSet chatFont = new SimpleAttributeSet();
	
	private ClientController clientController;
	
	
	public ClientUI(ClientController clientController) {
		this.clientController = clientController;
        initGraphics();
		tfMessageWindow.addActionListener(new EnterListener());
	}

    public void initGraphics() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(900,600));

        tpChatWindow.setFont(txtFont);
        tfMessageWindow.setFont(txtFont);

        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        DefaultCaret caret = (DefaultCaret)tpChatWindow.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        tpChatWindow.setEditable(false);
        
        StyleConstants.setForeground(chatFont, Color.BLACK);
        StyleConstants.setBold(chatFont, true);
        add(scroll, BorderLayout.CENTER);
        
        southPanel();
        eastPanel();
    }

    public void focusTextField() {
        tfMessageWindow.requestFocusInWindow();
    }
    
    public void southPanel() {
    	southPanel.setLayout(new BorderLayout());
    	southPanel.add(tfMessageWindow,BorderLayout.CENTER);
    	southPanel.setPreferredSize(new Dimension(600,50));
    	btnSend.setPreferredSize(new Dimension(75,40));
    	southPanel.add(btnSend,BorderLayout.EAST);
    	add(southPanel,BorderLayout.SOUTH);
    }
    
    public void eastPanel() {
    	eastPanel.setLayout(new BorderLayout());
    	eastPanelNorth.add(btnUsers);
    	eastPanelNorth.add(btnGroups);
    	eastPanel.add(eastPanelNorth,BorderLayout.NORTH);
    	eastPanel.add(tpUsers,BorderLayout.CENTER);
    	eastPanel.add(btnLobby,BorderLayout.SOUTH);
    	add(eastPanel,BorderLayout.EAST);
    }

    public void appendText(String message){
    	StyledDocument doc = tpChatWindow.getStyledDocument();
    	try {
			doc.insertString(doc.getLength(), message + "\n", chatFont);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
    }
    
	private class EnterListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
//			clientController.sendMessage(tfMessageWindow.getText());
			appendText(tfMessageWindow.getText());
			tfMessageWindow.setText("");
		}
	}
}
