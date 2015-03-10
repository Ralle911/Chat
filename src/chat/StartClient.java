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
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
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
 * @author Emil Sandgren, Kalle Bornemark, Erik Sandgren,
 * Jimmy Maksymiw, Lorenz Puskas & Rasmus Andersson
 */

public class StartClient extends JPanel {
	private JPanel southPanel = new JPanel();
	private JPanel eastPanel = new JPanel();
	private JPanel eastPanelCenter = new JPanel(new BorderLayout());
	private JPanel eastPanelCenterNorth = new JPanel(new FlowLayout());
	private JPanel pnlGroupSend = new JPanel(new GridLayout(1,2,8,8));
	private JPanel pnlFileSend = new JPanel(new BorderLayout(5,5));
	
	private String userString = "";
	private int activeChatWindow = -1;
    private boolean createdGroup = false;
    
	private JLabel lblUser = new JLabel();
	private JButton btnSend = new JButton("Send");
	private JButton btnNewGroupChat = new JButton();
	private JButton btnLobby = new JButton("Lobby");
	private JButton btnCreateGroup = new JButton("");
	private JButton btnFileChooser = new JButton();
	
	private JTextPane tpConnectedUsers = new JTextPane();
	private ChatWindow cwLobby = new ChatWindow(-1);
	private ClientController clientController;
	private GroupPanel groupPanel;
	
	private JTextField tfMessageWindow = new JTextField();
	private BorderLayout bL = new BorderLayout();	
	
	private JScrollPane scrollConnectedUsers = new JScrollPane(tpConnectedUsers);
	private JScrollPane scrollChatWindow = new JScrollPane(cwLobby);
	private JScrollPane scrollGroupRooms = new JScrollPane(eastPanelCenterNorth);
	
	private JButton[] groupChatList = new JButton[20];
	private ArrayList<JCheckBox> arrayListCheckBox = new ArrayList<JCheckBox>();
	private ArrayList<ChatWindow> arrayListChatWindows = new ArrayList<ChatWindow>();
	
	private Font txtFont = new Font("Sans-Serif", Font.BOLD , 20);
	private Font fontGroupButton = new Font("Sans-Serif",Font.PLAIN, 12);
	private Font fontButtons = new Font("Sans-Serif", Font.BOLD,15);
	private SimpleAttributeSet chatFont = new SimpleAttributeSet();
	
	public StartClient(ClientController clientController) {
		this.clientController = clientController;
		arrayListChatWindows.add(cwLobby);
		groupPanel = new GroupPanel();
		groupPanel.start();
		lookAndFeel(); 
        initGraphics();
		initListeners();
	}
	
	/**
	 * Initiates graphics and design.
	 * Also initiates the panels and buttons.
	 */
    public void initGraphics() {
        setLayout(bL);
        setPreferredSize(new Dimension(900,600));
        eastPanelCenterNorth.setPreferredSize(new Dimension(130,260));
        initScroll();
        initButtons();
        add(scrollChatWindow, BorderLayout.CENTER);
        southPanel();
        eastPanel();
    }
    
    /**
     * Initiates the butons. 
     * Also sets the icons and the design of the buttons.
     */
    public void initButtons() {
    	btnNewGroupChat.setIcon(new ImageIcon("src/resources/newGroup.png"));
        btnNewGroupChat.setBorder(null);
        btnNewGroupChat.setPreferredSize(new Dimension(64,64));

        btnFileChooser.setIcon(new ImageIcon("src/resources/newImage.png"));
        btnFileChooser.setBorder(null);
        btnFileChooser.setPreferredSize(new Dimension(64, 64));
        
        btnLobby.setFont(fontButtons);
    	btnLobby.setForeground(new Color(1,48,69));
    	btnLobby.setBackground(new Color(201,201,201));
        btnLobby.setOpaque(true);
        btnLobby.setBorderPainted(false);
    	
    	btnCreateGroup.setFont(fontButtons);
    	btnCreateGroup.setForeground(new Color(1,48,69));
    }
    
    /**
     * Initiates the scrollpanes and styleconstants.
     */
    public void initScroll() {
    	scrollChatWindow.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollChatWindow.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollConnectedUsers.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollConnectedUsers.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        DefaultCaret caretConnected = (DefaultCaret)tpConnectedUsers.getCaret();
        caretConnected.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        tpConnectedUsers.setEditable(false);
        
        tfMessageWindow.setFont(txtFont);
        StyleConstants.setForeground(chatFont, Color.BLACK);
        StyleConstants.setBold(chatFont, true);
    }
    
