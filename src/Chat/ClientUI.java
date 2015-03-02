package Chat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

/**
 * 
 * @author Emil Sandgren
 *
 */

public class ClientUI extends JPanel {
	private JTextArea taChatWindow = new JTextArea();
	private JTextField tfMessageWindow = new JTextField();
    private JScrollPane scroll = new JScrollPane(taChatWindow);
	private Font txtFont = new Font("Sans-Serif", Font.BOLD , 30 );
	private ClientController clientController;
	
	public ClientUI(ClientController clientController) {
		this.clientController = clientController;
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(600,400));
		taChatWindow.setFont(txtFont);
		tfMessageWindow.setFont(txtFont);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        DefaultCaret caret = (DefaultCaret)taChatWindow.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        add(scroll, BorderLayout.CENTER);
//		add(taChatWindow, BorderLayout.CENTER);
		add(tfMessageWindow,BorderLayout.SOUTH);
		tfMessageWindow.addActionListener(new EnterListener());
	}

    public void appendText(String message){
        taChatWindow.append(message);
    }
	
	private class EnterListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			clientController.sendMessage(tfMessageWindow.getText());
			tfMessageWindow.setText("");
		}
	}
}
