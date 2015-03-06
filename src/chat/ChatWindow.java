package chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ChatWindow extends JPanel {
	private int ID;
	private JTextPane textPane;
	private Font txtFont = new Font("Sans-Serif", Font.BOLD , 20);
	private SimpleAttributeSet chatFont = new SimpleAttributeSet();
	
	public ChatWindow(int ID) {
		setLayout(new BorderLayout());
		this.ID = ID;
		textPane = new JTextPane();
		
		StyleConstants.setForeground(chatFont, Color.BLACK);
        StyleConstants.setBold(chatFont, true);
        
        textPane.setFont(txtFont);
        
        add(textPane, BorderLayout.CENTER);
	}
	
	public void append(String str) {
		StyledDocument doc = textPane.getStyledDocument();
		
    	try {
			doc.insertString(doc.getLength(), str + "\n", chatFont);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public int getID() {
		return ID;
	}
}
