package chat;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;

//test
public class ImageScaleHandler {

	private static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
		BufferedImage bimage = new BufferedImage(img.getWidth(null),
				img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
		return bimage;
	}

	public static BufferedImage createScaledImage(Image img, int height) {
		BufferedImage bimage = toBufferedImage(img);
		bimage = Scalr.resize(bimage, Method.ULTRA_QUALITY,
				Scalr.Mode.FIT_TO_HEIGHT, 0, height);

		return bimage;
	}

	 // Exempel
	public static void main(String[] args) {
		ImageIcon icon = new ImageIcon("src/filer/new1.jpg");
		Image img = icon.getImage();

		// Använd denna rad för att skala om bilder.
		BufferedImage scaledImage = ImageScaleHandler.createScaledImage(img, 75);
		icon = new ImageIcon(scaledImage);

		JLabel lbl = new JLabel();
		lbl.setIcon(icon);
		JPanel panel = new JPanel();
		panel.add(lbl);
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
}