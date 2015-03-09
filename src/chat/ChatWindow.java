package chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.*;
import javax.swing.text.*;

public class ChatWindow extends JPanel {
	private int ID;
	private JTextPane textPane;
	private Font txtFont = new Font("Sans-Serif", Font.PLAIN , 14);
	private SimpleAttributeSet chatFont = new SimpleAttributeSet();
	
	public ChatWindow(int ID) {
		setLayout(new BorderLayout());
		this.ID = ID;
		textPane = new JTextPane();
		
		StyleConstants.setForeground(chatFont, Color.BLACK);
        StyleConstants.setBold(chatFont, true);
        
        textPane.setFont(txtFont);
        
        add(textPane, BorderLayout.CENTER);
        DefaultCaret caret = (DefaultCaret)textPane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        textPane.setEditable(false);
	}
	
	public void append(Message message) {
        StyledDocument doc = textPane.getStyledDocument();
        Style style = doc.addStyle("Stylename", null);

        try {
            doc.insertString(doc.getLength(), message.getTimestamp() + " - " + message.getFromUserID() + ": ", chatFont);
            if (message.getContent() instanceof String) {
                doc.insertString(doc.getLength(), message.getContent() + "\n", chatFont);
            } else {
                Icon icon = (Icon)message.getContent();
                StyleConstants.setIcon(style, icon);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
	}

    public void append(String message) {
        StyledDocument doc = textPane.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), "[Server: " + message + "]\n", chatFont);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

	public int getID() {
		return ID;
	}
}
