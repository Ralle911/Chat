package chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.*;
import javax.swing.text.*;

public class ChatWindow extends JPanel {
	private int ID;
	private JScrollPane scrollPane;
	private JTextPane textPane;
//	private Font txtFont = new Font("Sans-Serif", Font.PLAIN , 15);
	
	private SimpleAttributeSet chatFont = new SimpleAttributeSet();
	private SimpleAttributeSet nameFont = new SimpleAttributeSet();
	
	public ChatWindow(int ID) {
		setLayout(new BorderLayout());
		this.ID = ID;
		textPane = new JTextPane();
		scrollPane = new JScrollPane(textPane);
		
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		StyleConstants.setForeground(chatFont, Color.BLACK);
        StyleConstants.setFontSize(chatFont, 20);
        
        StyleConstants.setForeground(nameFont, Color.BLACK);
        StyleConstants.setFontSize(nameFont, 20);
        StyleConstants.setBold(nameFont, true);
        
        add(scrollPane, BorderLayout.CENTER);
        DefaultCaret caret = (DefaultCaret)textPane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        textPane.setEditable(false);
	}
	
	public void append(Message message) {
        StyledDocument doc = textPane.getStyledDocument();

        try {
        	doc.insertString(doc.getLength(), message.getTimestamp() + " - ", chatFont);
            doc.insertString(doc.getLength(), message.getFromUserID() + ": ", nameFont);
            if (message.getContent() instanceof String) {
                doc.insertString(doc.getLength(), message.getContent() + "\n", chatFont);
            } else {
                ImageIcon icon = (ImageIcon)message.getContent();
                textPane.insertIcon(icon);
                doc.insertString(doc.getLength(), "\n", chatFont);
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