    /**
     * Requests that tfMessageWindow gets focus.
     */
    public void focusTextField() {
        tfMessageWindow.requestFocusInWindow();
    }
    
    /**
     * Initialises listeners.
     */
    public void initListeners() { 
    	tfMessageWindow.addKeyListener(new EnterListener());
    	GroupListener groupListener = new GroupListener();
    	SendListener sendListener = new SendListener();
    	LobbyListener disconnectListener = new LobbyListener();
    	btnNewGroupChat.addActionListener(groupListener);
    	btnCreateGroup.addActionListener(groupListener);
    	btnLobby.addActionListener(disconnectListener);
        btnFileChooser.addActionListener(new FileChooserListener());
        btnSend.addActionListener(sendListener);
    }
    
    /**
     * The method takes a ArrayList of the connected users and sets the user-checkboxes and
     * the connected user textpane based on the users in the ArrayList.
     * 
     * @param connectedUsers The ArrayList of the connected users.
     */
    public void setConnectedUsers(ArrayList<String> connectedUsers) {
    	setUserText();
        tpConnectedUsers.setText("");
        updateCheckBoxes(connectedUsers);
    	for (String ID : connectedUsers) {
    		appendConnectedUsers(ID);
    	}
    }
    
    /**
     * Sets the usertext in the labels to the connected user.
     */
    public void setUserText() {
    	lblUser.setText(clientController.getUserID());  
    	lblUser.setFont(txtFont);
    }
    
    /**
     * The south panel in the ClientUI BorderLayout.SOUTH.
     */
    public void southPanel() {
    	southPanel.setLayout(new BorderLayout());
    	southPanel.add(tfMessageWindow,BorderLayout.CENTER);
    	southPanel.setPreferredSize(new Dimension(600, 50));
    	
    	btnSend.setPreferredSize(new Dimension(134, 40));
    	btnSend.setFont(fontButtons);
    	btnSend.setForeground(new Color(1, 48, 69));
    	southPanel.add(pnlFileSend,BorderLayout.EAST);
    	
    	pnlFileSend.add(btnFileChooser,BorderLayout.WEST);
    	pnlFileSend.add(btnSend,BorderLayout.CENTER);
    	
    	add(southPanel, BorderLayout.SOUTH);
    }
    
    /**
     * The east panel in ClientUI BorderLayout.EAST.
     */
    public void eastPanel() {
    	eastPanel.setLayout(new BorderLayout());
    	eastPanel.add(lblUser, BorderLayout.NORTH);
    	eastPanel.add(eastPanelCenter, BorderLayout.CENTER);
    	eastPanelCenterNorth.add(pnlGroupSend);
    	eastPanelCenter.add(scrollGroupRooms, BorderLayout.NORTH);
    	eastPanelCenter.add(scrollConnectedUsers, BorderLayout.CENTER);
    	
    	pnlGroupSend.add(btnNewGroupChat);
    	
    	eastPanel.add(btnLobby,BorderLayout.SOUTH);
    	add(eastPanel, BorderLayout.EAST);
    }
    
    /**
     * Appends the message to the chatwindow object with the ID of the message object.
     * 
     * @param message The message object with an ID and a message.
     */
    public void appendContent(Message message) {
        getChatWindow(message.getConversationID()).append(message);
        if(activeChatWindow != message.getConversationID()) {
        	highlightGroup(message.getConversationID());
        }
    }
    
    /**
     * The method handles notice.
     * 
     * @param ID The ID of the group.
     */
    public void highlightGroup(int ID) {
    	if(ID != -1)
    		groupChatList[ID].setBackground(Color.PINK);
    }
    
    /**
     * Appends the string content in the chatwindow-lobby.
     * 
     * @param content Is a server message
     */
    public void appendServerMessage(String content) {
        cwLobby.append(content.toString());
    }
    
    /**
     * The method updates the ArrayList of checkboxes and add the checkboxes to the panel.
     * Also checks if the ID is your own ID and doesn't add a checkbox of yourself.
     * Updates the UI.
     * 
     * @param checkBoxUserIDs ArrayList of UserID's.
     */
    public void updateCheckBoxes(ArrayList<String> checkBoxUserIDs) {
    	arrayListCheckBox.clear();
    	groupPanel.pnlNewGroup.removeAll();
    	for (String ID : checkBoxUserIDs) {
    		if (!ID.equals(clientController.getUserID())) {
    			arrayListCheckBox.add(new JCheckBox(ID));
    		}
    	}
    	for (JCheckBox box: arrayListCheckBox) {
    		groupPanel.pnlNewGroup.add(box);
    	}
    	groupPanel.pnlOuterBorderLayout.revalidate();
    }
    
