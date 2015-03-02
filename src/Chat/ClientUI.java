package Chat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * 
 * @author Emil Sandgren
 *
 */

public class ClientUI extends JPanel{
	private JTextArea txtArea1 = new JTextArea();
	private JTextField txtField1 = new JTextField();
	private Font txtFont = new Font("Sans-Serif", Font.BOLD , 30 );
	
	public ClientUI() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(600,400));
		
		txtArea1.setFont(txtFont);
		txtField1.setFont(txtFont);
		
		add(txtArea1,BorderLayout.CENTER);
		add(txtField1,BorderLayout.SOUTH);
		
		txtField1.addActionListener(new EnterListener());
		
	}
	
	private class EnterListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			txtArea1.setText(txtField1.getText());
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		ClientUI ui =new ClientUI();
		frame.add(ui);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(true);
	}
}
