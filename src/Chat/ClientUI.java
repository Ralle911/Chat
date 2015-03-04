package Chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
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
	private JPanel eastPanelCenter = new JPanel(new BorderLayout());
	private JPanel eastPanelCenterNorth = new JPanel(new GridLayout(3,1));
	private JPanel pnlGroupSend = new JPanel(new GridLayout(1,2,8,8));
	
	private JPanel pnlNewGroup = new JPanel();
	private JPanel pnlOuterBorderLayout = new JPanel(new BorderLayout());
	
	private JLabel lblUser = new JLabel("User");

	private JButton btnSend = new JButton("Send");
	private JButton btnNewGroupChat = new JButton("Gr");
	private JButton btnNewPrivateMessage = new JButton("pr");
	private JButton btnGroup = new JButton("Groups");
	private JButton btnGroup2 = new JButton("Groups2");
	private JButton btnLobby = new JButton("Lobby");
	private JButton btnCreateGroup = new JButton("Skapa grupp");
	
	private JTextPane tpChatWindow = new JTextPane();
	private JTextPane tpConnectedUsers = new JTextPane();
	
	private JTextField tfMessageWindow = new JTextField();
	
	private JScrollPane scrollConnectedUsers = new JScrollPane(tpConnectedUsers);
	private JScrollPane scroll = new JScrollPane(tpChatWindow);
	private JScrollPane scrollCheckConnectedUsers = new JScrollPane(pnlNewGroup);
    
	private Font txtFont = new Font("Sans-Serif", Font.BOLD , 20);
	private SimpleAttributeSet chatFont = new SimpleAttributeSet();
	private ClientController clientController;
	private GroupPanel groupPanel;
	
	public ClientUI(ClientController clientController) {
		this.clientController = clientController;
		lookAndFeel(); 
        initGraphics();
		initListeners();
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
        
        scrollConnectedUsers.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollConnectedUsers.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        DefaultCaret caretConnected = (DefaultCaret)tpConnectedUsers.getCaret();
        caretConnected.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        tpConnectedUsers.setEditable(false);
        
        scrollCheckConnectedUsers.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollCheckConnectedUsers.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        StyleConstants.setForeground(chatFont, Color.BLACK);
        StyleConstants.setBold(chatFont, true);
        
        add(scroll, BorderLayout.CENTER);
        
        southPanel();
        eastPanel();
    }

    public void focusTextField() {
        tfMessageWindow.requestFocusInWindow();
    }
    
    public void initListeners() { 
    	tfMessageWindow.addActionListener(new EnterListener());
    	GroupListener groupListener = new GroupListener();
    	btnNewGroupChat.addActionListener(groupListener);
    	btnCreateGroup.addActionListener(groupListener);
    }
    /*
     * N�r servern updaterar Users
     */
    public void setConnectedUsers(ArrayList<User> connectedUsers) {
        tpConnectedUsers.setText("");
    	for (User user: connectedUsers) {
    		appendConnectedUsers(user.getId());
    	}
    }
    /*
     * S�tt sen till controller.setUser(); eller n�got
     */
    public void setUserText(String user) {
    	lblUser.setText(user);
    }
    private class GroupPanel extends Thread {
    	private JFrame groupFrame;
    	
    	public JFrame getFrame() {
    		return groupFrame;
    	}
	    public void run() {
	    	pnlOuterBorderLayout.add(btnCreateGroup,BorderLayout.SOUTH);
	    	pnlOuterBorderLayout.add(scrollCheckConnectedUsers,BorderLayout.CENTER);
	    	scrollCheckConnectedUsers.setPreferredSize(new Dimension(200,500));
	    	pnlNewGroup.setLayout(new GridLayout(100,1,5,5));
	    	groupFrame = new JFrame();
	    	groupFrame = new JFrame();
	    	groupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			groupFrame.add(pnlOuterBorderLayout);
			groupFrame.pack();
			groupFrame.setVisible(true);
			groupFrame.setLocationRelativeTo(null);
			/*
			 * Under �r en simulation av anv�ndare.
			 */
			for (int i = 0; i <= 10; i++) {
				pnlNewGroup.add(new JCheckBox("anv�ndare " +i));
			}
	    }
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
    	eastPanel.add(lblUser,BorderLayout.NORTH);
    	eastPanel.add(eastPanelCenter,BorderLayout.CENTER);
    	
    	eastPanelCenter.add(eastPanelCenterNorth,BorderLayout.NORTH);
    	eastPanelCenter.add(scrollConnectedUsers,BorderLayout.CENTER);
    	
    	pnlGroupSend.add(btnNewGroupChat);
    	pnlGroupSend.add(btnNewPrivateMessage);
    	
    	eastPanelCenterNorth.add(pnlGroupSend);
    	eastPanelCenterNorth.add(btnGroup);
    	eastPanelCenterNorth.add(btnGroup2);
    	
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
    
    public void appendConnectedUsers(String message){
    	StyledDocument doc = tpConnectedUsers.getStyledDocument();
    	try {
			doc.insertString(doc.getLength(), message + "\n", chatFont);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
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
    
	private class EnterListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			clientController.sendMessage(tfMessageWindow.getText());
//			appendText(tfMessageWindow.getText()); //Tempor�r f�r att testa utan server
//			appendConnectedUsers(tfMessageWindow.getText()); //Tempor�r f�r att testa utan server
			tfMessageWindow.setText("");
		}
	}
	
	private class GroupListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (btnNewGroupChat == e.getSource()) {
				groupPanel = new GroupPanel();
				groupPanel.start();
			}
			if (btnCreateGroup == e.getSource()) {
//				clientController.newConversation();
				groupPanel.getFrame().dispose();
			}
		}
	}
}
