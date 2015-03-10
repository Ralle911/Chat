package chat;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.*;
import javax.swing.text.*;

/**
 * Class used to present content in the main window.
 *
 * @author Emil Sandgren, Kalle Bornemark, Erik Sandgren,
 * Jimmy Maksymiw, Lorenz Puskas & Rasmus Andersson
 */
public class ChatWindow extends JPanel {
	private int ID;
	private JScrollPane scrollPane;
	private JTextPane textPane;

	private SimpleAttributeSet chatFont = new SimpleAttributeSet();
	private SimpleAttributeSet nameFont = new SimpleAttributeSet();

    /**
     * Constructor that takes an ID from a Conversation, and creates a window to display it.
     *
     * @param ID The Conversation object's ID.
     */
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
        textPane.setEditable(false);
	}

    /**
     * Appends a new message into the panel window.
     * The message can either contain a String or an ImageIcon.
     *
     * @param message The message object which content will be displayed.
     */
	public void append(final Message message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                StyledDocument doc = textPane.getStyledDocument();
                try {
                    doc.insertString(doc.getLength(), message.getTimestamp() + " - ", chatFont);
                    doc.insertString(doc.getLength(), message.getFromUserID() + ": ", nameFont);
                    if (message.getContent() instanceof String) {
                        doc.insertString(doc.getLength(), (String)message.getContent(), chatFont);
                    } else {
                        ImageIcon icon = (ImageIcon)message.getContent();
                        StyleContext context = new StyleContext();
                        Style labelStyle = context.getStyle(StyleContext.DEFAULT_STYLE);
                        JLabel label = new JLabel(icon);
                        StyleConstants.setComponent(labelStyle, label);
                        doc.insertString(doc.getLength(), "Ignored", labelStyle);
                    }
                    doc.insertString(doc.getLength(), "\n", chatFont);
                    textPane.setCaretPosition(textPane.getDocument().getLength());

                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        });
	}

    /**
     * Appends a string into the panel window.
     *
     * @param stringMessage The string to be appended.
     */
    public void append(String stringMessage) {
        StyledDocument doc = textPane.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), "[Server: " + stringMessage + "]\n", chatFont);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the ChatWindow's ID.
     *
     * @return The ChatWindow's ID.
     */
	public int getID() {
		return ID;
	}
}