    /**
     * The method appends the text in the textpane of the connected users. 
     * 
     * @param message Is a username.
     */
    public void appendConnectedUsers(String message){
    	StyledDocument doc = tpConnectedUsers.getStyledDocument();
    	try {
			doc.insertString(doc.getLength(), message + "\n", chatFont);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Sets the text on the groupbuttons to the users you check in the checkbox.
     * Adds the new group chat connected with a button and a ChatWindow.
     * Enables you to change rooms.
     * Updates UI.
     * 
     * @param participants String-Array of the participants of the new groupchat.
     * @param ID The ID of the participants of the new groupchat.
     */
    public void createConversation(String[] participants, int ID) {
    	GroupButtonListener gbListener = new GroupButtonListener();
    	for (int i = 0; i < participants.length; i++) {
    		if (!(participants[i].equals(clientController.getUserID()))) {
    			if (i == participants.length - 1) {
    				userString += participants[i];
    			}else {
    				userString += participants[i] + " " ;
    			}
    		}
    	}
    	if (ID < groupChatList.length && groupChatList[ID] == null) {
	    	groupChatList[ID] = (new JButton(userString));
	    	groupChatList[ID].setPreferredSize(new Dimension(120,30));
	    	groupChatList[ID].setOpaque(true);
	    	groupChatList[ID].setBorderPainted(false);
	    	groupChatList[ID].setFont(fontGroupButton);
	    	groupChatList[ID].setForeground(new Color(93,0,0));
	    	groupChatList[ID].addActionListener(gbListener);
	    	
    		eastPanelCenterNorth.add(groupChatList[ID]);
    		
    		if (getChatWindow(ID)==null) {
    			arrayListChatWindows.add(new ChatWindow(ID));
    		}
    		
	    	eastPanelCenterNorth.revalidate();
	    	if (createdGroup) {  
		    	if (activeChatWindow == -1) {
					btnLobby.setBackground(null);
				}
				else {
					groupChatList[activeChatWindow].setBackground(null);
				}
		    	
				groupChatList[ID].setBackground(new Color(201,201,201));
				remove(bL.getLayoutComponent(BorderLayout.CENTER));
				add(getChatWindow(ID), BorderLayout.CENTER);
				activeChatWindow = ID;
				validate();
				repaint();
				createdGroup = false;
	    	}
    	}
    	this.userString = "";
    }
    
    /**
     * Sets the "Look and Feel" of the panels.
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
     * The method goes through the ArrayList of chatwindow object and 
     * returns the correct one based on the ID.
     * 
     * @param ID The ID of the user.
     * @return ChatWindow A ChatWindow object with the correct ID.
     */
	public ChatWindow getChatWindow(int ID) {
		for(ChatWindow cw : arrayListChatWindows) {
			if(cw.getID() == ID) {
				return cw;
			}
		}
		return null;
	}
	
	/**
	 * The class extends Thread and handles the Create a group panel.
	 */
	private class GroupPanel extends Thread {
		private JFrame groupFrame;
    	private JPanel pnlOuterBorderLayout = new JPanel(new BorderLayout());
    	private JPanel pnlNewGroup = new JPanel();
    	private JScrollPane scrollCheckConnectedUsers = new JScrollPane(pnlNewGroup);
    	
    	/**
    	 * The metod returns the JFrame groupFrame.
    	 * 
    	 * @return groupFrame
    	 */
    	public JFrame getFrame() {
    		return groupFrame;
    	}
    	
    	/**
    	 * Runs the frames of the groupPanes.
    	 */
	    public void run() {
	    	panelBuilder();
	    	groupFrame = new JFrame();
	    	groupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			groupFrame.add(pnlOuterBorderLayout);
			groupFrame.pack();
			groupFrame.setVisible(false);
			groupFrame.setLocationRelativeTo(null);
	    }
	    
	    /**
	     * Initiates the scrollpanels and the panels of the groupPanel.
	     */
	    public void panelBuilder() {
	    	scrollCheckConnectedUsers.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	        scrollCheckConnectedUsers.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	        btnCreateGroup.setText("New Conversation");
	        pnlOuterBorderLayout.add(btnCreateGroup, BorderLayout.SOUTH);
		   	pnlOuterBorderLayout.add(scrollCheckConnectedUsers,BorderLayout.CENTER);
		   	scrollCheckConnectedUsers.setPreferredSize(new Dimension(200,500));
		   	pnlNewGroup.setLayout(new GridLayout(100,1,5,5));
	    }
    }
	
	/**
	 * KeyListener for the messagewindow.
	 * Enables you to send a message with enter.
	 */
	private class EnterListener implements KeyListener {
		public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (!(tfMessageWindow.getText().isEmpty())) {
                    clientController.sendMessage(activeChatWindow, tfMessageWindow.getText());
                    tfMessageWindow.setText("");
                }
            }
        }

		public void keyReleased(KeyEvent e) {}

		public void keyTyped(KeyEvent e) {}
	}
	
