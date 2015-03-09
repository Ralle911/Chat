package chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
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

public class ClientUI extends JPanel {
    private JButton[] groupChatList = new JButton[20];
    private int activeChatWindow = -1;
    private boolean createdGroup = false;
	
	private JPanel southPanel = new JPanel();
	private JPanel eastPanel = new JPanel();
	private JPanel eastPanelCenter = new JPanel(new BorderLayout());
	private JPanel eastPanelCenterNorth = new JPanel(new FlowLayout());
	private JPanel pnlGroupSend = new JPanel(new GridLayout(1,2,8,8));
	private JPanel pnlFileSend = new JPanel(new BorderLayout(5,5));
	
	private String userString = "";
	
	private JLabel lblUser = new JLabel();

	private JButton btnSend = new JButton("Send");
	private JButton btnNewGroupChat = new JButton();
	private JButton btnLobby = new JButton("Lobby");
	private JButton btnCreateGroup = new JButton("");
	private JButton btnFileChooser = new JButton();
	
	private JTextPane tpChatWindow = new JTextPane();
	private JTextPane tpConnectedUsers = new JTextPane();
	
	//Lobby chat 
	private ChatWindow cwLobby = new ChatWindow(-1);
	
	private JTextField tfMessageWindow = new JTextField();
		
	private JScrollPane scrollConnectedUsers = new JScrollPane(tpConnectedUsers);
	private JScrollPane scrollChatWindow = new JScrollPane(cwLobby);
	private JScrollPane scrollGroupRooms = new JScrollPane(eastPanelCenterNorth);
	
	private SimpleAttributeSet chatFont = new SimpleAttributeSet();
	private ClientController clientController;
	private GroupPanel groupPanel;
	
	private ArrayList<JCheckBox> arrayListCheckBox = new ArrayList<JCheckBox>();
	private ArrayList<ChatWindow> arrayListChatWindows = new ArrayList<ChatWindow>();
	
	private Font txtFont = new Font("Sans-Serif", Font.BOLD , 20);
	private Font fontGroupButton = new Font("Sans-Serif",Font.PLAIN, 12);
	private Font fontButtons = new Font("Sans-Serif", Font.BOLD,15);
	
	private BorderLayout bL = new BorderLayout();
//	
//	private CheckBoxListener checkBoxListener = new CheckBoxListener();
//	private RadioButtonListener radioButtonListener = new RadioButtonListener();
	
	public ClientUI(ClientController clientController) { //ClientController clientController
		this.clientController = clientController;
		arrayListChatWindows.add(cwLobby);
		groupPanel = new GroupPanel();
		groupPanel.start();
		lookAndFeel(); 
        initGraphics();
		initListeners();
	}
	
    public void initGraphics() {
        setLayout(bL);
        setPreferredSize(new Dimension(1200,800));
        eastPanelCenterNorth.setPreferredSize(new Dimension(130,260));

        tpChatWindow.setFont(txtFont);
        tfMessageWindow.setFont(txtFont);

        scrollChatWindow.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollChatWindow.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        DefaultCaret caret = (DefaultCaret)tpChatWindow.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        tpChatWindow.setEditable(false);
        
        scrollConnectedUsers.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollConnectedUsers.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        DefaultCaret caretConnected = (DefaultCaret)tpConnectedUsers.getCaret();
        caretConnected.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        tpConnectedUsers.setEditable(false);
        
        StyleConstants.setForeground(chatFont, Color.BLACK);
        StyleConstants.setBold(chatFont, true);

        btnNewGroupChat.setIcon(new ImageIcon("src/resources/newGroup.png"));
        btnNewGroupChat.setBorder(null);
        btnNewGroupChat.setPreferredSize(new Dimension(64,64));

        btnFileChooser.setIcon(new ImageIcon("src/resources/newImage.png"));
        
        btnLobby.setFont(fontButtons);
        btnLobby.setBorder(null);
    	btnLobby.setBackground(Color.LIGHT_GRAY);
    	
    	btnCreateGroup.setFont(fontButtons);
    	btnCreateGroup.setForeground(new Color(1,48,69));
    	
        add(scrollChatWindow, BorderLayout.CENTER);
        
        southPanel();
        eastPanel();
    }

    public void focusTextField() {
        tfMessageWindow.requestFocusInWindow();
    }
    
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
    /*
     * N�r servern updaterar Users
     */
    public void setConnectedUsers(ArrayList<String> connectedUsers) {
    	setUserText();
        tpConnectedUsers.setText("");
        updateCheckBoxes(connectedUsers);
    	for (String ID : connectedUsers) {
    		appendConnectedUsers(ID);
    	}
    }
    /*
     * S�tt sen till controller.setUser(); eller n�got
     */
    public void setUserText() {
    	lblUser.setText(clientController.getUserID());  // <------
    	lblUser.setFont(txtFont);
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
	    	groupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			groupFrame.add(pnlOuterBorderLayout);
			groupFrame.pack();
			groupFrame.setVisible(false);
			groupFrame.setLocationRelativeTo(null);
	    }
	    
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
    
    
    
    public void southPanel() {
    	southPanel.setLayout(new BorderLayout());
    	southPanel.add(tfMessageWindow,BorderLayout.CENTER);
    	southPanel.setPreferredSize(new Dimension(600, 50));
    	
    	btnSend.setPreferredSize(new Dimension(134, 50));
    	btnSend.setFont(fontButtons);
    	btnSend.setForeground(new Color(1,48,69));
    	btnFileChooser.setPreferredSize(new Dimension(64,64));
    	southPanel.add(pnlFileSend,BorderLayout.EAST);
    	
    	pnlFileSend.add(btnFileChooser,BorderLayout.WEST);
    	pnlFileSend.add(btnSend,BorderLayout.CENTER);
    	
    	add(southPanel, BorderLayout.SOUTH);
    }
    
