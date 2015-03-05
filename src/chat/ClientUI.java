package chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
	
	private String userString = "";
	private String userString2 = "";
	
//	private JPanel pnlNewGroup = new JPanel();
//	private JPanel pnlOuterBorderLayout = new JPanel(new BorderLayout());
	
	private JLabel lblUser = new JLabel("User");

	private JButton btnSend = new JButton("Send");
	private JButton btnNewGroupChat = new JButton("Gr");
	private JButton btnNewPrivateMessage = new JButton("pr");
//	private JButton btnGroup = new JButton("Groups");
//	private JButton btnGroup2 = new JButton("Groups2");
	private JButton btnLobby = new JButton("Disconnect");
	private JButton btnCreateGroup = new JButton("");
	private JButton btnCreatePrivateMessage = new JButton("");
	
	private JTextPane tpChatWindow = new JTextPane();
	private JTextPane tpConnectedUsers = new JTextPane();
	
	private JTextField tfMessageWindow = new JTextField();
	
	private JCheckBox cbUser1 = new JCheckBox("Kallexander");
	private JCheckBox cbUser2 = new JCheckBox("Jimmo");
	
	private JScrollPane scrollConnectedUsers = new JScrollPane(tpConnectedUsers);
	private JScrollPane scroll = new JScrollPane(tpChatWindow);
//	private JScrollPane scrollCheckConnectedUsers = new JScrollPane(pnlNewGroup);
    
	private Font txtFont = new Font("Sans-Serif", Font.BOLD , 20);
	private SimpleAttributeSet chatFont = new SimpleAttributeSet();
	private ClientController clientController;
	private GroupPanel groupPanel;
	private GroupPanel2 groupPanel2;
	
	public ClientUI(ClientController clientController) { //ClientController clientController
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
        
//        scrollCheckConnectedUsers.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//        scrollCheckConnectedUsers.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        
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
    	PrivateListener privateListener = new PrivateListener();
    	CheckBoxListener checkBoxListener = new CheckBoxListener();
    	DisconnectListener disconnectListener = new DisconnectListener();
    	btnNewGroupChat.addActionListener(groupListener);
    	btnCreateGroup.addActionListener(groupListener);
    	btnNewPrivateMessage.addActionListener(privateListener);
    	btnCreatePrivateMessage.addActionListener(privateListener);
    	cbUser1.addActionListener(checkBoxListener);
    	cbUser2.addActionListener(checkBoxListener);
    	btnLobby.addActionListener(disconnectListener);
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
    	private JPanel pnlOuterBorderLayout = new JPanel(new BorderLayout());
    	private JPanel pnlNewGroup = new JPanel();
    	private JScrollPane scrollCheckConnectedUsers = new JScrollPane(pnlNewGroup);
    	
    	public JFrame getFrame() {
    		return groupFrame;
    	}
	    public void run() {
	    	panelBuilder();
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
//			for (int i = 0; i <= 10; i++) {
//				pnlNewGroup.add(new JCheckBox("anv�ndare " +i));
//			}
	    }
	    
	    public void panelBuilder() {
	    	scrollCheckConnectedUsers.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	        scrollCheckConnectedUsers.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	        pnlOuterBorderLayout.add(btnCreateGroup,BorderLayout.SOUTH);
		    btnCreateGroup.setText("Create Group");
		   	pnlOuterBorderLayout.add(scrollCheckConnectedUsers,BorderLayout.CENTER);
		   	scrollCheckConnectedUsers.setPreferredSize(new Dimension(200,500));
		   	pnlNewGroup.setLayout(new GridLayout(100,1,5,5));
		   	pnlNewGroup.add(cbUser1);
		   	pnlNewGroup.add(cbUser2);
	         
	    }
    }
    
    private class GroupPanel2 extends Thread {
    	private JFrame groupFrame;
    	private JPanel pnlOuterBorderLayout = new JPanel(new BorderLayout());
    	private JPanel pnlNewGroup = new JPanel();
    	private JScrollPane scrollCheckConnectedUsers = new JScrollPane(pnlNewGroup);
    	
    	
    	public JFrame getFrame() {
    		return groupFrame;
    	}
	    public void run() {
	    	panelBuilder();
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
//			for (int i = 0; i <= 10; i++) {
//				pnlNewGroup.add(new JCheckBox("anv�ndare " +i));
//				
//			}
	    }
	    
	    public void panelBuilder() {
	    	scrollCheckConnectedUsers.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	        scrollCheckConnectedUsers.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	     	pnlOuterBorderLayout.add(btnCreatePrivateMessage,BorderLayout.SOUTH);
	    	btnCreatePrivateMessage.setText("Private Message");
	    	pnlOuterBorderLayout.add(scrollCheckConnectedUsers,BorderLayout.CENTER);
	    	scrollCheckConnectedUsers.setPreferredSize(new Dimension(200,500));
	    	pnlNewGroup.setLayout(new GridLayout(100,1,5,5));
	    	pnlNewGroup.add(cbUser1);
		   	pnlNewGroup.add(cbUser2);
	         
	    }
    }
    
    public void southPanel() {
    	southPanel.setLayout(new BorderLayout());
    	southPanel.add(tfMessageWindow,BorderLayout.CENTER);
    	southPanel.setPreferredSize(new Dimension(600,50));
    	
    	btnSend.setPreferredSize(new Dimension(101,40));
    	
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
//    	eastPanelCenterNorth.add(btnGroup);
//    	eastPanelCenterNorth.add(btnGroup2);
    	
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
    
    public void setNewChatTab(String userString) {
    	String[] users = userString.split(",");
    	JButton btn2 = new JButton("TEST");
    	for (int i = 0; i <= users.length -1; i++) {
    		
    		JButton btn1 = new JButton(users[i]);
    		eastPanelCenterNorth.add(btn1);
    		System.out.println(users[i]);
    		eastPanelCenterNorth.add(btn2); 
    	}
    	this.userString = "";
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
				setNewChatTab(userString);
//				clientController.newConversation();
				groupPanel.getFrame().dispose();
			}
		}
	}
	
	private class PrivateListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (btnNewPrivateMessage == e.getSource()) {
				groupPanel2 = new GroupPanel2();
				groupPanel2.start();
			}
			if (btnCreatePrivateMessage == e.getSource()) {
//				clientController.newConversation();
				setNewChatTab(userString);
				groupPanel2.getFrame().dispose();
			}
		}
	}
	
	private class CheckBoxListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if (cbUser1.isSelected()==true) {
				userString += cbUser1.getText() +",";
			}
			if (cbUser2.isSelected()==true) {
				userString += cbUser2.getText() +",";
			}
		}
	}
	
	private class DisconnectListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (btnLobby==e.getSource()) {
				controller.disconnectClient();
			}
		}
	}
//	
//	public static void main(String[] args) {
//		ClientUI ui = new ClientUI();
//		JFrame frame = new JFrame();
//		frame.add(ui);
//		frame.pack();
//		frame.setVisible(true);
//	}
}