	/**
	 * Listener that listens to New Group Chat-button and the Create Group Chat-button.
	 * If create group is pressed, a new button will be created with the right name,
	 * the right participants. 
	 * The method use alot of ArrayLists of checkboxes, participants and strings.
	 * Also some error-handling with empty buttons. 
	 */
	private class GroupListener implements ActionListener {
		private ArrayList<String> participants = new ArrayList<String>();
		private String[] temp;
		public void actionPerformed(ActionEvent e) {
			if (btnNewGroupChat == e.getSource() && arrayListCheckBox.size() > 0) {
				groupPanel.getFrame().setVisible(true);
			}
			if (btnCreateGroup == e.getSource()) {
				participants.clear();
				temp = null;
				for(int i = 0; i < arrayListCheckBox.size(); i++) {
					if(arrayListCheckBox.get(i).isSelected()) {
						participants.add(arrayListCheckBox.get(i).getText());
					}
				}
				
				temp = new String[participants.size() + 1];
				temp[0] = clientController.getUserID();
				for (int i = 1; i <= participants.size(); i++) {
					temp[i] = participants.get(i-1);
				}
				if (temp.length > 1) { 
					clientController.sendParticipants(temp);
					groupPanel.getFrame().dispose();
					createdGroup = true;
				} else {
					JOptionPane.showMessageDialog(null, "You have to choose atleast one person!");
				}
			}
		}
	}
	
	/**
	 * Listener that connects the right GroupChatButton in an ArrayList to the right
	 * active chat window. 
	 * Updates the UI.
	 */
	private class GroupButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			for(int i = 0; i < groupChatList.length; i++) {
				if(groupChatList[i]==e.getSource()) {
					if(activeChatWindow == -1) {
						btnLobby.setBackground(null);
					}
					else {
						groupChatList[activeChatWindow].setBackground(null);
					}
					groupChatList[i].setBackground(new Color(201,201,201));
					remove(bL.getLayoutComponent(BorderLayout.CENTER));
					add(getChatWindow(i), BorderLayout.CENTER);
					activeChatWindow = i;
					validate();
					repaint();
				}
			}
		}
	}
	
	/**
	 * Listener that connects the user with the lobby chatWindow through the Lobby button.
	 * Updates UI.
	 */
	private class LobbyListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (btnLobby==e.getSource()) { 
				btnLobby.setBackground(new Color(201,201,201));
				if(activeChatWindow != -1)
					groupChatList[activeChatWindow].setBackground(null);
				remove(bL.getLayoutComponent(BorderLayout.CENTER));
				add(getChatWindow(-1), BorderLayout.CENTER);
				activeChatWindow = -1;
				invalidate();
				repaint();
			}
		}
	}
	
	/**
	 * Listener that creates a JFileChooser when the button btnFileChooser is pressed.
	 * The JFileChooser is for images in the chat and it calls the method sendImage in the controller.
	 */
	private class FileChooserListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (btnFileChooser==e.getSource()) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String fullPath = selectedFile.getAbsolutePath();
                    clientController.sendImage(activeChatWindow, fullPath);
                }
			}
		}
	}
	
	/**
	 * Listener for the send message button. 
	 * Resets the message textfield text.
	 */
	private class SendListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (btnSend==e.getSource()) {
                if (!(tfMessageWindow.getText().isEmpty())) {
                    clientController.sendMessage(activeChatWindow, tfMessageWindow.getText());
                    tfMessageWindow.setText("");
                }
			}
		}
	}
}