    public void eastPanel() {
    	eastPanel.setLayout(new BorderLayout());
    	eastPanel.add(lblUser,BorderLayout.NORTH);
    	eastPanel.add(eastPanelCenter,BorderLayout.CENTER);
    	eastPanelCenterNorth.add(pnlGroupSend);
    	eastPanelCenter.add(scrollGroupRooms,BorderLayout.NORTH);
    	eastPanelCenter.add(scrollConnectedUsers,BorderLayout.CENTER);
    	
    	pnlGroupSend.add(btnNewGroupChat);
    	
    	eastPanel.add(btnLobby,BorderLayout.SOUTH);
    	add(eastPanel, BorderLayout.EAST);
    }
    
    public void appendContent(Message message){
        getChatWindow(message.getConversationID()).append(message);
    }

    public void appendServerMessage(String content) {
        cwLobby.append(content.toString());
    }
    
    
    public void updateCheckBoxes(ArrayList<String> checkBoxUserIDs) {
    	arrayListCheckBox.clear();
    	groupPanel.pnlNewGroup.removeAll();
    	for (String ID : checkBoxUserIDs) {
    		if (!ID.equals(clientController.getUserID())) {
    			arrayListCheckBox.add(new JCheckBox(ID));
    		}
    	}
    	for (JCheckBox box: arrayListCheckBox) {
//    		box.addActionListener(checkBoxListener);
    		groupPanel.pnlNewGroup.add(box);
    	}
    	groupPanel.pnlOuterBorderLayout.revalidate();
//	   	validate();
    }
    
    public void appendConnectedUsers(String message){
    	StyledDocument doc = tpConnectedUsers.getStyledDocument();
    	try {
			doc.insertString(doc.getLength(), message + "\n", chatFont);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
    }
    
    public void createConversation(String[] participants, int ID) {
    	GroupButtonListener gbListener = new GroupButtonListener();
    	for (int i = 0; i < participants.length; i++) {
    		if (participants[i].equals(clientController.getUserID()) == false) {
    			if (i == participants.length - 1) {
    				userString += participants[i];
    			}else {
    				userString += participants[i] + " " ;
    			}
    		}
    	}
    	if(ID < groupChatList.length && groupChatList[ID] == null) {
    	//GroupChatList är en JButton Array
	    	groupChatList[ID] = (new JButton(userString));
	    	groupChatList[ID].setPreferredSize(new Dimension(120,30));
	    	groupChatList[ID].setOpaque(true);
	    	groupChatList[ID].setFont(fontGroupButton);
	    	groupChatList[ID].setForeground(new Color(93,0,0));
	    	groupChatList[ID].addActionListener(gbListener);
	    	
    		eastPanelCenterNorth.add(groupChatList[ID]);
    		
    		if(getChatWindow(ID)==null) {
    			arrayListChatWindows.add(new ChatWindow(ID));
    		}
    		
	    	eastPanelCenterNorth.revalidate();
	    	
	    	if(createdGroup) {  //<--------
		    	if(activeChatWindow == -1) {
					btnLobby.setBackground(null);
				}
				else {
					groupChatList[activeChatWindow].setBackground(Color.WHITE);
				}
				groupChatList[ID].setBackground(Color.DARK_GRAY);
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
    
	private class EnterListener implements KeyListener {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode()==KeyEvent.VK_ENTER) {
				clientController.sendMessage(activeChatWindow, tfMessageWindow.getText());
				tfMessageWindow.setText("");
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private class GroupListener implements ActionListener {
		private ArrayList<String> participants = new ArrayList<String>();
		private String[] temp;
		public void actionPerformed(ActionEvent e) {
			if (btnNewGroupChat == e.getSource()) {
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
				if (temp.length > 1) {  //Tomma rutor inte ska göras
					clientController.sendParticipants(temp);
					groupPanel.getFrame().dispose();
					createdGroup = true;
				} else {
					JOptionPane.showMessageDialog(null, "Du måste välja minst en person!");
				}
			}
		}
	}
	
	public ChatWindow getChatWindow(int ID) {
		for(ChatWindow cw : arrayListChatWindows) {
			if(cw.getID() == ID) {
//				System.out.println("Returning ChatWindow with " + ID);
				return cw;
			}
		}
		return null;
	}
	
	/*
	 * Kanske gör för många objekt? Gör objekt i createScroll
	 */
	private class GroupButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			for(int i = 0; i < groupChatList.length; i++) {
				if(groupChatList[i]==e.getSource()) {
					if(activeChatWindow == -1) {
						btnLobby.setBackground(null);
					}
					else {
						groupChatList[activeChatWindow].setBackground(Color.WHITE);
					}
					groupChatList[i].setBackground(Color.DARK_GRAY);
					remove(bL.getLayoutComponent(BorderLayout.CENTER));
					add(getChatWindow(i), BorderLayout.CENTER);
					activeChatWindow = i;
					validate();
					repaint();
				}
			}
		}
	}
	
	private class LobbyListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (btnLobby==e.getSource()) { 
				btnLobby.setBackground(Color.LIGHT_GRAY);
				if(activeChatWindow != -1)
					groupChatList[activeChatWindow].setBackground(Color.WHITE);
				remove(bL.getLayoutComponent(BorderLayout.CENTER));
				add(getChatWindow(-1), BorderLayout.CENTER);
				activeChatWindow = -1;
				invalidate();
				repaint();
			}
		}
	}
	
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
	
	private class SendListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (btnSend==e.getSource()) {
				clientController.sendMessage(activeChatWindow, tfMessageWindow.getText());
				tfMessageWindow.setText("");
			}
		}
	}
}